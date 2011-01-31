abstract class Types() {
	
	formal Foo simpleType;
	formal Foo<Bar> typeWithParameter;
	formal Foo<Bar,Baz> typeWithMultipleParameters;
	formal Foo<Bar<Baz>> typeWithNestedParameter;
	formal Foo.Bar nestedType;
	formal Foo.Bar.Baz multiplyNestedType;
	formal Foo.Bar<Baz> nestedTypeWithTypeParameter;
	formal Foo<Bar.Baz> typeWithNestedTypeParameter;
	formal Foo<Bar.Baz<Qux>> anythingGoes;
	formal Foo<Bar>.Baz evenThis;
	formal Foo<Bar>.Baz<Qux> orThis;
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
