"The `Float` value of the given string representation of 
 a decimal number or `null` if the string does not 
 represent a decimal number.
 
 The syntax accepted by this method is the same as the 
 syntax for a `Float` literal in the Ceylon language 
 except that it may optionally begin with a sign 
 character (`+` or `-`)."
shared native Float? parseFloat(String string);
