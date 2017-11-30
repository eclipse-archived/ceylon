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
import ceylon.language.meta { type }

String bug1655mpl()(Anything a) {
    if (is Object a) {
        return a.string;
    }
    return "<null>";
}
class Bug1655() {
    shared String attribute => "";
    shared String method(Integer i) {
        return i.string;
    }
    shared void higher(Anything fn) {
        assert(fn is String(Integer)(Boolean));
    }
}
shared void bug1655() {
    String(Anything) onceInvoked = bug1655mpl();
    assert(is String(Anything) x=(onceInvoked of Anything) );
    assert(type(onceInvoked).string.endsWith("Callable<ceylon.language::String,ceylon.language::Tuple<ceylon.language::Anything,ceylon.language::Anything,ceylon.language::Empty>>")); 
    assert("Hello, World!" == onceInvoked("Hello, World!"));
    
    String(Anything)() ref = bug1655mpl;
    assert(is String(Anything)() y=(ref of Anything) );
    assert(type(ref).string.endsWith("Callable<ceylon.language::Callable<ceylon.language::String,ceylon.language::Tuple<ceylon.language::Anything,ceylon.language::Anything,ceylon.language::Empty>>,ceylon.language::Empty>"));
    
    String(Bug1655) staticAttrRef = Bug1655.attribute;
    assert((staticAttrRef of Anything) is String(Bug1655));
    
    String(Integer)(Bug1655) staticMethRef = Bug1655.method;
    assert((staticMethRef of Anything) is String(Integer)(Bug1655));
    
    Bug1655().higher{
        function fn(Boolean b) {
            return Integer.string;
        }
    };
    Bug1655().higher((Boolean b) => Integer.string);
}