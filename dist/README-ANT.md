## Ant tasks for Ceylon

We include support for Ceylon ant tasks in
`repo/com/redhat/ceylon/ant/0.1/com.redhat.ceylon.ant-0.1.jar`.

### Loading the ant tasks

    <property name="ceylon.home" value="path/to/ceylon-0.1"/>
    <path id="ant-tasks">
      <pathelement location="${ceylon.home}/repo/com/redhat/ceylon/ant/0.1/com.redhat.ceylon.ant-0.1.jar"/>
    </path>
    <taskdef name="ceylonc" classname="com.redhat.ceylon.compiler.ant.Ceylonc" classpathref="ant-tasks"/>
    <taskdef name="ceylond" classname="com.redhat.ceylon.compiler.ant.Ceylond" classpathref="ant-tasks"/>
    <taskdef name="ceylon" classname="com.redhat.ceylon.compiler.ant.Ceylon" classpathref="ant-tasks"/> 

### Ceylonc ant task

    <ceylonc>
        <!-- By file name -->
        <include name="com/acme/module1/*.ceylon"/>
        <!-- By module name -->
        <module name="com.acme.module2"/>
    </ceylonc>

Supported attributes:

- `src`: Source path (default: `./source`)
- `out`: Output path (default: `./modules`)
- `executable`: Path to `ceylonc` program (default: `${ceylon.home}/bin/ceylonc`)
- `classpath`: Additional classpath for jars that are not Ceylon modules
- `classpathref`: Additional classpath id for jars that are not Ceylon modules

Nested elements:

    <!-- Module to compile -->
    <module name="module.name">
    <!-- File name to compile -->
    <include name="com/acme/*.ceylon"/>
    <!-- File name to not compile -->
    <exclude name="com/acme/*Test.ceylon"/>
    <!-- Additional module repository, defaults to ./modules -->
    <rep url="path/to/repository">

### Ceylond ant task

    <ceylond>
        <module name="com.acme.module"/>
    </ceylond>

Supported attributes:

- `src`: Source path (default: `./source`)
- `out`: Output path (default: `./modules`)
- `executable`: Path to `ceylond` program (default: `${ceylon.home}/bin/ceylond`)
- `omitSource`: Do not include pointers to the source code (default: no)
- `includePrivate`: Document even non-shared declarations (default: no)

Nested elements:

    <!-- Module to document -->
    <module name="module.name">
    <!-- Additional module repository, defaults to ./modules -->
    <rep url="path/to/repository">

### Ceylon ant task

    <ceylon>
        <module name="com.acme.module"/>
    </ceylon>

Supported attributes:

- `module`: Module name and version (name/version): required
- `run`: Name of toplevel method or class to run (default: `module.name.run`)
- `src`: Source path (default: `./source`)
- `executable`: Path to `ceylon` program (default: `${ceylon.home}/bin/ceylon`)

Nested elements:

    <!-- Additional module repository, defaults to ./modules -->
    <rep url="path/to/repository">
