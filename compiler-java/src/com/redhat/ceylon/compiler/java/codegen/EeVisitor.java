package com.redhat.ceylon.compiler.java.codegen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Annotation;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.langtools.tools.javac.main.Option;
import com.redhat.ceylon.langtools.tools.javac.util.Options;
import com.redhat.ceylon.model.loader.model.AnnotationProxyMethod;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
/**
 * Figures out which declarations should be compiled using 
 * {@link AbstractTransformer#isEe() EE mode}
 */
public class EeVisitor extends Visitor {

    private final boolean eeOption;
    private final Set<String> imports;
    private final Set<String> annotations;
    
    private static final int EE = 1<<0; 
    private static final int TRANSIENT = 1<<1;
    private static final int VOLATILE = 1<<2;
    private static final int SYNCHRONIZED = 1<<3;
    private static final int STRICTFP = 1<<4;
    
    private Map<Declaration,Integer> eeModeDecls = new HashMap<Declaration,Integer>();
    private Set<Package> eeModePackages = new HashSet<Package>();
    private Set<Module> eeModeModules = new HashSet<Module>();

    public EeVisitor(Options options) {
        this.eeOption = options.get(Option.CEYLONEE) != null;
        if (options.getMulti(Option.CEYLONEEIMPORTS) != null) {
            this.imports = new HashSet<String>(options.getMulti(Option.CEYLONEEIMPORTS));
        } else {
            // If you're updating this also update 
            // https://ceylon-lang.org/documentation/1.3/reference/interoperability/ee-mode/#activating_ee_mode
            imports = new HashSet<String>();
            imports.add("javax.javaeeapi");
            imports.add("maven:\"javax:javaee-api\"");
        }
        if (options.getMulti(Option.CEYLONEEANNOTATIONS) != null) {
            this.annotations = new HashSet<String>(options.getMulti(Option.CEYLONEEANNOTATIONS));
        } else {
            // If you're updating this also update 
            // https://ceylon-lang.org/documentation/1.3/reference/interoperability/ee-mode/#activating_ee_mode
            annotations = new HashSet<String>();
            annotations.add("javax.xml.bind.annotation.XmlAccessorType");
            annotations.add("javax.persistence.Entity");
            annotations.add("javax.persistence.Embeddable");
            annotations.add("javax.inject.Inject");
            annotations.add("javax.ejb.Stateless");
            annotations.add("javax.ejb.Stateful");
            annotations.add("javax.ejb.MessageDriven");
            annotations.add("javax.ejb.Singleton");
        }
    }

    private boolean containsEeAnnotation(List<Annotation> annotations2) {
        for (Tree.Annotation a : annotations2) {
            Tree.Primary prim = a.getPrimary();
            if (prim instanceof Tree.BaseMemberOrTypeExpression) {
                Declaration annoCtor = ((Tree.BaseMemberOrTypeExpression)prim).getDeclaration();
                if (annoCtor instanceof AnnotationProxyMethod) {
                    String annotationTypeName = ((AnnotationProxyMethod)annoCtor).proxyClass.iface.getQualifiedNameString();
                    if (annotations.contains(annotationTypeName.replace("::", "."))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public void visit(Tree.Declaration that) {
        super.visit(that);
        Declaration a = that.getDeclarationModel();
        if (that.getAnnotationList() != null && that.getAnnotationList().getAnnotations() != null) {
            for (Tree.Annotation an : that.getAnnotationList().getAnnotations()) {
                Declaration ad = ((Tree.BaseMemberOrTypeExpression)an.getPrimary()).getDeclaration();
                if (ad != null) {
                    String qualifiedName = ad.getQualifiedNameString();
                    if ("java.lang::transient".equals(qualifiedName)) {
                        setModifier(a, TRANSIENT);
                    }
                    if ("java.lang::volatile".equals(qualifiedName)) {
                        setModifier(a, VOLATILE);
                    }
                    if ("java.lang::synchronized".equals(qualifiedName)) {
                        setModifier(a, SYNCHRONIZED);
                    }
                    if ("java.lang::strictfp".equals(qualifiedName)) {
                        setModifier(a, STRICTFP);
                    }
                    // note: native is handledby the typechecker
                }
            }
        }
    }

    private void setModifier(Declaration decl, int flag) {
        Integer mods = eeModeDecls.get(decl);
        int m;
        if (mods == null) {
            m = 0;
        } else {
            m = mods;
        }
        m |= flag;
        eeModeDecls.put(decl, m);
    }
    
    @Override
    public void visit(Tree.ClassDefinition that) {
        super.visit(that);
        if (containsEeAnnotation(that.getAnnotationList().getAnnotations())) {
            setModifier(that.getDeclarationModel(), EE);
        }
    }
    
    @Override
    public void visit(Tree.Constructor that) {
        super.visit(that);
        if (containsEeAnnotation(that.getAnnotationList().getAnnotations())) {
            setModifier(ModelUtil.getConstructedClass(that.getDeclarationModel()), EE);
        }
    }
    
    @Override
    public void visit(Tree.AnyAttribute that) {
        super.visit(that);
        if (containsEeAnnotation(that.getAnnotationList().getAnnotations())) {
            TypedDeclaration attribute = that.getDeclarationModel();
            if (attribute.isMember()) {
                setModifier(ModelUtil.getClassOrInterfaceContainer(attribute), EE);
            } else if (attribute.isToplevel()) {
                setModifier(attribute, EE);
            }
        }
    }
    
    @Override
    public void visit(Tree.AnyMethod that) {
        super.visit(that);
        if (containsEeAnnotation(that.getAnnotationList().getAnnotations())) {
            Function method = that.getDeclarationModel();
            if (method.isMember()) {
                setModifier(ModelUtil.getClassOrInterfaceContainer(method), EE);
            } else if (method.isToplevel()) {
                setModifier(method, EE);
            }
        }
    }
    
    @Override
    public void visit(Tree.PackageDescriptor that) {
        super.visit(that);
        if (containsEeAnnotation(that.getAnnotationList().getAnnotations())) {
            eeModePackages.add((Package)that.getScope());
        }
    }
    
    @Override
    public void visit(Tree.ModuleDescriptor that) {
        super.visit(that);
        that.getImportModuleList();
    }
    
    @Override
    public void visit(Tree.ImportModule that) {
        super.visit(that);
        if (that.getName() != null) {
            String name = that.getName();
            if (that.getNamespace() != null) {
                name = that.getNamespace().getText() +":" + name + "";
            }
            if (imports.contains(name)) {
                eeModeModules.add(that.getUnit().getPackage().getModule());
            }
        }
    }
    
    @Override
    public void visit(Tree.CompilationUnit that) {
        
        if (!(eeOption// we its enabled globally then don't bother visiting
                || annotations.isEmpty() && imports.isEmpty())) {
            super.visit(that);
        }
    }
    
    public boolean isEeMode(Declaration d) {
        d = Decl.getToplevelDeclarationContainer(d);
        if (eeOption
                || eeModeModules.contains(ModelUtil.getModule(d))
                || eeModePackages.contains(ModelUtil.getPackage(d))) {
            return true;
        }
        Integer mods = eeModeDecls.get(d);
        return mods != null && (mods & EE) != 0;
    }
    
    @Override
    public String toString() {
        return getClass().getName()  + "( imports: " + imports +", annotations: " + annotations + ")";
    }

    public boolean isJavaTransient(Declaration d) {
        Integer mods = eeModeDecls.get(d);
        return mods != null && (mods & TRANSIENT) != 0;
    }
    
    public boolean isJavaVolatile(Declaration d) {
        Integer mods = eeModeDecls.get(d);
        return mods != null && (mods & VOLATILE) != 0;
    }
    
    public boolean isJavaSynchronized(Declaration d) {
        Integer mods = eeModeDecls.get(d);
        return mods != null && (mods & SYNCHRONIZED) != 0;
    }
    
    public boolean isJavaStrictfp(Declaration d) {
        Integer mods = eeModeDecls.get(d);
        return mods != null && (mods & STRICTFP) != 0;
    }
}
