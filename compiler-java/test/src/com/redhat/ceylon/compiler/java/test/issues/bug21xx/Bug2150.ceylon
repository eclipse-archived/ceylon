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
abstract class Bug2150Boolean() of bug2150True | bug2150False {}
@noanno
object bug2150True extends Bug2150Boolean() {}
@noanno
object bug2150False extends Bug2150Boolean() {}

@noanno
void bug2150(){
    Boolean b = { if (true) true }.first else false;
    Bug2150Boolean mb = { if (true) bug2150True }.first else bug2150False; // error: required MyBoolean, found Object
    
    Comparison c = { if (true) smaller}.first else larger; // error: required Comparison, found Object
}