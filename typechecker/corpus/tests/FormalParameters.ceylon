class FormalParameters() {
	
	class MethodParameters() {
	
		void noParameter() {}
	
		void singleParameter(Natural count) {}
		
		void annotatedParameter(deprecated doc "use something else" Natural count) {}
		
		void multipleParameters(Natural count, String description) {}
		
		void defaultValueParameters(Natural param=1, 
		                            String description="", 
		                            Lock lock=null) {}
		
		void sequencedParameter(String... strings) {}
		
		void entryParameter(String name->Person person) {}
		
		void combinationParameters(String name->Person person,
		                           Natural count, 
		                           String description="",
		                           Lock? lock=null, 
		                           String... strings) {}
		
		void functionalParameters(String produce(), void consume(String x)) { 
			consume(produce()); 
		}
		
		void multipleParameterLists(Lock? lock=null)(String... strings) {}
		
	}
	
	class InitParameters() {
	
		class NoParameter() {}
	
		class SingleParameter(Natural count) {}
		
		class AnnotatedParameter(deprecated doc "use something else" Natural count) {}
		
		class MultipleParameters(Natural count, String description) {}
		
		class DefaultValueParameters(Natural param=1, 
		                             String description="", 
		                             Lock? lock=null) {}
		
		class SequencedParameter(String... strings) {}
		
		class EntryParameter(String name->Person person) {}
		
		class CombinationParameters(String name->Person person,
		                            Natural count, 
		                            String description="",
		                            Lock? lock=null, 
		                            String... strings) {}
		
		class FunctionalParameters(String produce(), void consume(String x)) { 
			consume(produce()); 
		}
		
	}
	
}
