package com.redhat.ceylon.compiler.model;

import static com.redhat.ceylon.compiler.model.Util.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A method. Note that a method must have
 * at least one parameter list.
 * 
 * @author Gavin King
 *
 */
public class Method extends MethodOrValue implements Generic, Scope, Functional {
	
    boolean formal;
    
    List<TypeParameter> typeParameters = Collections.emptyList();	
    List<ParameterList> parameterLists = new ArrayList<ParameterList>();
    List<Declaration> members = new ArrayList<Declaration>();

	public boolean isFormal() {
		return formal;
	}
	
	public void setFormal(boolean formal) {
		this.formal = formal;
	}
	
	public ProducedType getType() {
		return type;
	}
	
	public void setType(ProducedType type) {
		this.type = type;
	}
	
	public List<TypeParameter> getTypeParameters() {
		return typeParameters;
	}
	
	public void setTypeParameters(List<TypeParameter> typeParameters) {
        this.typeParameters = typeParameters;
    }
	
	@Override
	public List<Declaration> getMembers() {
	    return members;
	}
	
	@Override
	public List<ParameterList> getParameterLists() {
	    return parameterLists;
	}
	
	@Override
	public void addParameterList(ParameterList pl) {
	    parameterLists.add(pl);
	}

	@Override
    public boolean acceptsArguments(List<ProducedType> typeArguments) {
        //TODO!
        return this.typeParameters.size()==typeArguments.size();
    }
    
    @Override
    public ProducedTypedReference getProducedTypedReference(List<ProducedType> typeArguments) {
        if (!acceptsArguments(typeArguments)) {
            throw new RuntimeException( getName() + 
                    " does not accept given type arguments");
        }
        ProducedTypedReference pt = new ProducedTypedReference();
        pt.setDeclaration(this);
        pt.setTypeArguments( arguments(this, typeArguments) );
        return pt;
    }
    
    @Override
    public ProducedTypedReference getTypedReference() {
        ProducedTypedReference pt = new ProducedTypedReference();
        pt.setDeclaration(this);
        Map<TypeParameter, ProducedType> map = new HashMap<TypeParameter, ProducedType>();
        for (TypeParameter p: getTypeParameters()) {
            ProducedType pta = new ProducedType();
            pta.setDeclaration(p);
            map.put(p, pta);
        }
        pt.setTypeArguments(map);
        return pt;
    }
    
}
