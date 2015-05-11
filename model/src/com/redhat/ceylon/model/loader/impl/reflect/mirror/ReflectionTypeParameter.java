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

package com.redhat.ceylon.model.loader.impl.reflect.mirror;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.model.loader.mirror.TypeMirror;
import com.redhat.ceylon.model.loader.mirror.TypeParameterMirror;

public class ReflectionTypeParameter implements TypeParameterMirror {

    private TypeVariable<?> type;
    private ArrayList<TypeMirror> bounds;

    public ReflectionTypeParameter(Type type) {
        this.type = (TypeVariable<?>) type;
    }

    @Override
    public String getName() {
        return type.getName();
    }

    @Override
    public List<TypeMirror> getBounds() {
        if(bounds != null)
            return bounds;
        Type[] javaBounds = type.getBounds();
        bounds = new ArrayList<TypeMirror>(javaBounds.length);
        for(Type bound : javaBounds)
            bounds.add(new ReflectionType(bound));
        return bounds;
    }

    @Override
    public String toString() {
        return "[ReflectionTypeParameter: "+type.toString()+"]";
    }
}
