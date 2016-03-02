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

import java.util.Collections;

import com.redhat.ceylon.compiler.java.tools.LanguageCompiler;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.langtools.tools.javac.util.List;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.model.UnknownType;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Package;

public class TypeFactory extends Unit {
    private Context context;

    public static TypeFactory instance(com.redhat.ceylon.langtools.tools.javac.util.Context context) {
        TypeFactory instance = context.get(TypeFactory.class);
        if (instance == null) {
            instance = new TypeFactory(LanguageCompiler.getCeylonContextInstance(context));
            context.put(TypeFactory.class, instance);
        }
        return instance;
    }
    
    public TypeFactory(Context context) {
        this.context = context;
    }
    
    public Context getContext() {
        return context;
    }
    
    /**
     * Determines whether the given Type is a union
     * @param pt 
     * @return whether the type is a union type
     */
    public boolean isUnion(Type pt) {
        if (pt==null) return false;
        return pt.isUnion() && pt.getCaseTypes().size() > 1;
    }

    /**
     * Determines whether the given Type is an intersection
     * @param pt 
     * @return whether the type is an intersection type
     */
    public boolean isIntersection(Type pt) {
        if (pt==null) return false;
        return pt.isIntersection() && pt.getSatisfiedTypes().size() > 1;
    }

    public TypeDeclaration getArraySequenceDeclaration() {
        return (Class) getLanguageModuleDeclaration("ArraySequence");
    }
    
    public Type getArraySequenceType(Type et) {
        return ModelUtil.appliedType(getArraySequenceDeclaration(), et);
    }
    
    /**
     * Returns a Type corresponding to {@code Array<T>}
     * @param et The Type corresponding to {@code T}
     * @return The Type corresponding to {@code Array<T>}
     */
    public Type getArrayType(Type et) {
        return ModelUtil.appliedType(getArrayDeclaration(), et);
    }

    public Type getArrayElementType(Type type) {
        Type st = type.getSupertype(getArrayDeclaration());
        if (st!=null && st.getTypeArguments().size()==1) {
            return st.getTypeArgumentList().get(0);
        }
        else {
            return null;
        }
    }
    
    public Type getCallableType(java.util.List<Type> typeArgs) {
        if (typeArgs.size()!=2) {
            throw new IllegalArgumentException("Callable type always has two arguments: " + typeArgs);
        }
        if (!typeArgs.get(1).isSubtypeOf(
                getSequentialDeclaration().appliedType(
                        null, Collections.singletonList(getAnythingType())))) {
            throw new IllegalArgumentException("Callable's second argument should be sequential " + typeArgs.get(1));
        }
        return getCallableDeclaration().appliedType(null, typeArgs);
    }
    
    public Type getCallableType(Type resultType) {
        return getCallableType(List.<Type>of(resultType,getEmptyType()));
    }

    public Type getUnknownType() {
        return new UnknownType(this).getType();
    }
    
    public Type getIteratedAbsentType(Type type) {
        Type st = type.getSupertype(getIterableDeclaration());
        if (st!=null && st.getTypeArguments().size()>1) {
            return st.getTypeArgumentList().get(1);
        }
        else {
            return null;
        }
    }

    public Declaration getBooleanTrueDeclaration() {
        return getLanguageModuleDeclaration("true");
    }

    public Declaration getBooleanFalseDeclaration() {
        return getLanguageModuleDeclaration("false");
    }

    public TypeDeclaration getBooleanTrueClassDeclaration() {
        Declaration trueDecl = getBooleanTrueDeclaration();
        return trueDecl instanceof TypedDeclaration ? ((TypedDeclaration)trueDecl).getTypeDeclaration() : null;
    }

    public TypeDeclaration getBooleanFalseClassDeclaration() {
        Declaration trueDecl = getBooleanFalseDeclaration();
        return trueDecl instanceof TypedDeclaration ? ((TypedDeclaration)trueDecl).getTypeDeclaration() : null;
    }

    public TypeDeclaration getMetamodelTypeDeclaration() {
        return (TypeDeclaration) getLanguageModuleModelDeclaration("Type");
    }

    public TypedDeclaration getMetamodelNothingTypeDeclaration() {
        return (TypedDeclaration) getLanguageModuleModelDeclaration("nothingType");
    }

    public TypeDeclaration getMetamodelDeclarationDeclaration() {
        return (TypeDeclaration) getLanguageModuleDeclarationDeclaration("Declaration");
    }
    
    public TypeDeclaration getAssertionErrorDeclaration() {
        return (TypeDeclaration)getLanguageModuleDeclaration("AssertionError");
    }
    
    public Type getReferenceType(Type value) {
        final Type serializedValueType;
        TypeDeclaration referenceTypeDecl = (TypeDeclaration)getLanguageModuleSerializationDeclaration("Reference");
        serializedValueType = referenceTypeDecl.appliedType(null, Collections.singletonList(value));
        return serializedValueType;
    }
    
    public Type getDeconstructorType() {
        return ((TypeDeclaration)getLanguageModuleSerializationDeclaration("Deconstructor")).getType();
    }
    
    /**
     * Copy of Unit.isTupleLengthUnbounded which is more strict on what we consider variadic. For example
     * we do not consider Args|[] as a variadic tuple in a Callable. See https://github.com/ceylon/ceylon-compiler/issues/1908
     */
    public boolean isTupleOfVariadicCallable(Type args) {
        if (args!=null) {
            /*Boolean simpleTupleLengthUnbounded = 
                    isSimpleTupleLengthUnbounded(args);
            if (simpleTupleLengthUnbounded != null) {
                return simpleTupleLengthUnbounded.booleanValue();
            }*/
            if (isEmptyType(args)) {
                return false;
            }
            else if (isVariadicElement(args)) {
                return true;
            }
            Class td = getTupleDeclaration();
            Type tuple = 
                    nonemptyArgs(args)
                        .getSupertype(td);
            if (tuple==null) {
                return false;
            }
            else {
                while (true) {
                    java.util.List<Type> tal = 
                            tuple.getTypeArgumentList();
                    if (tal.size()>=3) {
                        Type rest = tal.get(2);
                        if (rest==null) {
                            return false;
                        }
                        else if (isEmptyType(rest)) {
                            return false;
                        }
                        else if (isVariadicElement(rest)) {
                            return true;
                        }
                        else {
                            tuple = nonemptyArgs(rest)
                                    .getSupertype(td);
                            if (tuple==null) {
                                return false;
                            }
                            //else continue the loop!
                        }
                    }
                    else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    private boolean isVariadicElement(Type args) {
        return args.isClassOrInterface() 
                && (args.isSequential() || args.isSequence());
    }
    
    /**
     * Get the interface for {@code java.io.Serializable} from
     * {@code java.base}, or null if it could not be found.
     * @return
     */
    public Interface getJavaIoSerializable() {
        for (com.redhat.ceylon.model.typechecker.model.Module m : context.getModules().getListOfModules()) {
            if ("java.base".equals(m.getNameAsString())) {
                return (Interface)m.getPackage("java.io").getDirectMember("Serializable", null, false);
            }
        }
        return null;
    }
    
    public Type getJavaIoSerializableType() {
        Interface ser = getJavaIoSerializable();
        if (ser != null) {
            return ser.getType();
        } else {
            return null;
        }
    }
    
    public Interface getJavaIteratorDeclaration() {
        Package lang = getJavaUtilPackage();
        if (lang==null) {
            return null;
        }
        else {
            return (Interface) lang.getMember("Iterator", null, false);
        }
    }
    
    public Type getJavaIteratorType(Type iteratedType) {
        return getJavaIteratorDeclaration().appliedType(null, Collections.singletonList(iteratedType));
    }
}
