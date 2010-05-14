class Statements {

	String hello = "Hello World"; //specification
	String getHello() = get hello; //method specification
	mutable String string := hello; //initialization
	string := "Hello Gavin"; //assignment
	
	Natural x;
	x = 0; //lazy specification
	mutable Natural y := x; //initialization
	y := y*x; //assignment
	y++; //increment
	y++; //decrement
	y+=5; //add
	y-=5; //subtract
	y*=2; //multiply
	y/=2; //divide
	y%=7; //remainder
	
	mutable Boolean b := true;
	b|=b; //or
	b&=b; //and
	b^=false; //exclusive or
	
	class Bar(Boolean b) {}
	
	Bar(true); //instantiation
	Bar { b=false; } //instantiation
	
	class Foo {
		void instanceMethod(Natural n) {}
		static staticMethod(Float f) {}
	}
	
	Foo().instanceMethod(2); //instance method invocation
	Foo.staticMethod(1.0); //static method invocation

	Foo().instanceMethod { n=0; }; //instance method invocation
	Foo.staticMethod { f=2.5; }; //static method invocation
		
}