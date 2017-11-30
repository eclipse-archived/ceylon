import com.example.interop { JavaHelper { javaPrint }}
import java.lang { JInteger = Integer }

"The classic Hello World program"
shared void hello(String name = "World") {
    value i = JInteger(2);
    javaPrint("Hello, `` name ``!");
    JavaPrinter(name).print("Hello again, ");
}

"The runnable method of the module." 
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
