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
		class Baz(String s) {}
	}

	void method(Float f) {}
	
	//instance method invocation
	Foo().instanceMethod(2); 
	Foo().instanceMethod { n=0; };

	//method invocation
	method(1.0); 
	method { f=2.5; };
	
	//nested instance instantiation
	Foo().Baz("hello");
	Foo().Baz { s="goodbye"; };
		
}