interface MultipleInheritance {

interface Top {
	shared formal Object id;
}

interface Middle satisfies Top {
	shared actual default Object id { return "hello"; }
}

interface Bottom satisfies Middle {
	shared actual default String id { return "world"; }
}

class Super() satisfies Middle {}

class Impl() extends Super() satisfies Top & Middle & Bottom {}

void method() {
	@type["String"] value id = Impl().id;
}

}