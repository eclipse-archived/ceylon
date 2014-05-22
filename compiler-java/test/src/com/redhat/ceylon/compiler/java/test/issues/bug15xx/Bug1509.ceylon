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
abstract shared class Bug1509() {
    shared {Bug1509+} allFoo = [bug1509];
}

object bug1509 extends Bug1509(){}
Integer bug1509_2 = (function (){ throw Bug1509Exception("Hah"); })();

class Bug1509Exception(String desc) extends Exception(desc){}

void testBug1509() {
    // first time we used to have the exception
    try{
        value f = bug1509;
        assert(false);
    }catch(Throwable t){
        assert(is InitializationError t);
        assert(t.message == "Cyclic initialization trying to read the value of 'bug1509' before it was set");
    }
    // second time a NoClassDefFound
    try{
        value f = bug1509;
        assert(false);
    }catch(Throwable t){
        assert(is InitializationError t);
        assert(t.message == "Cyclic initialization trying to read the value of 'bug1509' before it was set");
    }
    // try with the metamodel too
    try{
        value f = `bug1509`.get();
        assert(false);
    }catch(Throwable t){
        assert(is InitializationError t);
        assert(t.message == "Cyclic initialization trying to read the value of 'bug1509' before it was set");
    }
    // now this variant where we throw ourselves
    try{
        value f = bug1509_2;
        assert(false);
    }catch(Throwable t){
        assert(is Bug1509Exception t);
        assert(t.message == "Hah");
    }
    // second time's the charm
    try{
        value f = bug1509_2;
        assert(false);
    }catch(Throwable t){
        assert(is Bug1509Exception t);
        assert(t.message == "Hah");
    }
}
