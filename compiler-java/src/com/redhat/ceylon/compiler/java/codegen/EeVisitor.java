package com.redhat.ceylon.compiler.java.codegen;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Annotation;
import com.redhat.ceylon.langtools.tools.javac.main.Option;
import com.redhat.ceylon.langtools.tools.javac.util.Options;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.loader.model.AnnotationProxyMethod;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
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
    
    private Set<Declaration> eeModeDecls = new HashSet<Declaration>();
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
    public void visit(Tree.ClassDefinition that) {
        super.visit(that);
        if (containsEeAnnotation(that.getAnnotationList().getAnnotations())) {
            eeModeDecls.add(that.getDeclarationModel());
        }
    }
    
    @Override
    public void visit(Tree.Constructor that) {
        super.visit(that);
        if (containsEeAnnotation(that.getAnnotationList().getAnnotations())) {
            eeModeDecls.add(Decl.getConstructedClass(that.getDeclarationModel()));
        }
    }
    
    @Override
    public void visit(Tree.AnyAttribute that) {
        super.visit(that);
        if (containsEeAnnotation(that.getAnnotationList().getAnnotations())) {
            TypedDeclaration attribute = that.getDeclarationModel();
            if (attribute.isMember()) {
                eeModeDecls.add(Decl.getClassOrInterfaceContainer(attribute));
            } else if (attribute.isToplevel()) {
                eeModeDecls.add(attribute);
            }
        }
    }
    
    @Override
    public void visit(Tree.AnyMethod that) {
        super.visit(that);
        if (containsEeAnnotation(that.getAnnotationList().getAnnotations())) {
            Function method = that.getDeclarationModel();
            if (method.isMember()) {
                eeModeDecls.add(Decl.getClassOrInterfaceContainer(method));
            } else if (method.isToplevel()) {
                eeModeDecls.add(method);
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
        if (that.getQuotedLiteral() != null) {
            String name = that.getQuotedLiteral().getText();
            if (that.getNamespace() != null) {
                name = that.getNamespace().getText() +":" + name + "";
            }
            if (imports.contains(name)) {
                eeModeModules.add(that.getUnit().getPackage().getModule());
            }
        }
        
        if (that.getImportPath() != null) {
            StringBuffer sb = new StringBuffer();
            boolean sebsequent = false;
            for (Tree.Identifier x : that.getImportPath().getIdentifiers()) {
                if (sebsequent) {
                    sb.append(".");
                }
                sebsequent = true;
                sb.append(x.getText());
            }
            if (imports.contains(sb.toString())) {
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
        return eeOption
                || eeModeModules.contains(Decl.getModule(d))
                || eeModePackages.contains(Decl.getPackage(d))
                || eeModeDecls.contains(d);
    }
    
    @Override
    public String toString() {
        return getClass().getName()  + "( imports: " + imports +", annotations: " + annotations + ")";
    }
}
