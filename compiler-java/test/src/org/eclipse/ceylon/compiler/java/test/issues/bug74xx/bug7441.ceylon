import ceylon.language.meta.declaration {
	ValueDeclaration
}

shared final annotation class TestAnnotation satisfies OptionalAnnotation<TestAnnotation,ValueDeclaration> {
	shared new (Anything args) {}
}
shared annotation TestAnnotation testAnnotation(Anything args) => TestAnnotation(args); 