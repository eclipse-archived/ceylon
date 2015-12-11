import ceylon.language.meta{invokeCallable}
import ceylon.language.meta.model{IncompatibleTypeException}


abstract class Vertex(id) of Partial|Factory{
    "The id"// It's an object rather than an id to save space: we require a lot of partials!
    shared Object id;
    
    shared variable Integer index = -1;
    shared variable Integer lowlink = -1;
    shared variable Boolean onStack = false;
    shared variable Integer scc = -1;
    
    shared variable Integer traversal = 0;
    shared default Boolean reset(Integer traversal) {
        if (this.traversal != traversal) {
            index = -1;
            lowlink = -1;
            onStack = false;
            this.traversal = traversal;
            return true;
       }
       return false;
    }
    
    shared formal {Object*} refersTo;
    
    shared formal Boolean initialized;
    
    "Gets the fully initialized instance, or throws"
    shared formal Anything instance();
}
 
class Factory(Object id) extends Vertex(id) {
     "The factory function, if it's been set"
     shared variable Callable<Anything,Nothing>? callable = null;
     "The ids of the arguments, if they've been set"
     shared variable Object[]? arguments = null;
     
     shared actual {Object*} refersTo => arguments else [];
     
     variable Boolean called = false;
     
     variable Anything result = null;
     
     shared Anything call<Id>(Integer scc, NativeMap<Id,Anything> instances) {
         assert(!called);
         assert(exists fn=callable);
         assert(exists args=this.arguments);
         Array<Anything> arguments = Array<Anything>.ofSize(args.size, null);
         variable value index = 0;
         for (id in args) {
             assert(is Id id);
             value argument = instances.get(id);
             if (is Vertex argument) {
                 // check that all the arguments were in previous SCCs
                 assert (argument.scc != -1);
                 if (argument.scc == this.scc) {
                      throw DeserializationException("cyclic factory invocation: id=``this.id``, scc=``scc``");
                 }
                 arguments.set(index, argument.instance());
             } else {
               arguments.set(index, argument);
           }
             index++;
         }
         // if so we can invoke it
         try {
             result = invokeCallable(fn, arguments.sequence());
         } catch (IncompatibleTypeException e) {
             throw DeserializationException("error invoking factory ``id``: ``e.message``");
         }
         called = true;
         return result;
     }
     
     shared actual Boolean initialized => called;
     
     "Gets the fully initialized instance, or throws"
     shared actual Anything instance() {
         assert (!called);
         return result;
     }
 }