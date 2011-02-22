abstract class Types() {
	
    interface Foo {
        interface Bar {
            interface Baz {}
        }
        interface Bar1<T> {}
    }
    interface Foo1<T> {
        interface Baz {}
    }
    interface Foo2<U,V> {}
    interface Bar {
        interface Baz {}
        interface Baz1<T> {}
    }
    interface Bar1<T> {}
    interface Baz {}
    interface Qux {}
    
	formal Foo simpleType;
	formal Foo1<Bar> typeWithParameter;
	formal Foo2<Bar,Baz> typeWithMultipleParameters;
	formal Foo1<Bar1<Baz>> typeWithNestedParameter;
	formal Foo.Bar nestedType;
	formal Foo.Bar.Baz multiplyNestedType;
	formal Foo.Bar1<Baz> nestedTypeWithTypeParameter;
	formal Foo1<Bar.Baz> typeWithNestedTypeParameter;
	formal Foo2<Bar.Baz1<Qux>> anythingGoes;
	formal Foo1<Bar>.Baz evenThis;
	formal Foo1<Bar>.Baz<Qux> orThis;
	formal subtype theSubtype;
	formal Foo[] sequenceType;
	formal Foo? optionalType;
	formal Foo[][] sequenceOfSequenceType;
	formal Foo[]? x;
	formal Foo?[] y;
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
