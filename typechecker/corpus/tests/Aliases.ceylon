class Aliases {
	
	interface Foo<X,Y,Z> {}
	interface Bar<X> {}
	interface Baz<X> {}
	
	alias Simple satisfies Bar<Baz<String>>;
	
	alias WithTypeParameter<X> satisfies Bar<Baz<X>>;
	
	alias WithTypeConstraint<X> satisfies Bar<Baz<X>>
		where X>=String;
	
	alias Multiple satisfies Foo<Bar<Baz<String>>, Natural, Character>, Bar<Baz<String>>;
	
	doc "an alias"
	by "Gavin King" "Andrew Haley"
	see #Foo #Bar #Baz
	public alias WithAnnotations satisfies Foo<Bar<Baz<String>>, Natural, Character>;
	
}