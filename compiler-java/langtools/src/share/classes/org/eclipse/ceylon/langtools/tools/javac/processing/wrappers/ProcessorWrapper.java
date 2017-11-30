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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.ceylon.javax.annotation.processing.Completion;
import org.eclipse.ceylon.javax.annotation.processing.ProcessingEnvironment;
import org.eclipse.ceylon.javax.annotation.processing.Processor;
import org.eclipse.ceylon.javax.annotation.processing.RoundEnvironment;
import org.eclipse.ceylon.javax.lang.model.SourceVersion;
import org.eclipse.ceylon.javax.lang.model.element.AnnotationMirror;
import org.eclipse.ceylon.javax.lang.model.element.Element;
import org.eclipse.ceylon.javax.lang.model.element.ExecutableElement;
import org.eclipse.ceylon.javax.lang.model.element.TypeElement;

public class ProcessorWrapper implements Processor {

    javax.annotation.processing.Processor d;
    private javax.annotation.processing.ProcessingEnvironment processingEnvironment;

    public ProcessorWrapper(javax.annotation.processing.Processor d) {
        this.d = d;
    }

    @Override
    public Set<String> getSupportedOptions() {
        // FIXME: probably check that the method is not overridden from AbstractProcessor
        javax.annotation.processing.SupportedOptions so2 = 
                d.getClass().getAnnotation(javax.annotation.processing.SupportedOptions.class);
        if(so2 != null)
            return arrayToSet(so2.value());
        return d.getSupportedOptions();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        // FIXME: probably check that the method is not overridden from AbstractProcessor
        javax.annotation.processing.SupportedAnnotationTypes sat2 = 
                d.getClass().getAnnotation(javax.annotation.processing.SupportedAnnotationTypes.class);
        if(sat2 != null)
            return arrayToSet(sat2.value());
        return d.getSupportedAnnotationTypes();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        // FIXME: probably check that the method is not overridden from AbstractProcessor
        javax.annotation.processing.SupportedSourceVersion ssv2 = 
                d.getClass().getAnnotation(javax.annotation.processing.SupportedSourceVersion.class);
        if(ssv2 != null)
            return Wrappers.wrap(ssv2.value());
        return Wrappers.wrap(d.getSupportedSourceVersion());
    }

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        // keep it around so it does not get GCed (required by NetBeans) who uses the Filer as
        // a weak key
        processingEnvironment = Facades.facade(processingEnv);
        d.init(processingEnvironment);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return d.process(Facades.facadeTypeElements(annotations), Facades.facade(roundEnv));
    }


    @Override
    public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation, ExecutableElement member, String userText) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Set<String> arrayToSet(String[] array) {
        assert array != null;
        Set<String> set = new HashSet<String>(array.length);
        for (String s : array)
            set.add(s);
        return Collections.unmodifiableSet(set);
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ProcessorWrapper == false)
            return false;
        return d.equals(((ProcessorWrapper)obj).d);
    }
    
    @Override
    public int hashCode() {
        return d.hashCode();
    }
    
    @Override
    public String toString() {
        return d.toString();
    }
}
