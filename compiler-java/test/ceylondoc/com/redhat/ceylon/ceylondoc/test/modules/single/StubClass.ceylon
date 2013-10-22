import com.redhat.ceylon.ceylondoc.test.modules.single.a { A1, AliasA2 = A2 }

/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
"This is `StubClass`"
see(`interface StubInterface`, `value stubTopLevelAttribute`, `function stubTopLevelMethod`)
tagged("stubTag1", "stubTag2")
throws(`class StubException`)
shared class StubClass(
  "Initializer parameter `a`" Integer a,
  "Initializer parameter `b`" Integer b,
  shared void printHello(String name) => print("Hello ``name``")) 
    satisfies StubInterface {

    "The stub attribute with `throws`."
    throws(`class OverflowException`, "if the number is too large to be represented as an integer")
    shared Integer attributeWithThrows = 0;
    
    "The stub attribute with `see`."
    see(`function methodWithSee`/* NOT SUPPORTED YET:, `stubObject.foo`*/)
    shared Integer attributeWithSee = 0;
    
    "The stub attribute with `tagged`."
    tagged("stubTag1")
    shared Integer attributeWithTagged = 0;
    
    "The stub method with parameters documentation."
    shared void methodWithParametersDocumentation(
        "Method parameter `a`" Integer a, 
        "Method parameter `b`" Integer b) {}
    
    "The stub method with `throws`."
    throws(`class StubException`, "`when` with __WIKI__ syntax")
    shared void methodWithThrows() {}
    
    "The stub method with `see`."
    see(`value attributeWithSee`, `class StubException`, `class A1`, `class AliasA2`)
    shared void methodWithSee() {}
    
    "The stub method with `tagged` and long description <i>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</i>"
    tagged("stubTag2")
    shared void methodWithTagged() {}
    
    "The stub method with sequenced parameter."
    shared void methodWithSequencedParameter(Integer* numbers) {}
    
    shared void methodWithCallableParameter1(void onClick()) {}
    
    shared void methodWithCallableParameter2<Element>(Boolean selecting(Element element)) {}
    
    shared void methodWithCallableParameter3(void fce1(void fce2(void fce3()))) {}
    
    shared void methodWithTouple1(Tuple<Integer|Float,Integer,Tuple<Float,Float,[]>> t) {}
    
    shared void methodWithTouple2<T>([String|T,Integer=,Float*] t) {}
    
    shared void methodWithDefaultedParameter1(Boolean b1 = true, Boolean b2 = false, 
                                               Integer i1 = 0, Integer i2 = 1, Integer i3 = -1, 
                                               Float f1 = 0.0, Float f2 = 1.0, Float f3 = -1.0,
                                               String s1 = "", String? s2 = null, String[] s3 = []) {}
    
    shared void methodWithDefaultedParameter2(String a = "a", String b = constAbc) {}
    
    shared void methodWithDefaultedParameter3("Defaulted parameter with loooong text." String a = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.") {}
                                                           
    shared void methodWithDefaultedParameter4(Boolean(Character) separator = (Character ch) => ch.whitespace) {}
    
    shared Anything methodWithAnything() { throw; }
    
    "Test fenced code block delimited by backticks \` with syntax highlighter.
         
     \`\``ceylon
     shared default Boolean subset(Set set) {
         for (element in this) {
             if (!set.contains(element)) {
                 return false;
             }
         }
         return true;
     }
     \`\``
     
     <i>Lorem ipsum dolor sit amet, consectetur...</i>"
    shared void methodWithCodeExamples() {}
    
    "Test fenced code block delimited by tilde ~ with syntax highlighter.
     
     ~~~~~~ceylon
     shared actual default Integer hash {
         variable Integer hashCode = 1;
         for (Element elem in this) {
             hashCode *= 31;
             hashCode += elem.hash;
         }
         return hashCode;
     }         
     ~~~~~~
     
     <i>Lorem ipsum dolor sit amet, consectetur...</i>"
    shared void methodWithCodeExamples2() {}
    
    """Test fenced code block with brushes: html, css, plain, java, javascript ...
    
       * HTML
           
       ```html
       <body>
           <h1>My web page!</h1>
           <p>Hello world!</p>
       </body>       
       ```
       
       * CSS
       
       ```css
       body {
           background-color:#b0c4de;
       }
       ```
       
       * PLAIN
       
       ```plain
       my custom test
       ```
       
       * JAVA
       
       ```java
       public static void helloWorld(String name); 
       ```
       
       * JAVASCRIPT
       
       ```javascript
       document.write("<p>My Hello World!</p>");
       ```
    
    """
    shared void methodWithCodeExamples3() {}
    
    "Test automatic syntax highlighter.
     
         shared default Boolean subset(Set set) {
             for (element in this) {
                 if (!set.contains(element)) {
                     return false;
                 }
             }
             return true;
         }
     
     <i>Lorem ipsum dolor sit amet, consectetur...</i>"
    shared void methodWithCodeExamplesAutomaticSyntaxHighlighter() {}
    
    
    "Wiki-style links:
     
     1. StubClass = [[StubClass]]
     1. class StubClass = [[class StubClass]]
     1. StubInterface = [[StubInterface]]
     1. interface StubInterface = [[interface StubInterface]]
     1. StubInnerException = [[StubInnerException]]
     1. stubTopLevelMethod = [[stubTopLevelMethod]]
     1. function stubTopLevelMethod = [[function stubTopLevelMethod]]
     1. stubTopLevelAttribute = [[stubTopLevelAttribute]]
     1. value stubTopLevelAttribute = [[value stubTopLevelAttribute]]
     1. StubInterface.formalMethodFromStubInterface = [[StubInterface.formalMethodFromStubInterface]]
     1. StubClass.StubInnerClass = [[StubClass.StubInnerClass]]
     1. StubClass.StubInnerClass.innerMethod = [[StubClass.StubInnerClass.innerMethod]]
     1. StubInterface with custom name = [[custom stub interface|StubInterface]]
     1. stubObject = [[stubObject]]
     1. stubObject.foo = [[stubObject.foo]]
     1. stubObject.stubInnerObject = [[stubObject.stubInnerObject]]
     1. stubObject.stubInnerObject.fooInner = [[stubObject.stubInnerObject.fooInner]]
     1. unresolvable1 = [[unresolvable]]
     1. unresolvable2 = [[unresolvable with custom name|unresolvable]]
     1. imported A1 = [[A1]]
     1. imported AliasA2 = [[AliasA2]]
     1. StubClassWithGenericTypeParams = [[StubClassWithGenericTypeParams]]
     1. StubClassWithGenericTypeParams with custom name = [[custom with type params|StubClassWithGenericTypeParams]]
     1. module = [[module com.redhat.ceylon.ceylondoc.test.modules.single]]
     1. package = [[package com.redhat.ceylon.ceylondoc.test.modules.single]]
     
     Wiki-style links with full qualified name:
     
     1. fullStubInterface = [[com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface]]
     1. fullStubInterface.formalMethodFromStubInterface = [[com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface.formalMethodFromStubInterface]]
     1. fullStubInterface with custom name = [[full custom stub interface|com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface]]
     1. fullUnresolvable1 = [[unresolvable::Bar]]
     1. fullUnresolvable2 = [[unresolvable.bar::Bar.foo]]
     
     Wiki-style links to parameters:
     
     1. parameter s = [[s]]
     1. parameter methodWithParametersDocumentation.a = [[methodWithParametersDocumentation.a]]
     1. parameter stubTopLevelMethod.numbers = [[stubTopLevelMethod.numbers]] 
     
     "
    shared void methodWithLinksInDoc(String s) {}
        
    shared actual void formalMethodFromStubInterface() {}
    
    shared actual void defaultDeprecatedMethodFromStubInterface() {}
    
    shared String? bug691AbbreviatedOptionalType1() { throw; }
    
    shared Element? bug691AbbreviatedOptionalType2<Element>() { throw; }
    
    shared Iterable<Entry<Integer,Element&Object>> bug839<Element>() { throw; }

    shared Iterable<Integer> bug968_1() { throw; }
    shared Iterable<Integer, Nothing> bug968_2() { throw; }
    
    "This is `StubInnerInterface`"
    tagged("stubInnerTag1")
    shared interface StubInnerInterface {

        tagged("stubInnerMethodTag1")
        shared formal void innerMethod();
       
    }

    shared class StubInnerClass() satisfies StubInnerInterface {

        shared actual void innerMethod() {}

    }
    
    shared class StubInnerException() extends StubException() {

    }
    
    "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.

     Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
     
     Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
     
     Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
     
     Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
     
     Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    shared void zzzIssue1072LinkingToMemberShouldExpandAndScrollDoc() {}

    "Documented getter"
    shared Integer getter => 2;
    
    "Documented setter"
    assign getter => print(getter);
}