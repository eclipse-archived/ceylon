package com.redhat.ceylon.compiler.typechecker.analyzer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.ExternalUnit;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class DependedUponVisitor extends Visitor {
    
    private final PhasedUnit phasedUnit;
    private final PhasedUnits phasedUnits;
    private final List<PhasedUnits> phasedUnitsOfDependencies;
    private Set<Declaration> alreadyDone;
    
    public DependedUponVisitor(PhasedUnit phasedUnit, PhasedUnits phasedUnits, List<PhasedUnits> phasedUnitsOfDependencies) {
        this.phasedUnit = phasedUnit;
        this.phasedUnits = phasedUnits;
        this.phasedUnitsOfDependencies = phasedUnitsOfDependencies;
        alreadyDone = new HashSet<Declaration>();
    }
    
    private String getSrcFolderRelativePath(Unit u) {
        return u.getPackage().getQualifiedNameString().replace('.', '/') + 
                "/" + u.getFilename();
    }

    private void storeDependency(Declaration d) {
        if (d!=null && (d instanceof UnionType || 
                        d instanceof IntersectionType || 
                        !alreadyDone.contains(d))) {
            if (!(d instanceof UnionType || 
                        d instanceof IntersectionType)) {
                alreadyDone.add(d);
            }
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
            Unit currentUnit = phasedUnit.getUnit();
            if (declarationUnit != null) {
                String currentUnitName = getSrcFolderRelativePath(currentUnit);
                String dependedOnUnitName = getSrcFolderRelativePath(declarationUnit);
                if (! dependedOnUnitName.equals(currentUnitName)) {
                    if (declarationUnit instanceof ExternalUnit) {
                        ((ExternalUnit) declarationUnit).getDependentsOf().add(phasedUnit);
                    } else {
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
