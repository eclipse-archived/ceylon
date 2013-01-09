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
@nomodel
shared interface XIterable893<out Element> {
    shared default XIterable893<Element> success { return nothing; }
    shared default XIterable893<Element> fail1 { return nothing; }
    shared formal XIterable893<Element> fail2;
}
@nomodel
shared interface XSequential893<out Element>
        of XEmpty893|XSequence893<Element>
        satisfies XIterable893<Element> {
    
}
@nomodel
shared interface XSequence893<out Element>
        satisfies XSequential893<Element> {
    shared actual formal XEmpty893|XSequence893<Element> success;
    shared actual formal XEmpty893|XSequence893<Element> fail1;
    shared actual formal XEmpty893|XSequence893<Element> fail2;
}
@nomodel
shared interface XEmpty893
           satisfies XSequential893<Nothing> {
}
@nomodel
shared abstract class XSingleton893<out Element>(Element element)
        extends Object()
        satisfies XSequence893<Element>
        given Element satisfies Object {
    shared actual XEmpty893 success { return nothing; }
    shared actual XEmpty893 fail1 => nothing;
    shared actual XEmpty893 fail2 = nothing;
}