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
abstract class Bug526Top() {
    shared formal Integer formalAttr;
    shared formal variable Integer formalVar;
    
    shared default Integer defaultAttr = 3;
    shared default variable Integer defaultVar = 3;
    
    shared default Integer defaultGetter { return 3; }
    shared default Integer defaultSetter { return 3; }
    assign defaultSetter {}
}

@noanno
class Bug526Bottom1() extends Bug526Top() {
    shared actual variable Integer formalAttr = 2;
    shared actual variable Integer formalVar = 2;

    shared actual variable Integer defaultAttr = 2;
    shared actual variable Integer defaultVar = 2;
    
    shared actual variable Integer defaultGetter = 2;
    shared actual variable Integer defaultSetter = 2;
}

@noanno
class Bug526Bottom2() extends Bug526Top() {
    shared actual Integer formalAttr { return 2;} assign formalAttr {}
    shared actual Integer formalVar { return 2;} assign formalVar {}

    shared actual Integer defaultAttr { return 2;} assign defaultAttr {}
    shared actual Integer defaultVar { return 2;} assign defaultVar {}
    
    shared actual Integer defaultGetter { return 2;} assign defaultGetter {}
    shared actual Integer defaultSetter { return 2;} assign defaultSetter {}
}