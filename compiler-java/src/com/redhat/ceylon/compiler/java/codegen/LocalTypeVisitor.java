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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Statement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Tuple;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.typechecker.tree.Walker;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Setter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Value;

/**
 * Visits everything, stops at types (aliases, objects, class, interface) and collects
 * their Java class name if they are local.
 * 
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class LocalTypeVisitor extends Visitor {

    private HasTypeVisitor hasTypeVisitor = new HasTypeVisitor();
    
    private Set<String> locals = new HashSet<String>();
    private Set<String> ignored = new HashSet<String>();
    private Set<String> localCompanionClasses = new HashSet<String>();
    private Set<Interface> localInterfaces = new HashSet<Interface>();

    private LinkedList<Integer> anonymous = new LinkedList<Integer>();
    {
        anonymous.add(1);
    }
    private String prefix = "";

    public Set<String> getLocals() {
        locals.removeAll(ignored);
        return locals;
    }

    public Set<Interface> getLocalInterfaces() {
        return localInterfaces;
    }

    public void startFrom(Node tree) {
        int mpl = 0;
        String prefix = null;
        // make sure we start with the right number of anonymous classes for methods with MPL before we visit the children
        if(tree instanceof Tree.AnyMethod){
            Function model = ((Tree.AnyMethod)tree).getDeclarationModel();
            mpl = model.getParameterLists().size();
            if(mpl > 1){
                prefix = this.prefix;
                for(int i=1;i<mpl;i++)
                    enterAnonymousClass();
            }
        }
        
        if(tree instanceof Tree.ClassDefinition){
            visitClassBody((Tree.ClassDefinition)tree);
        }else{
            tree.visitChildren(this);
        }
        if(mpl > 1){
            for(int i=1;i<mpl;i++)
                exitAnonymousClass();
            this.prefix = prefix;
        }
    }

    private void visitClassBody(ClassDefinition classDef) {
        List<Statement> statements = classDef.getClassBody().getStatements();
        // first visit the super call that goes at the start of the initialiser
        if(classDef.getExtendedType() != null){
            classDef.getExtendedType().visit(this);
        }
        // then visit everything that ends up in the initialiser
        for(Tree.Statement stmt : statements){
            if(!endsUpInInitializer(stmt))
                continue;
            stmt.visit(this);
        }
        // then visit default parameter values that go after the initialiser
        if(classDef.getParameterList() != null){
            classDef.getParameterList().visit(this);
        }
        
        // finally visit everything that is not part of the initialiser
        for(Tree.Statement stmt : statements){
            if(endsUpInInitializer(stmt))
                continue;
            stmt.visit(this);
        }
    }

    private boolean endsUpInInitializer(Statement stmt) {
        if(stmt instanceof Tree.MethodDefinition)
            return false;
        if(stmt instanceof Tree.MethodDeclaration
                && ((Tree.MethodDeclaration) stmt).getSpecifierExpression() instanceof Tree.LazySpecifierExpression)
            return false;
        // lazy attributes end up in the initialiser if they're neither captured nor shared
        if(stmt instanceof Tree.AttributeDeclaration
                && ((Tree.AttributeDeclaration) stmt).getSpecifierOrInitializerExpression() instanceof Tree.LazySpecifierExpression){
            Value model = ((Tree.AttributeDeclaration) stmt).getDeclarationModel();
            return !ModelUtil.isCaptured(model);
        }
        if(stmt instanceof Tree.SpecifierStatement
            && ((Tree.SpecifierStatement) stmt).getSpecifierExpression() instanceof Tree.LazySpecifierExpression){
            return false;
        }
        if(stmt instanceof Tree.AttributeGetterDefinition
                || stmt instanceof Tree.AttributeSetterDefinition)
            return false;
        if(stmt instanceof Tree.ClassOrInterface)
            return false;
        if(stmt instanceof Tree.TypeAliasDeclaration)
            return false;
        return true;
    }

    private void collect(Node that, Declaration model) {
        if(model != null && (!model.isMember() 
                || isMemberInInitialiser(model)
                || Decl.isObjectExpressionType(model))){
            String name = Naming.escapeClassName(model.getName());
            Set<String> locals = this.locals;
            // FIXME: better name processing
            if(model instanceof Value
                    && !model.isToplevel())
                name = Naming.suffixName(Naming.Suffix.$getter$, name);
            if(model instanceof TypedDeclaration || model.isAnonymous())
                name += "_";
            else if(model instanceof Interface){
                // for interfaces we point to the toplevel interface prefixed with ::
                // no need to find the local java class
                localInterfaces.add((Interface) model);
                name = Naming.suffixName(Naming.Suffix.$impl, name);
                locals = localCompanionClasses;
            }
            // find an unused name
            int i;
            String prefixedName;
            for(i=1;locals.contains(prefixedName = prefix+i+name);i++){}
            // add it
            locals.add(prefixedName);
            // only keep it if it contains locals
            if(model instanceof TypedDeclaration && !hasTypeVisitor.hasType(that))
                ignored.add(prefixedName);
        }
    }

    private boolean isMemberInInitialiser(Declaration model) {
        if(ModelUtil.isCaptured(model))
            return false;
        if(model instanceof ClassOrInterface)
            return false;
        if(model instanceof Value && ((Value) model).isTransient())
            return false;
        return true;
    }

    private void exitAnonymousClass() {
        anonymous.pop();
        Integer count = anonymous.peek();
        anonymous.set(0, count+1);
    }

    private void enterAnonymousClass() {
        anonymous.push(1);
        prefix = getPrefix();
    }

    private String getPrefix() {
        StringBuilder b = new StringBuilder();
        // ignore the first one and traverse last to second
        for(int i=anonymous.size()-1;i>0;i--){
            Integer c = anonymous.get(i);
            b.append(c).append("$");
        }
        return b.toString();
    }

    //
    // Possibly members
    
    @Override
    public void visit(Tree.AnyMethod that){
        Function model = that.getDeclarationModel();
        int mpl = model.getParameterLists().size();
        String prefix = null;
        if(mpl > 1){
            prefix = this.prefix;
            for(int i=1;i<mpl;i++)
                enterAnonymousClass();
        }
        collect(that, model);
        // stop at locals, who get a type generated for them
        if(model.isMember())
            super.visit(that);
        if(mpl > 1){
            for(int i=1;i<mpl;i++)
                exitAnonymousClass();
            this.prefix = prefix;
        }
    }

    @Override
    public void visit(Tree.AttributeGetterDefinition that){
        Value model = that.getDeclarationModel();
        collect(that, model);
        // stop at locals, who get a type generated for them
        if(model.isMember())
            super.visit(that);
    }

    @Override
    public void visit(Tree.AttributeDeclaration that){
        Value model = that.getDeclarationModel();
        // we don't need to collect these
        // stop at locals and private non-captured transient members which end up having a
        // locall getter for them
        if(!(model.isMember() 
                && !ModelUtil.isCaptured(model)
                && model.isTransient()))
            super.visit(that);
    }

    @Override
    public void visit(Tree.AttributeSetterDefinition that){
        Setter model = that.getDeclarationModel();
        // do not collect setters, they are always referenced by the getter
        // stop at locals, who get a type generated for them
        if(model.isMember())
            super.visit(that);
    }

    @Override
    public void visit(Tree.TypeAliasDeclaration that){
        // stop at aliases, do not collect them since we can never create any instance of them
        // and they are useless at runtime
    }

    @Override
    public void visit(Tree.ClassOrInterface that){
        ClassOrInterface model = that.getDeclarationModel();
        // stop at aliases, do not collect them since we can never create any instance of them
        // and they are useless at runtime
        if(!model.isAlias())
            collect(that, model);
    }

    @Override
    public void visit(Tree.ObjectDefinition that){
        Value model = that.getDeclarationModel();
        if(model != null)
            collect(that, model.getTypeDeclaration());
    }

    //
    // Expressions
    
    @Override
    public void visit(Tree.FunctionalParameterDeclaration that) {
        Tree.TypedDeclaration typedDeclaration = that.getTypedDeclaration();
        boolean anon = typedDeclaration instanceof Tree.MethodDeclaration
            && ((Tree.MethodDeclaration) typedDeclaration).getSpecifierExpression() instanceof Tree.LazySpecifierExpression;
        String prefix = this.prefix;
        if(anon)
            enterAnonymousClass();
        super.visit(that);
        if(anon)
            exitAnonymousClass();
        this.prefix = prefix;
    }
    

    @Override
    public void visit(Tree.Comprehension that) {
        String prefix = this.prefix;
        // one anonymous for Iterable, one for Iterator
        enterAnonymousClass();
        enterAnonymousClass();
        super.visit(that);
        exitAnonymousClass();
        exitAnonymousClass();
        this.prefix = prefix;
    }

    @Override
    public void visit(Tree.FunctionArgument that) {
        String prefix = this.prefix;
        enterAnonymousClass();
        super.visit(that);
        exitAnonymousClass();
        this.prefix = prefix;
    }

    @Override
    public void visit(Tree.MethodArgument that) {
        String prefix = this.prefix;
        enterAnonymousClass();
        super.visit(that);
        exitAnonymousClass();
        this.prefix = prefix;
    }

    @Override
    public void visit(Tree.QualifiedMemberExpression that) {
        if(that.getMemberOperator() instanceof Tree.SpreadOp){
            String prefix = this.prefix;
            enterAnonymousClass();
            super.visit(that);
            exitAnonymousClass();
            this.prefix = prefix;
        }else{
            super.visit(that);
        }
    }

    @Override
    public void visit(Tree.BaseMemberOrTypeExpression that) {
        Declaration model = that.getDeclaration();
        if(model != null
                && (model instanceof Function || model instanceof Class)
                && !that.getAssigned()
                && !model.isParameter() // if it's a parameter we don't need to wrap it in a class
                && (model instanceof Class == false || !that.getStaticMethodReferencePrimary())
                && !that.getDirectlyInvoked()){
            String prefix = this.prefix;
            enterAnonymousClass();
            super.visit(that);
            exitAnonymousClass();
            this.prefix = prefix;
        }else{
            super.visit(that);
        }
    }

    @Override
    public void visit(Tree.QualifiedMemberOrTypeExpression that) {
        Declaration model = that.getDeclaration();
        if(model != null
                && (model instanceof Function || model instanceof Class)
                && !that.getAssigned()
                && !model.isParameter() // if it's a parameter we don't need to wrap it in a class
                && (model instanceof Class == false || !that.getStaticMethodReferencePrimary())
                && !that.getDirectlyInvoked()){
            String prefix = this.prefix;
            enterAnonymousClass();
            super.visit(that);
            exitAnonymousClass();
            this.prefix = prefix;
        }else{
            super.visit(that);
        }
    }

    @Override
    public void visit(Tree.SequenceEnumeration that) {
        // WARNING: do not count those as they contain Tree.SequencedArgument that we do count
        super.visit(that);
    }

    /**
     * Make sure we visit the sequenced argument knowing we're in a tuple, because
     * those are not lazy
     */
    @Override
    public void visit(Tuple node) {
        Walker.walkAtom(this, node);
        if (node.getSequencedArgument()!=null)
            visit(node.getSequencedArgument(), true);
    }

    @Override
    public void visit(Tree.SequencedArgument that) {
        visit(that, false);
    }

    private void visit(Tree.SequencedArgument that, boolean inTuple) {
        if (Strategy.useConstantIterable(that) || inTuple) {
            // In this case we use a constant iterable or tuple, and those are not anonymous
            super.visit(that);
            return;
        } 
        // Otherwise, all listed arguments are in an iterable class
        String prefix = null;
        for(Tree.PositionalArgument arg : that.getPositionalArguments()){
            // any listed arg causes another anonymous class around the whole 
            if(arg instanceof Tree.ListedArgument){
                if(prefix == null){
                    prefix = this.prefix;
                    enterAnonymousClass();
                }
            }
            arg.visit(this);
        }
        if(prefix != null){
            exitAnonymousClass();
            this.prefix = prefix;
        }
    }

    @Override
    public void visit(Tree.ObjectArgument that){
        Value model = that.getDeclarationModel();
        if(model != null)
            collect(that, model.getTypeDeclaration());
    }

    @Override
    public void visit(Tree.ObjectExpression that){
        Class model = that.getAnonymousClass();
        if(model != null)
            collect(that, model);
    }

    @Override
    public void visit(Tree.AttributeArgument that){
        // if there's a block we end up generating an @Attribute class so we can stop there
        if(that.getBlock() != null){
            Value model = that.getDeclarationModel();
            if(model != null)
                collect(that, model);
        }else{
            super.visit(that);
        }
    }
}
