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

val ISSUE_TITLE = "[CI] - Quickstarts Native Build failed against Quarkus Master"
val REPO = "quarkusio/quarkus-quickstarts"

val github = GitHubBuilder().withOAuthToken(token).build()
val repository = github.getRepository("quarkusio/quarkus")
val issues = repository.getIssues(GHIssueState.ALL)

val issue = issues.firstOrNull { i -> i.getTitle() == ISSUE_TITLE}

val quickstartsCommit = getRepositoryCommit(".")
val quarkusCommit = getRepositoryCommit("quarkus")
if (succeed) {
    if (issue != null  && isOpen(issue)) {
        // close issue with a comment
        val comment = issue.comment("""
            Build fixed with:

            * Quarkus commit: ${quarkusCommit}
            * Quickstarts commit: ${quickstartsCommit}  
            * Link to build: https://github.com/quarkusio/quarkus-quickstarts/actions

        """.trimIndent())        
        issue.close()
        println("Comment added on issue ${issue.getHtmlUrl()} - ${comment.getHtmlUrl()}, the issue has also been closed")
    } else {
        println("Nothing to do - the build passed and the issue is already closed or not created")
    }
} else  {
    // Build failed
    if (issue == null) {
        // create a new issue
        val newIssue = repository.createIssue(ISSUE_TITLE)
            .body("""
                The build of the  `development` branch of the quickstarts against quarkus `master` has failed.
                This build verifies the native compilation and executes the native integration tests.

                The build failed with:

                * Quarkus commit: ${quarkusCommit}
                * Quickstarts commit: ${quickstartsCommit}     
                * Link to build: https://github.com/quarkusio/quarkus-quickstarts/actions 
                         
                This issue will be closed when the build gets fixed. It will be re-opened if it fails again later.
                Do not close the issue yourself (it will be re-opened automatically)
                You can subscribe and monitor the status.

            """.trimIndent())
                .label("area/infra")
                .create()
        println("New issue created: ${newIssue.getTitle()} - ${newIssue.getHtmlUrl()}")    
    } else {
        if (isOpen(issue)) {
            val comment = issue.comment("""
            Build still failing with:

            * Quarkus commit: ${quarkusCommit}
            * Quickstarts commit: ${quickstartsCommit}   
            * Link to build: https://github.com/quarkusio/quarkus-quickstarts/actions

        """.trimIndent())
            println("Comment added on issue ${issue.getHtmlUrl()} - ${comment.getHtmlUrl()}")
        } else {
            issue.reopen()
            val comment = issue.comment("""
            Unfortunately, the build fails again:

            * Quarkus commit: ${quarkusCommit}
            * Quickstarts commit: ${quickstartsCommit}   
            * Link to build: https://github.com/quarkusio/quarkus-quickstarts/actions

        """.trimIndent())
            println("Comment added on issue ${issue.getHtmlUrl()} - ${comment.getHtmlUrl()}, the issue has been re-opened.")

        }

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

fun isOpen(issue : GHIssue) : Boolean {
    return issue.getState() == GHIssueState.OPEN
}

