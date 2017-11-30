class BrokenActualMethodParametersSub<A>() extends 
    BrokenActualMethodParameters<A>(){}
object brokenActualMethodParametersSub extends 
    BrokenActualMethodParameters<Integer>(){}

shared void brokenActualMethodParametersUsage() {
    void isExpectedError(Throwable t, Boolean ok(String message)) {
        if (!ok(t.message)) {
            throw Exception("Unexpected error message: \"``t.message``\"");
        }
    }
    for (c in {BrokenActualMethodParameters<Integer>(), 
                BrokenActualMethodParametersSub<Integer>(), 
                brokenActualMethodParameters,
                brokenActualMethodParametersSub}) {
        print(c);
        
        try {
            c.simpleMethodNullary();
            throw;
        } catch (Throwable e) {
            isExpectedError(e, (message)=>message.startsWith("member does not have the same number of parameters as the member it refines"));
        }
        try {
            c.tpMethodANullary();
            throw;
        } catch (Throwable e) {
            isExpectedError(e, (message)=>message.startsWith("member does not have the same number of parameters as the member it refines"));
        }
        try {
            c.tpMethodBNullary();
            throw;
        } catch (Throwable e) {
            isExpectedError(e, (message)=>message.startsWith("member does not have the same number of parameters as the member it refines"));
        }
        try {
            c.tpMethodCNullary<Boolean>();
            throw;
        } catch (Throwable e) {
            isExpectedError(e, (message)=>message.startsWith("member does not have the same number of parameters as the member it refines"));
        }
        try {
            c.simpleMethodUnary("");
            throw;
        } catch (Throwable e) {
            isExpectedError(e, (message)=>message.startsWith("member does not have the same number of parameters as the member it refines"));
        }
        try {
            c.tpMethodAUnary(1);
            throw;
        } catch (Throwable e) {
            isExpectedError(e, (message)=>message.startsWith("missing parameter list in function declaration"));
        }
        try {
            c.tpMethodBUnary("");
            throw;
        } catch (Throwable e) {
            isExpectedError(e, (message)=>message.lowercased.equals("type of parameter 'f' of 'tpMethodBUnary' declared by 'BrokenActualMethodParameters' is different to type of corresponding parameter 'arg' of refined member 'tpMethodBUnary' of 'FormalMethods': 'Float' is not exactly 'String'".lowercased));
        }
        try {
            c.tpMethodCUnary<Boolean>(true);
            //throw;
        } catch (Throwable e) {
            //isExpectedError(e, (message)=>message.equals("foo"));
            throw e;
        }
        try {
            c.methodMpl<Boolean>(1);
            throw;
        } catch (Throwable e) {
            isExpectedError(e, (message)=>message.equals("member must have the same number of parameter lists as refined member: 'methodMpl' in 'FormalMethods'"));
        }
        try {
            c.simpleMethodFunctional(void(String s) => s.initial(0));
            throw;
        } catch (Throwable e) {
            isExpectedError(e, (message)=>message.lowercased.equals("type of parameter 'functional' of 'simpleMethodFunctional' declared by 'BrokenActualMethodParameters' is different to type of corresponding parameter 'functional' of refined member 'simpleMethodFunctional' of 'FormalMethods': 'Anything(Float)' is not exactly 'Anything(String)'".lowercased));
        }
        try {
            c.tpMethodFunctionalA(function(Integer s) => "");
            throw;
        } catch (Throwable e) {
            isExpectedError(e, (message)=>message.lowercased.startsWith("type of parameter 'functional' of 'tpMethodFunctionalA' declared by 'BrokenActualMethodParameters' is different to type of corresponding parameter 'functional' of refined member 'tpMethodFunctionalA' of 'FormalMethods': 'String()' is not exactly 'String(".lowercased));
        }
        /*TODOtry {
            c.tpMethodFunctionalB(function(String s) => 1);
            throw;
        } catch (Throwable e) {
            isExpectedError(e, (message)=>message.equals("foo"));
        }
        try {
            c.tpMethodFunctionalC<Boolean>(function(Boolean s) => "");
            throw;
        } catch (Throwable e) {
            isExpectedError(e, (message)=>message.equals("foo"));
        }*/
    }
    print("OK");
}
