package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;

import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;

public class MethodHandleUtil {

    public static MethodHandle insertReifiedTypeArguments(MethodHandle constructor, int insertAt, List<ProducedType> typeArguments) {
        Object[] typeDescriptors = new TypeDescriptor[typeArguments.size()];
        for(int i=0;i<typeDescriptors.length;i++){
            typeDescriptors[i] = Metamodel.getTypeDescriptorForProducedType(typeArguments.get(i));
        }
        return MethodHandles.insertArguments(constructor, insertAt, typeDescriptors);
    }

    public static MethodHandle unboxArguments(MethodHandle method, int typeParameterCount, int filterIndex, java.lang.Class<?>[] parameterTypes, List<ProducedType> producedTypes) {
        MethodHandle[] filters = new MethodHandle[parameterTypes.length - typeParameterCount];
        try {
            for(int i=0;i<filters.length;i++){
                java.lang.Class<?> paramType = parameterTypes[i + typeParameterCount];
                ProducedType producedType = producedTypes.get(i);
                if(paramType == java.lang.String.class){
                    // ((ceylon.language.String)obj).toString()
                    MethodHandle unbox = MethodHandles.lookup().findVirtual(ceylon.language.String.class, "toString", 
                                                                               MethodType.methodType(java.lang.String.class));
                    filters[i] = unbox.asType(MethodType.methodType(java.lang.String.class, java.lang.Object.class));
                }else if(paramType == byte.class 
                        || paramType == short.class
                        || (paramType == int.class && !isCeylonCharacter(producedType))){
                    // (paramType)((ceylon.language.Integer)obj).longValue()
                    MethodHandle unbox = MethodHandles.lookup().findVirtual(ceylon.language.Integer.class, "longValue", 
                                                                             MethodType.methodType(long.class));
                    filters[i] = MethodHandles.explicitCastArguments(unbox, MethodType.methodType(paramType, java.lang.Object.class));
                }else if(paramType == long.class){
                    // ((ceylon.language.Integer)obj).longValue()
                    MethodHandle unbox = MethodHandles.lookup().findVirtual(ceylon.language.Integer.class, "longValue", 
                                                                             MethodType.methodType(long.class));
                    filters[i] = unbox.asType(MethodType.methodType(long.class, java.lang.Object.class));
                }else if(paramType == float.class){
                    // (float)((ceylon.language.Float)obj).doubleValue()
                    MethodHandle unbox = MethodHandles.lookup().findVirtual(ceylon.language.Float.class, "doubleValue", 
                                                                             MethodType.methodType(double.class));
                    filters[i] = MethodHandles.explicitCastArguments(unbox, MethodType.methodType(float.class, java.lang.Object.class));
                }else if(paramType == double.class){
                    // ((ceylon.language.Float)obj).doubleValue()
                    MethodHandle unbox = MethodHandles.lookup().findVirtual(ceylon.language.Float.class, "doubleValue", 
                                                                             MethodType.methodType(double.class));
                    filters[i] = unbox.asType(MethodType.methodType(double.class, java.lang.Object.class));
                }else if(paramType == int.class && isCeylonCharacter(producedType)){
                    // ((ceylon.language.Character)obj).intValue()
                    MethodHandle unbox = MethodHandles.lookup().findVirtual(ceylon.language.Character.class, "intValue", 
                                                                             MethodType.methodType(int.class));
                    filters[i] = unbox.asType(MethodType.methodType(int.class, java.lang.Object.class));
                }else if(paramType == char.class){
                    // ((ceylon.language.Character)obj).charValue()
                    MethodHandle unbox = MethodHandles.lookup().findVirtual(ceylon.language.Character.class, "intValue", 
                                                                             MethodType.methodType(int.class));
                    filters[i] = MethodHandles.explicitCastArguments(unbox, MethodType.methodType(char.class, java.lang.Object.class));
                }else if(paramType == boolean.class){
                    // ((ceylon.language.Boolean)obj).booleanValue()
                    MethodHandle unbox = MethodHandles.lookup().findVirtual(ceylon.language.Boolean.class, "booleanValue", 
                                                                             MethodType.methodType(boolean.class));
                    filters[i] = unbox.asType(MethodType.methodType(boolean.class, java.lang.Object.class));
                }else if(paramType != java.lang.Object.class){
                    // just cast from Object to type
                    MethodHandle unbox = MethodHandles.identity(java.lang.Object.class);
                    filters[i] = unbox.asType(MethodType.methodType(paramType, java.lang.Object.class));
                }
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to filter parameter", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to filter parameter", e);
        }
        return MethodHandles.filterArguments(method, filterIndex, filters);
    }

    private static boolean isCeylonCharacter(ProducedType producedType) {
        if(producedType == null)
            return false;
        TypeDeclaration declaration = producedType.getDeclaration();
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Class == false)
            return false;
        // this is probably the fastest check we can make
        return declaration.getQualifiedNameString().equals("ceylon.language::Character");
    }

    public static MethodHandle boxReturnValue(MethodHandle method, java.lang.Class<?> type, ProducedType producedType) {
        try {
            // FIXME: more boxing for interop
            if(type == java.lang.String.class){
                // ceylon.language.String.instance(obj)
                MethodHandle box = MethodHandles.lookup().findStatic(ceylon.language.String.class, "instance", 
                                                        MethodType.methodType(ceylon.language.String.class, java.lang.String.class));
                method = MethodHandles.filterReturnValue(method, box);
            }else if(type == int.class && isCeylonCharacter(producedType)){
                // ceylon.language.Character.instance(obj)
                MethodHandle box = MethodHandles.lookup().findStatic(ceylon.language.Character.class, "instance", 
                                                                     MethodType.methodType(ceylon.language.Character.class, int.class));
                return MethodHandles.filterReturnValue(method, box);
            }else if(type == byte.class || type == int.class || type == short.class){
                // ceylon.language.Integer.instance((long)obj)
                MethodHandle box = MethodHandles.lookup().findStatic(ceylon.language.Integer.class, "instance", 
                                                                     MethodType.methodType(ceylon.language.Integer.class, long.class));
                box = box.asType(MethodType.methodType(ceylon.language.Integer.class, type));
                return MethodHandles.filterReturnValue(method, box);
            }else if(type == long.class){
                // ceylon.language.Integer.instance(obj)
                MethodHandle box = MethodHandles.lookup().findStatic(ceylon.language.Integer.class, "instance", 
                                                                     MethodType.methodType(ceylon.language.Integer.class, long.class));
                return MethodHandles.filterReturnValue(method, box);
            }else if(type == double.class){
                // ceylon.language.Float.instance(obj)
                MethodHandle box = MethodHandles.lookup().findStatic(ceylon.language.Float.class, "instance", 
                                                                     MethodType.methodType(ceylon.language.Float.class, double.class));
                return MethodHandles.filterReturnValue(method, box);
            }else if(type == float.class){
                // ceylon.language.Float.instance((double)obj)
                MethodHandle box = MethodHandles.lookup().findStatic(ceylon.language.Float.class, "instance", 
                                                                     MethodType.methodType(ceylon.language.Float.class, double.class));
                box = box.asType(MethodType.methodType(ceylon.language.Float.class, type));
                return MethodHandles.filterReturnValue(method, box);
            }else if(type == char.class){
                // ceylon.language.Character.instance((int)obj)
                MethodHandle box = MethodHandles.lookup().findStatic(ceylon.language.Character.class, "instance", 
                                                                     MethodType.methodType(ceylon.language.Character.class, int.class));
                box = box.asType(MethodType.methodType(ceylon.language.Character.class, type));
                return MethodHandles.filterReturnValue(method, box);
            }else if(type == boolean.class){
                // ceylon.language.Boolean.instance(obj)
                MethodHandle box = MethodHandles.lookup().findStatic(ceylon.language.Boolean.class, "instance", 
                                                                     MethodType.methodType(ceylon.language.Boolean.class, boolean.class));
                return MethodHandles.filterReturnValue(method, box);
            }
            return method;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to filter return value", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to filter return value", e);
        }
    }

}
