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

void m5(Person[] people) {
    Boolean minors;
    for (p in people) {
        if (p.age<18) {
            if (1==1) {
                minors = true;
            }
            break;
        }
    }
    else {
        minors = false;
    }
}

void m6(Person[] people) {
    Boolean minors;
    String name;
    Integer age;
    for (p in people) {
        if (p.age<18) {
            if (1==1) {
                minors = true;
            }
            else {
                @error name = p.string;
                continue;
            }
            @error minors = false;
            age = p.age;
            break;
        }
    }
    else {
        minors = false;
    }
}

void m7(Person[] people) {
    Boolean minors;
    String name;
    Integer age;
    for (p in people) {
        if (p.age<18) {
            if (1==1) {
                @error name = p.string;
                continue;
            }
            else {
                minors = true;
            }
            @error minors = false;
            age = p.age;
            break;
        }
    }
    else {
        minors = false;
    }
}

void m8(Person[] people) {
    Boolean minors;
    String name;
    Integer age;
    for (p in people) {
        if (p.age<18) {
            switch (1==1) 
            case (true) {
                @error name = p.string;
                continue;
            }
            case (false) {
                minors = true;
            }
            age = p.age;
            break;
        }
    }
    else {
        minors = false;
    }
}

void m9(Person[] people) {
    Boolean minors;
    String name;
    Integer age;
    for (p in people) {
        if (p.age<18) {
            try {
                @error name = p.string;
                continue;
            }
            catch (e) {
                minors = true;
            }
            age = p.age;
            break;
        }
    }
    else {
        minors = false;
    }
}

void m10(Person[] people) {
    Boolean minors;
    String name;
    Integer age;
    for (p in people) {
        if (p.age<18) {
            try {
                minors = true;
            }
            catch (e) {
                @error name = p.string;
                continue;
            }
            age = p.age;
            break;
        }
    }
    else {
        minors = false;
    }
}

void n1(Person[] people) {
    variable Boolean minors;
    Integer age;
    for (p in people) {
        if (p.age<18) {
            if (false) {
                minors = true;
            }
            break;
        }
    }
    else {
        minors = false;
    }
    @error print(minors);
}

void n2(Person[] people) {
    variable Boolean minors;
    Integer age;
    for (p in people) {
        if (p.age<18) {
            if (false) {
                //minors = true;
            }
            //break;
        }
    }
    else {
        minors = false;
    }
    print(minors);
}

void n3(Person[] people) {
    variable Boolean minors;
    Integer age;
    for (p in people) {
        if (p.age<18) {
            if (false) {
                minors = true;
                break;
            }
        }
    }
    else {
        minors = false;
    }
    print(minors);
}

