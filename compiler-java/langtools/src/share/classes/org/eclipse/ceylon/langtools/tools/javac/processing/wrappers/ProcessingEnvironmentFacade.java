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
package org.eclipse.ceylon.langtools.tools.javac.processing.wrappers;

import java.util.Locale;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class ProcessingEnvironmentFacade implements ProcessingEnvironment {

    private org.eclipse.ceylon.javax.annotation.processing.ProcessingEnvironment f;
    private Filer filer;

    public ProcessingEnvironmentFacade(org.eclipse.ceylon.javax.annotation.processing.ProcessingEnvironment f) {
        this.f = f;
        this.filer = Facades.facade(f.getFiler());
    }

    @Override
    public Elements getElementUtils() {
        return Facades.facade(f.getElementUtils());
    }

    @Override
    public Filer getFiler() {
        return filer;
    }

    @Override
    public Locale getLocale() {
        return f.getLocale();
    }

    @Override
    public Messager getMessager() {
        return Facades.facade(f.getMessager());
    }

    @Override
    public Map<String, String> getOptions() {
        return f.getOptions();
    }

    @Override
    public SourceVersion getSourceVersion() {
        return Facades.facade(f.getSourceVersion());
    }

    @Override
    public Types getTypeUtils() {
        return Facades.facade(f.getTypeUtils());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ProcessingEnvironmentFacade == false)
            return false;
        return f.equals(((ProcessingEnvironmentFacade)obj).f);
    }
    
    @Override
    public int hashCode() {
        return f.hashCode();
    }
    
    @Override
    public String toString() {
        return f.toString();
    }
}
