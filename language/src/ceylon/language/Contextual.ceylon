import java.lang { ThreadLocal }

"""Stores values local to the current thread of execution
   meaning that each thread or process that accesses these
   values get to see their own copy. If the underlying
   platform does not support threading the practical effect
   of this class is no different than using a local value.
   
   Setting a value is done by creating an instance of
   `Contextual.Using`, passing it the required value, or a
   function that will return the needed value when necessary,
   and passing it to a try-resource statement.
   
   If a function is used to set the value then that value will
   be retrieved the moment the try-resource block is entered.
   If the same `Using` is re-used then the value will be
   refreshed by calling the function again.
   
   When entering a try-resource block any previous value is
   stored and then restored at the end of the block so nested
   try-resource blocks are possible.
   
   Retrieving the value is done using `get()`. Doing so when
   no try-resource statement is active will result in an
   assertion exception.
   
   An example:
   
       Contextual<String> stringValue = Contextual<String>();
       Contextual<Integer> intValue = Contextual<Integer>();
       try (stringValue.Using("foo"),
               intValue.Using(system.milliseconds)) {
           print(stringValue.get()); // prints "foo"
           print(intValue.get());    // prints the current time in ms
           try (stringValue.Using("bar")) {
               print(stringValue.get()); // prints "bar"
               print(intValue.get());    // prints same number as before
           }
       }
       
   NB: This example only shows how to *use* `Contextual` and
   does not show anything thread-related.
   """
by("Tako Schotanus")
native
shared class Contextual<Element>() {
    
    "Retrieves the value previously set. Will throw an assertion
     exception if called when not within a try-resource block"
    native shared Element get();
    
    "Used to set a value for this `Contextual`"
    native shared class Using(Element|Element() newValue)
            satisfies Obtainable {
        native shared actual void obtain() {}
        native shared actual void release(Throwable? error) {}
    }
}

native("jvm")
shared class Contextual<Element>() {
    value threadLocal = ThreadLocal<Element>();
    
    native("jvm") shared Element get() {
        assert (exists result = threadLocal.get());
        return result;
    }
    
    native("jvm") shared class Using(Element|Element() newValue)
            satisfies Obtainable {
        variable Element? previous = null; 
        
        native("jvm") shared actual void obtain() {
            previous = threadLocal.get();
            if (is Element() newValue) {
                threadLocal.set(newValue());    
            } else {
                threadLocal.set(newValue);
            }
        }
        
        native("jvm") shared actual void release(Throwable? error) {
            if (exists p=previous) {
                threadLocal.set(p);
            } else {
                threadLocal.remove();
            }
        }
    }
}

native("js")
shared class Contextual<Element>() {
    variable Element? val = null;
    
    native("js") shared Element get() {
        assert (exists result = val);
        return result;
    }
    
    native("js") shared class Using(Element|Element() newValue)
            satisfies Obtainable {
        variable Element? previous = null; 
        
        native("js") shared actual void obtain() {
            previous = val;
            if (is Element() newValue) {
                val = newValue();    
            } else {
                val = newValue;
            }
        }
        
        native("js") shared actual void release(Throwable? error) {
            if (exists p=previous) {
                val = p;
            } else {
                val = null;
            }
        }
    }
}
