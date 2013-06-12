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
"This one should test the code for creating links to types"
by("Tom Bentley")
shared interface Types<X,Y> {
    shared formal X&Y paramIntersection();
    shared formal X|Y paramUnion();
    shared formal String&Number typeIntersection();
    shared formal String|Number typeUnion();
    shared formal String&Types<X,Y> typeIntersectionWithSelf();
    shared formal String|Types<X,Y> typeUnionWithSelf();
    shared formal X? paramOrNull();
    shared formal X[] paramSequence();
    shared formal String? typeOrNull();
    shared formal String[] typeSequence();
    shared formal Types<X,Y>? selfOrNull();
    shared formal Types<X,Y>[] selfSequence();
    shared formal X?&Y? paramIntersectionOrNull();
    shared formal X?|Y? paramUnionOrNull();
    shared formal X[]&Y[] paramIntersectionSequence();
    shared formal X[]|Y[] paramUnionOrSequence();
}