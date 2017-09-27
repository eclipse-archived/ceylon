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
class Bug954(Boolean b) {
    Integer n = 2;
    
    Integer x(Integer i);
    x(Integer i) => i * n;
    

    Integer y(Integer i);
    if (b) {
        y(Integer i) => i * n * 2;
    } else {
        y(Integer i) => i * n * 3;
    }

    shared Integer pubx(Integer i);
    pubx(Integer i) => i * n;
    
    shared Integer puby(Integer i);
    if (b) {
        puby(Integer i) => i * n * 2;
    } else {
        puby(Integer i) => i * n * 3;
    }
}
void bug954(Boolean b) {
    Integer n = 2;
    
    Integer x(Integer i);
    x(Integer i) => i * n;
    
    Integer y(Integer i);
    if (b) {
        y(Integer i) => i * n * 2;
    } else {
        y(Integer i) => i * n * 3;
    }
}
