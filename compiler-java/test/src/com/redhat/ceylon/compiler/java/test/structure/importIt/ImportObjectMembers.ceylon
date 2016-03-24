import com.redhat.ceylon.compiler.java.test.structure.importIt.obj{
    toplevel{
        fn, 
        val, 
        Class,
        Interface//, 
        //inner
    }
}
@noanno
void importObjectMembers() {
    fn();
    value fnRef = fn;
    
    val = val;//
    val++;//
    ++val;//
    val+=1;//
    
    Class c = Class();//
    value classRef = Class;
    //object extend extends Class(){}
    
    Interface i = c;
    //object satisfy satisfies Interface {
    //}
    
    function f(Class c,Interface i) {
        return nothing;
    }
    
    //print(inner);
}