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
class DestroyableCategory() satisfies Destroyable & Category<String> {
    contains(String s) => false;
    destroy(Throwable? er) => print("asd");
}

@noanno
void bug1887() {
    try (value y = nothing) {}
    // this works because the LHS type is just ignored, we use the RHS type which is always expressible in Java
    try (Destroyable&Category<String> y = DestroyableCategory()) {}
    Obtainable&Category<String> obtainableCategory = nothing;
    try (Obtainable&Category<String> y = obtainableCategory) {}
    try (nothing) {}
}