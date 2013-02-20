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
@nomodel
void spreadArguments(Integer i, String s) {}
@nomodel
class SpreadArguments<T>(Integer i,  T s) {
    shared void m(Integer i, T s) {}
    shared void m2(Integer i, T* s) {}
    //shared void m3(Integer+ i) {}
    shared void m4(Integer* i) {}

    void spreadTuple([Integer, String] args) {
        spreadArguments(*args);
        spreadArguments(0, *args.rest);
        
        SpreadArguments<String>(*args);
        SpreadArguments<String>(0, *args.rest);
        
        Anything(Integer, String) f = nothing;
        f(*args);
        f(1, *args.rest);
        
        SpreadArguments<String> sa = nothing;
        sa.m2(*[0]);
        sa.m2(*[0, "hello"]);
        sa.m2(*[0, "hello", "world"]);
        [Integer, String, String+] t = nothing;
        sa.m2(*t);
    }
    void spreadTupleWithSpreadOp([Integer, String] args) {
        Iterable<SpreadArguments<String>> iter = nothing;
        iter*.m(*args);
        iter*.m(0, *args.rest);
        iter*.m2(0, *args.rest);
        iter*.m2(0, *args.rest.rest);
        iter*.m2(*[0]);
        iter*.m2(*[0, "hello"]);
        iter*.m2(*[0, "hello", "world"]);
    }
    void spreadIterator({Integer*} iter, {Integer+} nonEmptyIter) {
        //sa.m3(*nonEmptyIter);
        m4(*iter);
        m4(*nonEmptyIter);
    }
    void spreadIteratorWithSpreadOp({Integer*} iter, {Integer+} nonEmptyIter) {
        Iterable<SpreadArguments<String>> sas = nothing;
        sas*.m4(*iter);
        sas*.m4(*nonEmptyIter);
    }
}