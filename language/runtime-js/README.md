This directory contains native JS code for the language module.

#Generating the JS language module

The ceylon-js repo reads the files in this dir to create the language module file. The process is as follows:

* The `ceylon.language.js` is read line by line and copied to another file, omitting blank lines and lines which only contain a single-line comment.
* When the line `//#METAMODEL` is found, the metamodel for the language module is generated from Ceylon sources, and written to the output file.
* When a line starting with `//#COMPILE` is found, the specified sources are compiled and written to the output file.

##The `//#COMPILE` directive

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

##Compiling declarations with the `native` annotation

Some types and methods in the language module are annotated `native`. Depending on the declaration in question,
the compiler will expect to find certain files with the native implementations, which will be merged into the
code being generated.

With a toplevel method, such as `shared native void foo();` the compiler will look for the `foo.js` file inside
this directory. If the method is not inside the main package (`ceylon.language`) but a subpackage, then the
subpackages must be prepended to the name. For example if `foo()` is inside `meta/model` then the compiler
will look for the file `_meta_declaration-foo.js` and stop with an error if it's not found.

Toplevel objects are compiled just as regular objects, but any `native` members need to have a file in this directory.
For example for `ceylon.language::process.write` the file needs to be called `process_write.js`.

Toplevel types are a bit different. The compiler will look for a file with the declaration's name, with the
`.js` extension (for example, `Array.js`); if it finds the file, it will use it instead of generating the code
for the constructor (the file must contain the constructor function). If no JS file exists then the constructor
is compiled from the Ceylon source.

After that, any members of the type annotated `native` are treated the same way already explained for object
members.

##Contents of the native JS snippets

For methods, the native file must contain a function definition.

For values, the native file must only contain the code inside the getter function. For example for the value
`ceylon.language::runtime.integerSize` the file `runtime_integerSize` only contains the code `return 53;`.

Remember the `exports` objects is actually called `ex$` if you want to export something in your JS code (this
is usually only needed if the compiler generates references to that declaration).
