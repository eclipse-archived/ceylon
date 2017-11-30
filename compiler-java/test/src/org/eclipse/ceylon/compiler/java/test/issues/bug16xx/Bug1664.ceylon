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
import ceylon.language.meta.model { Class }

@noanno
shared abstract class Bug1664UnitOfTime<in UnitType>() {
    shared formal NewUnitType convertTo<NewUnitType>() given NewUnitType satisfies UnitType;
}

@noanno
shared class Bug1664Milliseconds() extends Bug1664UnitOfTime<Bug1664Milliseconds>() {

    shared actual NewUnitType convertTo<NewUnitType>() => nothing;

}

@noanno
shared class Bug1664Seconds() extends Bug1664UnitOfTime<Bug1664Seconds>() {

    shared actual NewUnitType convertTo<NewUnitType>() => nothing;

}

@noanno
shared void bug1664() { 
    Bug1664Milliseconds millis = Bug1664Milliseconds();
    Bug1664Seconds sec = millis.convertTo<Bug1664Seconds>(); // ISSUE HERE
}
