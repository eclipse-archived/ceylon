interface Hiding {
	class Hidden() {}
	String hidden() { return "hello"; }
	
	class Super1() {
		shared class Hidden(String val) {}
		shared Integer hidden(String val) { return 0; }
		@type:"Hiding.Super1.Hidden" Hidden("hello");
		@type:"Integer" hidden("hello");
	}
	
	class Sub1() extends Super1() {
		void method() {
			@type:"Hiding.Super1.Hidden" Hidden("hello");
			@type:"Integer" hidden("hello");
		}
	}

	class Super2() {
		class Hidden(String val) {}
		Float hidden(String val) { return 0.0; }
        @type:"Hiding.Super2.Hidden" Hidden("hello");
        @type:"Float" hidden("hello");
	}
	
	class Sub2() extends Super2() {
		void method() {
		    @type:"Hiding.Hidden" Hidden();
		    @type:"String" hidden();
		}
	}
	
	class Super() {
		shared interface InnerInterface {
			shared formal String hello;
		}
	}
	
	@error class BadSub() 
			extends Super() 
			satisfies InnerInterface {
		//@error //slightly undesirable
		shared actual String hello = "hi";
	}
	
	interface SuperInterface {
		shared interface InnerInterface {
			shared formal String hello;
		}
	}
	
	@error class BadInterfaceImpl()
			satisfies SuperInterface & InnerInterface {
		@error //slightly undesirable
		shared actual String hello = "hi";
	}
	
	class GoodSub() 
			extends Super() {
		class Inner() {
			shared class Impl() 
					satisfies InnerInterface {
				shared actual String hello = "hi";
			}
		}
		void method() {
			Inner.Impl impl = Inner().Impl();
			InnerInterface intfc = impl;
		}
	}
	
	void outer0() {
	    Integer i=0;
	    if (true) {
	        @error Integer i=1;
	    }
	    for (c in "hello") {
	        @error Integer i=2;
	    }
	    void inner1() {
	        Integer i=1;
	    }
        void inner2() {
            if (true) {
                Integer i=1;
            }
            else {
                Integer i=3;
            }
        }
	}

    class Outer0() {
        Integer i=0;
        if (true) {
            @error Integer i=1;
        }
        for (c in "hello") {
            @error Integer i=2;
        }
        class Inner1() {
            Integer i=1;
        }
        class Inner2() {
            if (true) {
                Integer i=1;
            }
            else {
                Integer i=3;
            }
        }
    }

}