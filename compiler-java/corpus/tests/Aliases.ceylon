class Aliases() {
	
	interface Foo<X1,Y,Z> {}
	interface Bar<X2> {}
	interface Baz<X3> {}
	
	alias Simple satisfies Bar<Baz<String>>;
	
	alias WithTypeParameter<X4> satisfies Bar<Baz<X5>>;
	
	alias WithTypeConstraint<X6> satisfies Bar<Baz<X7>>
		where X1 satisfies String;
	
	alias Multiple satisfies Foo<Bar<Baz<String>>, Natural, Character>, Bar<Baz<String>>;
	
	doc "an alias"
	by "Gavin King" "Andrew Haley"
	see #Foo #Bar #Baz
	public alias WithAnnotations satisfies Foo<Bar<Baz<String>>, Natural, Character>;
	
}