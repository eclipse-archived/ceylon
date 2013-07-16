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
 
void m0(Person[] people) {
    Boolean minors;
    for (p in people) {
        if (p.age<18) {
            minors = true;
            break;
        }
    }
    else {
        minors = false;
    }
}

void m1(Person[] people) {
    Boolean minors;
    for (p in people) {
        if (p.age<18) {
            if (1==1) {
                continue;
            }
            minors = true;
            break;
        }
    }
    else {
        minors = false;
    }
}

void m2(Person[] people) {
    Boolean minors;
    for (p in people) {
        if (p.age<18) {
            @error minors = true;
            if (1==1) {
                continue;
            }
            break;
        }
    }
    else {
        minors = false;
    }
}

void m3(Person[] people) {
    Boolean minors;
    for (p in people) {
        if (p.age<18) {
            String s;
            s = "hello";
            if (1==1) {
                continue;
            }
            minors = true;
            break;
        }
    }
    else {
        minors = false;
    }
}

void m4(Person[] people) {
    Boolean minors;
    for (p in people) {
        if (p.age<18) {
            String s;
            minors = true;
            s = "hello";
            if (1==1) {
                print(s);
            }
            else {
                for (c in s) {
                    continue;
                }
            }
            break;
        }
    }
    else {
        minors = false;
    }
}