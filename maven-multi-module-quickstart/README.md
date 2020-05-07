# Quarkus multi-module Maven example
This is a minimal example showing how to set up a multi-module Maven project with [Quarkus](https://quarkus.io).

## Modules
- **maven-multi-module-quickstart** (root)
- **domain** (contains the domain objects of the application)
- **services** (contains the services of the application)
- **webapp** (contains the Quarkus web application)

### Root
Contains the basic information of the application and no actual code, so the packaging is set to `pom`. We set the version to a properyy such that the submodules can use this version to make sure each module is using the right version.
```xml
<project>
    <groupId>org.acme</groupId>
    <artifactId>maven-multi-module-quickstart</artifactId>
    <packaging>pom</packaging>
    <version>${revision}</version>
    <properties>
        <revision>1.0.0-SNAPSHOT</revision>
    </properties>
</project>
```

The [pom.xml](pom.xml) includes the Quarkus BOM to centrally manage all the version for the Quarkus extensions to make sure everything is compatible between the modules and Quarkus itself. It also sets up the Quarkus Maven plugin.

### Submodules
Next we set up the submodules. Each `pom.xml` includes information about the parent project.
```xml
<parent>
    <groupId>org.acme</groupId>
    <artifactId>maven-multi-module-quickstart</artifactId>
    <version>${revision}</version>
</parent>
```

If we need to depend on another submodule we can simply include it as a dependency.
```xml
<dependency>
    <groupId>org.acme</groupId>
    <artifactId>domain</artifactId>
    <version>${project.parent.version}</version>
    <scope>compile</scope>
</dependency>
```

## Dependency Injection
Now everything compiles, however when we use dependency injection we will get an exception during runtime stating that it can't satisfy the bean. We need to apply a little trick to make sure the beans can be found. Simply drop an empty `beans.xml` file in the `src/main/resources/META-INF` folder of that submodule. This way the beans will be resolved again. 
