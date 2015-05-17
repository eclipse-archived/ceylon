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
    
    private final boolean covariant;
    private final boolean contravariant;

    ProducedTypedReference(boolean covariant, boolean contravariant) {
        this.covariant = covariant;
        this.contravariant = contravariant;
    }
    
    @Override
    public TypedDeclaration getDeclaration() {
        return (TypedDeclaration) super.getDeclaration();
    }
    
    @Override
    void setDeclaration(Declaration declaration) {
        if (declaration instanceof TypedDeclaration) {
            super.setDeclaration(declaration);
        }
        else {
            throw new IllegalArgumentException("not a TypedDeclaration");
        }
    }
    
    public ProducedType getType() {
        TypedDeclaration dec = getDeclaration();
        if (dec==null) {
            return null;
        }
        else {
            ProducedType type = dec.getType();
            if (type==null) {
                return null;
            }
            // FIXME: perhaps this should be in type.substitute?
            else if (type.isUnknown()) {
                return type;
            }
            else {
                ProducedType qt = getQualifyingType();
                if (qt!=null) {
                    type = qt.applyVarianceOverrides(type, 
                            covariant, contravariant);
                }
                //the type arguments to the member
                return type.substitute(getTypeArguments());
            }
        }
    }
    
    @Override
    public String toString() {
        return "Reference[" + getProducedName() + "]";
    }

    @Override
    public String getProducedName() {
        TypedDeclaration dec = getDeclaration();
        StringBuilder name = new StringBuilder();
        ProducedType type = getQualifyingType();
        if (type!=null) {
            name.append(type.getProducedTypeName());
        }
        name.append(dec.getName());
        if (dec instanceof Generic) {
            Generic g = (Generic) dec;
            List<TypeParameter> tps = g.getTypeParameters();
            if (!tps.isEmpty()) {
                name.append("<");
                Map<TypeParameter, ProducedType> args = 
                        getTypeArguments();
                for (int i=0, l=tps.size(); i<l; i++) {
                    if (i!=0) {
                        name.append(",");
                    }
                    TypeParameter tp = tps.get(i);
                    ProducedType arg = args.get(tp);
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
