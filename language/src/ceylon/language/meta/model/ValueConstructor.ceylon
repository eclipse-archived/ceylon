import ceylon.language.meta.declaration{ValueConstructorDeclaration}

"""A callable constructor model represents the model of a Ceylon class 
   value constructor that you can get and inspect
   
   ## Gettablity
   
   As with [[Value]] you can also get the value of a `ValueConstructor`, 
   doing so obtains instance:
   
        shared class Color {
            shared String hex;
            shared new black {
                this.hex="#000000";
            }
            shared new white {
                this.hex="#ffffff";
            }
        }
        
        void test() {
        ValueConstructor<Color> ctor = `Color.black`;
        // This will print: #000000
        print(ctor.get());
        
   """
shared sealed interface ValueConstructor<out Type=Object>
        satisfies ValueModel<Type> & Gettable<Type> {
    
    "This value's declaration."
    shared formal actual ValueConstructorDeclaration declaration;
    
    "This value's closed type."
    shared formal actual Class<Type> type;
    
    "The class containing this constructor; the type of instances produced 
     by this constructor."
    shared actual formal Class<Type>? container;
}

