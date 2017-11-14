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
package org.eclipse.ceylon.compiler.java.codegen;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.model.typechecker.model.ControlBlock;

public class LabelVisitor extends Visitor {

    private int id = 0;
    
    private Tree.ControlClause loop;
    

    private Map<ControlBlock, Integer> loopMap = new HashMap<>();

    private Map<Tree.SwitchClause, Integer> switchMap = new HashMap<>();
    
    /** Returns a unique identifier (unique within the compilation unit)
     * for the control block if it's associated with a {@code while} or 
     * {@code for} loop, null otherwise.
     */
    public Integer getLoopId(ControlBlock controlBlock) {
        return loopMap.get(controlBlock);
    }
    
    public Integer getSwitchId(Tree.SwitchClause switchClause) {
        return switchMap.get(switchClause);
    }
    
    @Override
    public void visit(Tree.WhileClause that) {
        Tree.ControlClause prev = loop;
        loop = that;
        loopMap.put(that.getControlBlock(), id++);
        super.visit(that);
        loop = prev;
    }
    
    @Override
    public void visit(Tree.ForClause that) {
        Tree.ControlClause prev = loop;
        loop = that;
        loopMap.put(that.getControlBlock(), id++);
        super.visit(that);
        loop = prev;
    }
    
    @Override
    public void visit(Tree.SwitchClause that) {
        switchMap.put(that, id++);
    }
    
}
