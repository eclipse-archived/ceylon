doc "The classic Hello World program"
shared void hello(String name = "World") {
    print("Hello, " name "!");
}

doc "Run the module `@[module.name]`." 
shared void run(){
    if (nonempty args=process.arguments) {
        for (arg in args) {
            hello(arg);
        }
    }
    else {
        hello();
    }
}

