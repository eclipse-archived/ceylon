"The Ceylon language module containing the core definitions 
 referred to by the [language specification][spec], along 
 with some basic functionality of use to most programs:
 
 - The [[root package|package ceylon.language]] defines 
   general-purpose functionality including support for 
   [[numbers|Numeric]] and [[character strings|String]], 
   [[streams|Iterable]] and [[sequences|Sequential]], 
   [[exceptions|Throwable]], and [[null values|Null]].
 - The Ceylon _metamodel_ is defined in 
   [[package ceylon.language.meta]] and its subpackages 
   [[package ceylon.language.meta.model]] and 
   [[package ceylon.language.meta.declaration]], which
   define interfaces for interacting with applied types and 
   unapplied type declarations respectively.
 
 This module defines an abstraction over the basic 
 facilities of the Java or JavaScript virtual machine, 
 containing only functionality that can be easily 
 implemented on both platforms. Thus, certain functionality, 
 for example, concurrency, for which there is no common
 virtual machine-agnostic model, is not covered by the
 language module.
 
 The language module is an implicit dependency of every
 other Ceylon module, and may not be explicitly imported.
 
 [spec]: http://ceylon-lang.org/documentation/current/spec"
by ("Gavin King", "Tom Bentley", "Tako Schotanus",
   "Stephane Epardaud", "Enrique Zamudio")
license ("http://www.apache.org/licenses/LICENSE-2.0.html")
module ceylon.language "1.1.1" {
    native("jvm") import java.base "7";
}
