class Operators() {

	Natural sum = 1+2;
	Integer difference = 1-2;
	Natural product = 2*2;
	Natural power = 2**2;
	Natural quotient = 2/2;
	Natural remainder = 3%2;
	
	Float parenthesized = (1.0 + 2.0)/3.0 + 0.5;
	
	Entry<String,String> name = "Gavin"->"King";
	Range<Natural> range = 1..5;
	Range<Character> charRange = `A`..`Z`;
	
	String defaulted = null ? "Hello";
	Boolean nullExists = null exists;
	Boolean noneNonempty = none nonempty;
	
	Boolean not = !true;
	Boolean or = true||false;
	Boolean and = or&&not;
	
	Boolean equal = 1==2;
	Boolean notEqual = 1!=2;
	Boolean smaller = 1<2;
	Boolean larger = 1>2;
	Boolean smallAs = 1<=2;
	Boolean largeAs = 1>=2;
	Comparison compare = 1<=>2;
	
	Boolean complement = ~true;
	Boolean bitwiseOr = true|false;
	Boolean bitwiseAnd = true&true;
	Boolean exclusiveOr = true^false;
	
	Boolean instanceOf = 1 is Natural;
	Boolean containedIn = 3 in range;
	Boolean identical = compare===compare;
	
	String render = $123;
	
	String join = "Gavin" + " " + "King";
	
	List<String> list = {"Gavin", "Andrew", "Emmanuel"};
	List<String>? nullList = null;

	String element = list[1];
	String? element = nullList?[666];
	List<String> elements = list[{3,2,1}];
	List<String> subrange = list[0..1];
	List<String> upperRange = list[1...];
	
	String? input = null;
	Iterable<String>? tokens = input?.tokens();
	Sequence<Natural> sizes = tokens[].size;
	
	//TODO: := .=
	
}