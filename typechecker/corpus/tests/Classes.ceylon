class Classes() {

	class Simple() {}
	
	class WithMethods() {
		void doNothing() {}
		Natural returnZero() { return 0; }
		String returnArgument(String arg) { return arg; }
	}
	
	class WithAttributes() {
		Natural count = 0;
		variable String description := "";
		String countAsString { return $count; }
		assign countAsString { count := countAsString.parseNatural; }
	}
	
	abstract class WithAbstractMethods() {
		shared formal void doNothing();
		shared formal Natural returnZero();
		shared formal String returnArgument(String arg);
	}
	
	abstract class WithAbstractAttributes() {
		shared formal Natural count;
		shared formal variable String description;
	}
	
	class WithInitParameters(Natural count, String description) {
		Natural count = count;
		variable String description := description;
	}
	
	doc "This class has annotations. Remember that literal
	     strings can be spread across multiple lines."
	by "Gavin King"
	   "Andrew Haley"
	scope(session)
	see (Simple)
	throws (DatabaseException
	        -> "if database access fails")
	entity table { name="someTable"; schema="someSchema"; }
	class WithAnnotations() {}
	
	class Extends() extends Simple() {}
	
	class ExtendsWithInitParameters(Natural count, String description, Float tolerance) 
			extends WithInitParameters(count, description) {
		Float tol = tolerance;
	}
	
    object foo extends Enum() {}
    object bar extends Enum() {}
    object baz extends Enum() {}
    class Qux() extends Enum() {}
    class Enum() of foo | bar | baz | Qux extends Case() {}
    
	class WithTypeParameters<X, Y>() {}
	
	class WithTypeConstraints<X, Y>()
		given X satisfies String 
		given Y(Natural count) {}
		
	class WithNestedClass() {
		class NestedClass() {}
	}
	
	class WithNestedInterface() {
		interface NestedInterface {}
	}
	
	shared interface Counter {
		shared formal Natural count;
		shared formal void inc();
	}
	
	shared interface Resettable {
		shared formal void reset();
	}

	class Satisfies() satisfies Counter {
		shared actual variable Natural count := 0;
		shared actual void inc() { count++; }
	}
	
	class SatisfiesMultiple() satisfies Counter & Resettable {
		shared actual variable Natural count := 0;
		shared actual void inc() { count++; }
		shared actual void reset() { count := 0; }
	}
	
}
