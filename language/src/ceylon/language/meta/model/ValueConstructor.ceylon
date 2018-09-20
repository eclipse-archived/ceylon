/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.declaration{ValueConstructorDeclaration}

"""A value constructor model represents the model of a Ceylon class 
   value constructor that you can get and inspect.
   
   ## Gettablity
   
   As with [[Value]] you can also get the value of a `ValueConstructor`, 
   doing so obtains the instance:
   
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
        }
        
   """
since("1.2.0")
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

