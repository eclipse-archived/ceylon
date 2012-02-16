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

import java.util.Iterator;

import javax.tools.JavaFileObject;

import com.redhat.ceylon.compiler.java.util.Decl;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCImport;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javac.util.Position.LineMap;

/**
 * Main transformer that delegates all transforming of ceylon to java to auxiliary classes.
 */
public class CeylonTransformer extends AbstractTransformer {
    private Options options;
    private LineMap map;
    private JavaFileObject fileObject;
    
    public static CeylonTransformer getInstance(Context context) {
        CeylonTransformer trans = context.get(CeylonTransformer.class);
        if (trans == null) {
            trans = new CeylonTransformer(context);
            context.put(CeylonTransformer.class, trans);
        }
        return trans;
    }

    public CeylonTransformer(Context context) {
        super(context);
        setup(context);
    }

    private void setup(Context context) {
        options = Options.instance(context);
        // It's a bit weird to see "invokedynamic" set here,
        // but it has to be done before Resolve.instance().
        options.put("invokedynamic", "invokedynamic");
    }

    @Override
    public void setMap(LineMap map) {
        this.map = map;
    }

    @Override
    protected LineMap getMap() {
        return map;
    }

    public void setFileObject(JavaFileObject fileObject) {
        this.fileObject = fileObject;
    }
    
    public JavaFileObject getFileObject() {
        return fileObject;
    }

    /**
     * In this pass we only make an empty placeholder which we'll fill in the
     * EnterCeylon phase later on
     */
    public JCCompilationUnit makeJCCompilationUnitPlaceholder(Tree.CompilationUnit t, JavaFileObject file, String pkgName, PhasedUnit phasedUnit) {
        JCExpression pkg = pkgName != null ? getPackage(pkgName) : null;
        at(t);
        JCCompilationUnit topLev = new CeylonCompilationUnit(List.<JCTree.JCAnnotation> nil(), pkg, List.<JCTree> nil(), null, null, null, null, t, phasedUnit);

        topLev.lineMap = getMap();
        topLev.sourcefile = file;
        topLev.isCeylonProgram = true;

        return topLev;
    }

    /**
     * This runs after _some_ typechecking has been done
     */
    @SuppressWarnings("unchecked")
    public ListBuffer<JCTree> transformAfterTypeChecking(Tree.CompilationUnit t) {
        disableModelAnnotations = false;
        ToplevelAttributesDefinitionBuilder builder = new ToplevelAttributesDefinitionBuilder(this);
        CeylonVisitor visitor = new CeylonVisitor(this, builder);
        t.visitChildren(visitor);
        
        ListBuffer<JCTree> result = ListBuffer.lb();
        result.appendList((ListBuffer<JCTree>) visitor.getResult());
        result.appendList(builder.build());
        
        return result;
    }

    // Make a name from a list of strings, using only the first component.
    Name makeName(Iterable<String> components) {
        Iterator<String> iterator = components.iterator();
        String s = iterator.next();
        assert (!iterator.hasNext());
        return names().fromString(s);
    }

    String toFlatName(Iterable<String> components) {
        StringBuffer buf = new StringBuffer();
        Iterator<String> iterator;

        for (iterator = components.iterator(); iterator.hasNext();) {
            buf.append(iterator.next());
            if (iterator.hasNext())
                buf.append('.');
        }

        return buf.toString();
    }

    // FIXME: port handleOverloadedToplevelClasses when I figure out what it
    // does

    private JCExpression getPackage(String fullname) {
        return makeQuotedQualIdentFromString(fullname);
    }

    public JCImport transform(Tree.ImportPath that) {
        String[] names = new String[that.getIdentifiers().size()];
        int i = 0;
        for (Tree.Identifier component : that.getIdentifiers()) {
            names[i++] = component.getText();
        }
        return at(that).Import(makeQuotedQualIdent(null, names), false);
    }
    
    public List<JCTree> transform(Tree.AnyAttribute decl) {
        return transformAttribute(decl, null);
    }

    public List<JCTree> transform(Tree.AttributeSetterDefinition decl) {
        return transformAttribute(decl, null);
    }
    
    public List<JCTree> transformAttribute(Tree.TypedDeclaration decl, Tree.AttributeSetterDefinition setterDecl) {
        at(decl);
        String attrName = decl.getIdentifier().getText();
        String attrClassName = attrName;
        TypedDeclaration declarationModel = decl.getDeclarationModel();
        if (Decl.isLocalBroken(decl)) {
            if (decl instanceof Tree.AttributeGetterDefinition) {
                attrClassName += "$getter";
            } else if (decl instanceof Tree.AttributeSetterDefinition) {
                attrClassName += "$setter";
                declarationModel = ((Tree.AttributeSetterDefinition)decl).getDeclarationModel().getParameter();
            }
        }

        AttributeDefinitionBuilder builder = AttributeDefinitionBuilder
            .wrapped(this, attrName, declarationModel)
            .className(attrClassName)
            .is(Flags.PUBLIC, declarationModel.isShared());

        // if it's a module or package add a special annotation
        if(declarationModel.isToplevel()){
            if(attrName.equals("module")
        		&& declarationModel.getUnit().getFilename().equals("module.ceylon")){
                // module
                Package pkg = (Package) declarationModel.getContainer();
                Module module = pkg.getModule();
                builder.annotations(makeAtModule(module));
            }else if(attrName.equals("package")
                    && declarationModel.getUnit().getFilename().equals("package.ceylon")){
                // package
                Package pkg = (Package) declarationModel.getContainer();
                builder.annotations(makeAtPackage(pkg));
            }
        }

        if (decl instanceof Tree.AttributeSetterDefinition) {
            // For inner setters
            Tree.AttributeSetterDefinition sdef = (Tree.AttributeSetterDefinition)decl;
            JCBlock setterBlock = make().Block(0, statementGen().transformStmts(sdef.getBlock().getStatements()));
            builder.setterBlock(setterBlock);
            builder.skipGetter();
        } else {
            if (decl instanceof Tree.AttributeDeclaration) {
                // For inner and toplevel value attributes
                if (!declarationModel.isVariable()) {
                    builder.immutable();
                }
                Tree.AttributeDeclaration adecl = (Tree.AttributeDeclaration)decl;
                if (adecl.getSpecifierOrInitializerExpression() != null) {
                    builder.initialValue(expressionGen().transformExpression(
                            adecl.getSpecifierOrInitializerExpression().getExpression(), 
                            Util.getBoxingStrategy(declarationModel),
                            declarationModel.getType()));
                }
            } else {
                // For inner and toplevel getters
                Tree.AttributeGetterDefinition gdef = (Tree.AttributeGetterDefinition)decl;
                JCBlock getterBlock = make().Block(0, statementGen().transformStmts(gdef.getBlock().getStatements()));
                builder.getterBlock(getterBlock);
                
                if (Decl.isLocalBroken(decl)) {
                    // For inner getters
                    builder.immutable();
                } else {
                    // For toplevel getters
                    if (setterDecl != null) {
                        JCBlock setterBlock = make().Block(0, statementGen().transformStmts(setterDecl.getBlock().getStatements()));
                        builder.setterBlock(setterBlock);
                    } else {
                        builder.immutable();
                    }
                }
            }
        }
        
        if (Decl.isLocal(decl)) {
            return builder.build().append(makeLocalIdentityInstance(attrClassName, declarationModel.isShared()));
        } else {
            builder.is(Flags.STATIC, true);
            return builder.build();
        }
    }
}
