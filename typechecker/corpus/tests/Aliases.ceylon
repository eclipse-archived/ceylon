class Aliases {
	
	interface Foo {}
	interface Bar<X> {}
	
	alias Simple satisfies Bar<String>;
	
	doc "an alias"
	by "Gavin King" "Andrew Haley"
	see #Foo
	public alias WithAnnotations satisfies Foo;
	
	alias Multiple satisfies Foo, Bar<String>;
	
	alias WithTypeParameter<X> satisfies Bar<X>;
	
	alias WithTypeConstraint<X> satisfies Bar<X>
		where X>=String;
	
}