class MoreDeclarations() {

	String[] voyage = { "Melbourne", "San Francisco", "Boston", "Atlanta", "Guanajuato" }; 
	String? sf = voyage[2]; 
	String[] usLeg = voyage[1..3]; 
	String[] longerVoyage := voyage + { "Rome", "Paris", "Edinburgh" };
	
	shared String greeting = "Hello, " (nickName ? name ? "World") "!";

	Float diagonal = (length**2+width**2)**0.5;
	
	Character[] alphanum = (`A`..`Z`) + (`0`..`9`);
	
	Comparison order<X>(X x, X y) = reverse;
	void display(String message) = log.info;
	String newString(Character... chars) = String;
	void print<T>(T val) given T of String | Integer | Float = printer.print;
	void printStr(String string) = printer.print<String>;
	
	void sort(List<String> list, Comparison by(String x, String y)) { }
	
	shared class TextInput(Natural size=30, String onInit(), String onUpdate(String s)) {}
	
	Comparison getOrder(Boolean reverse=false)(Natural x, Natural y) { 
		if (reverse) {
			local reverse(Natural x, Natural y) { return y<=>x; } 
			return reverse;
		} 
		else {
			local natural(Natural x, Natural y) { return x<=>y; } 
			return natural;
		}
	}
	
	class Catalog() {
	    class Schema() {
	        Catalog catalog { return outer; }
	        class Table() {
	            Schema schema { return outer; }
	            Catalog catalog { return outer.outer; }
	        }
	    }
	}
	
	shared Integer add(Integer x, Integer y) { 
		return x + y;
	}
	
	Identifier createToken() { 
		return Token();
	}
	
	shared void print(Object... objects) { 
		for (Object obj in objects) {
			log.info($obj);
		}
	}
	
	shared void addEntry(V key -> U value) { 
		map.define(key,value);
	}
	
	shared Set<T> singleton<T>(T element) 
			given T satisfies Comparable<T> {
		return TreeSet(element);
	}
	
	
	variable Natural count := 0;
	shared Integer max = 99;
	shared Decimal pi = calculatePi();
	shared Natural[] evenDigits = {0,2,4,6,8};
	
	shared Float total { 
		Float sum := 0.0; 
		for (LineItem li in lineItems) {
			sum += li.amount; 
			return sum;
		}
	}
	
	shared String name { return join(firstName, lastName); } 
	shared assign name { firstName = first(name); lastName = last(name); }
	
	
	shared interface Writer { 
		shared formal Formatter formatter; 
		shared formal void write(String string);
		shared void writeLine(String string) { 
			write(string);
			write(process.newLine);
		}
		shared void writeFormattedLine(String formatString, Object... args) { 
			writeLine( formatter.format(formatString, args) );
		}
	}
	
	shared class ConsoleWriter() satisfies Writer {
		formatter = StringFormatter();
		shared actual void write(String string) { 
			process.write(string);
		}
	}
	
	shared class Suit() 
		of hearts | diamonds | clubs | spades 
		extends Case() {}
	shared object hearts extends Suit() {} 
	shared object diamonds extends Suit() {} 
	shared object clubs extends Suit() {} 
	shared object spades extends Suit() {}
	
	void say<T>(T dest) 
			given T of OutputStream | Log | Buffer {
		switch(dest) 
		case (is Outputstream) {
			dest.writeLine(greeting); 
		}
		case (is Log) {
			dest.info(greeting);
		}
		case (is Buffer) {
			dest.append(greeting);
		}
	}
	
	void print<T>(T printable) 
			given T of String | Named {
		 String string;
		 switch (T) 
		 case (satisfies String) {
		 	string = printable;
		 } 
		 case (satisfies Named) {
			 string = printable.name;
		 }
		 process.writeLine(string);
	}
	
	shared class Payment(PaymentMethod method, Currency currency, Float amount) {}
	
	Payment payment { 
		method = user.paymentMethod; 
		currency = order.currency; 
		Float amount {
			variable Float total := 0.0; 
			for (Item item in order.items) {
				total += item.quantity * item.product.unitPrice; 
			}
			return total;
		}
	}
	
}