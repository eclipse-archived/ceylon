package com.redhat.ceylon.model.typechecker.model;

import java.util.List;
import java.util.Map;


/**
 * A produced reference to a method or 
 * attribute with actual type arguments.
 * 
 * @author Gavin King
 *
 */
public class ProducedTypedReference extends ProducedReference {
    
    private TypedDeclaration declaration;
    private final boolean covariant;
    private final boolean contravariant;

    ProducedTypedReference(boolean covariant, boolean contravariant) {
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
    
    public Type getType() {
        TypedDeclaration declaration = getDeclaration();
        if (declaration==null) {
            return null;
        }
        else {
            Type type = declaration.getType();
            return type==null ? null : type.substitute(this);
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
        return getProducedName() + " (typed reference)";
    }

    @Override
    public String getProducedName() {
        TypedDeclaration dec = getDeclaration();
        StringBuilder name = new StringBuilder();
        Type type = getQualifyingType();
        if (type!=null) {
            name.append(type.getProducedTypeName())
                .append(".");
        }
        name.append(dec.getName());
        if (dec instanceof Generic) {
            Generic g = (Generic) dec;
            List<TypeParameter> tps = g.getTypeParameters();
            if (!tps.isEmpty()) {
                name.append("<");
                Map<TypeParameter, Type> args = 
                        getTypeArguments();
                for (int i=0, l=tps.size(); i<l; i++) {
                    if (i!=0) {
                        name.append(",");
                    }
                    TypeParameter tp = tps.get(i);
                    Type arg = args.get(tp);
                    if (arg==null) {
                        name.append("unknown");
                    }
                    else {
                        name.append(arg.getProducedTypeName());
                    }
                }
                name.append(">");
            }
        }
        return name.toString();
    }
    
}
