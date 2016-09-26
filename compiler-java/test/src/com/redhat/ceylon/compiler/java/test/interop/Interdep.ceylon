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
shared annotation final class CeylonAnnotation(Integer x) satisfies OptionalAnnotation<CeylonAnnotation>{}
shared annotation CeylonAnnotation ceylonAnnotation(Integer x) => CeylonAnnotation(x);

interdepJavaAnnotation(2)
shared class Interdep(InterdepJava j){
    shared void foo(InterdepJava j){
        InterdepJava(this);
        j.foo(this);
        Interdep x = j.interdepField;
        Interdep y = j.interdepProperty;
    }
    shared interface InnerInterface{}
    shared class Inner(InterdepJava.Inner j, InterdepJava.InnerInterface k) satisfies InnerInterface {
        
        shared InterdepJava.Inner foo(InterdepJava.Inner j){
            Inner i = j.foo(this);
            return j;
        }
        shared InterdepJava.InnerInterface bar(InterdepJava.InnerInterface j){
            InnerInterface i = j.bar(this);
            return j;
        }
    }

}

shared object toplevel {
    shared void foo(InterdepJava j){
        j.foo(null);
    }
}

shared class Ceylon(Interdep i) extends InterdepJava(i) satisfies InterdepJavaInterface {
    shared actual Interdep method(Interdep x) => x;
}