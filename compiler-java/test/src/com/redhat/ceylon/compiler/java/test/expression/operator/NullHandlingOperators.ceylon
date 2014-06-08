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
@noanno
shared class NullHandlingOperators() {

    T box<T>(T t){
        return t;
    }
    
    void nullHandling() {
        Integer? natOrNothing1 = 0;
        Integer? intOrNothing = +0;
        Integer n = natOrNothing1 else 2;
        Integer? nBoxed = natOrNothing1 else box(0); 
        variable Integer? nullSafeMember = intOrNothing?.negated;
        variable Integer? nullSafeInvoke = intOrNothing?.plus(+1);
        nullSafeInvoke = intOrNothing?.plus{
            other = +1;
        };
        
        String[]? s1 = null;
        String[] s2 = s1 else {};
    }
    
    void testEmpty() {
        variable Boolean sync = false;
        sync = "".sequence() nonempty;
        String[] seq = {};
        sync = seq nonempty;
        // boxing
        Boolean? boxed = seq nonempty;
    }


    void testExists() {
        variable Boolean sync = false;
        Object? foo = sync; 
        sync = foo exists;
        // boxing
        Boolean? boxed = foo exists;
    }
    
    void testThenElse() {
        Integer n = 5;
        String? foo = (n > 0) then "yes";
        String? bar = foo else "yes";
        String? baz = (n > 0) then "yes" else "no";
        Float x = 5.0;
        Float y = (x>0.0 then x else 1.0);
        // test empty raw casts
        Integer[] seq = false then {} else 0..2;
    }
}