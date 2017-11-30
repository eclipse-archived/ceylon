shared class Top(){}
shared class Bottom() extends Top(){}

shared interface Interface<out Key>{}

shared interface SubInterface1 satisfies Interface<Top>{}
shared interface SubInterface2 satisfies Interface<Bottom>{}

shared class Class1() satisfies SubInterface1{}
shared class Class2() extends Class1() satisfies SubInterface2{}

