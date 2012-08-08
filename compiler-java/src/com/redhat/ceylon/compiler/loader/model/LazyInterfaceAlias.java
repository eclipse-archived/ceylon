package com.redhat.ceylon.compiler.loader.model;

import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.mirror.ClassMirror;
import com.redhat.ceylon.compiler.typechecker.model.InterfaceAlias;

/**
 * Represents a (not so) lazy interface alias. This interface is not lazy like the
 * LazyInterface because overriding every method is just a pain and we don't
 * really need it in this case because class aliases are not reified so the
 * types that are required by this alias (extended type and parameter list
 * types) can't depend on this alias, so there can't be any circularity issue.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class LazyInterfaceAlias extends InterfaceAlias {
    public ClassMirror classMirror;

    public LazyInterfaceAlias(ClassMirror classMirror) {
        this.classMirror = classMirror;
        setName(Util.getMirrorName(classMirror));
    }

}
