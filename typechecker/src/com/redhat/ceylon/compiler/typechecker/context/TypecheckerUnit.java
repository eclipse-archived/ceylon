package com.redhat.ceylon.compiler.typechecker.context;

import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.common.BackendSupport;
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
    
    private Set<String> supportedBackends = new HashSet<String>();

    @Override
    public Set<String> getSupportedBackends() {
        return supportedBackends;
    }
    
    public void setSupportedBackends(Set<String> backends) {
        supportedBackends = backends;
    }
}
