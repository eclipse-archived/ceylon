/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"""The Ceylon metamodel open type and declaration package.
   
   As described in the [ceylon.language.meta](../index.html) documentation, this package contains all
   the types that represent Ceylon declarations and open types.
   
   ### Usage example
   
   The following code will list all the classes in the `ceylon.language` package and print their
   extended type:
   
       for(decl in `package ceylon.language`.members<ClassDeclaration>()){
           if(exists extendedType = decl.extendedType){
               print("Class ``decl.name`` extends: ``extendedType``");
           }else{
               print("Class ``decl.name`` does not extend anything");
           }
       }

   The following code will iterate all the class declarations in the `ceylon.language` package that
   are not abstract, anonymous or annotations, and that have no type parameters nor initialiser
   parameters. For each matching class, we will apply it to get a class model which we can then
   use to instantiate the class and display its instance:
   
       for(decl in `package ceylon.language`.members<ClassDeclaration>()){
           if(!decl.abstract 
                   && !decl.anonymous 
                   && !decl.annotation
                   && decl.parameterDeclarations.empty
                   && decl.typeParameterDeclarations.empty){
               Class<Object,[]> classModel = decl.classApply<Object,[]>();
               Object instance = classModel();
               print("Instance of ``decl.name`` is: ``instance``");
           }
       }
   """
by ("Gavin King", "Stephane Epardaud", "Tom Bentley")
tagged("Metamodel")
shared package ceylon.language.meta.declaration;
