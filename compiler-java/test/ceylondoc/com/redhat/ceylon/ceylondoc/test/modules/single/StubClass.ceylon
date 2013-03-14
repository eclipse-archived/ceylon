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
doc "This is `StubClass`"
see(StubInterface, stubTopLevelAttribute, stubTopLevelMethod)
tagged("stubTag1", "stubTag2")
throws (StubException)
shared class StubClass(
  doc "Constructor parameter `a`" Integer a,
  doc "Constructor parameter `b`" Integer b,
  shared void printHello(String name) => print("Hello ``name``")) 
    satisfies StubInterface {

    doc "The stub attribute with `throws`."
    throws (OverflowException, "if the number is too large to be represented as an integer")
    shared Integer attributeWithThrows = 0;
    
    doc "The stub attribute with `see`."
    see (methodWithSee, stubObject.foo)
    shared Integer attributeWithSee = 0;
    
    doc "The stub attribute with `tagged`."
    tagged("stubTag1")
    shared Integer attributeWithTagged = 0;
    
    doc "The stub method with parameters documentation."
    shared void methodWithParametersDocumentation(
        doc "Method parameter `a`" Integer a, 
        doc "Method parameter `b`" Integer b) {}
    
    doc "The stub method with `throws`."
    throws (StubException, "`when` with __WIKI__ syntax")
    shared void methodWithThrows() {}
    
    doc "The stub method with `see`."
    see (attributeWithSee, StubException, A1)
    shared void methodWithSee() {}
    
    doc "The stub method with `tagged` and long description <i>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</i>"
    tagged("stubTag2")
    shared void methodWithTagged() {}
    
    doc "The stub method with sequenced parameter."
    shared void methodWithSequencedParameter(Integer* numbers) {}
    
    shared void methodWithCallableParameter1(void onClick()) {}
    
    shared void methodWithCallableParameter2<Element>(Boolean selecting(Element element)) {}
    
    shared void methodWithCallableParameter3(void fce1(void fce2(void fce3()))) {}
    
    shared void methodWithTouple1(Tuple<Integer|Float,Integer,Tuple<Float,Float,[]>> t) {}
    
    shared void methodWithTouple2<T>([String|T,Integer=,Float*] t) {}
    
    shared Anything methodWithAnything() { throw; }
    
    doc "Test fenced code block delimited by backticks \` with syntax highlighter.
         
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
    
    doc "Test fenced code block delimited by tilde ~ with syntax highlighter.
         
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
    
    doc "Test automatic syntax highlighter.
         
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
    
    
    doc "Wiki-style links:
         
         1. StubClass = [[StubClass]]
         1. StubInterface = [[StubInterface]]
         1. StubInnerException = [[StubInnerException]]
         1. stubTopLevelMethod = [[stubTopLevelMethod]]
         1. stubTopLevelAttribute = [[stubTopLevelAttribute]]
         1. StubInterface.formalMethodFromStubInterface = [[StubInterface.formalMethodFromStubInterface]]
         1. StubClass.StubInnerClass = [[StubClass.StubInnerClass]]
         1. StubClass.StubInnerClass.innerMethod = [[StubClass.StubInnerClass.innerMethod]]
         1. StubInterface with custom name = [[custom stub interface|StubInterface]]
         1. stubObject = [[stubObject]]
         1. stubObject.foo = [[stubObject.foo]]
         1. stubObject.stubInnerObject = [[stubObject.stubInnerObject]]
         1. stubObject.stubInnerObject.fooInner = [[stubObject.stubInnerObject.fooInner]]
         1. unresolvable = [[unresolvable]]
         1. imported A1 = [[A1]]
         1. imported AliasA2 = [[AliasA2]]
         
         
         Wiki-style links with full qualified name:
         
         1. fullStubInterface = [[com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface]]
         1. fullStubInterface.formalMethodFromStubInterface = [[com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface.formalMethodFromStubInterface]]
         1. fullStubInterface with custom name = [[full custom stub interface|com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface]]
         1. fullUnresolvable = [[unresolvable::StubInterface]]
         
         "
    shared void methodWithLinksInDoc() {}
        
    shared actual void formalMethodFromStubInterface() {}
    
    shared actual void defaultDeprecatedMethodFromStubInterface() {}
    
    shared String? bug691AbbreviatedOptionalType1() { throw; }
    
    shared Element? bug691AbbreviatedOptionalType2<Element>() { throw; }
    
    shared Iterable<Entry<Integer,Element&Object>> bug839<Element>() { throw; }

    shared Iterable<Integer> bug968_1() { throw; }
    shared Iterable<Integer, Nothing> bug968_2() { throw; }
    
    doc "This is `StubInnerInterface`"
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
    
    doc "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
    
         Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
         
         Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
         
         Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
         
         Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
         
         Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    shared void zzzIssue1072LinkingToMemberShouldExpandAndScrollDoc() {}
    
}