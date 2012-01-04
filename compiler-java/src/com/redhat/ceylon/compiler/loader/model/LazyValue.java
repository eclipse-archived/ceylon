/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.compiler.loader.model;

import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.ModelCompleter;
import com.redhat.ceylon.compiler.loader.mirror.ClassMirror;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Value;

/**
 * Represents a lazy toplevel attribute declaration.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class LazyValue extends Value {
    
    public ClassMirror classSymbol;
    private ModelCompleter completer;
    private boolean isLoaded = false;

    public LazyValue(ClassMirror classSymbol, ModelCompleter completer) {
        this.classSymbol = classSymbol;
        this.completer = completer;
        classSymbol.getSimpleName();
        // FIXME: why doesn't that move to getSimpleName()?
        setName(Util.strip(classSymbol.getSimpleName()));
    }

    private void load() {
        if(!isLoaded){
            isLoaded = true;
            completer.complete(this);
        }
    }
    
    @Override
    public String toString() {
        if (!isLoaded) {
            return "UNLOADED:" + super.toString();
        }
        return super.toString();
    }

    @Override
    public boolean isVariable() {
        load();
        return super.isVariable();
    }
    
    @Override
    public ProducedType getType() {
        load();
        return super.getType();
    }

}
