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

import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Identifier;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCImport;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
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
    public boolean disableModelAnnotations = false;
    CeylonVisitor visitor;
    
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
        TypedDeclaration declarationModel = decl.getDeclarationModel(); 
        final String attrName = decl.getIdentifier().getText();
        final String attrClassName = naming.getAttrClassName(declarationModel, 0);
        final Tree.SpecifierOrInitializerExpression expression;
        final Tree.Block block;
        if (decl instanceof Tree.AttributeDeclaration) {
            Tree.AttributeDeclaration adecl = (Tree.AttributeDeclaration)decl;
            expression = adecl.getSpecifierOrInitializerExpression();
            block = null;
        } else if (decl instanceof Tree.AttributeGetterDefinition) {
            expression = null;
            Tree.AttributeGetterDefinition gdef = (Tree.AttributeGetterDefinition)decl;
            block = gdef.getBlock();
        } else if (decl instanceof Tree.AttributeSetterDefinition) {
            expression = null;
            Tree.AttributeSetterDefinition sdef = (Tree.AttributeSetterDefinition)decl;
            block = sdef.getBlock();
            if (Decl.isLocal(decl)) {
                declarationModel = ((Tree.AttributeSetterDefinition)decl).getDeclarationModel().getParameter();
            }
        } else {
            throw new RuntimeException();
        }
        return transformAttribute(declarationModel, attrName, attrClassName,
                block, expression, setterDecl);
    }

    /** Creates a module class in the package, with the Module annotation required by the runtime. */
    public List<JCTree> transformModuleDescriptor(Tree.ModuleDescriptor module) {
        at(module);
        JCTree getter = make().MethodDef(make().Modifiers(Flags.STATIC), names().fromString("getModule"),
                //The return type is a class in the language module
                makeJavaType(((com.redhat.ceylon.compiler.typechecker.model.Class)typeFact().getLanguageModuleDeclaration("Module")).getType()),
                //just a getter, no params
                List.<JCTypeParameter>nil(), List.<JCTree.JCVariableDecl>nil(), List.<JCExpression>nil(),
                //TODO for now we just return null - do we really need to return something?
                make().Block(0, List.<JCStatement>of(make().Return(makeNull()))), null);
        ListBuffer<JCExpression> annargs = new ListBuffer<JCExpression>();
        ListBuffer<JCExpression> authors = new ListBuffer<JCExpression>();
        StringBuilder modname = new StringBuilder();
        for (Identifier id : module.getImportPath().getIdentifiers()) {
            if (modname.length() > 0) {
                modname.append('.');
            }
            modname.append(id.getText());
        }
        String modvers = module.getVersion().getText();
        modvers = modvers.substring(0, modvers.length()-1).substring(1);
        annargs.add(make().Assign(naming.makeUnquotedIdent("name"), make().Literal(modname.toString())));
        annargs.add(make().Assign(naming.makeUnquotedIdent("version"), make().Literal(modvers)));
        for (Tree.Annotation a : module.getAnnotationList().getAnnotations()) {
            String annName = ((BaseMemberExpression)a.getPrimary()).getIdentifier().getText();
            at(a);
            if ("doc".equals(annName)) {
                annargs.add(make().Assign(naming.makeUnquotedIdent("doc"), expressionGen().transformExpression(
                        a.getPositionalArgumentList().getPositionalArguments().get(0).getExpression(), BoxingStrategy.UNBOXED, null)));
            } else if ("by".equals(annName)) {
                //There can be several by's each with an author name
                authors.add(expressionGen().transformExpression(
                        a.getPositionalArgumentList().getPositionalArguments().get(0).getExpression(), BoxingStrategy.UNBOXED, null));
            } else if ("license".equals(annName)) {
                annargs.add(make().Assign(naming.makeUnquotedIdent("license"), expressionGen().transformExpression(
                        a.getPositionalArgumentList().getPositionalArguments().get(0).getExpression(), BoxingStrategy.UNBOXED, null)));
            }
            //TODO dependencies
        }
        if (!authors.isEmpty()) {
            //TODO create array of authors
            //annargs.add(make().Assign(naming.makeUnquotedIdent("by"), authors.toList()));
        }
        return ClassDefinitionBuilder
                .klass(this, false, "module", null)
                .modifiers(Flags.FINAL)
                .constructorModifiers(Flags.PRIVATE)
                .annotations(List.<JCTree.JCAnnotation>of(make().Annotation(
                        makeIdent(syms().ceylonAtModuleType), annargs.toList())))
                .defs(List.<JCTree>of(getter))
                .build();

    }

    public List<JCTree> transformPackageDescriptor(Tree.PackageDescriptor pack) {
        at(pack);
        JCTree getter = make().MethodDef(make().Modifiers(Flags.STATIC), names().fromString("getPackage"),
                //The return type is a class in the language module
                makeJavaType(((com.redhat.ceylon.compiler.typechecker.model.Class)typeFact().getLanguageModuleDeclaration("Package")).getType()),
                //just a getter, no params
                List.<JCTypeParameter>nil(), List.<JCTree.JCVariableDecl>nil(), List.<JCExpression>nil(),
                //TODO for now we just return null - do we really need to return something?
                make().Block(0, List.<JCStatement>of(make().Return(makeNull()))), null);
        ListBuffer<JCExpression> annargs = new ListBuffer<JCExpression>();
        ListBuffer<JCExpression> authors = new ListBuffer<JCExpression>();
        StringBuilder modname = new StringBuilder();
        for (Identifier id : pack.getImportPath().getIdentifiers()) {
            if (modname.length() > 0) {
                modname.append('.');
            }
            modname.append(id.getText());
        }
        annargs.add(make().Assign(naming.makeUnquotedIdent("name"), make().Literal(modname.toString())));
        boolean sharePack = false;
        for (Tree.Annotation a : pack.getAnnotationList().getAnnotations()) {
            String annName = ((BaseMemberExpression)a.getPrimary()).getIdentifier().getText();
            at(a);
            if ("doc".equals(annName)) {
                annargs.add(make().Assign(naming.makeUnquotedIdent("doc"), expressionGen().transformExpression(
                        a.getPositionalArgumentList().getPositionalArguments().get(0).getExpression(), BoxingStrategy.UNBOXED, null)));
            } else if ("by".equals(annName)) {
                //There can be several by's each with an author name
                authors.add(expressionGen().transformExpression(
                        a.getPositionalArgumentList().getPositionalArguments().get(0).getExpression(), BoxingStrategy.UNBOXED, null));
            } else if ("license".equals(annName)) {
                annargs.add(make().Assign(naming.makeUnquotedIdent("license"), expressionGen().transformExpression(
                        a.getPositionalArgumentList().getPositionalArguments().get(0).getExpression(), BoxingStrategy.UNBOXED, null)));
            } else if ("shared".equals(annName)) {
                sharePack = true;
            }
        }
        annargs.add(make().Assign(naming.makeUnquotedIdent("shared"), makeBoolean(sharePack)));
        if (!authors.isEmpty()) {
            //TODO create array of authors
            //annargs.add(make().Assign(naming.makeUnquotedIdent("by"), authors.toList()));
        }
        return ClassDefinitionBuilder
                .klass(this, false, "$package", null)
                .modifiers(Flags.FINAL)
                .constructorModifiers(Flags.PRIVATE)
                .annotations(List.<JCTree.JCAnnotation>of(make().Annotation(
                        makeIdent(syms().ceylonAtPackageType), annargs.toList())))
                .defs(List.<JCTree>of(getter))
                .build();

    }

    public List<JCTree> transformAttribute(
            TypedDeclaration declarationModel,
            String attrName, String attrClassName,
            final Tree.Block block,
            final Tree.SpecifierOrInitializerExpression expression, 
            final Tree.AttributeSetterDefinition setterDecl) {
        AttributeDefinitionBuilder builder = AttributeDefinitionBuilder
            .wrapped(this, attrClassName, attrName, declarationModel, declarationModel.isToplevel())
            .is(Flags.PUBLIC, declarationModel.isShared());

        // if it's a module or package add a special annotation
        if (declarationModel.isToplevel()) {
            if (attrName.equals("module")
        		&& declarationModel.getUnit().getFilename().equals("module.ceylon")) {
                // module
                Package pkg = (Package) declarationModel.getContainer();
                Module module = pkg.getModule();
                builder.annotations(makeAtModule(module));
            } else if (attrName.equals("package")
                    && declarationModel.getUnit().getFilename().equals("package.ceylon")) {
                // package
                Package pkg = (Package) declarationModel.getContainer();
                builder.annotations(makeAtPackage(pkg));
            }
        }

        if (declarationModel instanceof Setter
                || declarationModel instanceof Parameter) {
            // For local setters
            JCBlock setterBlock = make().Block(0, statementGen().transformStmts(block.getStatements()));
            builder.setterBlock(setterBlock);
            builder.skipGetter();
        } else {
            if (declarationModel instanceof Value) {
                // For local and toplevel value attributes
                if (!declarationModel.isVariable()) {
                    builder.immutable();
                }
                if (expression != null) {
                    builder.initialValue(expressionGen().transformExpression(
                            expression.getExpression(), 
                            CodegenUtil.getBoxingStrategy(declarationModel),
                            declarationModel.getType()));
                } else {
                    Parameter p = CodegenUtil.findParamForDecl(attrName, declarationModel);
                    if (p != null) {
                        builder.initialValue(naming.makeName(p, Naming.NA_MEMBER | Naming.NA_ALIASED));
                    }
                }
            } else {
                // For local and toplevel getters
                JCBlock getterBlock = make().Block(0, statementGen().transformStmts(block.getStatements()));
                builder.getterBlock(getterBlock);
                
                if (Decl.isLocal(declarationModel)) {
                    // For local getters
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
        
        if (Decl.isLocal(declarationModel)) {
            return builder.build().append(makeLocalIdentityInstance(attrClassName, attrClassName, declarationModel.isShared()));
        } else {
            builder.is(Flags.STATIC, true);
            return builder.build();
        }
    }
}
