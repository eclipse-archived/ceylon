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
shared interface IBug2068 {
    shared class IShared() {}
    shared formal class IFormal() {}
    shared default class IDefault() {}
    shared formal class IFormal2() {}
    shared default class IDefault2() {}
}
@noanno
shared interface IBug2068_2 satisfies IBug2068 {
    shared actual formal class IFormal2() extends super.IFormal2() {}
    shared actual default class IDefault2() extends super.IDefault2() {}
}
@noanno
shared abstract class CBug2068() {
    shared class CShared() {}
    shared formal class CFormal() {}
    shared default class CDefault() {}
    shared formal class CFormal2() {}
    shared default class CDefault2() {}
}
@noanno
shared abstract class CBug2068_2() extends CBug2068() {
    shared actual formal class CFormal2() extends super.CFormal2() {}
    shared actual default class CDefault2() extends super.CDefault2() {}
}
@noanno
shared abstract class AbstractBug2068() extends CBug2068_2() satisfies IBug2068_2 {
}
@noanno
shared class ConcreteBug2068() extends AbstractBug2068() {
    shared actual class IFormal() extends super.IFormal() {}
    shared actual class IFormal2() extends super.IFormal2() {}
    shared actual class CFormal() extends super.CFormal() {}
    shared actual class CFormal2() extends super.CFormal2() {}
}