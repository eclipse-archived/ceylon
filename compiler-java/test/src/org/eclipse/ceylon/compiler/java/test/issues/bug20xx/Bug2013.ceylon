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
interface Bug2013Car {}
@noanno
interface Bug2013BigCar satisfies Bug2013Car {}
@noanno
class Bug2013Truck() satisfies Bug2013BigCar {}
@noanno
interface Bug2013Driver {
    shared formal Bug2013Car driveCar( Float speed = 0.0 );
}
@noanno
interface Bug2013ExperiencedDriver satisfies Bug2013Driver {
    shared formal actual Bug2013BigCar driveCar( Float speed );
}
@noanno
class Bug2013Trucker() satisfies Bug2013ExperiencedDriver {
    shared actual Bug2013BigCar driveCar( Float speed ) {
        return Bug2013Truck();
    }
}
@noanno
Bug2013ExperiencedDriver d = Bug2013Trucker();

//Car carA = d.driveCar(); // ok
//Car carB = d.driveCar( 1.0 ); // ok
//BigCar carBigA = d.driveCar( 1.0 ); // ok
@noanno
Bug2013BigCar carBigB = d.driveCar(); // Incompatible types required: $BigCar_ found: $Car_ 