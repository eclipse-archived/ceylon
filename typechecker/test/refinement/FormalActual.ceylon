abstract class WithFormal() {
	shared formal String hello;
}

class WithActualDefault() extends WithFormal() {
	shared actual default String hello = "hello";
}

abstract class WithActualFormal() extends WithActualDefault() {
	shared actual formal String hello;
}

class WithActual() extends WithActualFormal() {
	shared actual String hello = "hi";
}

@error class WithoutActual() extends WithActualFormal() {}


abstract class Super2() {
    shared default Integer defaultGetterSetter {
        return 2;
    } 
    assign defaultGetterSetter {}
}

abstract class Super1() extends Super2() {
	 // we make a default attr formal
    shared variable actual formal Integer defaultGetterSetter;
}

@error object obj extends WithFormal() {}

