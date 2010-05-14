class Classes {

	interface Simple {}
	
	interface WithMethods {
		void doNothing();
		Natural returnZero();
		String returnArgument(String arg);
	}
	
	interface WithAttributes {
		Natural count;
		mutable String description;
	}
	
	doc "This interface has annotations. Remember that literal
	     strings can be spread across multiple lines."
	by "Gavin King"
	   "Andrew Haley"
	scope(session)
	see #Simple
	throws #DatabaseException
	       "if database access fails"
	entity table { name="someTable"; schema="someSchema"; }
	interface WithAnnotations {}
	
	interface Satisfies satisfies Simple {}
		
	interface WithTypeParameters<X, Y> {}
	
	interface WithTypeConstraints<X, Y> 
		where X>=String & Y(Natural count) {}
		
	interface WithNestedInterface {
		interface NestedInterface {}
	}
	
	interface WithNestedClass {
		class NestedClass {}
	}
	
}