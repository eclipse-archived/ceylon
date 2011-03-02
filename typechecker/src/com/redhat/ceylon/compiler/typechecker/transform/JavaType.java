package com.redhat.ceylon.compiler.typechecker.transform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;

public abstract class JavaType {
    
    public abstract boolean isPrimitive();
    
    public abstract List<String> getQualifiedName();
    
    private static Map<String,JavaType> specialCases = new HashMap<String,JavaType>();
    static {
        specialCases.put("Boolean", new JavaPrimitiveType("boolean"));
        specialCases.put("Character", new JavaPrimitiveType("char"));
        specialCases.put("Natural", new JavaPrimitiveType("long"));
        specialCases.put("Integer", new JavaPrimitiveType("long"));
        specialCases.put("Float", new JavaPrimitiveType("double"));
        specialCases.put("Boolean", new JavaPrimitiveType("boolean"));
        specialCases.put("String", new JavaObjectType("java", "lang", "String"));
        //specialCases.put("Whole", new JavaObjectType("java", "math", "BigInteger"));
        //specialCases.put("BigDecimal", new JavaObjectType("java", "math", "BigDecimal"));
        specialCases.put("Object", new JavaObjectType("java", "lang", "Object"));
        specialCases.put("Void", new JavaObjectType("java", "lang", "Object"));
        //specialCases.put("IdentityObject", new JavaObjectType(false, "java", "lang", "Object"));
    }
    
    private static Map<String,JavaType> optionalSpecialCases = new HashMap<String,JavaType>();
    static {
        optionalSpecialCases.put("Boolean", new JavaObjectType("java", "lang", "Boolean"));
        optionalSpecialCases.put("Character", new JavaObjectType("java", "lang", "Character"));
        optionalSpecialCases.put("Natural", new JavaObjectType("java", "lang", "Long"));
        optionalSpecialCases.put("Integer", new JavaObjectType("java", "lang", "Long"));
        optionalSpecialCases.put("Float", new JavaObjectType("java", "lang", "Double"));
        optionalSpecialCases.put("Boolean", new JavaObjectType("java", "lang", "Boolean"));
        optionalSpecialCases.put("String", new JavaObjectType("java", "lang", "String"));
        //optionalSpecialCases.put("Whole", new JavaObjectType("java", "math", "BigInteger"));
        //optionalSpecialCases.put("BigDecimal", new JavaObjectType("java", "math", "BigDecimal"));
        optionalSpecialCases.put("Object", new JavaObjectType("java", "lang", "Object"));
        optionalSpecialCases.put("Void", new JavaObjectType("java", "lang", "Object"));
        //optionalSpecialCases.put("IdentityObject", new JavaObjectType("java", "lang", "Object"));
    }
    
    public static JavaType javaType(final ProducedType type) {
        if ( isTypeParameter(type) ) {
            List<ProducedType> upperBounds = type.getDeclaration().getSatisfiedTypes();
            if (upperBounds.size()==0) {
                return new JavaObjectType("java", "lang", "Object");
            }
            else {
                return javaType(upperBounds.get(0));
            }
        }
        else if (isOptionalType(type)) {
            ProducedType argType = type.getTypeArgumentList().get(0);
            if (isLanguageModuleType(argType)) {
                JavaType jt = optionalSpecialCases.get(argType.getDeclaration().getName());
                if (jt!=null) {
                    return jt;
                }
            }
            return javaType(argType);
        }
        else if (isLanguageModuleType(type)) {
            JavaType jt = specialCases.get(type.getDeclaration().getName());
            if (jt!=null) {
                return jt;
            }
        }
        return new JavaObjectType(type.getDeclaration().getQualifiedName());
    }

    private static boolean isTypeParameter(ProducedType type) {
        return type.getDeclaration() instanceof TypeParameter;
    }

    private static boolean isOptionalType(ProducedType type) {
        if ( isLanguageModuleType(type) ) { 
            String typeName = type.getDeclaration().getName();
            return typeName.equals("Optional") ||
                    typeName.equals("Something") ||
                    typeName.equals("Nothing");
        }
        else {
            return false;
        }
    }

    private static boolean isLanguageModuleType(ProducedType type) {
        List<String> qualifiedName = type.getDeclaration().getQualifiedName();
        return qualifiedName.size()==3 && 
                qualifiedName.get(0).equals("ceylon") &&
                qualifiedName.get(1).equals("language");
    }
}
