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
package org.eclipse.ceylon.compiler.java.codegen;

public class Annotations {

    public static final int NONE = 0;
    
    public static final int IGNORE = 1<<0;
    public static final int MODEL = 1<<1;
    public static final int USER = 1<<2;
    
    public static final int MODEL_AND_USER = MODEL | USER;
    
    private Annotations() {}
    
    /** determine whether to include model annotations */
    public static boolean includeModel(int flags) {
        return (flags & MODEL) != 0;
    }
    
    /** determine whether to include user annotations */
    public static boolean includeUser(int flags) {
        return (flags & USER) != 0;
    }
    
    /** determine whether to include the {@Ignore} annotation */
    public static boolean includeIgnore(int flags) {
        return (flags & IGNORE) != 0;
    }
    /** Clears the {@link #MODEL} bit and sets the {@link #IGNORE} bit */
    public static int ignore(int annotationFlags) {
        return (annotationFlags & ~MODEL) | IGNORE;
    }
    /** Clears the {@link #IGNORE} bit and sets the {@link #MODEL} bit */
    public static int model(int annotationFlags) {
        return (annotationFlags & ~IGNORE) | MODEL;
    }
    /** Clears the {@link #MODEL} and {@link #USER} bits */
    public static int noUserOrModel(int annotationFlags) {
        return (annotationFlags & ~(MODEL|USER));
    }
    /** Clears the {@link #MODEL} bit */
    public static int noModel(int annotationFlags) {
        return (annotationFlags & ~MODEL);
    }
}
