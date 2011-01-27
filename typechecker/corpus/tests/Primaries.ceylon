class Primaries() {

	class Literals() {
	
		String hello = "Hello";
		String names = "Gavin\nAndrew\nEmmanuel\n";
		Natural one = 1;
		Natural reallyLong = 123_456_789;
		Float zero = 0.0;
		Float big = 1.0e10;
		Float small = 1.0e-10;
		Float veryLong = 123_456.000_789;
		Character x = `x`;
		Character newline = `\n`;
		Quoted quoted = 'quoted literal';
		
		Class<Primaries> primariesClass = Primaries;
		Type<Primaries.Literals> literalsClass = Primaries.Literals;
		Attribute<Literals,String> helloAttribute = hello;
		Attribute<Primaries.Literals,Natural> oneAttribute = Primaries.Literals.one;
		Method<Object,Boolean,Object> method = Object.equals;
        Type<List<String>> stringListType = List<String>;
        //Type<Matrix<#5,#3>> matrix5by3Type = Matrix<#5,#3>;
        
        Natural kilo = 1k;
        Float mega = 1.5M;
        Natural giga = 3G;
        Float tera = 3.45T;
        
        Float milli = 12.3m;
        Float micro = 1.2u;
        Float nano = 12.0n;
        Float pico = 3.0p;
		
	}
	
	class Specials() {
	
		String? nothing = null;

		String[] empty = none;
		
		String? thisNothing = this.nothing;

		Specials s = this;
		
		Primaries p = outer;

		class Subclass() extends Specials() {
			String? superNothing = super.nothing;
			Primaries p = outer.outer;
		}
		
	}
	
	class Templates() {
		//String hw = "Hello" "World";
		String reference = "Hello" hw "World";
		String primary = "Hello" process.args.first "World";
		String literal = "Hello" 3 "World";
		String operator = "Hello" 1+1 "World";
		String booleanOperator = "Hello" true || false "World";
		String prefixOperator = "Hello" ++count "World";
		String postfixOperator = "Hello" count++ "World";
		String expOperator = "Hello" 3**2 "World";
		String expOperator2 = "Hello" count**2 "World";
		String negOperator = "Hello" -1 "World";
		String instOperator = "Hello" key -> value "World";
		String compareOperator = "Hello" count>10 "World";
		String paren = "Hello" (1+1) "World";
		String defaultOperator = "Hello" null ? 3 "World";
		String withStringLiteral = "Hello, " name ? "World" "!";
		String withStringLiteral2 = "Hello, " nickName ? name ? "World" "!";
		String withEnum = "Hello" {1,2,3} "World";
	}
	
	class Enumerations() {
	
		//List<String> empty = {};
		String[] singleton = { "hello" };
		String[] enumeration = { "foo", "bar", "baz" };
	
	}

	class MethodReferences() {
	
		class Person(String name) {
		
			shared variable String name := name;
			
			shared void hello() {
				log.info("Hello " + name);
			}
			
		}
		
		void helloWorld() {
			log.info("Hello World");
		}
			
		Person person = Person("Gavin King");
		void hello() = person.hello;
		void helloWorld() = helloWorld;
		String getName() = get(person.name);
		void setName(String name) = set(person.name);
		Person newPerson(String name) = Person;
		
	}
	
}
