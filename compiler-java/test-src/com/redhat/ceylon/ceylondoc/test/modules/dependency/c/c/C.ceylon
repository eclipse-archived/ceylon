import b { B }

class C() extends B(){}

doc "The classic Hello World program"
shared void helloworld() {
    if (nonempty args=process.arguments) {
        process.write("Hello ");
        for (String arg in args) {
            process.write(arg);
            process.write(" ");
        }
        process.write("\n");
    } else {
        process.writeLine("Hello World");
    }
}

shared void run(){
    helloworld();
}
