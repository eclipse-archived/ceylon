package com.redhat.ceylon.model.typechecker.model;

import static com.redhat.ceylon.model.typechecker.model.ModelUtil.EMPTY_VARIANCE_MAP;

import java.util.List;
import java.util.Map;


/**
 * An applied reference to a method or attribute - a typed
 * declaration together with actual type arguments.
 * 
 * @author Gavin King
 *
 */
public class TypedReference extends Reference {
    
    private TypedDeclaration declaration;
    private final boolean covariant;
    private final boolean contravariant;
    private Map<TypeParameter, SiteVariance> capturedWildcards =
            EMPTY_VARIANCE_MAP;

    TypedReference(boolean covariant, boolean contravariant) {
        this.covariant = covariant;
        this.contravariant = contravariant;
    }
    
    @Override
    public TypedDeclaration getDeclaration() {
        return declaration;
    }
    
    void setDeclaration(TypedDeclaration declaration) {
        this.declaration = declaration;
    }
    
    Map<TypeParameter, SiteVariance> getCapturedWildcards() {
        return capturedWildcards;
    }
    
    void setCapturedWildcards(Map<TypeParameter, SiteVariance> capturedWildcards) {
        this.capturedWildcards = capturedWildcards;
    }
    
    public Type getType() {
        TypedDeclaration declaration = 
                getDeclaration();
        if (declaration==null) {
            return null;
        }
        else {
            Type type = declaration.getType();
            return type==null ? null : 
                type.applyCapturedWildcards(this)
                    .substitute(this);
        }
    }
    
    public boolean isContravariant() {
        return contravariant;
    }
    
    public boolean isCovariant() {
        return covariant;
    }
    
    @Override
    public String toString() {
        return asString() + " (typed reference)";
    }

    @Override
    public String asString() {
        TypedDeclaration dec = getDeclaration();
        StringBuilder name = new StringBuilder();
        Type type = getQualifyingType();
        if (type!=null) {
            name.append(type.asString())
                .append(".");
        }
        name.append(dec.getName());
        if (dec.isParameterized()) {
            List<TypeParameter> typeParameters = 
                    dec.getTypeParameters();
            name.append("<");
            Map<TypeParameter, Type> args = 
                    getTypeArguments();
            for (int i=0, l=typeParameters.size(); 
                    i<l; i++) {
                if (i!=0) {
                    name.append(",");
                }
                TypeParameter typeParam = 
                        typeParameters.get(i);
                Type arg = args.get(typeParam);
                if (arg==null) {
                    name.append("unknown");
                }
                else {
                    name.append(arg.asString());
                }
            }
            name.append(">");
        }
        return name.toString();
    }
    
}
