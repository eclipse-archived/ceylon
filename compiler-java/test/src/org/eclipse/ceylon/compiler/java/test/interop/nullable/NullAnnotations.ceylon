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

shared class NullAnnotations(){
    shared void foo(){
        value j = NullAnnotationsJava("a", "b", "c");
        NullAnnotationsJava(null, "b", null);

        j.method("a", "b", "c");
        j.method(null, "b", null);
        
        String unknown = j.unknown();
        @type:"String?"
        j.nullable();
        String notNull = j.notNull();

        String unknown2 = j.unknownProperty;
        @type:"String?"
        value nullable2 = j.nullableProperty;
        String notNull2 = j.notNullProperty;
        @type:"String?"
        value nullable3 = j.nullableProperty2;
        String notNull3 = j.notNullProperty2;

        j.unknownProperty = "foo";
        j.unknownProperty = null;
        j.nullableProperty = "foo";
        j.nullableProperty = null;
        j.notNullProperty = "foo";
        j.nullableProperty2 = "foo";
        j.nullableProperty2 = null;
        j.notNullProperty2 = "foo";

        @error
        j.notNullProperty = null;
        @error
        j.notNullProperty2 = null;
        @error
        NullAnnotationsJava("a", null, "c");
        @error
        j.method("a", null, "c");
   }
}