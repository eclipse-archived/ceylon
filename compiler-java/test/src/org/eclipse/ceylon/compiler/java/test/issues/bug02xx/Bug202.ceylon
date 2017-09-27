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
interface Bug202_ReadList<out T> {
    shared formal T get(Integer i);
}

@noanno
class Bug202_Tricky() satisfies Bug202_ReadList<Bug202_ReadList<Bug202_Tricky>> {
    shared actual Bug202_ReadList<Bug202_Tricky> get(Integer i) {
        throw;
    }
}

@noanno
Bug202_Tricky|Bug202_ReadList<Bug202_Tricky> bug202_tricky(Integer i) { 
    if ( i <= 0) {
        return Bug202_Tricky(); 
    } 
    else { 
        return bug202_tricky(i-1).get(i); 
    }
}
