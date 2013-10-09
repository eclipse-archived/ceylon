CEYLON RUNTIME
==============

Build and test the Ceylon runtime
---------------------------------

For setting up the development environment and compiling and building the distribution
take a look at [ceylon-dist](https://github.com/ceylon/ceylon-spec/README.md).

If after having built the distribution you want to build and test the runtime
return to `ceylon-runtime` and run

    ant clean publish
    
To run the tests type

    ant test
    
Building with Maven (obsolete)
------------------------------

This project can also be built using Maven. THe above procedure is preferred though.
There are a few steps needed to be able to build and test the project.

 * We need to install Ceylon Language and Common into our local Maven repository.

mvn install:install-file -Dfile=ceylon.language-0.6.car -DgroupId=ceylon.language -DartifactId=ceylon-language -Dversion=0.6 -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true

mvn install:install-file -Dfile=com.redhat.ceylon.common-0.6.jar -DgroupId=com.redhat.ceylon -DartifactId=ceylon-common -Dversion=0.6 -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true

 * We need to checkout and build (mvn clean install) Ceylon Module Resolver project (CMR).

https://github.com/ceylon/ceylon-module-resolver

The CMR build version must match the one used in this project -- see root pom.xml.


The Runtime uses JBoss Modules to build modular runtime system.
Initially JBoss Modules uses out dist/ directory as its modules repository.
This includes modules we need on the "bootstrap":
 * Ceylon Language
 * Ceylon Module Resolver
 * Ceylon Runtime
 * JBoss Modules (as a module info, actual classes are part of system classpath)

dist/ repository is built as part of the testsuite sub-project build.
jars are copied from our Maven repository, while the module.xml information is static.

This is the part that the user needs locally.
To ease things, we create a zipped version of dist/ repository,
and place it under <CEYLON_REPOSITORY>/ceylon-runtime-bootstrap/ceylon-runtime-bootstrap.zip
In order to use this zipped module repository we need to use custom ModuleLoader - DistributionModuleLoader.
(see ceylon.sh script for the actual usage)
DistributionModuleLoader explodes (if not already present) this zipped repository at initialization,
and places the exploded repository under <CEYLON_REPOSITORY>/ceylon-runtime-bootstrap/ceylon-runtime-bootstrap-exploded directory.
You can force update with -Dforce.bootstrap.update=true system property flag.

Afterwards Ceylon Runtime uses Ceylon Module Resolver to get its modules.
By default we use ~/.ceylon/repo as local CMR repository, but different repositories can be mounted.
(proper mounting API is wip)

In order to run your Ceylon app / module, you need to first place it into ~/.ceylon/repo.
Then you can use dist/bin/ceylon.sh to run the app / module.

ceylon.sh expects module name and version as its only parameter.
e.g. ./ceylon.sh hello/1.0.0 --> [full module name]/[version], where default version is 0.0.0 if left out

(I think the version atm needs to be in the form x.y.z[.qualifier]).

