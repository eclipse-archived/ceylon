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

package com.redhat.ceylon.compiler.java.codegen;

import java.util.ArrayList;
import java.util.HashMap;

import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;

public class ToplevelAttributesDefinitionBuilder {
    private CeylonTransformer gen;
    private ArrayList<Tree.AnyAttribute> attribs;
    private HashMap<String, Tree.AttributeSetterDefinition> setters;
    
    
    public ToplevelAttributesDefinitionBuilder(CeylonTransformer gen) {
        this.gen = gen;
        attribs = new ArrayList<Tree.AnyAttribute>();
        setters = new HashMap<String, Tree.AttributeSetterDefinition>();
    }

    public void add(Tree.AttributeDeclaration decl) {
        attribs.add(decl);
    }
    
    public void add(Tree.AttributeGetterDefinition decl) {
        attribs.add(decl);
    }
    
    public void add(Tree.AttributeSetterDefinition decl) {
        String attrName = decl.getIdentifier().getText();
        setters.put(attrName, decl);
    }
    
    public ListBuffer<JCTree> build() {
        ListBuffer<JCTree> result = ListBuffer.lb();
        for (Tree.AnyAttribute attrib : attribs) {
            boolean annots = gen.checkCompilerAnnotations(attrib);
            String attrName = attrib.getIdentifier().getText();
            Tree.AttributeSetterDefinition setter = setters.get(attrName);
            result.appendList(gen.transformAttribute(attrib, setter));
            gen.resetCompilerAnnotations(annots);
        }
        return result;
    }
}
