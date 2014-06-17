This directory contains native JS code for the language module.

##Generating the JS language module

The ceylon-js repo reads the files in this dir to create the language module file. The process is as follows:

* The `ceylon.language.js` is read line by line and copied to another file, omitting blank lines and lines which only contain a single-line comment.
* When the line `//#METAMODEL` is found, the metamodel for the language module is generated from Ceylon sources, and written to the output file.
* When a line starting with `//#COMPILE` is found, the specified sources are compiled and written to the output file.

###The `//#COMPILE` directive

This directive expects a list of files, which can be Ceylon sources or native js files. For Ceylon sources,
only the filename without the extension is needed, and the path needs to be relative to `src/ceylon/language`
in this repo. So for example to compile `src/ceylon/language/Object.ceylon` only needs to be specified as
`Object`, but `src/ceylon/language/meta/declaration/OpenType.ceylon` needs to be specified as
`meta/declaration/OpenType`.

All the files specified in one `//#COMPILE` line will be compiled in one pass and written to the output file,
in the order in which they are specified (the order in which things are added to the module can be important
for some code).

To add native JS code, just specify the file with the `.js` extension. For example, `functions.js` will
look for that file inside this directory (`runtime-js` in case you forgot).

###Compiling declarations with the `native` annotation

Some types and methods in the language module are annotated `native`. Depending on the declaration in question,
the compiler will expect to find certain files with the native implementations, which will be merged into the
code being generated.

With a toplevel method, such as `shared native void foo();` the compiler will look for the `foo.js` file inside
this directory. If the method is not inside the main package (`ceylon.language`) but a subpackage, then the
subpackages must be prepended to the name. For example if `foo()` is inside `meta/model` then the compiler
will look for the file `meta/model/foo.js` and stop with an error if it's not found.

Toplevel objects are compiled just as regular objects, but any `native` members need to have a file in a
subdirectory with the same name as the object.
For example for `ceylon.language::process.write` the file needs to be called `process/write.js`.

Toplevel types are a bit different. The compiler will look for a file with the declaration's name, with the
`.js` extension (for example, `Array.js`); if it finds the file, it will use it instead of generating the code
for the constructor (the file must contain the constructor function). If no JS file exists then the constructor
is compiled from the Ceylon source. But for the members of a type the lookup is the same; for example the
`get` method of the `Array` type will be looked up in `Array/get.js`.

After that, any members of the type annotated `native` are treated the same way already explained for object
members.

###Contents of the native JS snippets

For methods, the native file must contain a function definition.

For values, the native file must only contain the code inside the getter function. For example for the value
`ceylon.language::runtime.integerSize` the file `runtime/integerSize` only contains the code `return 53;`.

Remember the `exports` objects is actually called `ex$` if you want to export something in your JS code (this
is usually only needed if the compiler generates references to that declaration).

###Customizing type initialization

All types have a type initializer, which is a function that returns the type's constructor. Initializing a type
consists of calling the `initTypeProto` native JS function, which adds type information to the constructor's
prototype (which acts as a class), and adding any actual members to the prototype.

Certain native types require a custom initialization, usually for interop reasons. To customize a type's
initialization, you can simply add a file called `Type$init.js` (where `Type` is the actual type's name,
according to the rules previously stated). If the compiler finds this file, it will include its contents
in the type initializer, instead of generating the usual code. This feature is independent of the file
containing the constructor for the type; this means you could have both, just one, or none of
`YourType.js` and `YourType$init.js`.

###Customizing type constructors

Having to write a complete constructor in JS is rarely necessary; in most cases only a little customization
is needed, to add some private native values to the instance that will be returned.

Type constructors in the JS backend are very similar to their Ceylon counterparts: they're a function that
returns a new instance. The difference is that interfaces also have a function but in this case they
*must* receive an already existing instance, and the functions only add stuff to it.

For native types, this process can be customized at two points: a function can be called _before_ any
stuff is added to the type, and another can be called _after_ the constructor's code is executed, right
before the instance is returned.

This is even doable when compiling regular Ceylon code to JS. The difference when compiling the language
module is _where_ the files with these functions are expected. The files need to be in the same directory
where the constructor would be, with the suffixes `_cons_before` and `_cons_after`. For example, you can
have one (or both) of `YourType_cons_before.js` and `YourType_cons_after.js`.

If these files exist, they must contain an anonymous function, with the same parameters as the
constructor. For classes, all the parameters are passed first, then the type arguments (if it's a
parameterized type) and finally the instance which is being constructed. For interfaces, only the
type arguments (for parameterized interfaces) and the existing instance are passed. The function is
then immediately invoked and the execution continues.

When compiling the language module, a message is printed when the compiler finds one of these
functions and stitches it into the constructor.
