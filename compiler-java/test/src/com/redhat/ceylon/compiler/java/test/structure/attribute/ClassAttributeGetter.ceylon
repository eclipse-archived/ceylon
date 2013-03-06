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
abstract class ClassAttributeGetter(){
    String f {
        return "";
    }
    shared String f2 {
        return "";
    }
    shared default String f3 {
        return "";
    }
    String f4 => "";
    shared String f5 => "";
    shared default String f6 => "";
}
@nomodel
class SubClassAttributeGetter() extends ClassAttributeGetter() {
    shared actual String f3 {
        return "";
    }
    shared actual String f6 => "";
}
