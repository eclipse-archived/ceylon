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
void bug1918(){
    value valList = {1, 30.0, "", null};
    //while(exists current = valList.deleteFirst()) { // same with if or assert
    if(exists current = valList.first){
        if(is Integer current) {
            print(current + 2);
        }
        else { // can not find symbol 'current'
            print(current);
        }
        print(current);
        if(is Integer current) {
            print(current + 2);
        }
        else if(is String current) { // can not find symbol 'current'
            print(current.lowercased);
        }
    }
}
