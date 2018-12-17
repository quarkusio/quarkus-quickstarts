# Contributing guide

**Want to contributeto quickstarts? That's awesome!**
We try to make it easy, and all contributions, even the smaller ones, are more than welcome.
This includes bug reports, fixes, documentation, new quickstarts...
But first, read this page (including the small print at the end).

## Reporting an issue

This project uses Github issues to manage the issues. Open an issue directly in Github.

## Before you contribute and structure of the repository

To contribute, use Github Pull Requests, from your **own** fork.

* The `master` branch uses the latest release of Protean.
* The `development` branch uses a snapshot version of Protean.

Depending on your contribution, you may want to target the `master` or `development` branch. Here are a few rules:

* If your changes require a snapshot version of Shamrock -> `development`
* If your changes impact the documentation -> `development`
* If your changes are just a hot fix and do not require an update of Shamrock or the documentation -> `master`
* For pull reuqest depending on feature not yet merged in Shamrock, prepend the title with "[DO NOT MERGE]"

### Code reviews

All submissions, including submissions by project members, need to be reviewed before being merged.

### Continuous Integration

Because we are all humans, the project use a continuous integration approach and each pull request triggers a full build.
Please make sure to monitor the output of the build and act accordingly.

### Tests and documentation are not optional

* All quickstarts must contain unit tests,
* All quickstarts supporting the native mode must contain native tests,
* If your quicstart does not work in native mode, write a comment in the pull request,
* New quickstarts must be listed in the [README.md](./README.md) page

## Build

* Clone the repository: `git clone https://github.com/jbossas/protean-quickstarts.git`
* Navigate to the directory: `cd protean-quickstarts`
* Invoke `mvn clean verify -Pnative` from the root directory

```bash
git clone https://github.com/jbossas/protean-quickstarts.git
cd protean-quickstarts
mvn clean verify -Pnative
# Wait... success!
```

## The small print

This project is an open source project, please act responsibly, be nice, polite and enjoy!
