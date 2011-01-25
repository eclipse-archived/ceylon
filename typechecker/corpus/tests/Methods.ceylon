class Methods() {

	abstract class VoidMethods() {

		void simpleMethod() {}
		
		void methodWithParams(Whole amount, Object something) {}
		
		void methodWithMultipleParamLists()(Whole amount, Object something) {
			return this.methodWithParams
		}
		
		formal void abstractMethod();
		
		void methodReference() = this.simpleMethod;
		
		void methodReferenceWithParams(Whole amount, Object something) = this.voidMethodWithParam;
		
		doc "This method has annotations. Remember that literal
		     strings can be spread across multiple lines."
		by "Gavin King"
		   "Andrew Haley"
		throws (Exception 
		        -> "if something goes wrong")
		see (Methods.VoidMethods.simpleMethod)
		void annotatedMethod() {
			simpleMethod();
		}
		
		void methodWithTypeParameter<X>(OpenList<X> list, X x) { list.add(x); }
		
		void methodWithTypeConstraint<X>(OpenList<X> list, X x) given X satisfies String { list.add(x); }
		
		void methodWithNestedMethod() {
			void nestedMethod(String param) {
				log.info(param);
			}
			nestedMethod("Hello");
		
		}
		
		void methodWithNestedClass() {
			class NestedClass() {
				void hello() {
					log.info("Hello");
				}
			}
			NestedClass().hello();
		}
		
		void methodWithLocal() {
			String hello = "Hello";
			log.info(hello);
		}
		
		void methodWithFunctionalParameter(void method(String param)) {
			method("Hello");
		}
		
		methodWithFunctionalParameter { 
			void method(String x) { log.info(x); } 
		};
		
	}
	
	abstract class NonvoidMethods() {
	
		String simpleMethod() { return "Hello"; }
		
		String methodWithParam(Whole amount) { return $amount; }
		
		String methodWithMultipleParamLists(Boolean b)(Whole amount) {
			if (b) {
				return this.methodWithParam
			}
			else {
				throw
			}
		}
		
		formal String abstractMethod();
		
		String methodReference() = this.simpleMethod;
		
		String methodReferenceWithParam(Whole amount) = this.nonvoidMethodWithParam;
		
		doc "This method has annotations. Remember that literal
		     strings can be spread across multiple lines."
		by "Gavin King"
		   "Andrew Haley"
		throws (Exception 
		        -> "if something goes wrong")
		see (Methods.VoidMethods.simpleMethod)
		String annotatedMethod() {
			return simpleMethod();
		}
		
		List<X> methodWithTypeParameter<X>(X x) { return List(x); }
		
		List<X> methodWithTypeConstraint<X>(X x) given X satisfies String { return List(x); }
		
		String methodWithNestedMethod() {
			String nestedMethod(String param) {
				return param;
			}
			return nestedMethod("Hello");
		}
		
		String methodWithNestedClass() {
			class NestedClass() {
				String hello = "Hello";
			}
			return NestedClass().hello;
		}
		
		String methodWithLocal() {
			String hello = simpleMethod();
			return hello;
		}
		
		String methodWithFunctionalParameter(String method()) {
			return method();
		}
		
		log.info { 
			methodWithFunctionalParameter { 
				String method() { return "Hello"; } 
			} 
		};
		
        class Name(String firstName, Character? initial, String lastName) {
            shared String firstName = firstName;
            shared Character? initial = initial;
            shared String lastName = lastName;
        }
    
        Name namedArgumentMethod(String firstName, String lastName) {
            firstName = firstName;
            initial = null;
            lastName = lastName;
        }
    
        Div namedArgumentMethodWithVarargs(String firstName, String lastName) {
            cssClass = "name";
        	firstName, lastName
        }
	}

}
