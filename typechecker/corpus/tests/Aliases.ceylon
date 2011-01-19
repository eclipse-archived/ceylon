class Aliases() {
	
	interface Foo<X,Y,Z> {}
	interface Bar<X> {}
	interface Baz<X> {}
	class Qux<T>(T value) {}
	
	interface Simple = Bar<Baz<String>>;
	
	interface WithTypeParameter<X> = Bar<Baz<X>>;
	
	interface WithTypeConstraint<X>
		given X satisfies String 
		= Bar<Baz<X>>;
	
	class Class(Natural n) = Qux<Natural>;
	
    class ClassWithTypeParameter<T>(T value) = Qux<T>;

    class ClassWithTypeConstraint<T>(T value) 
        given T satisfies Bar<Object> & Equality<T>
        = Qux<T>;

	doc "an alias"
	by "Gavin King" "Andrew Haley"
	see (Foo, Bar, Baz)
	shared interface WithAnnotations = Bar<Baz<String>>;
	
}
