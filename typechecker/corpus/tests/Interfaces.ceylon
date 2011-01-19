class Interfaces() {

	interface Simple {}
	
	interface WithMethods {
		shared formal void doNothing();
		shared formal Natural returnZero();
		shared formal String returnArgument(String arg);
	}
	
	interface WithAttributes {
		shared formal Natural count;
		shared formal variable String description;
	}
	
	doc "This interface has annotations. Remember that literal
	     strings can be spread across multiple lines."
	by "Gavin King"
	   "Andrew Haley"
	scope(session)
	see (Simple)
	throws (DatabaseException
	        -> "if database access fails")
	entity table { name="someTable"; schema="someSchema"; }
	interface WithAnnotations {}
	
	interface Satisfies satisfies Simple {}
	
	interface SatisfiesMultiple satisfies Simple & WithMethods & WithAttributes {}
		
	interface WithTypeParameters<X, Y> {}
	
	interface WithTypeConstraints<X, Y> 
		given X satisfies String 
		given Y(Natural count) {}
		
	interface WithNestedInterface {
		interface NestedInterface {}
	}
	
	interface WithNestedClass {
		class NestedClass() {}
	}

    object foo extends Case() satisfies Enum {}
    object bar extends Case() satisfies Enum {}
    object baz extends Case() satisfies Enum {}
    class Qux satisfies Enum {}
    interface Enum of foo | bar | baz | Qux {}
	
}
