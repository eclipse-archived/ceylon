abstract class Types() {
	
	Foo simpleType;
	Foo<Bar> typeWithParameter;
	Foo<Bar,Baz> typeWithMultipleParameters;
	Foo<Bar<Baz>> typeWithNestedParameter;
	Foo.Bar nestedType;
	Foo.Bar.Baz multiplyNestedType;
	Foo.Bar<Baz> nestedTypeWithTypeParameter;
	Foo<Bar.Baz> typeWithNestedTypeParameter;
	Foo<Bar.Baz<Qux>> anythingGoes;
	Foo<Bar>.Baz evenThis;
	Foo<Bar>.Baz<Qux> orThis;
	subtype theSubtype;
	Foo[] sequenceType;
	Foo? optionalType;
	Foo[][] sequenceOfSequenceType;
	Foo[]? x;
	Foo?[] y;
	//Foo<#2> typeWithDimension;
	//Foo<#3,#5> typeWithMultipleDimensions;
	//Foo[10] sequenceWithDimension;
	
	/*abstract class Bar<#n,#m,P...>() {
	    Foo<#n> typeWithVariantDimension;
	    Foo[m] sequenceWithVariantDimension;
        Foo<#n+#m+#1> typeWithLinearDimension;
        Foo[n+m+2] sequenceWithLinearDimension;
        Foo<P...> typeWithSequencedTypeArgument;
	}*/
}
