doc "Annotation to mark a type or member as shared.
Annotating them with `shared` makes them visible outside the current unit of code."
shared Nothing shared() { return null; }

doc "Annotation to mark an attribute as variable. 
`variable` attributes must be assigned with `:=` and can be reassigned over time." 
shared Nothing variable() { return null; }

doc "Annotation to mark a class as abstract.
`abstract` classes may not be directly instantiated."
shared Nothing abstract() { return null; }

doc "Annotation to mark a type or member as overriding an other type/member."
shared Nothing actual() { return null; }

doc "Annotation to mark a type or member whose implementation must be provided
by subtypes."  
shared Nothing formal() { return null; }

doc "Annotation to mark a type or member whose implementation may be overridden
by subtypes. Non-`default` declarations may not be overridden."
shared Nothing default() { return null; }

doc "Annotation to specify API documentation on a type or member." 
shared Nothing doc(String description) { return null; }

doc "Annotation to specify API references to other related API members."
shared Nothing see(Void... programElements) { return null; }

doc "Annotation to specify API authors."
shared Nothing by(String... authors) { return null; }

doc "Annotation to mark methods as throwing an exception."
shared Nothing throws(Void type, String? when=null) { return null; }

doc "Annotation to mark types or members which should not be used anymore."
shared Nothing deprecated(String? reason=null) { return null; }

doc "Annotation to categorize the API by tag." 
shared Nothing tagged(String... tags) { return null; }

doc "Annotation to specify the URL of the license of a module or package." 
shared Nothing license(Quoted url) { return null; }

shared Nothing export() { return null; }

shared Nothing optional() { return null; }

doc "Annotation to specify a hexadecimal literal." 
shared Integer hex(Quoted number) { return 0; }

doc "Annotation to specify a binary literal." 
shared Integer bin(Quoted number) { return 0; }
