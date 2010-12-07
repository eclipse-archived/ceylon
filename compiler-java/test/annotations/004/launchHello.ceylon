void launchHello(Process p) {
     Hello greeter = Hello("Andrew");
     p.writeLine(greeter.greeting());
     p.writeLine(Hello("Emmanuel").greeting());
}
