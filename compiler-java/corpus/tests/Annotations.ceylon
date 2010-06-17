class Annotations() {

	class NoArguments() {

		public deprecated default
		void methodAnnotations(deprecated Natural param=0) {}
		
		public deprecated default
		Natural attributeAnnotations = 1;
		
		package deprecated abstract
		class ClassAnnotations() {}
		
		module
		interface InterfaceAnnotations {}
		
	}

	class LiteralArguments() {	

		doc "The method doc"
		by "Gavin King"
		   "Andrew Haley"
		version '1.2.0'
		timeout 1000
		see #ClassAnnotations
		    #methodAnnotations
		    #Object.equals
		throws #DatabaseException
	           "if database access fails"
		void methodAnnotations(
			deprecated
			doc "use something else" 
			see #otherMethod 
			Natural param=0) {}
		
		doc "The attribute doc"
		by "Gavin King"
		   "Andrew Haley"
		see #ClassAnnotations
		    #methodAnnotations
		    #Object.equals
		version '1.0.0'
		Natural attributeAnnotations = 1;
		
		see #ClassAnnotations
		    #methodAnnotations
		    #Object.equals
		doc "The class doc"
		throws #DatabaseException
	           "if database access fails"
		version '2.1.0'
		by "Gavin King"
		   "Andrew Haley"
		class ClassAnnotations() {}
		
		doc "The interface doc"
		version '1.0.0 beta'
		see #ClassAnnotations
		    #methodAnnotations
		    #Object.equals
		by "Gavin King"
		   "Andrew Haley"
		interface InterfaceAnnotations {}
		
	}

	class NamedArguments() {

	    doc { text="some text about the method"; }
		lock { timeout=1000; }
		throws { type = #DatabaseException;
	             description = "if database access fails"; }
		by { "Gavin King", "Andrew Haley" }
		see { #ClassAnnotations, #methodAnnotations, #Object.equals }
		void methodAnnotations(
			deprecated
			doc { text="use something else"; }
			see { #otherMethod }
			Natural param=0) {}
		
		see { #ClassAnnotations, #methodAnnotations, #Object.equals }
		doc { text="some text about the attribute"; }
		by { "Gavin King" }
		column { name="columnName"; comment="some comment"; }
		Natural attributeAnnotations = 1;
		
		by { "Gavin King", "Andrew Haley" }
		doc { text="some text about the class"; }
		throws { type = #DatabaseException;
	             description = "if database access fails"; }
		table { name="tableName"; schema="schemaName"; }
		see { #ClassAnnotations, #methodAnnotations, #Object.equals }
		scope { scopeType=session; }
		class ClassAnnotations() {}
		
		doc { text="some text about the interface"; }
		see { #ClassAnnotations, #methodAnnotations, #Object.equals }
		by { "Andrew Haley" }
		interface InterfaceAnnotations {}
	
	}
	
	class PositionalArguments() {
	
		throws(#DatabaseException,
	           "if database access fails")
	    doc("some text about the method")
		lock(1000)
		version('2.1.0')
		by("Gavin King", "Andrew Haley")
		see (#ClassAnnotations, #methodAnnotations, #Object.equals)
		void methodAnnotations(
			deprecated
			doc("use something else") 
			see(#otherMethod) 
			Natural param=0) {}
		
		doc("some text about the attribute")
		by("Gavin King", "Andrew Haley")
		see (#ClassAnnotations, #methodAnnotations, #Object.equals)
		version('1.2.0')
		Natural attributeAnnotations = 1;
		
		version('1.0.0')
		by("Gavin King", "Andrew Haley")
		see (#ClassAnnotations, #methodAnnotations, #Object.equals)
		doc("some text about the class")
		scope(session)
		throws(#DatabaseException,
	           "if database access fails")
		class ClassAnnotations() {}
		
		see (#ClassAnnotations, #methodAnnotations, #Object.equals)
		version('1.0.0 beta')
		doc("some text about the interface")
		by("Gavin King", "Andrew Haley")
		interface InterfaceAnnotations {}
	
	}
	
	class ComplexArguments() {
	    
	    queries( Query('select p.name from Person p'), 
	             Query('select p.name from Person p where p.age>18') )
	    see #List<String>
	    class ClassAnnotations() {}
	
	}
	
}