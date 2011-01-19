abstract class Attributes() {

	shared formal String abstractAttribute;
	
	shared Natural computedAttribute = 2 * 3 + 1;
	
	String immutableAttribute = "Hello World";
	
	variable String mutableAttribute := "Hello World";
	
	String? nullAttribute = null;
	
	variable String? nullMutableAttribute := null;
	
	String attribute { return mutableAttribute.uppercase; }
	
	assign attribute { mutableAttribute:=attribute.lowercase; }
	
	String attributeWithLocal { 
		String value = mutableAttribute.uppercase;
		return value; 
	}
	
	assign attributeWithLocal {
		String value = attributeWithLocal;
		mutableAttribute:=value.lowercase;
	}
	
	String attributeWithNestedMethod {
		String nestedMethod(String param) {
			return param.uppercase;
		}
		return nestedMethod(mutableAttribute);
	}
	
	assign attributeWithNestedMethod {
		String nestedMethod(String param) {
			return param.lowercase;
		}
		mutableAttribute:=nestedMethod(attributeWithNestedMethod);
	}
	
	String attributeWithNestedClass {
		class NestedClass(String param) {
			shared String value = param.uppercase;
		}
		return NestedClass(mutableAttribute).value;
	}
		
	assign attributeWithNestedClass {
		class NestedClass(String param) {
			shared String value = param.lowercase;
		}
		mutableAttribute:=NestedClass(attributeWithNestedClass).value;
	}	
	
	doc "This attribute has annotations. Remember that literal
	     strings can be spread across multiple lines."
	by "Gavin King"
	   "Andrew Haley"
	see (Attributes, 
	    immutableAttribute)
	column { name="columnName"; comment="some comment"; }
	public String annotatedAttribute = immutableAttribute;
	
	class Name(String firstName, String lastName) {
	    shared String firstName = firstName;
	    shared String lastName = lastName;
	}
	
	Name namedArgumentAttribute {
	    firstName = "Gavin";
	    lastName = "King";
	}
		
}
