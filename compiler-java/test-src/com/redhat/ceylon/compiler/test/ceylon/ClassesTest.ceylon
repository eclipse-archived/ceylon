shared class ClassesTest() extends Test() {
	
	@test
	shared void testWithMethods() {	
		class WithMethods() {
			void doNothing() {}
			shared Natural returnZero() { return 0; }
			shared String returnArgument(String arg) { return arg; }		
		}
		
		WithMethods instance = WithMethods();
		assertEquals(0,instance.returnZero());
		assertEquals("Flavio",instance.returnArgument("Flavio"));
	}

	@test
	shared void testWithAtributes() {
		class WithAttributes() {
			shared variable Natural count := 0;		
			shared void inc() { count += 1;}		
		}
	
		WithAttributes instance = WithAttributes();
		assertEquals(0,instance.count);
		instance.inc();
		assertEquals(1,instance.count);
	}
	
	@test
	shared void testWithAbstractMethods() {
		abstract class WithAbstractMethods() {					
			shared formal Natural returnZero();		
		}
		
		class Concrete() extends WithAbstractMethods() {
			shared actual Natural returnZero() {return 0;}		
		}
		
		Concrete instance = Concrete();
		assertEquals(0, instance.returnZero());
	}
	
	@test
	shared void testWithAbstractAttributes() {
		abstract class Hello() {		    
		    shared formal String greeting;		    
		    shared String say() {
		        return greeting;
		    }
		}
		
		class DefaultHello() extends Hello() {
		    shared actual String greeting {
		        return "Hello, World!";
		    }
		}

		DefaultHello hello = DefaultHello();
		assertEquals("Hello, World!", hello.say());
	}
	
	@test
	shared void testWithParameters() {
		abstract class Hello(String name) {		    
		    shared formal String greeting;		    
		    shared String say() {
		        return greeting + " " + name;
		    }
		}
		
		class DefaultHello(String name) extends Hello(name) {
		    shared actual String greeting {
		        return "Hello, World!";
		    }
		}
		String name = "Flavio";
		DefaultHello hello = DefaultHello(name);
		assertEquals("Hello, World!" + " " + name, hello.say());
	}	
		
}