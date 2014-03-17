
"The supertype of all errors, which represent unexpected non-transient problems. 
 This includes such things assertion failures and problems with 
 the virtual machine.
 
 Errors cannot usually be recovered from, and therefore are not usually caught."
by ("Gavin", "Tom")
shared native class Error(String? description=null, Throwable? cause=null) 
        extends Throwable(description, cause) {
    
}