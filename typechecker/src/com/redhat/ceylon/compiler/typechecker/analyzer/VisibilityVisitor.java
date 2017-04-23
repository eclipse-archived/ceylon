package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getParameterTypeErrorNode;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTypeErrorNode;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isCompletelyVisible;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ParameterList;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Element;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.ModuleImport;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeAlias;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;

/**
 * Validates the schema of type declarations to ensure that
 * they do not involve types that are not visible everywhere
 * the declaration is visible. 
 * 
 * @author Gavin King
 *
 */
public class VisibilityVisitor extends Visitor {
    
    private List<Module> modules = new ArrayList<Module>(1);
    
    private String exportHint() {
        StringBuilder hint = new StringBuilder(" (mark module import");
        if (modules.size()>1) {
            hint.append("s");
        }
        hint.append(" 'shared' for ");
        boolean first = true;
        for (Module m: modules) {
            if (first) {
                first = false;
            }
            else {
                hint.append(", ");
            }
            hint.append("'")
                .append(m.getNameAsString())
                .append("'");
        }
        hint.append(")");
        modules.clear();
        return hint.toString();
        
    }
    
    @Override public void visit(Tree.TypedDeclaration that) {
        checkVisibility(that, that.getDeclarationModel());
        super.visit(that);
    }

    @Override public void visit(Tree.TypedArgument that) {
        checkVisibility(that, that.getDeclarationModel());
        super.visit(that);
    }

    @Override
    public void visit(Tree.AnyClass that) {
        super.visit(that);
        checkParameterVisibility(that.getDeclarationModel(), 
                that.getParameterList());
    }

    @Override
    public void visit(Tree.AnyMethod that) {
        super.visit(that);
        checkParameterVisibility(that.getDeclarationModel(), 
                that.getParameterLists());
    }

    @Override
    public void visit(Tree.Constructor that) {
        super.visit(that);
        checkParameterVisibility(that.getDeclarationModel(), 
                that.getParameterList());
    }

    @Override
    public void visit(Tree.MethodArgument that) {
        super.visit(that);
        checkParameterVisibility(that.getDeclarationModel(), 
                that.getParameterLists());
    }

    @Override
    public void visit(Tree.FunctionArgument that) {
        super.visit(that);
        Function m = that.getDeclarationModel();
        checkVisibility(that, m);
        checkParameterVisibility(m, that.getParameterLists());
    }

    private void checkParameterVisibility(Function m,
            List<ParameterList> parameterLists) {
        for (Tree.ParameterList list: parameterLists) {
            checkParameterVisibility(m, list);
        }
    }

    private void checkParameterVisibility(Declaration m, 
            Tree.ParameterList list) {
        if (list!=null) {
            for (Tree.Parameter tp: list.getParameters()) {
                if (tp!=null) {
                    Parameter p = tp.getParameterModel();
                    if (p!=null) {
                        checkParameterVisibility(tp, m, p);
                    }
                }
            }
        }
    }

    @Override public void visit(Tree.TypeDeclaration that) {
        validateSupertypes(that, that.getDeclarationModel());
        super.visit(that);
    }

    @Override public void visit(Tree.ObjectDefinition that) {
        validateSupertypes(that, 
                that.getAnonymousClass());
        super.visit(that);
    }

    @Override public void visit(Tree.ObjectArgument that) {
        validateSupertypes(that, 
                that.getAnonymousClass());
        super.visit(that);
    }

    @Override public void visit(Tree.ObjectExpression that) {
        validateSupertypes(that, 
                that.getAnonymousClass());
        super.visit(that);
    }

    private void validateSupertypes(Node that, TypeDeclaration td) {
        if (td instanceof TypeAlias) {
            Type at = td.getExtendedType();
            if (at!=null) {
                if (!isCompletelyVisible(td, at)) {
                    that.addError("aliased type is not visible everywhere type alias '" 
                            + td.getName() + "' is visible: '" 
                            + at.asString(that.getUnit()) 
                            + "' involves an unshared type declaration", 
                            713);
                }
                if (!checkModuleVisibility(td, at, modules)) {
                    that.addError("aliased type of type alias '" 
                            + td.getName() 
                            + "' that is visible outside this module comes from an imported module that is not re-exported: '" 
                            + at.asString(that.getUnit()) 
                            + "' involves an unexported type declaration"
                            + exportHint(), 
                            714);
                }
            }
        }
        else {
            List<Type> supertypes = td.getType().getSupertypes();
            if (!td.isInconsistentType()) {
                for (Type st: supertypes) {
                    // don't do this check for ObjectArguments
                    if (that instanceof Tree.Declaration 
                            && !st.getDeclaration().equals(td)) {
                        if (!isCompletelyVisible(td, st)) {
                            // temporarily disable error for https://github.com/ceylon/ceylon/issues/5882
                            that.addError("supertype is not visible everywhere type '" 
                                    + td.getName() 
                                    + "' is visible: '" 
                                    + st.asString(that.getUnit()) 
                                    + "' involves an unshared or restricted type declaration", 
                                    713);
                        }
                        if (!checkModuleVisibility(td, st, modules)) {
                            that.addError("supertype of type '" 
                                    + td.getName() 
                                    + "' that is visible outside this module comes from an imported module that is not re-exported: '" 
                                    + st.asString(that.getUnit()) 
                                    + "' involves an unexported type declaration"
                                    + exportHint(), 
                                    714);
                        }
                    }
                }
            }
        }
//        validateMemberRefinement(td, that, unit);
    }


    private static boolean checkModuleVisibility(
            Declaration member, Type pt, 
            List<Module> modules) {
        if (inExportedScope(member)) {
            Module declarationModule = getModule(member);
            if (declarationModule!=null) {
                return isCompletelyVisibleFromOtherModules(
                        member, pt, declarationModule, 
                        modules);
            }
        }
        return true;
    }

    private static boolean inExportedScope(Declaration decl) {
        // if it has a visible scope it's not exported outside the module
        if (decl.getVisibleScope() != null) {
            return false;
        }
        // now perhaps its package is not shared
        Package p = decl.getUnit().getPackage();
        return p != null && p.isShared();
    }

    static boolean isCompletelyVisibleFromOtherModules(
            Declaration member, Type pt, 
            Module thisModule, List<Module> modules) {
        if (pt.isUnion()) {
            for (Type ct: pt.getCaseTypes()) {
                if (!isCompletelyVisibleFromOtherModules(
                        member, ct.substitute(pt), 
                        thisModule, modules)) {
                    return false;
                }
            }
            return true;
        }
        else if (pt.isIntersection()) {
            for (Type st: pt.getSatisfiedTypes()) {
                if (!isCompletelyVisibleFromOtherModules(
                        member, st.substitute(pt), 
                        thisModule, modules)) {
                    return false;
                }
            }
            return true;
        }
        else {
            if (!isVisibleFromOtherModules(member, 
                    thisModule, pt.getDeclaration(),
                    modules)) {
                return false;
            }
            for (Type at: pt.getTypeArgumentList()) {
                if (at!=null && 
                        !isCompletelyVisibleFromOtherModules(
                                member, at, 
                                thisModule, modules)) {
                    return false;
                }
            }
            return true;
        }
    }

    private static String getName(Declaration td) {
        if (td.isAnonymous()) {
            return "anonymous function";
        }
        else {
            return "'" + td.getName() + "'";
        }
    }

    private void checkVisibility(Node that, 
            TypedDeclaration td) {
        Type type = td.getType();
        if (type!=null) {
            Node typeNode = getTypeErrorNode(that);
            if (!isCompletelyVisible(td, type)) {
                typeNode.addError("type of declaration " 
                        + getName(td) 
                        + " is not visible everywhere declaration is visible: '" 
                        + type.asString(that.getUnit()) 
                        + "' involves an unshared type declaration", 
                        711);
            }
            if (!checkModuleVisibility(td, type, modules)) {
                typeNode.addError("type of declaration " 
                        + getName(td) 
                        + " that is visible outside this module comes from an imported module that is not re-exported: '" 
                        + type.asString(that.getUnit()) 
                        + "' involves an unexported type declaration"
                        + exportHint(), 
                        712);
            }
        }
    }

    private void checkParameterVisibility(
            Tree.Parameter tp, Declaration td, Parameter p) {
        if (p.getModel()!=null) {
            Type pt = p.getType();
            if (pt!=null) {
                if (!isCompletelyVisible(td, pt)) {
                    getParameterTypeErrorNode(tp)
                        .addError("type of parameter '" 
                                + p.getName() + "' of " + getName(td) 
                                + " is not visible everywhere declaration is visible: '" 
                                + pt.asString(tp.getUnit()) 
                                + "' involves an unshared type declaration", 
                            710);
                }
                if (!checkModuleVisibility(td, pt, modules)) {
                    getParameterTypeErrorNode(tp)
                        .addError("type of parameter '" 
                                + p.getName() + "' of " + getName(td) 
                                + " that is visible outside this module comes from an imported module that is not re-exported: '" 
                                + pt.asString(tp.getUnit()) 
                                + "' involves an unexported type declaration"
                                + exportHint(), 
                            714);
                }
            }
        }
    }
    
    private static boolean isVisibleFromOtherModules(
            Declaration member, Module thisModule, 
            TypeDeclaration type, List<Module> modules) {
        // type parameters are OK
        if (type instanceof TypeParameter) {
            return true;
        }
        
        Module typeModule = getModule(type);
        if (typeModule!=null && thisModule!=null && 
                !thisModule.equals(typeModule)) {
            // find the module import, but only in exported 
            // imports, otherwise it's an error anyways

            // language module stuff is implicitly exported
            if (typeModule.isLanguageModule()) {
                return true;
            }
            // try to find a direct import first
            for (ModuleImport imp: thisModule.getImports()) {
                if (imp.isExport() && 
                        imp.getModule()
                           .equals(typeModule)) {
                    // found it
                    return true;
                }
            }
            // then try the more expensive implicit imports
            Set<Module> visited = new HashSet<Module>();
            visited.add(thisModule);
            for (ModuleImport imp : thisModule.getImports()) {
                // now try implicit dependencies
                if (imp.isExport() && 
                        includedImplicitly(imp.getModule(), 
                                typeModule, visited)) {
                    // found it
                    return true;
                }
            }
            modules.add(typeModule);
            // couldn't find it
            return false;
        }
        // no module or it does not belong to a module? 
        // more likely an error was already reported
        return true;
    }

    private static boolean includedImplicitly(Module importedModule, 
            Module targetModule, Set<Module> visited) {
        // don't visit them twice
        if (visited.add(importedModule)) {
            for (ModuleImport imp: importedModule.getImports()) {
                // only consider modules it exported back to us
                if (imp.isExport()
                        && (imp.getModule().equals(targetModule)
                        || includedImplicitly(imp.getModule(), 
                                targetModule, visited))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static Module getModule(Element element){
        Package typePackage = element.getUnit().getPackage();
        return typePackage != null ? typePackage.getModule() : null;
    }
    
}
