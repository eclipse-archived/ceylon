package com.redhat.ceylon.compiler.codegen;

import static com.sun.tools.javac.code.Flags.FINAL;

import java.util.Iterator;

import javax.tools.JavaFileObject;

import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AnyAttribute;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeGetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeSetterDefinition;
import com.redhat.ceylon.compiler.util.Util;
import com.sun.tools.javac.main.OptionName;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCImport;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Convert;
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

    /**
     * In this pass we only make an empty placeholder which we'll fill in the
     * EnterCeylon phase later on
     */
    public JCCompilationUnit makeJCCompilationUnitPlaceholder(Tree.CompilationUnit t, JavaFileObject file, String pkgName, PhasedUnit phasedUnit) {
        if(options.get(OptionName.VERBOSE) != null)
            System.err.println(t);
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

    private JCExpression makeIdentFromIdentifiers(Iterable<Tree.Identifier> components) {

        JCExpression type = null;
        for (Tree.Identifier component : components) {
            if (type == null)
                type = make().Ident(names().fromString(component.getText()));
            else
                type = makeSelect(type, component.getText());
        }

        return type;
    }

    // FIXME: port handleOverloadedToplevelClasses when I figure out what it
    // does

    private JCExpression getPackage(String fullname) {
        String shortName = Convert.shortName(fullname);
        String packagePart = Convert.packagePart(fullname);
        if (packagePart == null || packagePart.length() == 0)
            return make().Ident(names().fromString(shortName));
        else
            return make().Select(getPackage(packagePart), names().fromString(shortName));
    }

    public JCImport transform(Tree.ImportPath that) {
        return at(that).Import(makeIdentFromIdentifiers(that.getIdentifiers()), false);
    }
    
    public List<JCTree> transform(AnyAttribute decl) {
        return transformAttribute(decl, null);
    }

    public List<JCTree> transform(Tree.AttributeSetterDefinition decl) {
        return transformAttribute(decl, null);
    }
    
    public List<JCTree> transformAttribute(Tree.TypedDeclaration decl, AttributeSetterDefinition setterDecl) {
        at(decl);
        String attrName = decl.getIdentifier().getText();
        String attrClassName = attrName;
        if (isInner(decl)) {
            if (decl instanceof AttributeGetterDefinition) {
                attrClassName += "$getter";
            } else if (decl instanceof AttributeSetterDefinition) {
                attrClassName += "$setter";
            }
        }

        TypedDeclaration declarationModel = decl.getDeclarationModel();
        AttributeDefinitionBuilder builder = globalGen()
            .defineGlobal(makeJavaType(actualType(decl)), attrName)
            .className(attrClassName)
            .classAnnotations(makeAtAttribute())
            .valueAnnotations(makeJavaTypeAnnotations(declarationModel, actualType(decl)))
            .classIsFinal(true);

        // if it's a module add a special annotation
        if(declarationModel.isToplevel() 
        		&& attrName.equals("module")
        		&& declarationModel.getUnit().getFilename().equals("module.ceylon")){
            Package pkg = (Package) declarationModel.getContainer();
            Module module = pkg.getModule();
            builder.classAnnotations(makeAtModule(module));
        }

        if (declarationModel.isShared()) {
            builder
                .classIsPublic(true)
                .getterIsPublic(true)
                .setterIsPublic(true);
        }
        
        if (decl instanceof AttributeSetterDefinition) {
            // For inner setters
            AttributeSetterDefinition sdef = (AttributeSetterDefinition)decl;
            JCBlock setterBlock = make().Block(0, statementGen().transformStmts(sdef.getBlock().getStatements()));
            builder.setterBlock(setterBlock);
            builder.skipGetter();
        } else {
            if (decl instanceof AttributeDeclaration) {
                // For inner and toplevel value attributes
                if (!declarationModel.isVariable()) {
                    builder.immutable();
                }
                AttributeDeclaration adecl = (AttributeDeclaration)decl;
                if (adecl.getSpecifierOrInitializerExpression() != null) {
                    builder.initialValue(expressionGen().transformExpression(
                            adecl.getSpecifierOrInitializerExpression().getExpression(), Util.getBoxingStrategy(declarationModel)));
                }
            } else {
                // For inner and toplevel getters
                AttributeGetterDefinition gdef = (AttributeGetterDefinition)decl;
                JCBlock getterBlock = make().Block(0, statementGen().transformStmts(gdef.getBlock().getStatements()));
                builder.getterBlock(getterBlock);
                
                if (isInner(decl)) {
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
        
        boolean isMethodLocal = declarationModel.getContainer() instanceof com.redhat.ceylon.compiler.typechecker.model.Method;
        if (isMethodLocal) {
            // Add a "foo foo = new foo();" at the decl site
            JCTree.JCIdent name = make().Ident(names().fromString(attrClassName));
            
            JCExpression initValue = at(decl).NewClass(null, null, name, List.<JCTree.JCExpression>nil(), null);
            List<JCAnnotation> annots2 = List.<JCAnnotation>nil();
    
            int modifiers = declarationModel.isShared() ? 0 : FINAL;
            JCTree.JCVariableDecl var = at(decl).VarDef(at(decl)
                    .Modifiers(modifiers, annots2), 
                    names().fromString(attrClassName), 
                    name, 
                    initValue);
            
            return builder.build().append(var);
        } else {
            builder
                .getterIsStatic(true)
                .setterIsStatic(true);
            return builder.build();
        }
    }
}
