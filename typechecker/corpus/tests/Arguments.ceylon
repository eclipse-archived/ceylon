class Arguments {
	
	class NoArguments {
		
		void greet(String value="Hello") {
			log.info(value);
		}
		
		greet();
		greet {};
		
		class Greeting(String value="Hello") {
			log.info(value);
		}
		
		Greeting();
		Greeting {};
		
	}
	
	class SingleArgument {
		
		void outp(String value) {
			log.info(value);
		}
		
		outp("Hello");
		outp { value="Hi"; };
		
		class Output(String value) {
			log.info(value);
		}
		
		Output("Hello");
		Output { value="Hi"; };
		
	}
	
	class MultipleArguments {
	
		String fullName(String firstName, String lastName) {
			return firstName + " " + lastName;
		}
		
		String methodResultWithPositionalParams = fullName("Gavin", "King");
		String methodResultWithNamedParams = fullName { firstName="Andrew"; lastName="Haley"; };
		
		class FullName(String firstName, String lastName) {
			String value = firstName + " " + lastName;
		}
		
		FullName instantiationResultWithPositionalParams = FullName("Gavin", "King");
		FullName instantiationResultWithNamedParams = FullName { firstName="Andrew"; lastName="Haley"; };
		
	}
	
	class Varargs {
	
		String join(String sep, String... strings) {
			return (sep+" ").join(strings);
		}
		
		String methodResultWithPositionalVarargs = join(",", "foo", "bar"); 
		String methodResultWithNamedVarargs = join { sep=","; "one", "two" };
		
		String methodResultWithPositionalEnumerationParam = join( ",", {"foo", "bar"} ); 
		String methodResultWithNamedEnumerationParam = join { sep=","; strings={"one", "two"}; };
		
		List<String> instantiationResultWithPositionalVarargs = List("foo", "bar", "baz");
		List<String> instantiationResultWithNamedVarargs = List { "one", "two", "three" };
		
		List<String> instantiationResultWithPositionalEnumerationParam = List( {"foo", "bar", "baz"} );
		List<String> instantiationResultWithNamedEnumerationParam = List { strings={"one", "two", "three"}; };
		
	}
	
	class FunctionalArguments {
	
		void logLazily(String message()) {
			if (log.infoEnabled) { 
				log.info( message() );
			}
		}
		
		logLazily() message ("hello");
		logLazily() message { return "hello" };
		logLazily { message() { return "hello" } };
	
	
		public static void from<Y>(Y initial, 
								   Boolean until(Y y), 
								   Y each(Y y), 
								   void perform(Y y)) {
			do (mutable Y y := initial)
			while (!until(y)) {
				perform(y);
				y:=each(y);
			}
		}
		
		from(0) 
			until (Y y) (y==10)
			each (Y y) (y+2)
			perform (Y y) { log.info(y); };
		
		from(0) 
			until(Y y) { return y==10 } 
			each(Y y) { return y+2 } 
			perform(Y y) { log.info(y); };
		
		from {
			initial=0; 
			until(Y y) { return y==10 } 
			each(Y y) { return y+2 } 
			perform(Y y) { log.info(y); }
		};
		
		class Processor<X,Y>(Y process(X x)) {
			Y handle(X x) { return process(x).strip }
		}
		
		Processor<Float,String> p = 
			Processor<Float,String>() 
				process (Float f) ($f);
		
		Processor<Float,String> q = 
			Processor<Float,String>() 
				process (Float f) { return $f };
		
		Processor<Float,String> r = 
			Processor<Float,String> { 
				process (Float f) { return $f }
			};
			
	}

}