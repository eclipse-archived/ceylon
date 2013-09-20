"""The Ceylon metamodel base package.
   
   The Ceylon metamodel allows you to: 
   
   * dynamically inspect modules, packages, functions, values and types, 
   * invoke functions and class initialisers, or read and write values, and
   * inspect the annotations on program elements.
   
   ### A little bit of terminology
   
   There are a few concepts and terms in the Ceylon metamodel API that you should be familiar with:
   
   - A _declaration_ is the definition of a Ceylon construct, such as a module, package, value, function
     or class. Declarations are singletons: there is only a single instance of a given class declaration,
     for example. You can inspect declarations to get information about how they were defined by their
     author. You cannot directly invoke function or class declarations, but you can _apply_ them to get
     a _model_ that you can invoke.
   - A _model_ is a Ceylon definition that represents a declaration where all the type variables have
     been bound to _closed type_ values. You can query models for their member models and you can directly
     invoke models.
   - A _closed type_ is a type which does not contain any unbound type variables.
   - An _open type_ is a type which may contain unbound type variables.
   
   For example, given the following Ceylon program:
   
       shared abstract class MyList<T>() satisfies List<T>{}
   
   The declaration of `MyList` represents the class declaration and contains the information that it is
   `abstract` and that it satisfies the `List<T>` open type. That type is open because it contains an
   unbound type variable `T`, which is not bound when we inspect the `MyList` class declaration.
   
   Given an particular instance of `MyList`, we can query its (closed) type with the [[type]] function,
   and we obtain a closed type representing (for example) `MyList<Integer>`. Object instances necessarily
   have a closed type at runtime, since in order to instantiate an object, all type arguments must be
   provided and known during instantiation, so the type of an object instance at runtime is necessarily
   a closed type: they cannot contain unbound type variables.
   
   Closed types that represent class or interfaces are also models. For example, the closed type of our
   `MyList<Integer>` instance is both a closed type and a class model: you can query its satisfied types
   and find that it satisfies `List<Integer>` closed type and model (as opposed to the class declaration
   of `MyList` which satisfies the `List<T>` open type). You can also invoke that model to obtain a new
   instance of `MyList<Integer>`.
   
   ### Model and declaration literals
   
   Ceylon supports getting declaration values using either the declaration API or using declaration
   literals:
   
   - <code>\`module ceylon.file\`</code> returns the [[Module|ceylon.language.meta.declaration::Module]] 
     declaration which corresponds to the `ceylon.file` module you imported in your module descriptor.
   - <code>\`package ceylon.language.meta\`</code> returns the [[Package|ceylon.language.meta.declaration::Package]]
     declaration from your current module imports.
   - <code>\`interface List\`</code> returns the [[InterfaceDeclaration|ceylon.language.meta.declaration::InterfaceDeclaration]] 
     for the [[List|ceylon.language::List]] type.
   - <code>\`class Integer\`</code> returns the [[ClassDeclaration|ceylon.language.meta.declaration::ClassDeclaration]] 
     for the [[Integer|ceylon.language::Integer]] type.
   - <code>\`function type\`</code> returns the [[FunctionDeclaration|ceylon.language.meta.declaration::FunctionDeclaration]] 
     for the [[type]] function. Similarly <code>\`function List.shorterThan\`</code> for methods.
   - <code>\`value modules\`</code> returns the [[ValueDeclaration|ceylon.language.meta.declaration::ValueDeclaration]] 
     for the [[modules]] value. Similarly <code>\`function List.size\`</code> for attributes.
   - <code>\`alias AliasName\`</code> returns the [[AliasDeclaration|ceylon.language.meta.declaration::AliasDeclaration]]
     for the `AliasName` type alias. 
   - <code>\`given T\`</code> returns the [[TypeParameter|ceylon.language.meta.declaration::TypeParameter]] 
     for the `T` type parameter.
   
   Note that declaration literals cannot have type arguments specified on types or methods, as declarations are not types.
   
   You can also get access to closed types and model using either the model API or using literals: 
   
   - <code>\`List&lt;Integer>\`</code> returns the [[Interface|ceylon.language.meta.model::Interface]] model and 
     closed type for the [[List|ceylon.language::List]] type applied with the [[Integer|ceylon.language::Integer]] type argument.
   - <code>\`Integer\`</code> returns the [[Class|ceylon.language.meta.model::Class]] model and closed type 
     for the [[Integer|ceylon.language::Integer]] type.
   - <code>\`type&lt;Integer>\`</code> returns the [[Function|ceylon.language.meta.model::Function]] model 
     for the [[type]] function applied with the [[Integer|ceylon.language::Integer]] type argument.
     Similarly <code>\`List&lt;Integer>.shorterThan\`</code> for method models.
   - <code>\`modules\`</code> returns the [[Value|ceylon.language.meta.model::Value]] model 
     for the [[modules]] value. Similarly <code>\`List&lt;Integer>.size\`</code> for attribute models.
   - <code>\`A & B\`</code> returns a [[IntersectionType|ceylon.language.meta.model::IntersectionType]]
     for the `A & B` intersection type.
   - <code>\`A | B\`</code> returns a [[UnionType|ceylon.language.meta.model::UnionType]]
     for the `A | B` union type.
   - <code>\`T\`</code> returns a [[Type|ceylon.language.meta.model::Type]] representing the runtime type 
     argument value for the `T` type parameter.
   
   Notice that all model and close type literals must be applied with all required type arguments.
   
   ### Accessing the metamodel using the API
   
   Aside from declaration and model literals there are several ways you can start using the metamodel API:
   
   - The [[modules]] object contains a list of all currently loaded [[Module|ceylon.language.meta.declaration::Module]]
     declarations. Note that these contain even modules you did not import as it contains all transitive
     dependencies, and may contain multiple different versions of the same module.
   - The [[type]] function will return the closed type of the given instance, which can only be a
     [[ClassModel|ceylon.language.meta.model::ClassModel]] since only classes can be instantiated.
   - The [[typeLiteral]] function is the functional equivalent to closed type literals: it turns a type argument
     value into a metamodel closed type.
   - The [declaration](declaration/index.html) package contains all the declaration and open types.
   - The [model](model/index.html) package contains all the model and closed types.
   
   ### Inspecting annotations
   
   Constrained annotations can be inspected using the [[annotations]] 
   function, like this:
   
       // Does the process declaration have the Shared annotation?
       value isShared = annotations(`Shared`, `value process`) exists;
   
   or the related [[optionalAnnotation]] and [[sequencedAnnotations]] functions.
   
   Note that annotations are queried for via their 
   [[ceylon.language::Annotation]] type, 
   not by the annotation constructor which was used to annotate 
   the program element.
   """
by ("Gavin King", "Stephane Epardaud")
shared package ceylon.language.meta;