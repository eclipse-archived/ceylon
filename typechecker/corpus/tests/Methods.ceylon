class Methods {

	abstract class VoidMethods {

		void simpleMethod() {}
		
		void methodWithParams(Whole amount, Object something) {}
		
		abstract void abstractMethod();
		
		void methodReference() = this.voidMethod;
		
		void methodReferenceWithParams(Whole amount, Object something) = this.voidMethodWithParam;
		
		doc "This method has annotations. Remember that literal
		     strings can be spread across multiple lines."
		by "Gavin King"
		   "Andrew Haley"
		throws #Exception 
		       "if something goes wrong"
		see #Methods.VoidMethods.simpleMethod
		void annotatedMethod() {
			simpleMethod();
		}
		
		void methodWithTypeParameter<X>(OpenList<X> list, X x) { list.add(x); }
		
		void methodWithTypeConstraint<X>(OpenList<X> list, X x) where X>=String { list.add(x); }
		
		void methodWithNestedMethod() {
			void nestedMethod(String param) {
				log.info(param);
			}
			nestedMethod("Hello");
		
		}
		
		void methodWithNestedClass() {
			class NestedClass {
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
		
		methodWithFunctionalParameter(method(String x) { log.info(x); });
		
	}
	
	abstract class NonvoidMethods {
	
		String simpleMethod() { return "Hello"; }
		
		String methodWithParam(Whole amount) { return $amount; }
		
		abstract String abstractMethod();
		
		String methodReference() = this.nonvoidMethod;
		
		String methodReferenceWithParam(Whole amount) = this.nonvoidMethodWithParam;
		
		doc "This method has annotations. Remember that literal
		     strings can be spread across multiple lines."
		by "Gavin King"
		   "Andrew Haley"
		throws #Exception 
		       "if something goes wrong"
		see #Methods.VoidMethods.simpleMethod
		String annotatedMethod() {
			return simpleMethod();
		}
		
		List<X> methodWithTypeParameter<X>(X x) { return List(x); }
		
		List<X> methodWithTypeConstraint<X>(X x) where X>=String { return List(x); }
		
		String methodWithNestedMethod() {
			String nestedMethod(String param) {
				return param;
			}
			return nestedMethod("Hello");
		}
		
		String methodWithNestedClass() {
			class NestedClass {
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
		
		log.info( methodWithFunctionalParameter(method() { return "Hello"; }) );
		
	}

}