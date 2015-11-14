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
class UnaryCtor {
    shared String s;
    shared new (String s1) {
        s = s1;
    }
    new fromInteger(Integer i) {
        s = i.string;
    }
    shared new sharedFromInteger(Integer i) {
        s = i.string;
    }
    void use() {
        value o1 = UnaryCtor{s1="";};
        value o3 = fromInteger{i=0;};
        value o4 = UnaryCtor.fromInteger{i=0;};
    }
}
