class MultipleInheritance() {

interface Top {
	shared formal Value id;
}

interface Middle satisfies Top {
	shared actual default Value id { return "hello"; }
}

interface Nothing satisfies Middle {
	shared actual default String id { return "world"; }
}

class Super() satisfies Middle {}

class Impl() extends Super() satisfies Top & Middle & Nothing {}

void method() {
	@type:"String" value id = Impl().id;
}


interface Formal {
    shared formal String name;
}

interface Actual1 satisfies Formal {
    shared actual default String name { return "Gavin"; }
}

interface Actual2 satisfies Formal {
    shared actual default String name { return "Emmanuel"; }
}

@error object bad satisfies Actual1 & Actual2 {}

object good satisfies Actual1 & Actual2 {
    shared actual String name = "Tom";
}

@error class BadConcrete() satisfies Actual1 & Actual2 {}

class GoodConcrete() satisfies Actual1 & Actual2 {
    shared actual String name = "Tako";
}

@error interface BadInterface satisfies Actual1 & Actual2 {}

interface GoodInterface satisfies Actual1 & Actual2 {
    shared actual String name { return "Stef"; }
}

}