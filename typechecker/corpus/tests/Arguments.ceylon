class Arguments {
	
	class NoArguments {
		
		void greet(String value="Hello") {
			log.info(value);
		}
		
		greet();
		greet {};
		
	}
	
	class SingleArgument {
		
		void out(String value) {
			log.info(value);
		}
		
		out("Hello");
		out { value="Hi"; };
		
	}
	
	class MutipleArguments {
	
		String fullName(String firstName, String lastName) {
			return firstName + " " + lastName;
		}
		
		String gavin = fullName("Gavin", "King"); //positional param method invocation
		String andrew = fullName { firstName="Andrew"; lastName="Haley"; }; //named param method invocation
		
	}
	
	class Varargs {
	
		String join(String sep, String... strings) {
			return (sep+" ").join(strings);
		}
		
		String joined1 = join(",", "foo", "bar"); //positional param method invocation with varargs
		String joined2 = join { sep=","; "one", "two" }; //named param method invocation with varargs
		
	}
	
	class FunctionalArguments {
	
		String stringify(Natural n, String process(String value), String format(Natural n)) {
			return process(format(n));
		}
		
		/*String result1 = stringify(256, 
				process(String value) { return "value=" + value; },
				format(Natural n) { return $n; });*/
				
		String result2 = stringify { 
			n=256; 
			process(String value) { return "value=" + value; }
			format(Natural n) { return $n; }
		};
		
	}		

}