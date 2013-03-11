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
class MethodWhileExists2dArray() {
    void ints() {
        value t = arrayOfSize(2,arrayOfSize(2,0));
        while (exists r=t[0]) {
            r.set(0,100);
        }
    }
    void strings() {
        value t = arrayOfSize(2,arrayOfSize(2,""));
        while (exists r=t[0]) {
            r.set(0,"");
        }
    }
    void nulls() {
        value t = arrayOfSize(2,arrayOfSize(2,null));
        while (exists r=t[0]) {
            r.set(0,null);
        }
    }
    void objects(Object obj) {
        value t = arrayOfSize(2,arrayOfSize(2,obj));
        while (exists r=t[0]) {
            r.set(0,obj);
        }
    }
    void union(String|Float obj) {
        value t = arrayOfSize(2,arrayOfSize(2,obj));
        while (exists r=t[0]) {
            r.set(0,obj);
        }
    }
}