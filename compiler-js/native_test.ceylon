
shared Null nativejs() { return null; }

shared nativejs interface Foo {
	shared formal variable Integer num;
	shared formal variable String str;
    shared formal variable Boolean bool;
}
shared nativejs interface Bar {
    shared formal variable Foo foo;
}
shared interface Baz {
    shared formal variable Bar bar;
}
shared void m(Baz b) {
	Foo f = b.bar.foo;
	f.num -= 1;
	print(f);
}

