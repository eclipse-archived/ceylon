package com.redhat.ceylon.compiler.analyzer;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.context.Context;
import com.redhat.ceylon.compiler.model.Class;
import com.redhat.ceylon.compiler.model.ClassOrInterface;
import com.redhat.ceylon.compiler.model.Declaration;
import com.redhat.ceylon.compiler.model.Import;
import com.redhat.ceylon.compiler.model.Interface;
import com.redhat.ceylon.compiler.model.Module;
import com.redhat.ceylon.compiler.model.Package;
import com.redhat.ceylon.compiler.model.ProducedType;
import com.redhat.ceylon.compiler.model.TypeDeclaration;
import com.redhat.ceylon.compiler.model.TypedDeclaration;
import com.redhat.ceylon.compiler.model.Unit;
import com.redhat.ceylon.compiler.model.Value;
import com.redhat.ceylon.compiler.tree.Node;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Tree.TypeArgumentList;
import com.redhat.ceylon.compiler.tree.Visitor;
import com.redhat.ceylon.compiler.util.PrintUtil;

/**
 * Second phase of type analysis.
 * Scan the compilation unit looking for literal type 
 * declarations and maps them to the associated model 
 * objects. Also builds up a list of imports for the 
 * compilation unit. Finally, assigns types to the 
 * associated model objects of declarations declared 
 * using an explicit type (this must be done in
 * this phase, since shared declarations may be used
 * out of order in expressions).
 * 
 * @author Gavin King
 *
 */
public class TypeVisitor extends Visitor {
    
    private Unit unit;
    private Package importPackage;
    private Context context;

    public TypeVisitor(Unit u, Context context) {
        unit = u;
        this.context = context;
    }
    
    @Override
    public void visit(Tree.ImportPath that) {
        Module m = unit.getPackage().getModule();
        for (Package mp: m.getAllPackages()) {
            if ( hasName(that.getIdentifiers(), mp) ) {
                importPackage = mp;
                return;
            }
        }
        that.addError("Package not found: " + 
                PrintUtil.importNodeToString(that.getIdentifiers()) );
    }

    private boolean hasName(List<Tree.Identifier> importPath, Package mp) {
        if (mp.getName().size()==importPath.size()) {
            for (int i=0; i<mp.getName().size(); i++) {
                if (!mp.getName().get(i).equals(importPath.get(i).getText())) {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }
    
    @Override
    public void visit(Tree.ImportMemberOrType that) {
        Import i = new Import();
        Tree.Alias alias = that.getAlias();
        if (alias==null) {
            i.setAlias(that.getIdentifier().getText());
        }
        else {
            i.setAlias(alias.getIdentifier().getText());
        }
        Declaration d = Util.getDeclaration(importPackage, that, context);
        if (d==null) {
            that.addError("imported declaration not found: " + 
                    that.getIdentifier().getText());
        }
        else {
            i.setDeclaration(d);
            unit.getImports().add(i);
        }
    }
        
    @Override 
    public void visit(Tree.Type that) {
        TypeDeclaration d = Util.getDeclaration(that, context);
        if (d==null) {
            that.addError("type declaration not found: " + 
                    that.getIdentifier().getText());
        }
        else {
            super.visit(that);
            List<ProducedType> typeArguments = getTypeArguments(that);
            if (typeArguments!=null) {
                if (!com.redhat.ceylon.compiler.model.Util.acceptsArguments(d, typeArguments)) {
                    that.addError("does not accept the given type arguments");
                }
                else {
                    ProducedType pt = d.getProducedType(typeArguments);
                    if (pt==null) {
                        that.addError("incompatible type arguments");
                    }
                    else {
                        that.setTypeModel(pt);
                        that.setMemberReference(pt);
                        if (typeArguments!=null) {
                            typeArguments.add(pt);
                        }
                    }
                }
            }
        }
    }

    private List<ProducedType> getTypeArguments(Tree.Type that) {
        List<ProducedType> typeArguments = new ArrayList<ProducedType>();
        TypeArgumentList tal = that.getTypeArgumentList();
        if (tal!=null) {
            for (Tree.TypeOrSubtype ta: tal.getTypeOrSubtypes()) {
                ProducedType t = ta.getTypeModel();
                if (t==null) {
                    ta.addError("could not resolve type argument");
                    return null;
                }
                else {
                    typeArguments.add(t);
                }
            }
        }
        return typeArguments;
    }
    
    @Override 
    public void visit(Tree.VoidModifier that) {
        Class vd = (Class) Util.getLanguageModuleDeclaration("Void", context);
        that.setTypeModel(vd.getType());
    }
    
    @Override 
    public void visit(Tree.TypedDeclaration that) {
        super.visit(that);
        setType(that, Util.name(that), that.getTypeOrSubtype());
    }

    @Override 
    public void visit(Tree.TypedArgument that) {
        super.visit(that);
        setType(that, Util.name(that), that.getTypeOrSubtype());
    }
        
    private void setType(Node that, String name, Tree.TypeOrSubtype type) {
        if (type==null) {
            that.addError("missing type of declaration: " + name);
        }
        else if (!(type instanceof Tree.LocalModifier)) { //if the type declaration is missing, we do type inference later
            ProducedType t = type.getTypeModel();
            if (t==null) {
                //TODO: this case is temporary until we
                //      add support for sequenced parameters
            }
            else {
                ( (TypedDeclaration) that.getDeclarationModel() ).setType(t);
            }
        }
    }
    
    private List<ProducedType> getSatisfiedTypes(Tree.SatisfiedTypes st) {
        List<ProducedType> list = new ArrayList<ProducedType>();
        if (st!=null) {
            for (Tree.Type t: st.getTypes()) {
                if (t.getTypeModel()!=null) {
                    list.add(t.getTypeModel());
                }
            }
        }
        return list;
    }
    
    @Override 
    public void visit(Tree.ClassOrInterface that) {
        super.visit(that);
        ClassOrInterface ci = (ClassOrInterface) that.getDeclarationModel();
        if (ci==null) {
            //TODO: this case is temporary until we get aliases
        }
        else {
            Tree.SatisfiedTypes st = that.getSatisfiedTypes();
            if (st!=null) {
                ci.setSatisfiedTypes(getSatisfiedTypes(st));
            }
            Class od = (Class) Util.getLanguageModuleDeclaration("Object", context);
            if (ci instanceof Interface) {
                ci.getSatisfiedTypes().add(od.getType());
            }
        }
        //TODO: interfaces should have Object as a supertype!!
    }

    @Override 
    public void visit(Tree.AnyClass that) {
        super.visit(that);
        Class c = (Class) that.getDeclarationModel();
        if (c==null) {
            //TODO: this case is temporary until we get aliases
        }
        else {
            Tree.ExtendedType et = that.getExtendedType();
            Class vd = (Class) Util.getLanguageModuleDeclaration("Void", context);
            if (c!=vd) {
                if (et==null) {
                    Class iotd = (Class) Util.getLanguageModuleDeclaration("IdentifiableObject", context);
                    c.setExtendedType(iotd.getType());
                }
                else {
                    c.setExtendedType(et.getType().getTypeModel());
                }
            }
        }
    }
    
    @Override 
    public void visit(Tree.ObjectDeclaration that) {
        super.visit(that);
        Tree.ExtendedType et = that.getExtendedType();
        Tree.SatisfiedTypes st = that.getSatisfiedTypes();
        TypeDeclaration td = ( (Value) that.getDeclarationModel() ).getType().getDeclaration();
        if (td!=null) {
            if (et!=null) {
                td.setExtendedType(et.getType().getTypeModel());
            }
            if (st!=null) {
                td.setSatisfiedTypes(getSatisfiedTypes(st));
            }
        }
    }
    
    @Override 
    public void visit(Tree.ObjectArgument that) {
        super.visit(that);
        Tree.ExtendedType et = that.getExtendedType();
        Tree.SatisfiedTypes st = that.getSatisfiedTypes();
        TypeDeclaration td = ( (Value) that.getDeclarationModel() ).getType().getDeclaration();
        if (td!=null) {
            if (et!=null) {
                td.setExtendedType(et.getType().getTypeModel());
            }
            if (st!=null) {
                td.setSatisfiedTypes(getSatisfiedTypes(st));
            }
        }
    }
    
    /**
     * Suppress resolution of types that appear after the
     * member selection operator "." (but not their type
     * arguments).
     */
    @Override
    public void visit(Tree.MemberExpression that) {
        that.getPrimary().visit(this);
        super.visitAny( that.getMemberOrType() );
    }
    
}
