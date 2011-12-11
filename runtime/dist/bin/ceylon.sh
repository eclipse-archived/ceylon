## TODO -- add proper script
java -Dboot.module.loader=ceylon.modules.bootstrap.loader.DistributionModuleLoader -cp jboss-modules.jar:ceylon-runtime-bootstrap.jar org.jboss.modules.Main -mp .. ceylon.runtime +repository ~/.ceylon/repo +executable ceylon.modules.jboss.runtime.JBossRuntime +module $1
