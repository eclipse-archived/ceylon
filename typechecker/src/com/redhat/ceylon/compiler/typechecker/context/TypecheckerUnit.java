package com.redhat.ceylon.compiler.typechecker.context;

import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.common.BackendSupport;
import com.redhat.ceylon.common.Backends;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Identifier;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Unit;

public class TypecheckerUnit extends Unit implements BackendSupport {
    private Set<Identifier> unresolvedReferences = new HashSet<Identifier>();

    public Set<Identifier> getUnresolvedReferences() {
        return unresolvedReferences;
    }

    private Set<Declaration> missingNativeImplementations = new HashSet<Declaration>();

    public Set<Declaration> getMissingNativeImplementations() {
        return missingNativeImplementations;
    }
    
    private Backends supportedBackends = Backends.ANY;

    @Override
    public Backends getSupportedBackends() {
        return supportedBackends;
    }
    
    public void setSupportedBackends(Backends backends) {
        supportedBackends = backends;
    }
}
