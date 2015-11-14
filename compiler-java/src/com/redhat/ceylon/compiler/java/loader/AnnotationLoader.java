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

package com.redhat.ceylon.compiler.java.loader;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.java.codegen.AnnotationArgument;
import com.redhat.ceylon.compiler.java.codegen.AnnotationConstructorParameter;
import com.redhat.ceylon.compiler.java.codegen.AnnotationFieldName;
import com.redhat.ceylon.compiler.java.codegen.AnnotationInvocation;
import com.redhat.ceylon.compiler.java.codegen.AnnotationTerm;
import com.redhat.ceylon.compiler.java.codegen.BooleanLiteralAnnotationTerm;
import com.redhat.ceylon.compiler.java.codegen.CharacterLiteralAnnotationTerm;
import com.redhat.ceylon.compiler.java.codegen.CollectionLiteralAnnotationTerm;
import com.redhat.ceylon.compiler.java.codegen.Decl;
import com.redhat.ceylon.compiler.java.codegen.DeclarationLiteralAnnotationTerm;
import com.redhat.ceylon.compiler.java.codegen.FloatLiteralAnnotationTerm;
import com.redhat.ceylon.compiler.java.codegen.IntegerLiteralAnnotationTerm;
import com.redhat.ceylon.compiler.java.codegen.InvocationAnnotationTerm;
import com.redhat.ceylon.compiler.java.codegen.LiteralAnnotationTerm;
import com.redhat.ceylon.compiler.java.codegen.Naming;
import com.redhat.ceylon.compiler.java.codegen.ObjectLiteralAnnotationTerm;
import com.redhat.ceylon.compiler.java.codegen.ParameterAnnotationTerm;
import com.redhat.ceylon.compiler.java.codegen.StringLiteralAnnotationTerm;
import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.loader.ModelLoader.DeclarationType;
import com.redhat.ceylon.model.loader.mirror.AnnotatedMirror;
import com.redhat.ceylon.model.loader.mirror.AnnotationMirror;
import com.redhat.ceylon.model.loader.mirror.ClassMirror;
import com.redhat.ceylon.model.loader.mirror.MethodMirror;
import com.redhat.ceylon.model.loader.mirror.TypeMirror;
import com.redhat.ceylon.model.loader.model.AnnotationProxyClass;
import com.redhat.ceylon.model.loader.model.AnnotationProxyMethod;
import com.redhat.ceylon.model.loader.model.LazyFunction;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.Unit;

/**
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class AnnotationLoader {

    private AbstractModelLoader modelLoader;
    private Unit typeFactory;

    public AnnotationLoader(AbstractModelLoader modelLoader, Unit unit) {
        this.modelLoader = modelLoader;
        this.typeFactory = unit;
    }

    public void setAnnotationConstructor(LazyFunction method, MethodMirror meth) {
        AnnotationInvocation ai = loadAnnotationInvocation(method, method.classMirror, meth);
        if (ai != null) {
            loadAnnotationConstructorDefaultedParameters(method, meth, ai);
            ai.setConstructorDeclaration(method);
            method.setAnnotationConstructor(ai);
        }
    }
    
    private AnnotationInvocation loadAnnotationInvocation(
            LazyFunction method, 
            AnnotatedMirror annoInstMirror, MethodMirror meth) {
        AnnotationInvocation ai = null;
        AnnotationMirror annotationInvocationAnnotation = null;
        
        List<AnnotationMirror> annotationTree = getAnnotationArrayValue(annoInstMirror, AbstractModelLoader.CEYLON_ANNOTATION_INSTANTIATION_TREE_ANNOTATION, "value");
        if (annotationTree != null
                && !annotationTree.isEmpty()) {
            annotationInvocationAnnotation = annotationTree.get(0);
        } else {
            annotationInvocationAnnotation = annoInstMirror.getAnnotation(AbstractModelLoader.CEYLON_ANNOTATION_INSTANTIATION_ANNOTATION);
        }
        //stringValueAnnotation = annoInstMirror.getAnnotation(CEYLON_STRING_VALUE_ANNOTATION);
        if (annotationInvocationAnnotation != null) {
            ai = new AnnotationInvocation();
            setPrimaryFromAnnotationInvocationAnnotation(annotationInvocationAnnotation, ai);
            loadAnnotationInvocationArguments(new ArrayList<AnnotationFieldName>(2), method, ai, annotationInvocationAnnotation, annotationTree, annoInstMirror);
        }
        return ai;
    }
    
    private void loadAnnotationInvocationArguments(
            List<AnnotationFieldName> path,
            LazyFunction method,
            AnnotationInvocation ai, AnnotationMirror annotationInvocationAnnotation,
            List<AnnotationMirror> annotationTree, 
            AnnotatedMirror dpm) {
        @SuppressWarnings("unchecked")
        List<Short> argumentCodes = (List<Short>)annotationInvocationAnnotation.getValue(AbstractModelLoader.CEYLON_ANNOTATION_INSTANTIATION_ARGUMENTS_MEMBER);
        if(argumentCodes != null){
            for (int ii = 0; ii < argumentCodes.size(); ii++) {
                short code = argumentCodes.get(ii);
                AnnotationArgument argument = new AnnotationArgument();
                Parameter classParameter = ai.getParameters().get(ii);
                argument.setParameter(classParameter);
                path.add(argument);
                argument.setTerm(loadAnnotationArgumentTerm(path, method, ai, classParameter, annotationTree, dpm, code));
                path.remove(path.size()-1);
                ai.getAnnotationArguments().add(argument);
            }
        }
    }

    private AnnotationTerm decode(Module moduleScope, List<Parameter> sourceParameters, AnnotationInvocation info, 
            Parameter parameter, 
            AnnotatedMirror dpm, List<AnnotationFieldName> path, int code) {
        AnnotationTerm result;
        if (code == Short.MIN_VALUE) {
            return findLiteralAnnotationTerm(moduleScope, path, parameter, dpm);
        } else if (code < 0) {
            InvocationAnnotationTerm invocation = new InvocationAnnotationTerm();
            result = invocation;
        } else if (code >= 0 && code < 512) {
            ParameterAnnotationTerm parameterArgument = new ParameterAnnotationTerm();
            boolean spread = false;
            if (code >= 256) {
                spread = true;
                code-=256;
            }
            
            parameterArgument.setSpread(spread);
            Parameter sourceParameter = sourceParameters.get(code);
            parameterArgument.setSourceParameter(sourceParameter);
            //result.setTargetParameter(sourceParameter);
            result = parameterArgument;
        } else {
            throw new RuntimeException();
        }
        return result;
    }
    
    private AnnotationTerm loadAnnotationArgumentTerm(
            List<AnnotationFieldName> path,
            LazyFunction method,
            AnnotationInvocation ai, Parameter parameter,
            List<AnnotationMirror> annotationTree,
            AnnotatedMirror dpm,
            short code) {
        if (code < 0 && code != Short.MIN_VALUE) {
            AnnotationMirror i = annotationTree.get(-code);
            AnnotationInvocation nested = new AnnotationInvocation();
            setPrimaryFromAnnotationInvocationAnnotation(i, nested);
            loadAnnotationInvocationArguments(path, method, nested, i, annotationTree, dpm);
            InvocationAnnotationTerm term = new InvocationAnnotationTerm();
            term.setInstantiation(nested);
            return term;
        } else {
            AnnotationTerm term = decode(Decl.getModuleContainer(method), method.getFirstParameterList().getParameters(), ai, parameter, dpm, path, code);
            return term;
        }
    }
    private void setPrimaryFromAnnotationInvocationAnnotation(AnnotationMirror annotationInvocationAnnotation,
            AnnotationInvocation ai) {
        TypeMirror annotationType = (TypeMirror)annotationInvocationAnnotation.getValue(AbstractModelLoader.CEYLON_ANNOTATION_INSTANTIATION_ANNOTATION_MEMBER);
        ClassMirror annotationClassMirror = annotationType.getDeclaredClass();
        Module module = modelLoader.findModuleForClassMirror(annotationClassMirror);
        if (annotationClassMirror.getAnnotation(AbstractModelLoader.CEYLON_METHOD_ANNOTATION) != null) {
            ai.setPrimary((Function)modelLoader.convertToDeclaration(module, annotationClassMirror, DeclarationType.VALUE));
        } else {
            ai.setPrimary((Class)modelLoader.convertToDeclaration(module, annotationClassMirror, DeclarationType.TYPE));
        }
    }
    
    private void loadAnnotationConstructorDefaultedParameters(
            LazyFunction method, MethodMirror meth, AnnotationInvocation ai) {
        for (Parameter ctorParam : method.getFirstParameterList().getParameters()) {
            AnnotationConstructorParameter acp = new AnnotationConstructorParameter();
            acp.setParameter(ctorParam);
            if (ctorParam.isDefaulted()) {
                acp.setDefaultArgument(
                        loadAnnotationConstructorDefaultedParameter(method, meth, ctorParam, acp));
            }
            ai.getConstructorParameters().add(acp);
        }
    }
    private AnnotationTerm loadAnnotationConstructorDefaultedParameter(
            LazyFunction method, 
            MethodMirror meth,
            Parameter ctorParam, AnnotationConstructorParameter acp) {
        // Find the method mirror for the DPM
        for (MethodMirror mm : method.classMirror.getDirectMethods()) {
            if (mm.getName().equals(Naming.getDefaultedParamMethodName(method, ctorParam))) {
                // Create the appropriate AnnotationTerm
                if (mm.getAnnotation(AbstractModelLoader.CEYLON_ANNOTATION_INSTANTIATION_ANNOTATION) != null) {
                    // If the DPM has a @AnnotationInstantiation 
                    // then it must be an invocation term so recurse
                    InvocationAnnotationTerm invocationTerm = new InvocationAnnotationTerm();
                    invocationTerm.setInstantiation(loadAnnotationInvocation(method, mm, meth));
                    return invocationTerm;
                } else {
                    return loadLiteralAnnotationTerm(method, ctorParam, mm);
                }
            }
        }
        return null;
    }
    /** 
     * Loads a LiteralAnnotationTerm according to the presence of 
     * <ul>
     * <li>{@code @StringValue} <li>{@code @IntegerValue} <li>etc
     * </ul>
     * @param ctorParam 
     *  
     */
    private LiteralAnnotationTerm loadLiteralAnnotationTerm(LazyFunction method, Parameter parameter, AnnotatedMirror mm) {
        // FIXME: store iterable info somewhere else
        boolean singleValue = !typeFactory.isIterableType(parameter.getType())
            || parameter.getType().isString();
        AnnotationMirror valueAnnotation = mm.getAnnotation(AbstractModelLoader.CEYLON_STRING_VALUE_ANNOTATION);
        if (valueAnnotation != null) {
            return readStringValuesAnnotation(valueAnnotation, singleValue);
        }
        valueAnnotation = mm.getAnnotation(AbstractModelLoader.CEYLON_INTEGER_VALUE_ANNOTATION);
        if (valueAnnotation != null) {
            return readIntegerValuesAnnotation(valueAnnotation, singleValue);
        }
        valueAnnotation = mm.getAnnotation(AbstractModelLoader.CEYLON_BOOLEAN_VALUE_ANNOTATION);
        if (valueAnnotation != null) { 
            return readBooleanValuesAnnotation(valueAnnotation, singleValue);
        }
        valueAnnotation = mm.getAnnotation(AbstractModelLoader.CEYLON_DECLARATION_VALUE_ANNOTATION);
        if (valueAnnotation != null) {
            return readDeclarationValuesAnnotation(valueAnnotation, singleValue);
        }
        valueAnnotation = mm.getAnnotation(AbstractModelLoader.CEYLON_OBJECT_VALUE_ANNOTATION);
        if (valueAnnotation != null) {
            return readObjectValuesAnnotation(Decl.getModuleContainer(method), valueAnnotation, singleValue);
        }
        valueAnnotation = mm.getAnnotation(AbstractModelLoader.CEYLON_CHARACTER_VALUE_ANNOTATION);
        if (valueAnnotation != null) {
            return readCharacterValuesAnnotation(valueAnnotation, singleValue);
        } 
        valueAnnotation = mm.getAnnotation(AbstractModelLoader.CEYLON_FLOAT_VALUE_ANNOTATION);
        if (valueAnnotation != null) {
            return readFloatValuesAnnotation(valueAnnotation, singleValue);
        }
        return null;
    }
    /**
     * Searches the {@code @*Exprs} for one containing a {@code @*Value} 
     * whose {@code name} matches the given namePath returning the first 
     * match, or null.
     */
    private LiteralAnnotationTerm findLiteralAnnotationTerm(Module moduleScope, List<AnnotationFieldName> namePath, Parameter parameter, AnnotatedMirror mm) {
        // FIXME: store info somewhere else
        boolean singeValue = !typeFactory.isIterableType(parameter.getType())
            || parameter.getType().isString();
        final String name = Naming.getAnnotationFieldName(namePath);
        AnnotationMirror exprsAnnotation = mm.getAnnotation(AbstractModelLoader.CEYLON_STRING_EXPRS_ANNOTATION);
        if (exprsAnnotation != null) {
            for (AnnotationMirror valueAnnotation : getAnnotationAnnoValues(exprsAnnotation, "value")) {
                String path = (String)valueAnnotation.getValue("name");
                if (name.equals(path)) {
                    return readStringValuesAnnotation(valueAnnotation, singeValue);
                }
            }
        }
        exprsAnnotation = mm.getAnnotation(AbstractModelLoader.CEYLON_INTEGER_EXPRS_ANNOTATION);
        if (exprsAnnotation != null) {
            for (AnnotationMirror valueAnnotation : getAnnotationAnnoValues(exprsAnnotation, "value")) {
                String path = (String)valueAnnotation.getValue("name");
                if (name.equals(path)) {
                    return readIntegerValuesAnnotation(valueAnnotation, singeValue);
                }
            }
        }
        exprsAnnotation = mm.getAnnotation(AbstractModelLoader.CEYLON_BOOLEAN_EXPRS_ANNOTATION);
        if (exprsAnnotation != null) {
            for (AnnotationMirror valueAnnotation : getAnnotationAnnoValues(exprsAnnotation, "value")) {
                String path = (String)valueAnnotation.getValue("name");
                if (name.equals(path)) {
                    return readBooleanValuesAnnotation(valueAnnotation, singeValue);
                }
            }
        }
        exprsAnnotation = mm.getAnnotation(AbstractModelLoader.CEYLON_DECLARATION_EXPRS_ANNOTATION);
        if (exprsAnnotation != null) {
            for (AnnotationMirror valueAnnotation : getAnnotationAnnoValues(exprsAnnotation, "value")) {
                String path = (String)valueAnnotation.getValue("name");
                if (name.equals(path)) {
                    return readDeclarationValuesAnnotation(valueAnnotation, singeValue);
                }
            }
        }
        exprsAnnotation = mm.getAnnotation(AbstractModelLoader.CEYLON_OBJECT_EXPRS_ANNOTATION);
        if (exprsAnnotation != null) {
            for (AnnotationMirror valueAnnotation : getAnnotationAnnoValues(exprsAnnotation, "value")) {
                String path = (String)valueAnnotation.getValue("name");
                if (name.equals(path)) {
                    return readObjectValuesAnnotation(moduleScope, valueAnnotation, singeValue);
                }
            }
        }
        exprsAnnotation = mm.getAnnotation(AbstractModelLoader.CEYLON_CHARACTER_EXPRS_ANNOTATION);
        if (exprsAnnotation != null) {
            for (AnnotationMirror valueAnnotation : getAnnotationAnnoValues(exprsAnnotation, "value")) {
                String path = (String)valueAnnotation.getValue("name");
                if (name.equals(path)) {
                    return readCharacterValuesAnnotation(valueAnnotation, singeValue);
                }
            }
        }
        exprsAnnotation = mm.getAnnotation(AbstractModelLoader.CEYLON_FLOAT_EXPRS_ANNOTATION);
        if (exprsAnnotation != null) {
            for (AnnotationMirror valueAnnotation : getAnnotationAnnoValues(exprsAnnotation, "value")) {
                String path = (String)valueAnnotation.getValue("name");
                if (name.equals(path)) {
                    return readFloatValuesAnnotation(valueAnnotation, singeValue);
                }
            }
        }
        
        return null;
    }
    private LiteralAnnotationTerm readObjectValuesAnnotation(
            Module moduleScope,
            AnnotationMirror valueAnnotation, boolean singleValue) {
        if (singleValue) {
            TypeMirror klass = getAnnotationClassValues(valueAnnotation, "value").get(0);
            Type type = modelLoader.obtainType(moduleScope, klass, null, null, null);
            ObjectLiteralAnnotationTerm term = new ObjectLiteralAnnotationTerm(type);
            return term;
        } else {
            CollectionLiteralAnnotationTerm result = new CollectionLiteralAnnotationTerm(ObjectLiteralAnnotationTerm.FACTORY);
            for (TypeMirror klass : getAnnotationClassValues(valueAnnotation, "value")) {
                Type type = modelLoader.obtainType(moduleScope, klass, null, null, null);
                result.addElement(new ObjectLiteralAnnotationTerm(type));
            }
            return result;
        }
    }
    private LiteralAnnotationTerm readStringValuesAnnotation(
            AnnotationMirror valueAnnotation, boolean singleValue) {
        if (singleValue) {
            String value = getAnnotationStringValues(valueAnnotation, "value").get(0);
            StringLiteralAnnotationTerm term = new StringLiteralAnnotationTerm(value);
            return term;
        } else {
            CollectionLiteralAnnotationTerm result = new CollectionLiteralAnnotationTerm(StringLiteralAnnotationTerm.FACTORY);
            for (String value : getAnnotationStringValues(valueAnnotation, "value")) {
                result.addElement(new StringLiteralAnnotationTerm(value));
            }
            return result;
        }
    }
    private LiteralAnnotationTerm readIntegerValuesAnnotation(
            AnnotationMirror valueAnnotation, boolean singleValue) {
        if (singleValue) {
            Long value = getAnnotationLongValues(valueAnnotation, "value").get(0);
            IntegerLiteralAnnotationTerm term = new IntegerLiteralAnnotationTerm(value);
            return term;
        } else {
            CollectionLiteralAnnotationTerm result = new CollectionLiteralAnnotationTerm(IntegerLiteralAnnotationTerm.FACTORY);
            for (Long value : getAnnotationLongValues(valueAnnotation, "value")) {
                result.addElement(new IntegerLiteralAnnotationTerm(value));
            }
            return result;
        }
    }
    private LiteralAnnotationTerm readCharacterValuesAnnotation(
            AnnotationMirror valueAnnotation, boolean singleValue) {
        if (singleValue) {
            Integer value = getAnnotationIntegerValues(valueAnnotation, "value").get(0);
            CharacterLiteralAnnotationTerm term = new CharacterLiteralAnnotationTerm(value);
            return term;
        } else {
            CollectionLiteralAnnotationTerm result = new CollectionLiteralAnnotationTerm(CharacterLiteralAnnotationTerm.FACTORY);
            for (Integer value : getAnnotationIntegerValues(valueAnnotation, "value")) {
                result.addElement(new CharacterLiteralAnnotationTerm(value));
            }
            return result;
        }
    }
    private LiteralAnnotationTerm readFloatValuesAnnotation(
            AnnotationMirror valueAnnotation, boolean singleValue) {
        if (singleValue) {
            Double value = getAnnotationDoubleValues(valueAnnotation, "value").get(0);
            FloatLiteralAnnotationTerm term = new FloatLiteralAnnotationTerm(value);
            return term;
        } else {
            CollectionLiteralAnnotationTerm result = new CollectionLiteralAnnotationTerm(FloatLiteralAnnotationTerm.FACTORY);
            for (Double value : getAnnotationDoubleValues(valueAnnotation, "value")) {
                result.addElement(new FloatLiteralAnnotationTerm(value));
            }
            return result;
        }
    }
    private LiteralAnnotationTerm readBooleanValuesAnnotation(
            AnnotationMirror valueAnnotation, boolean singleValue) {
        if (singleValue) {
            boolean value = getAnnotationBooleanValues(valueAnnotation, "value").get(0);
            BooleanLiteralAnnotationTerm term = new BooleanLiteralAnnotationTerm(value);
            return term;
        } else {
            CollectionLiteralAnnotationTerm result = new CollectionLiteralAnnotationTerm(BooleanLiteralAnnotationTerm.FACTORY);
            for (Boolean value : getAnnotationBooleanValues(valueAnnotation, "value")) {
                result.addElement(new BooleanLiteralAnnotationTerm(value));
            }
            return result;
        }
    }
    private LiteralAnnotationTerm readDeclarationValuesAnnotation(
            AnnotationMirror valueAnnotation, boolean singleValue) {
        if (singleValue) {
            String value = getAnnotationStringValues(valueAnnotation, "value").get(0);
            DeclarationLiteralAnnotationTerm term = new DeclarationLiteralAnnotationTerm(value);
            return term;
        } else {
            CollectionLiteralAnnotationTerm result = new CollectionLiteralAnnotationTerm(DeclarationLiteralAnnotationTerm.FACTORY);
            for (String value : getAnnotationStringValues(valueAnnotation, "value")) {
                result.addElement(new DeclarationLiteralAnnotationTerm(value));
            }
            return result;
        }
    }

    @SuppressWarnings("unchecked")
    private List<TypeMirror> getAnnotationClassValues(AnnotationMirror annotation, String field) {
        return (List<TypeMirror>)annotation.getValue(field);
    }

    @SuppressWarnings("unchecked")
    private List<Integer> getAnnotationIntegerValues(AnnotationMirror annotation, String field) {
        return (List<Integer>)annotation.getValue(field);
    }
    
    @SuppressWarnings("unchecked")
    private List<Boolean> getAnnotationBooleanValues(AnnotationMirror annotation, String field) {
        return (List<Boolean>)annotation.getValue(field);
    }
    
    @SuppressWarnings("unchecked")
    private List<Long> getAnnotationLongValues(AnnotationMirror annotation, String field) {
        return (List<Long>)annotation.getValue(field);
    }
    
    @SuppressWarnings("unchecked")
    private List<Double> getAnnotationDoubleValues(AnnotationMirror annotation, String field) {
        return (List<Double>)annotation.getValue(field);
    }
    @SuppressWarnings("unchecked")
    private List<AnnotationMirror> getAnnotationAnnoValues(AnnotationMirror annotation, String field) {
        return (List<AnnotationMirror>)annotation.getValue(field);
    }

    @SuppressWarnings("unchecked")
    private List<String> getAnnotationStringValues(AnnotationMirror annotation, String field) {
        return (List<String>)annotation.getValue(field);
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getAnnotationArrayValue(AnnotatedMirror mirror, String type, String field) {
        return (List<T>) getAnnotationValue(mirror, type, field);
    }

    private Object getAnnotationValue(AnnotatedMirror mirror, String type, String fieldName) {
        AnnotationMirror annotation = mirror.getAnnotation(type);
        if(annotation != null){
            return annotation.getValue(fieldName);
        }
        return null;
    }

    public void makeInterorAnnotationConstructorInvocation(AnnotationProxyMethod ctor, AnnotationProxyClass klass, java.util.List<Parameter> ctorParams) {
        AnnotationInvocation ai = new AnnotationInvocation();
        ai.setConstructorDeclaration(ctor);
        ai.setPrimary(klass);
        ai.setInterop(true);
        ctor.setAnnotationConstructor(ai);
        java.util.List<AnnotationArgument> annotationArgs = new ArrayList<AnnotationArgument>();
        for(Parameter ctorParam : ctorParams){
            boolean isValue = ctorParam.getName().equals("value");
            ParameterAnnotationTerm term = new ParameterAnnotationTerm();
            AnnotationArgument argument = new AnnotationArgument();
            argument.setTerm(term);
            argument.setParameter(klass.getParameter(ctorParam.getName()));
            term.setSourceParameter(ctorParam);
            AnnotationConstructorParameter acp = new AnnotationConstructorParameter();
            acp.setParameter(ctorParam);
            if(isValue)
                ai.getConstructorParameters().add(0, acp);
            else
                ai.getConstructorParameters().add(acp);

            annotationArgs.add(argument);
        }
        ai.getAnnotationArguments().addAll(annotationArgs);
    }
}
