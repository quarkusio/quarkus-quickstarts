#!/usr/bin/env kscript

@file:MavenRepository("jcenter","https://jcenter.bintray.com/")
@file:MavenRepository("maven-central","https://repo1.maven.org/maven2/")
@file:DependsOn("org.kohsuke:github-api:1.101")


import org.kohsuke.github.*
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

val token = args[0]; 
val status = args[1];


// Handle status. Possible values are success, failure, or cancelled.
val succeed = status == "success";
if (status == "cancelled") {
    println("Job status is `cancelled` - exiting")
    System.exit(0)
}

val ISSUE_TITLE = "Quickstarts Native Build failed against Quarkus Master"
val REPO = "quarkusio/quarkus-quickstarts"

val github = GitHubBuilder().withOAuthToken(token).build()
val repository = github.getRepository("quarkusio/quarkus-quickstarts")
val issues = repository.getIssues(GHIssueState.OPEN)
val existingIssue = issues.firstOrNull { i -> i.getTitle() == ISSUE_TITLE}

val quickstartsCommit = getRepositoryCommit(".")
val quarkusCommit = getRepositoryCommit("quarkus")
if (succeed) {
    if (existingIssue != null) {
        // close issue with a comment
        val comment = existingIssue.comment("""
            Build fixed with:

            * Quarkus commit: ${quarkusCommit}
            * Quickstarts commit: ${quickstartsCommit}               

        """.trimIndent())        
        existingIssue.close()
        println("Comment added on issue ${existingIssue.getHtmlUrl()} - ${comment.getHtmlUrl()}, the issue has also been closed")
    } else {
        println("Nothing to do - the build passed and there are no issue to close")
    }
} else  {
    // Build failed
    if (existingIssue == null) {
        // create a new issue
        val newIssue = repository.createIssue(ISSUE_TITLE)
            .body("""
                The build of the  `development` branch of the quickstarts against quarkus `master` has failed.
                This build verifies the native compilation and executes the native integration tests.

                The build failed with:

                * Quarkus commit: ${quarkusCommit}
                * Quickstarts commit: ${quickstartsCommit}               

            """.trimIndent())
            .create()
        println("New issue created: ${newIssue.getTitle()} - ${newIssue.getHtmlUrl()}")    
    } else {
        // comment on issue
        val comment = existingIssue.comment("""
            Build still failing with:

            * Quarkus commit: ${quarkusCommit}
            * Quickstarts commit: ${quickstartsCommit}   

        """.trimIndent())
        println("Comment added on issue ${existingIssue.getHtmlUrl()} - ${comment.getHtmlUrl()}")

    }
}


fun getRepositoryCommit(workingDir: String) : String {
    return "git rev-parse HEAD".runCommand(workingDir)
}

fun String.runCommand(workingDir: String): String {
    try {
        val parts = this.split("\\s".toRegex())
        val proc = ProcessBuilder(*parts.toTypedArray())
                .directory(File(workingDir))
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

        proc.waitFor(60, TimeUnit.MINUTES)
        return proc.inputStream.bufferedReader().readText()
    } catch(e: IOException) {
        e.printStackTrace()
        return ""
    }
}

