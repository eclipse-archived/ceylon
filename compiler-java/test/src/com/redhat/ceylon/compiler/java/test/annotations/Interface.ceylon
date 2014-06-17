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
shared interface TopInterface {
    shared formal Integer topFormalAttr;
    shared default Integer concreteDefaultAttr { return 1; }

    shared formal Integer topFormalMethod(Integer p1);
    shared default Integer concreteDefaultMethod(Integer p1){ return p1; }
    
    shared formal Integer topFormalAttr2;
    shared formal Integer topFormalMethod2(Integer p1);
}

shared interface Interface satisfies TopInterface {
    shared actual Integer topFormalAttr { return 1; }
    shared Integer concreteAttr { return 1; }
    shared actual formal Integer concreteDefaultAttr;

    shared actual Integer topFormalMethod(Integer p1){ return p1; }
    shared formal Integer formalMethod(Integer p1);
    shared Integer concreteMethod(Integer p1){ return p1; }
    shared actual formal Integer concreteDefaultMethod(Integer p1);
    
    topFormalAttr2 => 1;
    topFormalMethod2(Integer p1) => 1;
}
