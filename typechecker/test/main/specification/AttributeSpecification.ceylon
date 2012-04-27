class Hello(String? name) {

       shared Hello brokenSelfRef {
           @error return this;
       }
       
       shared void brokenForwardRef() {
           @error printMessage("foo");
       }

       String greeting;
       if (exists name) {
           greeting = "Hi " + name;
       }
       else {
           greeting = "Hi";
       }

       shared void say() {
           printMessage(greeting);
       }

       shared void printMessage(String message) {
           print(message);
       }
       
       shared Hello thiz {
           return this;
       }
}

class Person() {
    shared Integer age = 0;
}
 
void m(Person[] people) {
    Boolean minors;
    for (p in people) {
        if (p.age<18) {
            //@error 
            minors = true;
            break;
        }
    }
    else {
        minors = false;
    }
}