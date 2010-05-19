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
	y--; //decrement
	++y; //increment
	--y; //decrement
	y+=5; //add
	y-=5; //subtract
	y*=2; //multiply
	y/=2; //divide
	y%=7; //remainder
	
	mutable Boolean b := true;
	b||=b; //or
	b&&=b; //and
	b|=b; //bitwise or
	b&=b; //bitwise and
	b^=false; //exclusive or
	
	mutable optional String qux := null;
	qux ?= "Hello";
	
	class Bar(Boolean b) {}
	
	//instantiation
	Bar(true); 
	Bar { b=false; };
	
	class Foo {
		void instanceMethod(Natural n) {}
		static staticMethod(Float f) {}
	}
	
	//instance method invocation
	Foo().instanceMethod(2); 
	Foo().instanceMethod { n=0; };

	//static method invocation
	Foo.staticMethod(1.0); 
	Foo.staticMethod { f=2.5; };
		
}