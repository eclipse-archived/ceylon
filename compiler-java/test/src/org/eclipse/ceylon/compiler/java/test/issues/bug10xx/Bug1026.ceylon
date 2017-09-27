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
void bug1026() {
    Float bug1026<Number>(Number n)
            given Number of Integer | Float {
        variable value sum=0.0;
        
            if (is Integer n) {
                sum+=n;
                sum=sum+n;
            }
            switch (n)
            case (is Integer) { 
                sum+=n;
                sum=sum+n; 
            }
            case (is Float) { 
                sum+=n; 
            }
        
        return sum;
    }
    assert(bug1026(1) == 4.0);
    assert(bug1026(1.0) == 1.0);
}