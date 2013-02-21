class GenericRefinement() {
    
    interface GoodTypes {
        
        class Foo<T,S>() {
            shared default T foo(T t) { return t; }
            shared default S bar(S s) { return s; }
            shared default T t { throw; }
            shared default S s { throw; }
        }
        
        class Bar<Q>() extends Foo<Q,String>() {
            shared actual Q foo(Q t) { return t; }
            shared actual String bar(String s) { return s; }
            shared actual Q t { throw; }
            shared actual String s { throw; }
        }
        
    }
    
    interface NestedTypes {
        
        interface Foo<X> {
            shared formal X x();
        }
        
        interface Bar<X> {
            shared formal Foo<X> foo();
        }
        
        class BarImpl1<Y>(Y xp) satisfies Bar<Y> {
            shared actual Foo<Y> foo() {
                class FooImpl() satisfies Foo<Y> {
                    shared actual Y x() { return xp; }
                }
                return FooImpl();
            }
        }
        class BarImpl2<Y>(Y xp) satisfies Bar<Y> {
            shared actual Foo<Y> foo() {
                object foo satisfies Foo<Y> {
                    shared actual Y x() { return xp; }
                }
                return foo;
            }
        }
        
    }

    interface NestedTypes2 {
        
        interface Foo<X> {
            shared formal X x();
        }
        
        interface Bar<X> {
            shared formal Foo<X> foo();
        }
        
        class BarImpl1<X>(X xp) satisfies Bar<X> {
            shared actual Foo<X> foo() {
                class FooImpl() satisfies Foo<X> {
                    shared actual X x() { return xp; }
                }
                return FooImpl();
            }
        }
        class BarImpl2<X>(X xp) satisfies Bar<X> {
            shared actual Foo<X> foo() {
                object foo satisfies Foo<X> {
                    shared actual X x() { return xp; }
                }
                return foo;
            }
        }
        
    }
    
    interface GenericRefinesConcrete {
    	abstract class WithNumber() {
    		shared formal Number num;
    	}
    	class WithNumeric<T>(Scalar<T> n) 
    	        extends WithNumber() 
    	        given T satisfies Scalar<T> {
    		shared actual default Scalar<T> num = n;
    	}
    	class WithFloat(Float f) 
    	        extends WithNumeric<Float>(f) {
    		shared actual Float num = f;
    	}
    }
    
    interface Constraints {
		interface Foo {
		    shared formal void accept<T>(T t) 
		            given T satisfies Comparable<T>;
		}
		class Bar() satisfies Foo {
		    shared actual void accept<T>(T t)
		            given T satisfies Comparable<T> {}
		}
    }

    interface MoreConstraints {
        interface Baz<X> {}
		interface Foo<S> {
		    shared formal void accept<T>(T t) 
		            given T satisfies Baz<T&S>;
		}
		class Bar() satisfies Foo<String> {
		    shared actual void accept<T>(T t)
		            given T satisfies Baz<T&String> {}
		}
    }

    interface OkConstraints {
        interface Baz<X> {}
		interface Foo<S> {
		    shared formal void accept<T>(T t) 
		            given T satisfies List<T&S>;
		}
		class Bar() satisfies Foo<String> {
		    shared actual void accept<T>(T t)
		            given T satisfies List<String> {}
		}
    }

    interface BadConstraints {
        interface Baz<X> {}
		interface Foo<S> {
		    shared formal void accept<T>(T t) 
		            given T satisfies Comparable<T&S>;
		}
		class Bar() satisfies Foo<String> {
		    @error
		    shared actual void accept<T>(T t)
		            given T satisfies Comparable<String> {}
		}
    }

    interface BrokenConstraints {
        interface I {} interface J {}
        interface Baz<X> {}
		interface Foo<S> {
		    shared formal void accept<T>(T t) 
		            given T satisfies Baz<T&S>;
		}
		class Bar() satisfies Foo<I> {
		    @error
		    shared actual void accept<T>(T t)
		            given T satisfies Baz<T&J> {}
		}
    }
    
    interface UnBrokenConstraints {
        interface I {} interface J {}
        interface Baz<X> {}
        interface Foo<S> {
            shared formal void accept<T>(T t) 
                    given T satisfies Baz<T&S>&J;
        }
        class Bar() satisfies Foo<I> {
            shared actual void accept<T>(T t)
                    given T satisfies Baz<T&I>&J {}
        }
    }
    
    void meth<Absent>(Absent n) 
            given Absent satisfies Null {
        @error
        object obj 
            extends Object()
            satisfies Empty & 
                Container<Nothing,Absent> {}
    }
}