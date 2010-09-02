class Primaries() {

	class Literals() {
	
		String hello = "Hello";
		String names = "Gavin\nAndrew\nEmmanuel\n";
		Natural one = 1;
		Float zero = 0.0;
		Float big = 1.0e10;
		Float small = 1.0e-10;
		Character x = `x`;
		Character newline = `\n`;
		Quoted quoted = 'quoted literal';
		
		Type<Literals> primariesClass = Primaries;
		Type<Literals> literalsClass = Primaries.Literals;
		Attribute<Literals,String> helloAttribute = hello;
		Attribute<Literals,String> oneAttribute = Primaries.Literals.one;
		Method<Object,Boolean> method = Object.equals;
		
	}
	
	class Specials() {
	
		String? nothing = null;

		List<String> empty = none;

		Specials s = this;

		class Subclass() extends Specials() {
			Specials s = super;
		}
		
	}
	
	class Enumerations() {
	
		List<String> empty = {};
		List<String> singleton = { "hello" };
		List<String> enumeration = { "foo", "bar", "baz" };
	
	}

	class MethodReferences() {
	
		class Person(String name) {
		
			mutable String name := name;
			
			void hello() {
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