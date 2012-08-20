package com.redhat.ceylon.compiler.loader.model;

import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.mirror.ClassMirror;
import com.redhat.ceylon.compiler.typechecker.model.ClassAlias;

/**
 * Represents a (not so) lazy class alias. This class is not lazy like the
 * LazyClass because overriding every method is just a pain and we don't
 * really need it in this case because class aliases are not reified so the
 * types that are required by this alias (extended type and parameter list
 * types) can't depend on this alias, so there can't be any circularity issue.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class LazyClassAlias extends ClassAlias {
    public ClassMirror classMirror;

    public LazyClassAlias(ClassMirror classMirror) {
        this.classMirror = classMirror;
        setName(Util.getMirrorName(classMirror));
    }

}
