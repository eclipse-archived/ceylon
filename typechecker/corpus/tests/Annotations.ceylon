class Annotations {

	class NoArguments {

		public deprecated final
		void methodAnnotations(deprecated Natural param=0) {}
		
		public deprecated final
		Natural attributeAnnotations = 1;
		
		package deprecated final
		class ClassAnnotations {}
		
		module
		interface InterfaceAnnotations {}
		
	}

	class LiteralArguments {	

		doc "The method doc"
		by "Gavin King"
		   "Andrew Haley"
		version '1.2.0'
		timeout 1000
		see #ClassAnnotations
		    #methodAnnotations
		    #Object.equals
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
		version '2.1.0'
		by "Gavin King"
		   "Andrew Haley"
		class ClassAnnotations {}
		
		doc "The interface doc"
		version '1.0.0 beta'
		see #ClassAnnotations
		    #methodAnnotations
		    #Object.equals
		by "Gavin King"
		   "Andrew Haley"
		interface InterfaceAnnotations {}
		
	}

	class NamedArguments {

	    doc { text="some text about the method"; }
		lock { timeout=1000; }
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
		table { name="tableName"; schema="schemaName"; }
		see { #ClassAnnotations, #methodAnnotations, #Object.equals }
		scope { scopeType=session; }
		class ClassAnnotations {}
		
		doc { text="some text about the interface"; }
		see { #ClassAnnotations, #methodAnnotations, #Object.equals }
		by { "Andrew Haley" }
		interface InterfaceAnnotations {}
	
	}
	
	class PositionalArguments {
	
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
		class ClassAnnotations {}
		
		see (#ClassAnnotations, #methodAnnotations, #Object.equals)
		version('1.0.0 beta')
		doc("some text about the interface")
		by("Gavin King", "Andrew Haley")
		interface InterfaceAnnotations {}
	
	}
	
}