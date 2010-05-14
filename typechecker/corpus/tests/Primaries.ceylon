class Primaries {

	class Literals {
	
		String hello = "Hello";
		Natural one = 1;
		Float zero = 0.0;
		Float big = 1.0e10;
		Float small = 1.0e-10;
		Quoted quoted = 'quoted literal';
		
		Type<Literals> primariesClass = #Primaries;
		Type<Literals> literalsClass = #Primaries.Literals;
		Attribute<Literals,String> helloAttribute = #hello;
		Attribute<Literals,String> oneAttribute = #Primaries.Literals.one;
		Method<Object,Boolean> method = #Object.equals;
		
	}
	
	class Specials {
	
		optional String nothing = null;

		List<String> empty = none;

		Specials l = this;

		class Subclass extends Specials {
			Specials s = super;
		}
		
	}
	
	class Enumerations {
	
		List<String> empty = {};
		List<String> singleton = { "hello" };
		List<String> enumeration = { "foo", "bar", "baz" };
	
	}

	class MethodReferences {
	
		class Person(String name) {
		
			mutable String name := name;
			
			void hello() {
				log.info("Hello ${name}");
			}
			
			static void helloWorld() {
				log.info("Hello World");
			}
			
		}
		
		Person g = Person("Gavin King");
		void hello() = g.hello;
		void helloWorld() = Person.helloWorld;
		String getName() = get g.name;
		void setName(String val) = set g.name;
		
	}
	
}