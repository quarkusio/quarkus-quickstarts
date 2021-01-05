
wait_for_url() {
    URL=$1
    MSG=$2

    if [[ $URL == https* ]]; then
        CMD="curl -k -sL -o /dev/null -w %{http_code} $URL"
    else
        CMD="curl -sL -o /dev/null -w %{http_code} $URL"
    fi

    until [ "200" == "`$CMD`" ]
    do
        echo "$MSG ($URL)"
        sleep 2
    done
}