doc "Annotation to mark a type or member as shared. A `shared` 
     member is visible outside the block of code in which it
     is declared."
shared Nothing shared() { return null; }

doc "Annotation to mark an attribute as variable. A `variable` 
     attribute must be assigned with `=` and may be 
     reassigned over time." 
shared Nothing variable() { return null; }

doc "Annotation to mark a class as abstract. An `abstract` 
     class may not be directly instantiated. An `abstract`
     class may have enumerated cases."
shared Nothing abstract() { return null; }

doc "Annotation to mark a member of a type as refining a 
     member of a supertype."
shared Nothing actual() { return null; }

doc "Annotation to mark a member whose implementation must 
     be provided by subtypes."  
shared Nothing formal() { return null; }

doc "Annotation to mark a member whose implementation may be 
     refined by subtypes. Non-`default` declarations may not 
     be refined."
shared Nothing default() { return null; }

doc "Annotation to specify API documentation of a program
     element." 
shared Nothing doc(String description) { return null; }

doc "Annotation to specify API references to other related 
     program elements."
shared Nothing see(Void... programElements) { return null; }

doc "Annotation to specify API authors."
shared Nothing by(String... authors) { return null; }

doc "Annotation to mark a program element that throws an 
     exception."
shared Nothing throws(Void type, String? when=null) { return null; }

doc "Annotation to mark program elements which should not be 
     used anymore."
shared Nothing deprecated(String? reason=null) { return null; }

doc "Annotation to categorize the API by tag." 
shared Nothing tagged(String... tags) { return null; }

doc "Annotation to specify the URL of the license of a module 
     or package." 
shared Nothing license(String url) { return null; }

shared Nothing export() { return null; }

shared Nothing optional() { return null; }

doc "Annotation to specify a hexadecimal literal." 
shared Integer hex(String number) { return 0; }

doc "Annotation to specify a binary literal." 
shared Integer bin(String number) { return 0; }
