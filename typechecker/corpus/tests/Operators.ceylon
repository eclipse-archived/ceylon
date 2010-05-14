class Operators {

	Natural sum = 1+2;
	Integer difference = 1-2;
	Natural product = 2*2;
	Natural power = 2**2;
	Natural quotient = 2/2;
	Natural remainder = 3%2;
	
	Entry<String,String> name = "Gavin"->"King";
	Range<Natural> range = 1..5;
	
	String default = null ? "Hello";
	Boolean nullExists = null exists;
	Boolean noneNonempty = none nonempty;
	
	Boolean not = !true;
	Boolean or = true||false;
	Boolean and = or&&not;
	Boolean implies = or=>and;
	
	Boolean equal = 1==2;
	Boolean notEqual = 1!=2;
	Boolean smaller = 1<2;
	Boolean larger = 1>2;
	Boolean smallAs = 1<=2;
	Boolean largeAs = 1>=2;
	Comparison compare = 1<=>2;
	
	Boolean instanceOf = 1 is Natural;
	Boolean containedIn = 3 in range;
	Boolean identical = 3===sum;
	
	String render = $123;
	
	//TODO:
	.
	^.
	?.
	*.
	:=
	.=
	[]
	?[]
	[,,]
	[..]
	[...]
	
}