doc "The classic Hello World program"
shared void helloworld() {
    if (nonempty args=process.arguments) {
        process.write("Hello ");
        for (String arg in args) {
            process.write(arg);
            process.write(" ");
        }
        process.writeLine(args.first);
    } else {
        process.writeLine("Hello World");
    }
}
