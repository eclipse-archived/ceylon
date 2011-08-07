package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.arguments;

import java.util.List;


public abstract class ClassOrInterface extends TypeDeclaration {

    @Override
    public boolean isMemberType() {
        return getContainer() instanceof ClassOrInterface;
    }

    @Override
    public ProducedType getDeclaringType(Declaration d) {
        //look for it as a declared or inherited 
        //member of the current class or interface
        ProducedType st = getType().getSupertype((TypeDeclaration) d.getContainer());
        if (st!=null) {
            return st;
        }
        else {
            return getContainer().getDeclaringType(d);
        }
    }

    public abstract boolean isAbstract();

    ProducedType aliasType(ProducedType outerType, List<ProducedType> typeArguments) {
    	if (getExtendedType()!=null) {
    		return getExtendedType().substitute(arguments(this, outerType, typeArguments));
    	}
    	else {
    		return super.getProducedType(outerType, typeArguments);
    	}
    }

}
