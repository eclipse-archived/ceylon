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
}