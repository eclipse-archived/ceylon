package com.redhat.ceylon.model.typechecker.model;

import java.util.List;

public interface Generic {
    public List<TypeParameter> getTypeParameters();
    public void setTypeParameters(List<TypeParameter> params);
}
