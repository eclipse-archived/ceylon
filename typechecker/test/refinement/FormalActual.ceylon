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