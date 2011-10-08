@nomodel
doc "The classic Hello World program"
shared void helloworld() {
    if (nonempty args=process.arguments) {
        process.write("Hello ");
        process.writeLine(args.first);
    } else {
        process.writeLine("Hello World");
    }
}