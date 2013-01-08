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
class QualifiedAttributeAccess(){
   Boolean b = true;
   variable Boolean b2 = true;
   QualifiedAttributeAccess q = QualifiedAttributeAccess();
   
   Boolean m(){
       return this.b;
   }

   Boolean m2(){
       return this.b2;
   }

   Boolean m3(){
       return QualifiedAttributeAccess().b;
   }

   Boolean m4(){
       return QualifiedAttributeAccess().b2;
   }

   Boolean m5(){
       return q.b;
   }

   Boolean m6(){
       return q.b2;
   }

   Boolean m7(Foo f){
       return f.b;
   }
}
@nomodel
class Foo() {
   shared Boolean b = true;
}