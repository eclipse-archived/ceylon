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
void bug1383(Bug1383Java j) {
    // fields
    j.hash_field = true;
    j.string_field = true;
    j.equals_field = true;
    if(j.hash_field && j.string_field && j.equals_field){}
    
    // original Object members
    Integer h = j.hash;
    String s = j.string;
    Boolean b = j.equals(j);
    
    // getters/setters
    j.setHash(j.getHash());
    j.setEquals(j.getEquals());
    j.setString(j.getString());
    
    // diverted methods
    Boolean b1 = j.hash_method();
    Boolean b2 = j.hash_method("hello");
    Boolean b3 = j.string_method();
    Boolean b4 = j.string_method("hello");
    // normal overload
    Boolean b5 = j.equals();
    Boolean b6 = j.equals("hello");
}

@noanno
shared class Bug1383() extends Bug1383JavaNoOverload(){
    shared actual String string = "str";
    shared actual Integer hash = 1;
    shared actual Boolean equals(Object o) => true;
    
    shared actual Boolean hash_method() => true;
    shared actual Boolean string_method() => true;
    
    shared actual Boolean getHash() => true;
    shared actual void setHash(Boolean b){}

    shared actual Boolean getString() => true;
    shared actual void setString(Boolean b){}

    shared actual Boolean getEquals() => true;
    shared actual void setEquals(Boolean b){}
}