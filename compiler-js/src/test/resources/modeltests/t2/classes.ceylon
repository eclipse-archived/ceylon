//Test classes for metamodel generation
class SimpleClass1() {}
shared class SimpleClass2(String name) {}
class SimpleClass3() extends Object() {
    shared actual Boolean equals(Object other) { return false; }
    shared actual Integer hash { return 0; }
}
shared class SimpleClass4(Integer x) extends SimpleClass2("simple4") {
}
shared class Satisfy1() satisfies Comparable<Satisfy1> {
    shared actual Comparison compare(Satisfy1 other) { return equal; }
}
shared class Satisfy2() satisfies Iterable<Integer> {
	shared actual Iterator<Integer> iterator {
		return {1}.iterator;
	}
}
class ParmTypes1<Element>(Element x) {}
class ParmTypes2<out Element>(Element... x)
		given Element satisfies Object {
}
class ParmTypes3<Type1,Type2>(Type1 a1, Type2 a2)
		given Type1 satisfies Number
		given Type2 of String|Singleton<String> {
}
class ParamTypes4<out Element>(Element... elems) satisfies Iterable<Element> {
	shared Element? primero = elems.first;
	shared actual Iterator<Element> iterator {
		return elems.iterator;
	}
}
