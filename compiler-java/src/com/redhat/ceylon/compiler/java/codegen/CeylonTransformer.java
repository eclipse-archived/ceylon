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

import com.redhat.ceylon.compiler.loader.SourceDeclarationVisitor;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Declaration;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCImport;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCStatement;
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
    public int disableAnnotations = 0;
    static final int DISABLE_MODEL_ANNOS = 1<<0;
    static final int DISABLE_USER_ANNOS = 1<<1;
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
        
        List<JCTree> defs = makeDefs(t);
        
        JCCompilationUnit topLev = new CeylonCompilationUnit(List.<JCTree.JCAnnotation> nil(), pkg, defs, null, null, null, null, t, phasedUnit);

        topLev.lineMap = getMap();
        topLev.sourcefile = file;
        topLev.isCeylonProgram = true;

        return topLev;
    }

    private List<JCTree> makeDefs(CompilationUnit t) {
        final ListBuffer<JCTree> defs = new ListBuffer<JCTree>();
        
        t.visit(new SourceDeclarationVisitor(){
            @Override
            public void loadFromSource(Declaration decl) {
                long flags = decl instanceof Tree.AnyInterface ? Flags.INTERFACE : 0;
                String name = Naming.toplevelClassName("", decl);
                
                defs.add(makeClassDef(decl, flags, name));
                if(decl instanceof Tree.AnyInterface){
                    String implName = Naming.getImplClassName(name);
                    defs.add(makeClassDef(decl, 0, implName));
                }
            }

            private JCTree makeClassDef(Declaration decl, long flags, String name) {
                ListBuffer<JCTree.JCTypeParameter> typarams = new ListBuffer<JCTree.JCTypeParameter>();
                if(decl instanceof Tree.ClassOrInterface){
                    Tree.ClassOrInterface classDecl = (ClassOrInterface) decl;
                    if(classDecl.getTypeParameterList() != null){
                        for(Tree.TypeParameterDeclaration typeParamDecl : classDecl.getTypeParameterList().getTypeParameterDeclarations()){
                            // we don't need a valid name, just a name, and making it BOGUS helps us find it later if it turns out
                            // we failed to reset everything properly
                            typarams.add(make().TypeParameter(names().fromString("BOGUS-"+typeParamDecl.getIdentifier().getText()), List.<JCExpression>nil()));
                        }
                    }
                }

                return make().ClassDef(make().Modifiers(flags | Flags.PUBLIC), names().fromString(name), typarams.toList(), null, List.<JCExpression>nil(), List.<JCTree>nil());
            }
        });
        
        return defs.toList();
    }

    /**
     * This runs after _some_ typechecking has been done
     */
    @SuppressWarnings("unchecked")
    public ListBuffer<JCTree> transformAfterTypeChecking(Tree.CompilationUnit t) {
        disableAnnotations = 0;
        
        GetterSetterPairingVisitor gspv = new GetterSetterPairingVisitor();
        t.visit(gspv);
        
        ToplevelAttributesDefinitionBuilder builder = new ToplevelAttributesDefinitionBuilder(this);
        LabelVisitor lv = new LabelVisitor();
        CeylonVisitor visitor = new CeylonVisitor(this, builder, lv, gspv);
        t.visit(lv);
        t.visit(visitor);
        
        
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
        final String attrName = declarationModel.getName();
        final String attrClassName = Naming.getAttrClassName(declarationModel, 0);
        final Tree.SpecifierOrInitializerExpression expression;
        final Tree.Block block;
        final Tree.AnnotationList annotationList = decl.getAnnotationList();
        if (decl instanceof Tree.AttributeDeclaration) {
            Tree.AttributeDeclaration adecl = (Tree.AttributeDeclaration)decl;
            expression = adecl.getSpecifierOrInitializerExpression();
            block = null;
        } else if (decl instanceof Tree.AttributeGetterDefinition) {
            expression = null;
            Tree.AttributeGetterDefinition gdef = (Tree.AttributeGetterDefinition)decl;
            block = gdef.getBlock();
        } else if (decl instanceof Tree.AttributeSetterDefinition) {
            Tree.AttributeSetterDefinition sdef = (Tree.AttributeSetterDefinition)decl;
            block = sdef.getBlock();
            expression = sdef.getSpecifierExpression();
            if (Decl.isLocal(decl)) {
                declarationModel = ((Tree.AttributeSetterDefinition)decl).getDeclarationModel().getParameter().getModel();
            }
        } else {
            throw new RuntimeException();
        }
        return transformAttribute(declarationModel, attrName, attrClassName,
                annotationList, block, expression, decl, setterDecl);
    }

    public List<JCTree> transformAttribute(
            TypedDeclaration declarationModel,
            String attrName, String attrClassName,
            final Tree.AnnotationList annotations,
            final Tree.Block block,
            final Tree.SpecifierOrInitializerExpression expression, 
            final Tree.TypedDeclaration decl,
            final Tree.AttributeSetterDefinition setterDecl) {
        
        final JCExpression initialValue;
        final HasErrorException expressionError;
        if (expression != null) {
            expressionError = errors().getFirstExpressionError(expression.getExpression());
            if (expressionError != null) {
                initialValue = makeErroneous(expression, expressionError.getErrorMessage().getMessage());
            } else {
                initialValue = transformValueInit(
                        declarationModel, attrName, expression);
            }
        } else {
            expressionError = null;
            initialValue = transformValueInit(
                    declarationModel, attrName, expression);
        }
        
        
        // For captured local variable Values, use a VariableBox
        if (Decl.isBoxedVariable(declarationModel)) {
            if (expressionError != null) {
                return List.<JCTree>of(expressionError.makeThrow(this));
            } else {
                return List.<JCTree>of(makeVariableBoxDecl(
                        initialValue, declarationModel));
            }
        }
        
        // For late-bound getters we only generate a declaration
        if (block == null && expression == null && !Decl.isToplevel(declarationModel)) {
            JCExpression typeExpr = makeJavaType(getGetterInterfaceType(declarationModel));
            JCTree.JCVariableDecl var = makeVar(attrClassName, typeExpr, null);
            return List.<JCTree>of(var);
        }
        
        // For everything else generate a getter/setter method
        AttributeDefinitionBuilder builder = AttributeDefinitionBuilder
            .wrapped(this, attrClassName, attrName, declarationModel, declarationModel.isToplevel())
            .is(Flags.PUBLIC, declarationModel.isShared());

        // Set the local declarations annotation
        if(decl != null){
            List<JCAnnotation> scopeAnnotations;
            if(Decl.isToplevel(declarationModel) && setterDecl != null){
                scopeAnnotations = makeAtLocalDeclarations(decl, setterDecl);
            }else{
                scopeAnnotations = makeAtLocalDeclarations(decl);
            }
            builder.classAnnotations(scopeAnnotations);
        }

        // Remember the setter class if we generate a getter
        if(Decl.isGetter(declarationModel)
                && declarationModel.isVariable()
                && Decl.isLocal(declarationModel)){
            // we must have a setter class
            Setter setter = ((Value)declarationModel).getSetter();
            if(setter != null){
                String setterClassName = Naming.getAttrClassName(setter, 0);
                JCExpression setterClassNameExpr = naming.makeUnquotedIdent(setterClassName);
                builder.setterClass(makeSelect(setterClassNameExpr, "class"));
            }
        }
        
        if (declarationModel instanceof Setter
                || (declarationModel instanceof MethodOrValue 
                    && ((MethodOrValue)declarationModel).isParameter())) {
            // For local setters
            JCBlock setterBlock = makeSetterBlock(declarationModel, block, expression);
            builder.setterBlock(setterBlock);
            builder.skipGetter();
            if(Decl.isLocal(decl)){
                // we need to find back the Setter model for local setters, because 
                // in transformAttribute(Tree.TypedDeclaration decl, Tree.AttributeSetterDefinition setterDecl)
                // we turn the declaration model from the Setter to its single parameter
                Setter setter = (Setter) declarationModel.getContainer();
                String getterClassName = Naming.getAttrClassName(setter.getGetter(), 0);
                JCExpression getterClassNameExpr = naming.makeUnquotedIdent(getterClassName);
                builder.isSetter(makeSelect(getterClassNameExpr, "class"));
            }
        } else {
            if (Decl.isValue(declarationModel)) {
                // For local and toplevel value attributes
                if (!declarationModel.isVariable() && !declarationModel.isLate()) {
                    builder.immutable();
                }
            } else {
                // For local and toplevel getters
                JCBlock getterBlock = makeGetterBlock(declarationModel, block, expression);
                builder.getterBlock(getterBlock);
                
                if (Decl.isLocal(declarationModel)) {
                    // For local getters
                    builder.immutable();
                } else {
                    // For toplevel getters
                    if (setterDecl != null) {
                        JCBlock setterBlock = makeSetterBlock(setterDecl.getDeclarationModel(),
                                setterDecl.getBlock(), setterDecl.getSpecifierExpression());
                        builder.setterBlock(setterBlock);
                        builder.userAnnotationsSetter(expressionGen().transform(setterDecl.getAnnotationList()));
                    } else {
                        builder.immutable();
                    }
                }
            }
        }
        
        builder.userAnnotations(expressionGen().transform(annotations));
        
        if (Decl.isToplevel(declarationModel)) {
            builder.userAnnotations(makeAtIgnore());
            builder.userAnnotationsSetter(makeAtIgnore());
        }
        
        if (Decl.isLocal(declarationModel)) {
            if (expressionError != null) {
                return List.<JCTree>of(expressionError.makeThrow(this));
            }
            builder.classAnnotations(makeAtLocalDeclaration(declarationModel.getQualifier()));
            if(initialValue != null)
                builder.valueConstructor();
            JCExpression typeExpr;
            if (declarationModel instanceof Setter 
                    || (declarationModel instanceof MethodOrValue
                        && ((MethodOrValue)declarationModel).isParameter())) {
                typeExpr = makeQuotedIdent(attrClassName);
            } else {
                typeExpr = makeJavaType(getGetterInterfaceType(declarationModel));
            }
            return builder.build().append(makeLocalIdentityInstance(
                    typeExpr,
                    attrClassName, 
                    attrClassName, declarationModel.isShared(), initialValue));
        } else {
            if (expressionError != null) {
                builder.initialValueError(expressionError);
            } else if(initialValue != null) {
                builder.initialValue(initialValue);
            }
            builder.is(Flags.STATIC, true);
            return builder.build();
        }
    }
    
    private JCTree.JCExpression transformValueInit(
            TypedDeclaration declarationModel, String attrName,
            final Tree.SpecifierOrInitializerExpression expression) {
        JCTree.JCExpression initialValue = null;
        if (Decl.isNonTransientValue(declarationModel)
                && !(expression instanceof Tree.LazySpecifierExpression)) {
            if (expression != null) {
                initialValue = expressionGen().transform(
                        expression, 
                        CodegenUtil.getBoxingStrategy(declarationModel),
                        declarationModel.getType());
            } else {
                Parameter p = CodegenUtil.findParamForDecl(attrName, declarationModel);
                if (p != null) {
                    initialValue = naming.makeName(p.getModel(), Naming.NA_MEMBER | Naming.NA_ALIASED);
                }
            }
        }
        return initialValue;
    }

    public JCExpression transformAttributeGetter(
            TypedDeclaration declarationModel,
            final JCExpression expression) {
        
        final String attrName = declarationModel.getName();
        final String attrClassName = Naming.getAttrClassName(declarationModel, 0);
        
        JCBlock getterBlock = makeGetterBlock(expression);
        
        // For everything else generate a getter/setter method
        AttributeDefinitionBuilder builder = AttributeDefinitionBuilder
            .indirect(this, attrClassName, attrName, declarationModel, declarationModel.isToplevel())
            .getterBlock(getterBlock)
            .immutable();
        
        List<JCTree> attr =  builder.build();
        JCNewClass newExpr = makeNewClass(attrClassName, false, null);
        JCExpression result = makeLetExpr(naming.temp(), List.<JCStatement>of((JCStatement)attr.get(0)), newExpr);
        return result;
    }
    
    /** Creates a module class in the package, with the Module annotation required by the runtime. */
    public List<JCTree> transformModuleDescriptor(Tree.ModuleDescriptor module) {
        at(module);
        ClassDefinitionBuilder builder = ClassDefinitionBuilder
                .klass(this, "module_", null)
                .modifiers(Flags.FINAL)
                .constructorModifiers(Flags.PRIVATE)
                .annotations(makeAtModule(module.getUnit().getPackage().getModule()));
        builder.annotations(expressionGen().transform(module.getAnnotationList()));
        for (Tree.ImportModule imported : module.getImportModuleList().getImportModules()) {
            String quotedName;
            if (imported.getImportPath() != null) {
                StringBuilder sb = new StringBuilder();
                for (Tree.Identifier part : imported.getImportPath().getIdentifiers()) {
                    sb.append(part.getText()).append('$');
                }
                quotedName = sb.substring(0, sb.length()-1);
            } else if (imported.getQuotedLiteral() != null) {
                quotedName = imported.getQuotedLiteral().getText();
                quotedName = quotedName.substring(1, quotedName.length()-1);
                quotedName = quotedName.replace('.', '$');
            } else {
                throw Assert.fail();
            }
            if (quotedName.equals("ceylon$language")) {
                continue;
            }
            List<JCAnnotation> importAnnotations = expressionGen().transform(imported.getAnnotationList());
            JCModifiers mods = make().Modifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL, importAnnotations);
            Name fieldName = names().fromString(quotedName);
            builder.defs(List.<JCTree>of(make().VarDef(mods, fieldName, make().Type(syms().stringType), makeNull())));
        }
        return builder.build();
    }

    public List<JCTree> transformPackageDescriptor(Tree.PackageDescriptor pack) {
        at(pack);
        ClassDefinitionBuilder builder = ClassDefinitionBuilder
                .klass(this, "package_", null)
                .modifiers(Flags.FINAL)
                .constructorModifiers(Flags.PRIVATE)
                .annotations(makeAtPackage(pack.getUnit().getPackage()));
        builder.annotations(expressionGen().transform(pack.getAnnotationList()));
        return builder.build();
    }
}
