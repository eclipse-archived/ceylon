abstract class Attributes {

	String abstractAttribute;
	
	String immutableAttribute = "Hello World";
	
	mutable String mutableAttribute := "Hello World";
	
	optional mutable String nullMutableAttribute;
	
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
			String value = param.uppercase;
		}
		return NestedClass(mutableAttribute).value;
	}
		
	assign attributeWithNestedClass {
		class NestedClass(String param) {
			String value = param.lowercase;
		}
		mutableAttribute:=NestedClass(attributeWithNestedClass).value;
	}	
	
	doc "This attribute has annotations. Remember that literal
	     strings can be spread across multiple lines."
	by "Gavin King"
	   "Andrew Haley"
	see #Attributes 
	    #immutableAttribute
	column { name="columnName"; comment="some comment"; }
	public String annotatedAttribute = immutableAttribute;
		
}