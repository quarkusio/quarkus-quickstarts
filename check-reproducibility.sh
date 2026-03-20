#!/bin/bash
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "$0")" && pwd)"
TIMESTAMP="$(date +%Y-%m-%d-%H-%M-%S)"
OUTPUT_DIR="$REPO_ROOT/reproducibility-checks/$TIMESTAMP"
REPORT_FILE="$OUTPUT_DIR/report.md"
NUM_RUNS=10
BUILD_TIMESTAMP="$(date -u +%Y-%m-%dT%H:%M:%SZ)"

mkdir -p "$OUTPUT_DIR"

SINGLE_MODULE=false
if [[ $# -gt 0 ]]; then
    # Use the provided directory as the single module to check
    # Strip trailing slash if present
    MODULE_ARG="${1%/}"
    if [[ ! -d "$REPO_ROOT/$MODULE_ARG" ]]; then
        echo "Error: directory '$MODULE_ARG' not found in $REPO_ROOT"
        exit 1
    fi
    MODULES=("$MODULE_ARG")
    SINGLE_MODULE=true
else
    # Parse modules from pom.xml using xmlstarlet (handles comments/namespaces properly)
    mapfile -t MODULES < <(xmlstarlet sel -N pom=http://maven.apache.org/POM/4.0.0 \
        -t -m '/pom:project/pom:modules/pom:module' -v '.' -n "$REPO_ROOT/pom.xml" | sort)
fi

echo "Found ${#MODULES[@]} modules to check"
echo "Output directory: $OUTPUT_DIR"
echo ""

declare -A RESULTS
declare -A RESULT_DETAILS
LAST_DIFF_FILE=""

for module in "${MODULES[@]}"; do
    MODULE_DIR="$REPO_ROOT/$module"
    if [[ ! -d "$MODULE_DIR" ]]; then
        echo "[$module] Directory not found, skipping"
        RESULTS["$module"]="BUILD FAILED"
        RESULT_DETAILS["$module"]="module directory not found"
        echo ""
        continue
    fi

    echo "[$module] Starting reproducibility check..."
    MODULE_OUTPUT_DIR="$OUTPUT_DIR/$module"
    mkdir -p "$MODULE_OUTPUT_DIR"

    # Determine mvnw path - use repo root's mvnw
    MVNW="$REPO_ROOT/mvnw"
    if [[ ! -x "$MVNW" ]]; then
        echo "[$module] mvnw not found or not executable at $MVNW, skipping"
        RESULTS["$module"]="BUILD FAILED"
        RESULT_DETAILS["$module"]="mvnw not found"
        echo ""
        continue
    fi

    # Build #1 (reference build)
    echo "[$module] Reference build (1/$NUM_RUNS)..."
    BUILD_LOG="$MODULE_OUTPUT_DIR/build-reference.log"
    if ! (cd "$MODULE_DIR" && "$MVNW" clean package -DskipTests -DskipITs -Dproject.build.outputTimestamp="$BUILD_TIMESTAMP" > "$BUILD_LOG" 2>&1); then
        echo "[$module] BUILD FAILED on reference build"
        RESULTS["$module"]="BUILD FAILED"
        RESULT_DETAILS["$module"]="build error on reference build"
        echo ""
        continue
    fi

    # Find all target/quarkus-app directories
    REF_APPS=()
    while IFS= read -r -d '' app_dir; do
        REF_APPS+=("$app_dir")
    done < <(find "$MODULE_DIR" -type d -path "*/target/quarkus-app" -print0 2>/dev/null)

    if [[ ${#REF_APPS[@]} -eq 0 ]]; then
        echo "[$module] No target/quarkus-app directories found, skipping"
        RESULTS["$module"]="BUILD FAILED"
        RESULT_DETAILS["$module"]="no quarkus-app directory produced"
        echo ""
        continue
    fi

    # Save reference snapshots
    REF_SNAPSHOT_DIR="$MODULE_OUTPUT_DIR/reference"
    mkdir -p "$REF_SNAPSHOT_DIR"
    for app_dir in "${REF_APPS[@]}"; do
        # Create a relative path for storing the snapshot
        rel_path="${app_dir#"$MODULE_DIR"/}"
        snapshot_dest="$REF_SNAPSHOT_DIR/$rel_path"
        mkdir -p "$(dirname "$snapshot_dest")"
        cp -a "$app_dir" "$snapshot_dest"
    done

    # Builds #2 through #NUM_RUNS
    reproducible=true
    fail_detail=""
    for ((run=2; run<=NUM_RUNS; run++)); do
        echo "[$module] Build $run/$NUM_RUNS..."
        BUILD_LOG="$MODULE_OUTPUT_DIR/build-$run.log"
        if ! (cd "$MODULE_DIR" && "$MVNW" clean package -DskipTests -DskipITs -Dproject.build.outputTimestamp="$BUILD_TIMESTAMP" > "$BUILD_LOG" 2>&1); then
            echo "[$module] BUILD FAILED on run $run"
            RESULTS["$module"]="BUILD FAILED"
            RESULT_DETAILS["$module"]="build error on run $run"
            reproducible=false
            break
        fi

        # Compare each quarkus-app directory against reference
        diff_found=false
        for app_dir in "${REF_APPS[@]}"; do
            rel_path="${app_dir#"$MODULE_DIR"/}"
            ref_snapshot="$REF_SNAPSHOT_DIR/$rel_path"

            if [[ ! -d "$app_dir" ]]; then
                echo "[$module] quarkus-app directory missing on run $run: $rel_path"
                diff_found=true
                fail_detail="missing quarkus-app on run $run: $rel_path"
                break
            fi

            DIFF_OUTPUT_DIR="$MODULE_OUTPUT_DIR/diffoscope-run-$run"
            mkdir -p "$DIFF_OUTPUT_DIR"
            # Sanitize rel_path for filename
            diff_file="$DIFF_OUTPUT_DIR/$(echo "$rel_path" | tr '/' '_').html"

            if ! diffoscope --html "$diff_file" --exclude-directory-metadata=recursive "$ref_snapshot" "$app_dir" > /dev/null 2>&1; then
                echo "[$module] DIFF DETECTED on run $run in $rel_path"
                diff_found=true
                fail_detail="failed on run $run, diff in $rel_path"
                LAST_DIFF_FILE="$diff_file"
                break
            fi
        done

        if [[ "$diff_found" == true ]]; then
            reproducible=false
            if [[ -z "${RESULTS["$module"]+x}" || "${RESULTS["$module"]}" != "BUILD FAILED" ]]; then
                RESULTS["$module"]="NOT REPRODUCIBLE"
                RESULT_DETAILS["$module"]="$fail_detail"
            fi
            break
        fi
    done

    if [[ "$reproducible" == true && -z "${RESULTS["$module"]+x}" ]]; then
        echo "[$module] REPRODUCIBLE ($NUM_RUNS/$NUM_RUNS builds matched)"
        RESULTS["$module"]="REPRODUCIBLE"
        RESULT_DETAILS["$module"]="$NUM_RUNS/$NUM_RUNS builds matched"
    fi

    # Clean up reference snapshots to save disk space
    rm -rf "$REF_SNAPSHOT_DIR"

    echo ""
done

# Generate report
REPRO_LIST=()
NOT_REPRO_LIST=()
FAILED_LIST=()

for module in "${MODULES[@]}"; do
    status="${RESULTS["$module"]:-UNKNOWN}"
    detail="${RESULT_DETAILS["$module"]:-}"
    case "$status" in
        REPRODUCIBLE) REPRO_LIST+=("- $module ($detail)") ;;
        "NOT REPRODUCIBLE") NOT_REPRO_LIST+=("- $module ($detail)") ;;
        "BUILD FAILED") FAILED_LIST+=("- $module ($detail)") ;;
        *) FAILED_LIST+=("- $module (unknown status)") ;;
    esac
done

{
    echo "# Quarkus Quickstarts Reproducibility Report"
    echo ""
    echo "**Date:** $TIMESTAMP"
    echo ""
    echo "## Reproducible (${#REPRO_LIST[@]})"
    echo ""
    if [[ ${#REPRO_LIST[@]} -gt 0 ]]; then
        printf '%s\n' "${REPRO_LIST[@]}"
    else
        echo "*none*"
    fi
    echo ""
    echo "## Not Reproducible (${#NOT_REPRO_LIST[@]})"
    echo ""
    if [[ ${#NOT_REPRO_LIST[@]} -gt 0 ]]; then
        printf '%s\n' "${NOT_REPRO_LIST[@]}"
    else
        echo "*none*"
    fi
    echo ""
    echo "## Build Failed (${#FAILED_LIST[@]})"
    echo ""
    if [[ ${#FAILED_LIST[@]} -gt 0 ]]; then
        printf '%s\n' "${FAILED_LIST[@]}"
    else
        echo "*none*"
    fi
} | tee "$REPORT_FILE"

echo ""
echo "Full report saved to: $REPORT_FILE"
echo "Diffoscope outputs saved in: $OUTPUT_DIR"

# Open the last diffoscope report in the browser when testing a single module
if [[ "$SINGLE_MODULE" == true && -n "$LAST_DIFF_FILE" && -f "$LAST_DIFF_FILE" ]]; then
    echo ""
    echo "Opening diffoscope report: $LAST_DIFF_FILE"
    xdg-open "$LAST_DIFF_FILE"
fi
