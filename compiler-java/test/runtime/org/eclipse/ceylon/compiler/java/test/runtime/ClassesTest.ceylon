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
shared class ClassesTest() {
    
    @test
    shared void testWithMethods() {    
        class WithMethods() {
            void doNothing() {}
            shared Integer returnZero() { return 0; }
            shared String returnArgument(String arg) { return arg; }        
        }
        
        WithMethods instance = WithMethods();
        assertEquals(0,instance.returnZero());
        assertEquals("Flavio",instance.returnArgument("Flavio"));
    }

    @test
    shared void testWithAtributes() {
        class WithAttributes() {
            shared variable Integer count = 0;        
            shared void inc() { count += 1;}        
        }
    
        WithAttributes instance = WithAttributes();
        assertEquals(0,instance.count);
        instance.inc();
        assertEquals(1,instance.count);
    }
    
    @test
    shared void testWithAbstractMethods() {
        abstract class WithAbstractMethods() {                    
            shared formal Integer returnZero();        
        }
        
        class Concrete() extends WithAbstractMethods() {
            shared actual Integer returnZero() {return 0;}        
        }
        
        Concrete instance = Concrete();
        assertEquals(0, instance.returnZero());
    }
    
    @test
    shared void testWithAbstractAttributes() {
        abstract class Hello() {            
            shared formal String greeting;            
            shared String say() {
                return greeting;
            }
        }
        
        class DefaultHello() extends Hello() {
            shared actual String greeting {
                return "Hello, World!";
            }
        }

        DefaultHello hello = DefaultHello();
        assertEquals("Hello, World!", hello.say());
    }
    
    @test
    shared void testWithParameters() {
        abstract class Hello(String name) {            
            shared formal String greeting;            
            shared String say() {
                return greeting + " " + name;
            }
        }
        
        class DefaultHello(String name) extends Hello(name) {
            shared actual String greeting {
                return "Hello, World!";
            }
        }
        String name = "Flavio";
        DefaultHello hello = DefaultHello(name);
        assertEquals("Hello, World!" + " " + name, hello.say());
    }    
        
}