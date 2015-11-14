import ceylon.language {
	d=doc
}
import ceylon.language.meta.declaration {
	FunctionDeclaration
}

shared annotation final class Doc(shared actual String string)
		satisfies OptionalAnnotation<Doc,FunctionDeclaration> {}

shared annotation Doc doc(String doc) => Doc(doc);

d("actual doc")
doc("fake doc")
shared void bug2101() {}