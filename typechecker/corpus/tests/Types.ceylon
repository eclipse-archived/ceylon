abstract class Types() {
	
    interface Foo {
        interface Bar {
            interface Baz {}
        }
        interface Bar1<T> {}
    }
    interface Foo1<T> {
        interface Baz {}
        interface Baz1<W> {}
    }
    interface Foo2<U,V> {}
    interface Bar {
        interface Baz {}
        interface Baz1<T> {}
    }
    interface Bar1<T> {}
    interface Baz {}
    interface Qux {}
    
	shared formal Foo simpleType;
	shared formal Foo1<Bar> typeWithParameter;
	shared formal Foo2<Bar,Baz> typeWithMultipleParameters;
	shared formal Foo1<Bar1<Baz>> typeWithNestedParameter;
	shared formal Foo.Bar nestedType;
	shared formal Foo.Bar.Baz multiplyNestedType;
	shared formal Foo.Bar1<Baz> nestedTypeWithTypeParameter;
	shared formal Foo1<Bar.Baz> typeWithNestedTypeParameter;
	shared formal Foo1<Bar.Baz1<Qux>> anythingGoes;
	shared formal Foo1<Bar>.Baz evenThis;
	shared formal Foo1<Bar>.Baz1<Qux> orThis;
	shared formal subtype theSubtype;
	shared formal Foo[] sequenceType;
	shared formal Foo? optionalType;
	shared formal Foo[][] sequenceOfSequenceType;
	shared formal Foo[]? x;
	shared formal Foo?[] y;
	//formal Foo<#2> typeWithDimension;
	//formal Foo<#3,#5> typeWithMultipleDimensions;
	//formal Foo[10] sequenceWithDimension;
	
	/*abstract class Bar<#n,#m,P...>() {
	    formal Foo<#n> typeWithVariantDimension;
	    formal Foo[m] sequenceWithVariantDimension;
        formal Foo<#n+#m+#1> typeWithLinearDimension;
        formal Foo[n+m+2] sequenceWithLinearDimension;
        formal Foo<P...> typeWithSequencedTypeArgument;
	}*/
}
