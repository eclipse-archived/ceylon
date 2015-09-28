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

import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.isForBackend;

import java.util.Iterator;

import javax.tools.JavaFileObject;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.compiler.java.codegen.recovery.HasErrorException;
import com.redhat.ceylon.compiler.loader.SourceDeclarationVisitor;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Declaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ModuleDescriptor;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PackageDescriptor;
import com.redhat.ceylon.compiler.typechecker.tree.TreeUtil;
import com.redhat.ceylon.model.loader.NamingBase.Suffix;
import com.redhat.ceylon.model.loader.model.OutputElement;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.Setter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Value;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.main.OptionName;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCImport;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
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

    private enum WantedDeclaration {
        Normal, Annotation, AnnotationSequence;
    }
    
    private List<JCTree> makeDefs(CompilationUnit t) {
        final ListBuffer<JCTree> defs = new ListBuffer<JCTree>();
        
        t.visit(new SourceDeclarationVisitor(){
            @Override
            public void loadFromSource(Declaration decl) {
                if(!checkNative(decl))
                    return;
                long flags = decl instanceof Tree.AnyInterface ? Flags.INTERFACE : 0;
                String name = Naming.toplevelClassName("", decl);
                
                defs.add(makeClassDef(decl, flags, name, WantedDeclaration.Normal));
                if(decl instanceof Tree.AnyInterface){
                    String implName = Naming.getImplClassName(name);
                    defs.add(makeClassDef(decl, 0, implName, WantedDeclaration.Normal));
                }
                // only do it for Bootstrap where we control the annotations, because it's so dodgy ATM
                if(options.get(OptionName.BOOTSTRAPCEYLON) != null
                        && decl instanceof Tree.AnyClass
                        && TreeUtil.hasAnnotation(decl.getAnnotationList(), "annotation", decl.getUnit())){
                    String annotationName = Naming.suffixName(Suffix.$annotation$, name);
                    defs.add(makeClassDef(decl, Flags.ANNOTATION, annotationName, WantedDeclaration.Annotation));
                    
                    for(Tree.StaticType sat : ((Tree.AnyClass)decl).getSatisfiedTypes().getTypes()){
                        if(sat instanceof Tree.BaseType 
                                && ((Tree.BaseType) sat).getIdentifier().getText().equals("SequencedAnnotation")){
                            String annotationsName = Naming.suffixName(Suffix.$annotations$, name);
                            defs.add(makeClassDef(decl, Flags.ANNOTATION, annotationsName, WantedDeclaration.AnnotationSequence));
                        }
                    }
                }
            }

            private JCTree makeClassDef(Declaration decl, long flags, String name, WantedDeclaration wantedDeclaration) {
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

                return make().ClassDef(make().Modifiers(flags | Flags.PUBLIC), names().fromString(name), 
                        typarams.toList(), null, List.<JCExpression>nil(), makeClassBody(decl, wantedDeclaration));
            }

            private List<JCTree> makeClassBody(Declaration decl, WantedDeclaration wantedDeclaration) {
                // only do it for Bootstrap where we control the annotations, because it's so dodgy ATM
                if(wantedDeclaration == WantedDeclaration.Annotation){
                    ListBuffer<JCTree> body = new ListBuffer<JCTree>();
                    for(Tree.Parameter param : ((Tree.ClassDefinition)decl).getParameterList().getParameters()){
                        String name;
                        
                        JCExpression type = make().TypeArray(make().Type(syms().stringType));
                        if(param instanceof Tree.InitializerParameter)
                            name = ((Tree.InitializerParameter)param).getIdentifier().getText();
                        else if(param instanceof Tree.ParameterDeclaration){
                            Tree.TypedDeclaration typedDeclaration = ((Tree.ParameterDeclaration)param).getTypedDeclaration();
                            name = typedDeclaration.getIdentifier().getText();
                            type = getAnnotationTypeFor(typedDeclaration.getType());
                        }else
                            name = "ERROR";
                        JCMethodDecl method
                            = make().MethodDef(make().Modifiers(Flags.PUBLIC), names().fromString(name), 
                                type,
                                List.<JCTypeParameter>nil(), 
                                List.<JCVariableDecl>nil(),
                                List.<JCExpression>nil(), 
                                null, 
                                null);
                        body.append(method);
                    }
                    return body.toList();
                }
                if(wantedDeclaration == WantedDeclaration.AnnotationSequence){
                    String name = Naming.toplevelClassName("", decl);
                    String annotationName = Naming.suffixName(Suffix.$annotation$, name);
                    JCExpression type = make().TypeArray(make().Ident(names().fromString(annotationName)));
                    JCMethodDecl method
                            = make().MethodDef(make().Modifiers(Flags.PUBLIC), names().fromString("value"), 
                                type,
                                List.<JCTypeParameter>nil(), 
                                List.<JCVariableDecl>nil(),
                                List.<JCExpression>nil(), 
                                null, 
                                null);
                    return List.<JCTree>of(method);
                }
                return List.<JCTree>nil();
            }

            private JCExpression getAnnotationTypeFor(Tree.Type type) {
                if(type instanceof Tree.BaseType){
                    String name = ((Tree.BaseType) type).getIdentifier().getText();
                    if(name.equals("String") || name.equals("Declaration"))
                        return make().Type(syms().stringType);
                    if(name.equals("Boolean"))
                        return make().Type(syms().booleanType);
                    if(name.equals("Integer"))
                        return make().Type(syms().longType);
                    if(name.equals("Float"))
                        return make().Type(syms().doubleType);
                    if(name.equals("Byte"))
                        return make().Type(syms().byteType);
                    if(name.equals("Character"))
                        return make().Type(syms().charType);
                    if(name.equals("Declaration")
                            || name.equals("ClassDeclaration")
                            || name.equals("InterfaceDeclaration")
                            || name.equals("ClassOrInterfaceDeclaration"))
                        return make().Type(syms().stringType);
                }
                if(type instanceof Tree.SequencedType){
                    return make().TypeArray(getAnnotationTypeFor(((Tree.SequencedType) type).getType()));
                }
                if(type instanceof Tree.SequenceType){
                    return make().TypeArray(getAnnotationTypeFor(((Tree.SequenceType) type).getElementType()));
                }
                if(type instanceof Tree.IterableType){
                    return make().TypeArray(getAnnotationTypeFor(((Tree.IterableType) type).getElementType()));
                }
                if(type instanceof Tree.TupleType){
                    // can only be one, must be a SequencedType
                    Tree.Type sequencedType = ((Tree.TupleType) type).getElementTypes().get(0);
                    return getAnnotationTypeFor(sequencedType);
                }
                System.err.println("Unknown Annotation type: "+type);
                return make().TypeArray(make().Type(syms().stringType));
            }

            @Override
            public void loadFromSource(ModuleDescriptor that) {
                // don't think we care about these
            }

            @Override
            public void loadFromSource(PackageDescriptor that) {
                // don't think we care about these
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
                decl, block, expression, decl, setterDecl);
    }

    public List<JCTree> transformAttribute(
            TypedDeclaration declarationModel,
            String attrName, String attrClassName,
            final Tree.Declaration annotated,
            final Tree.Block block,
            final Tree.SpecifierOrInitializerExpression expression, 
            final Tree.TypedDeclaration decl,
            final Tree.AttributeSetterDefinition setterDecl) {
        
        // For everything else generate a getter/setter method
        AttributeDefinitionBuilder builder = AttributeDefinitionBuilder
            .wrapped(this, attrClassName, null, attrName, declarationModel, declarationModel.isToplevel())
            .is(Flags.PUBLIC, declarationModel.isShared());
        
        final JCExpression initialValue;
        final HasErrorException expressionError;
        if (expression != null) {
            expressionError = errors().getFirstExpressionErrorAndMarkBrokenness(expression.getExpression());
            if (expressionError != null) {
                initialValue = make().Erroneous();
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
                return List.<JCTree>of(this.makeThrowUnresolvedCompilationError(expressionError));
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
        
        // Set the local declarations annotation
        if(decl != null){
            List<JCAnnotation> scopeAnnotations;
            if(Decl.isToplevel(declarationModel) && setterDecl != null){
                scopeAnnotations = makeAtLocalDeclarations(decl, setterDecl);
            }else{
                scopeAnnotations = makeAtLocalDeclarations(decl);
            }
            builder.classAnnotations(scopeAnnotations);
        }else if(block != null){
            List<JCAnnotation> scopeAnnotations = makeAtLocalDeclarations(block);
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
                || (declarationModel instanceof FunctionOrValue 
                    && ((FunctionOrValue)declarationModel).isParameter())) {
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
                boolean prevSyntheticClassBody;
                if (Decl.isLocal(declarationModel)) {
                    prevSyntheticClassBody = expressionGen().withinSyntheticClassBody(true);
                } else {
                    prevSyntheticClassBody = expressionGen().isWithinSyntheticClassBody();
                }
                JCBlock getterBlock = makeGetterBlock(declarationModel, block, expression);
                prevSyntheticClassBody = expressionGen().withinSyntheticClassBody(prevSyntheticClassBody);
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
                        //builder.userAnnotationsSetter(expressionGen().transformAnnotations(true, OutputElement.METHOD, setterDecl));
                        builder.userAnnotationsSetter(expressionGen().transformAnnotations(true, OutputElement.SETTER, setterDecl));
                    } else {
                        builder.immutable();
                    }
                }
            }
        }
        
        if (annotated != null) {
            builder.userAnnotations(expressionGen().transformAnnotations(true, OutputElement.GETTER, annotated));
        }
        
        if (Decl.isLocal(declarationModel)) {
            if (expressionError != null) {
                return List.<JCTree>of(this.makeThrowUnresolvedCompilationError(expressionError));
            }
            builder.classAnnotations(makeAtLocalDeclaration(declarationModel.getQualifier(), false));
            if(initialValue != null)
                builder.valueConstructor();
            JCExpression typeExpr;
            if (declarationModel instanceof Setter 
                    || (declarationModel instanceof FunctionOrValue
                        && ((FunctionOrValue)declarationModel).isParameter())) {
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
        at(null);
        
        ClassDefinitionBuilder builder = ClassDefinitionBuilder
                .klass(this, Naming.MODULE_DESCRIPTOR_CLASS_NAME, null, false);
        builder.modifiers(Flags.FINAL)
                .annotations(makeAtModule(module));
        builder.getInitBuilder().modifiers(Flags.PRIVATE);
        builder.annotations(expressionGen().transformAnnotations(true, OutputElement.TYPE, module));
        for (Tree.ImportModule imported : module.getImportModuleList().getImportModules()) {
            if (!isForBackend(imported.getAnnotationList(), Backend.Java, imported.getUnit())) {
                continue;
            }
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
                throw new BugException(imported, "unhandled module import");
            }
            List<JCAnnotation> importAnnotations = expressionGen().transformAnnotations(true, OutputElement.FIELD, imported);
            JCModifiers mods = make().Modifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL, importAnnotations);
            Name fieldName = names().fromString(quotedName);
            builder.defs(List.<JCTree>of(make().VarDef(mods, fieldName, make().Type(syms().stringType), makeNull())));
        }
        return builder.build();
    }

    public List<JCTree> transformPackageDescriptor(Tree.PackageDescriptor pack) {
        at(null);
        ClassDefinitionBuilder builder = ClassDefinitionBuilder
                .klass(this, Naming.PACKAGE_DESCRIPTOR_CLASS_NAME, null, false)
                .modifiers(Flags.FINAL)
                .annotations(makeAtPackage(pack.getUnit().getPackage()));
        builder.getInitBuilder().modifiers(Flags.PRIVATE);
        builder.annotations(expressionGen().transformAnnotations(true, OutputElement.TYPE, pack));
        return builder.build();
    }
}
