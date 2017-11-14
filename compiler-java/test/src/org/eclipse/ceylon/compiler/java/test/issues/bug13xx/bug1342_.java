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
package org.eclipse.ceylon.compiler.java.test.issues.bug13xx;

final class bug1342_ {
    
    static void bug134X(final ceylon.language.meta.model.Class<? extends ceylon.language.SharedAnnotation, ? super ceylon.language.Empty> s, 
            final ceylon.language.meta.declaration.ValueDeclaration a) {
        ceylon.language.SharedAnnotation sa = (ceylon.language.SharedAnnotation)(java.lang.Object)
                ceylon.language.meta.optionalAnnotation_.optionalAnnotation(null, null, (ceylon.language.meta.model.Class)s, a);
    }
}