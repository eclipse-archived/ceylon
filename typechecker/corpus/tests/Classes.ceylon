class Classes {

	class Simple {}
	
	class WithMethods {
		void doNothing() {}
		Natural returnZero() { return 0; }
		String returnArgument(String arg) { return arg; }
	}
	
	class WithAttributes {
		Natural count = 0;
		mutable String description := "";
		String countAsString { return $count; }
		assign countAsString { count := countAsString.parseNatural; }
	}
	
	class WithInitParameters(Natural count, String description) {
		Natural count = count;
		mutable String description := description;
	}
	
	doc "This class has annotations. Remember that literal
	     strings can be spread across multiple lines."
	by "Gavin King"
	   "Andrew Haley"
	scope(session)
	see #Simple
	throws #DatabaseException
	       "if database access fails"
	entity table { name="someTable"; schema="someSchema"; }
	class WithAnnotations {}
	
	class Extends extends Simple {}
	
	class ExtendsWithInitParameters(Natural count, String description, Float tolerance) 
			extends WithInitParameters(count, description) {
		Float tol = tolerance;
	}
	
	class WithClosedInstanceList { case foo, case bar, case baz; }
	
	class WithOpenInstanceList { case foo, case bar, case baz... }
	
	class WithTypeParameters<X, Y> {}
	
	class WithTypeConstraints<X, Y> 
		where X>=String & Y(Natural count) {}
		
	class WithNestedClass {
		class NestedClass {}
	}
	
	class WithNestedInterface {
		interface NestedInterface {}
	}
	
	public interface Counter {
		public Natural count;
		public void inc();
	}
	
	public interface Resettable {
		void reset();
	}

	class Satisfies satisfies Counter {
		mutable Natural count := 0;
		void inc() { count++; }
	}
	
	class SatisfiesMultiple satisfies Counter, Resettable {
		mutable Natural count := 0;
		void inc() { count++; }
		void reset() { count := 0; }
	}
	
}