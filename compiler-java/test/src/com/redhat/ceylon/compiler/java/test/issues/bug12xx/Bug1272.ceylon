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

import ceylon.language.meta.model{Class}
import ceylon.language.meta.declaration{ValueDeclaration}
import ceylon.language.meta{optionalAnnotation}

@noanno
Value? bug1272<Value,ProgramElement>(
            Class<OptionalAnnotation<Value,ProgramElement>> annotationType,
            ProgramElement programElement)
        given Value satisfies OptionalAnnotation<Value,ProgramElement>
        given ProgramElement satisfies Annotated { 
    return null;
}

@noanno
void bug1272_callsite() {
    Class<SharedAnnotation, []> s = `SharedAnnotation`;
    ValueDeclaration x => nothing;
    bug1272(s, x);// This is OK!
    SharedAnnotation? srd = bug1272(s, x); // This causes javac stackoverflow
    //assert(bug1272(s, x) exists);
}
