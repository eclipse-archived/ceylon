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
shared class NullHandlingOperators() {
    
    T box<T>(T t){
        return t;
    }
    
    void nullHandling() {
        Natural? natOrNothing1 = 0;
        Integer? intOrNothing = +0;
        Natural n = natOrNothing1 ? 2;
        Natural? nBoxed = natOrNothing1 ? box(0); 
        variable Integer? nullSafeMember := intOrNothing?.negativeValue;
        variable Integer? nullSafeInvoke := intOrNothing?.plus(+1);
        nullSafeInvoke := intOrNothing?.plus{
            other = +1;
        };
    }
    
    void testEmpty() {
        variable Boolean sync := false;
        sync := nonempty "".characters;
        Object foo = sync; 
        sync := nonempty foo;
        sync := nonempty {};
        Iterable<String> iter = {};
        sync := nonempty iter;
        String[] seq = {};
        sync := nonempty seq;
        // boxing
        Boolean? boxed = nonempty seq;
    }


    void testExists() {
        variable Boolean sync := false;
        Object? foo = sync; 
        sync := exists foo;
        // boxing
        Boolean? boxed = exists foo;
    }
}