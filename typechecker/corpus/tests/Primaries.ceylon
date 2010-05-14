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
	
	class Members {
	
		
	
	}
	
	class Enumerations {
	}
	
	class Specials {
	}

}