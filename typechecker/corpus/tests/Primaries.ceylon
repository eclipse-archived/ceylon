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
        Type<Matrix<#5,#3>> matrix5by3Type = Matrix<#5,#3>;
        
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

		class Subclass() extends Specials() {
			String? superNothing = super.nothing;
		}
		
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
