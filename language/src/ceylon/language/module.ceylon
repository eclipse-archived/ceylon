"The Ceylon language module containing the core types 
 referred to in the language specification, and including 
 the following broad areas of functionality:
 
 - the foundational types [[Anything]], [[Object]], and 
   [[Null]], and a [[logical boolean|Boolean]] type, 
 - abstractions of [[numeric|Numeric]] types, along with
   basic types representing [[integral|Integer]] and 
   [[floating point|Float]] values, 
-  [[characters|Character]] and [[character strings|String]],
 - support for functional programming with 
   [[streams|Iterable]],
 - abstract interfaces for unmodifiable [[lists|List]], 
   [[sets|Set]], and [[maps|Map]],
 - immutable [[sequences|Sequential]], [[ranges|Range]], and
   [[tuples|Tuple]], which provide the foundation for 
   representing [[function types|Callable]] and lists of
   function arguments,
 - generic higher-order functions, including for function 
   [[composition|compose]] and [[partial application|curry]],
 - [[exceptions|Throwable]] and support for management of
   heavyweight [[destroyable|Destroyable]] and 
   [[obtainable|Obtainable]] objects,
 - access to information about the current 
   [[virtual machine|runtime]], [[system|system]], 
   [[process]], and [[operating system|system]], and
 - support for [[annotations|Annotation]] and the metamodel 
   API.
 
 Finally, this module defines the [[most useful and
 interesting void function of all time ever|print]].
 
 This module defines an abstraction over the basic 
 facilities of the Java or JavaScript virtual machine, 
 containing only functionality that can be easily 
 implemented on both platforms. This, certain functionality, 
 for example, concurrency, for which there is no common
 virtual machine-agnostic model, is not covered by the
 langauge module."
by ("Gavin King", "Tom Bentley", "Tako Schotanus",
   "Stephane Epardaud", "Enrique Zamudio")
license ("http://www.apache.org/licenses/LICENSE-2.0.html")
module ceylon.language "1.1.0" {}
