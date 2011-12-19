interface Hiding {
	class Hidden() {}
	String hidden() { return "hello"; }
	
	class Super1() {
		shared class Hidden(String val) {}
		shared Integer hidden(String val) { return 0; }
	}
	
	class Sub1() extends Super1() {
		void method() {
			@type["Hiding.Super1.Hidden"] Hidden("hello");
			@type["Integer"] hidden("hello");
		}
	}

	class Super2() {
		class Hidden(String val) {}
		Float hidden(String val) { return 0.0; }
	}
	
	class Sub2() extends Super2() {
		void method() {
		    @type["Hiding.Hidden"] Hidden();
		    @type["String"] hidden();
		}
	}
	
	class Super() {
		shared interface Interface {
			shared formal String hello;
		}
	}
	
	@error class BadSub() 
			extends Super() 
			satisfies Interface {
		shared actual String hello = "hi";
	}
	
	class GoodSub() 
			extends Super() {
		class Inner() {
			shared class Impl() satisfies Interface {
				shared actual String hello = "hi";
			}
		}
		void method() {
			Inner.Impl impl = Inner().Impl();
			Interface intfc = impl;
		}
	}

}