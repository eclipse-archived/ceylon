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
		
		void out(String value) {
			log.info(value);
		}
		
		out("Hello");
		out { value="Hi"; };
		
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
		
		String gavin = fullName("Gavin", "King");
		String andrew = fullName { firstName="Andrew"; lastName="Haley"; };
		
		class FullName(String firstName, String lastName) {
			String value = firstName + " " + lastName;
		}
		
		String gk = FullName("Gavin", "King").value;
		String ah = FullName { firstName="Andrew"; lastName="Haley"; }.value;
		
	}
	
	class Varargs {
	
		String join(String sep, String... strings) {
			return (sep+" ").join(strings);
		}
		
		String joined1 = join(",", "foo", "bar"); 
		String joined2 = join { sep=","; "one", "two" };
		
		String joined3 = join( ",", {"foo", "bar"} ); 
		String joined4 = join { sep=","; strings={"one", "two"}; };
		
		List<String> tokens1 = List("foo", "bar", "baz");
		List<String> tokens2 = List { "one", "two", "three" };
		
		List<String> tokens3 = List( {"foo", "bar", "baz"} );
		List<String> tokens4 = List { strings={"one", "two", "three"}; };
		
	}
	
	class FunctionalArguments {
	
		String stringify(Natural n, String process(String value), String format(Natural n)) {
			return process(format(n));
		}
		
		/*String result1 = stringify(256, 
				process(String value) { return "value=" + value; },
				format(Natural n) { return $n; });*/
				
		String result = stringify { 
			n=256; 
			process(String value) { return "value=" + value; }
			format(Natural n) { return $n; }
		};
		
		class Processor<X,Y>(Y process(X x)) {
			Y handle(X x) { 
				return process(x).strip; 
			}
		}
		
		String string = Processor<Float,String> {
			process(Float f) {
				return $f;
			}
		}.handle(1.25);
		
	}

}