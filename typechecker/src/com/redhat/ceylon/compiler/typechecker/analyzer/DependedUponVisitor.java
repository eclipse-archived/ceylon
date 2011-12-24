package com.redhat.ceylon.compiler.typechecker.analyzer;

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class DependedUponVisitor extends Visitor {
    
    private final PhasedUnit phasedUnit;
    private final PhasedUnits phasedUnits;
    private final List<PhasedUnits> phasedUnitsOfDependencies;
    
    public DependedUponVisitor(PhasedUnit phasedUnit, PhasedUnits phasedUnits, List<PhasedUnits> phasedUnitsOfDependencies) {
        this.phasedUnit = phasedUnit;
        this.phasedUnits = phasedUnits;
        this.phasedUnitsOfDependencies = phasedUnitsOfDependencies;
    }
    
    private String getSrcFolderRelativePath(Unit u) {
        return u.getPackage().getQualifiedNameString().replace('.', '/') + 
                "/" + u.getFilename();
    }

    private void storeDependency(Declaration d) {
        if (d!=null) {
            if (d instanceof TypeDeclaration) {
                TypeDeclaration td = (TypeDeclaration) d;
                storeDependency(td.getExtendedTypeDeclaration());
                for (TypeDeclaration st: td.getSatisfiedTypeDeclarations()) {
                    storeDependency(st);
                }
                List<TypeDeclaration> caseTypes = td.getCaseTypeDeclarations();
                if (caseTypes!=null) {
                    for (TypeDeclaration ct: caseTypes) {
                        storeDependency(ct);
                    }
                }
            }
            if (d instanceof TypedDeclaration) {
                //TODO: is this really necessary?
                storeDependency(((TypedDeclaration) d).getTypeDeclaration());
            }
            Declaration rd = d.getRefinedDeclaration();
            if (rd!=d) {
                storeDependency(rd); //this one is needed for default arguments, I think
            }
            Unit declarationUnit = d.getUnit();
            if (declarationUnit != null) {
                String currentUnitName = getSrcFolderRelativePath(phasedUnit.getUnit());
                String dependedOnUnitName = getSrcFolderRelativePath(d.getUnit());
                if (! dependedOnUnitName.equals(currentUnitName)) {
                    PhasedUnit dependedOnPhasedUnit = phasedUnits.getPhasedUnitFromRelativePath(dependedOnUnitName);
                    if (dependedOnPhasedUnit != null) {
                        dependedOnPhasedUnit.getDependentsOf().add(phasedUnit);
                    } else {
                        for (PhasedUnits phasedUnitsOfDependency : phasedUnitsOfDependencies) {
                            dependedOnPhasedUnit = phasedUnitsOfDependency.getPhasedUnitFromRelativePath(dependedOnUnitName);
                            if (dependedOnPhasedUnit != null) {
                                dependedOnPhasedUnit.getDependentsOf().add(phasedUnit);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.MemberOrTypeExpression that) {
        storeDependency(that.getDeclaration());
        super.visit(that);
    }
        
    @Override
    public void visit(Tree.NamedArgument that) {
        //TODO: is this really necessary?
        storeDependency(that.getParameter());
        super.visit(that);
    }
        
    @Override
    public void visit(Tree.SequencedArgument that) {
        //TODO: is this really necessary?
        storeDependency(that.getParameter());
        super.visit(that);
    }
        
    @Override
    public void visit(Tree.PositionalArgument that) {
        //TODO: is this really necessary?
        storeDependency(that.getParameter());
        super.visit(that);
    }
        
    @Override
    public void visit(Tree.Type that) {
        ProducedType tm = that.getTypeModel();
        if (tm!=null) {
            storeDependency(tm.getDeclaration());
        }
        super.visit(that);
    }
        
    @Override
    public void visit(Tree.ImportMemberOrType that) {
        storeDependency(that.getDeclarationModel());
        super.visit(that);
    }
        
    @Override
    public void visit(Tree.TypeArguments that) {
        //TODO: is this really necessary?
        List<ProducedType> tms = that.getTypeModels();
        if (tms!=null) {
            for (ProducedType pt: tms) {
                storeDependency(pt.getDeclaration());
            }
        }
        super.visit(that);
    }
        
    @Override
    public void visit(Tree.Term that) {
        //TODO: is this really necessary?
        ProducedType tm = that.getTypeModel();
        if (tm!=null) {
            storeDependency(tm.getDeclaration());
        }
        super.visit(that);
    }
    
}
