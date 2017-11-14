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
package org.eclipse.ceylon.compiler.java.test.interop.nullable;

import javax.annotation.*;

public class NullAnnotationsJava {
    public String unknown;
    @Nonnull
    public String notNull;
    @Nullable
    public String nullable;
    
    public NullAnnotationsJava(String unknown, @Nonnull String notNull, @Nullable String nullable){}

    public void method(String unknown, @Nonnull String notNull, @Nullable String nullable){}
    
    public String unknown(){ return null; }
    @Nonnull
    public String notNull(){ return null; }
    @Nullable
    public String nullable(){ return null; }

    public String getUnknownProperty(){ return null; }
    @Nonnull
    public String getNotNullProperty(){ return null; }
    @Nullable
    public String getNullableProperty(){ return null; }

    @Nonnull
    public String getNotNullProperty2(){ return null; }
    @Nullable
    public String getNullableProperty2(){ return null; }

    public void setUnknownProperty(String str){}
    public void setNotNullProperty(@Nonnull String str){}
    public void setNullableProperty(@Nullable String str){}

    // non-annotated setters
    public void setNotNullProperty2(String str){}
    public void setNullableProperty2(String str){}
}