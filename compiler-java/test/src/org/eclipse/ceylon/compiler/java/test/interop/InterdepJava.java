package org.eclipse.ceylon.compiler.java.test.interop;

@interface InterdepJavaAnnotation {
    int value();
}

interface InterdepJavaInterface {
    Interdep method(Interdep i);
}

@CeylonAnnotation$annotation$(x = 2)
public class InterdepJava {
    public InterdepJava(Interdep ceylon) {
        ceylon.foo(this);
        toplevel_.get_().foo(this);
        interdepField = ceylon;
    }
    
    public void foo(Interdep ceylon){}
    
    public Interdep interdepField;
    
    public Interdep getInterdepProperty(){
        return interdepField;
    }
    
    public interface InnerInterface {
        public Interdep$InnerInterface bar(Interdep$InnerInterface ceylon);
    }
    public class Inner implements InnerInterface {
        public Inner(Interdep.Inner ceylon, Interdep$InnerInterface ceylonInterface){
            Inner i = ceylon.foo(this);
            InnerInterface j = ceylon.bar(this);
        }
        public Interdep.Inner foo(Interdep.Inner ceylon){
            return ceylon;
        }
        public Interdep$InnerInterface bar(Interdep$InnerInterface ceylon){
            return ceylon;
        }
    }
}

