import ceylon.language.meta.declaration {Import}
shared void bug6871() => print(`module`.dependencies.filter(shuffle(Import.annotated<Annotation>)()));