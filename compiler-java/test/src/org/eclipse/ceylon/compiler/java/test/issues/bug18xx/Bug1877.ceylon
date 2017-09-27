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
class Bug1877(){
    shared void meth(){}
}

@noanno
void bug1877(){
    value t1 = [Bug1877].clone();
    print([Bug1877].clone());
    print({Bug1877}.iterator());
    value t2 = [bug1877].clone();
    print([bug1877].clone());
    print({bug1877}.iterator());
    value fa = Bug1877.meth;
    value b = Bug1877();
    value fb = Bug1877.meth(b);
    value fc = Bug1877.meth(b)();
    value i1 = Bug1877Java.attr;
    value i2 = Bug1877Java.meth();
    Bug1877Java.meth();
    Bug1877Java.attr++;
    ++Bug1877Java.attr;
    Bug1877Java.attr+=2;
    Bug1877Java.Type t;
    value f1 = Bug1877Java.Type;
    value f2 = Bug1877Java.meth;
}
