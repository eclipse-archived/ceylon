import check { check }

class Test585_1(){    
    shared class Bar {      
        shared new (){
            Object b = outer;
            check(b is Test585_1, "#585.1");
        }               
    }   
}

class Test585_2(){    
    shared class Bar {      
        shared new Baz(){
            Object b = outer;
            check(b is Test585_2, "#585.2");
        }               
    }   
}

void testConstructors() {
  Test585_1().Bar();
  Test585_2().Bar.Baz();
}
