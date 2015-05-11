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

package com.redhat.ceylon.model.loader.model;

import java.util.EnumSet;
import java.util.List;

import com.redhat.ceylon.model.loader.mirror.AnnotationMirror;
import com.redhat.ceylon.model.typechecker.model.Class;

/**
 * Used for annotation proxies for interop.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class AnnotationProxyClass extends Class {

    public final LazyInterface iface;

    public AnnotationProxyClass(LazyInterface iface) {
        this.iface = iface;
    }
    
    /**
     * The elements in the {@code @Target} annotation, or null if 
     * the annotation type lacks the {@code @Target} annotation.
     */
    public EnumSet<AnnotationTarget> getAnnotationTarget() {
        AnnotationMirror targetAnno = iface.classMirror.getAnnotation("java.lang.annotation.Target");
        if (targetAnno != null) {
            List<String> targets = (List)targetAnno.getValue();
            EnumSet<AnnotationTarget> result = EnumSet.<AnnotationTarget>noneOf(AnnotationTarget.class);
            for (String name : targets) {
                result.add(AnnotationTarget.valueOf(name));
            }
            return result;
        }
        return null;
    }

}
