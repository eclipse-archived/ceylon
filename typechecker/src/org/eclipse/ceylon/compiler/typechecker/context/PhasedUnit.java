/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.typechecker.context;

import static org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil.formatPath;
import static org.eclipse.ceylon.model.typechecker.util.ModuleManager.MODULE_FILE;
import static org.eclipse.ceylon.model.typechecker.util.ModuleManager.PACKAGE_FILE;

import java.lang.ref.WeakReference;
import java.util.EnumSet;
import java.util.List;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.eclipse.ceylon.compiler.typechecker.analyzer.AliasVisitor;
import org.eclipse.ceylon.compiler.typechecker.analyzer.AnnotationVisitor;
import org.eclipse.ceylon.compiler.typechecker.analyzer.ControlFlowVisitor;
import org.eclipse.ceylon.compiler.typechecker.analyzer.DeclarationVisitor;
import org.eclipse.ceylon.compiler.typechecker.analyzer.DefaultTypeArgVisitor;
import org.eclipse.ceylon.compiler.typechecker.analyzer.ExpressionVisitor;
import org.eclipse.ceylon.compiler.typechecker.analyzer.ImportVisitor;
import org.eclipse.ceylon.compiler.typechecker.analyzer.InheritanceVisitor;
import org.eclipse.ceylon.compiler.typechecker.analyzer.LiteralVisitor;
import org.eclipse.ceylon.compiler.typechecker.analyzer.LocalDeclarationVisitor;
import org.eclipse.ceylon.compiler.typechecker.analyzer.ModuleSourceMapper;
import org.eclipse.ceylon.compiler.typechecker.analyzer.ModuleVisitor;
import org.eclipse.ceylon.compiler.typechecker.analyzer.RefinementVisitor;
import org.eclipse.ceylon.compiler.typechecker.analyzer.SelfReferenceVisitor;
import org.eclipse.ceylon.compiler.typechecker.analyzer.SpecificationVisitor;
import org.eclipse.ceylon.compiler.typechecker.analyzer.SupertypeVisitor;
import org.eclipse.ceylon.compiler.typechecker.analyzer.TypeArgumentVisitor;
import org.eclipse.ceylon.compiler.typechecker.analyzer.TypeHierarchyVisitor;
import org.eclipse.ceylon.compiler.typechecker.analyzer.TypeVisitor;
import org.eclipse.ceylon.compiler.typechecker.analyzer.VisibilityVisitor;
import org.eclipse.ceylon.compiler.typechecker.analyzer.Warning;
import org.eclipse.ceylon.compiler.typechecker.io.VirtualFile;
import org.eclipse.ceylon.compiler.typechecker.io.impl.Helper;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Validator;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.ImportPath;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.ModuleDescriptor;
import org.eclipse.ceylon.compiler.typechecker.util.AssertionVisitor;
import org.eclipse.ceylon.compiler.typechecker.util.DeprecationVisitor;
import org.eclipse.ceylon.compiler.typechecker.util.PrintVisitor;
import org.eclipse.ceylon.compiler.typechecker.util.ReferenceCounter;
import org.eclipse.ceylon.compiler.typechecker.util.StatisticsVisitor;
import org.eclipse.ceylon.compiler.typechecker.util.UsageVisitor;
import org.eclipse.ceylon.model.typechecker.context.TypeCache;
import org.eclipse.ceylon.model.typechecker.model.Cancellable;
import org.eclipse.ceylon.model.typechecker.model.Constructor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Module;
import org.eclipse.ceylon.model.typechecker.model.Package;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.Unit;
import org.eclipse.ceylon.model.typechecker.util.ModuleManager;

/**
 * Represent a unit and each of the type checking phases
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class PhasedUnit  implements Visitor.ExceptionHandler {
    
    private Tree.CompilationUnit rootNode;
    private Package pkg;
    private TypecheckerUnit unit;
    //must be the non qualified file name
    private String fileName;
    private WeakReference<ModuleManager> moduleManagerRef;
    private WeakReference<ModuleSourceMapper> moduleSourceMapperRef;
    private final String pathRelativeToSrcDir;
    private VirtualFile unitFile;
    private List<? extends Token> tokens;
    private ModuleVisitor moduleVisitor;
    private Tree.ModuleDescriptor moduleDescriptor;
    private VirtualFile srcDir;
    private boolean treeValidated = false;
    private boolean declarationsScanned = false;
    private boolean scanningDeclarations = false;
    private boolean typeDeclarationsScanned = false;
    private boolean refinementValidated = false;
    private boolean flowAnalyzed = false;
    private boolean fullyTyped = false;
    private boolean usageAnalyzed = false;
    private boolean literalsProcessed = false;
    private boolean moduleVisited = false;
    private EnumSet<Warning> suppressedWarnings = 
            EnumSet.noneOf(Warning.class);
    
    public VirtualFile getSrcDir() {
        return srcDir;
    }

    public PhasedUnit(VirtualFile unitFile, 
            VirtualFile srcDir, 
            Tree.CompilationUnit rootNode, 
            Package p, 
            ModuleManager moduleManager, 
            ModuleSourceMapper moduleManagerUtil, 
            Context context, 
            List<? extends Token> tokenStream) {
        this.rootNode = rootNode;
        this.pkg = p;
        this.unitFile = unitFile;
        this.srcDir = srcDir;
        this.fileName = unitFile.getName();
        this.pathRelativeToSrcDir = 
                Helper.computeRelativePath(unitFile, srcDir);
        this.moduleManagerRef = 
                new WeakReference<ModuleManager>
                    (moduleManager);
        this.moduleSourceMapperRef = 
                new WeakReference<ModuleSourceMapper>
                    (moduleManagerUtil);
        this.tokens = tokenStream;
        unit = createUnit();
        unit.setFilename(fileName);
        unit.setFullPath(unitFile.getPath());
        unit.setRelativePath(pathRelativeToSrcDir);
        unit.setPackage(pkg);
        unit.setSupportedBackends(moduleManager.getSupportedBackends());
        pkg.removeUnit(unit);
        pkg.addUnit(unit);
        rootNode.setUnit(unit);
    }

    public PhasedUnit(PhasedUnit other) {
        this.rootNode = other.rootNode;
        this.pkg = other.pkg;
        this.unit = other.unit;
        this.fileName = other.fileName;
        this.moduleManagerRef = 
                new WeakReference<ModuleManager>
                    (other.moduleManagerRef.get());
        this.moduleSourceMapperRef = 
                new WeakReference<ModuleSourceMapper>
                    (other.moduleSourceMapperRef.get());
        this.pathRelativeToSrcDir = other.pathRelativeToSrcDir;
        this.unitFile = other.unitFile;
        this.tokens = other.tokens;
        this.moduleVisitor = other.moduleVisitor;
        this.srcDir = other.srcDir;
        this.treeValidated = other.treeValidated;
        this.declarationsScanned = other.declarationsScanned;
        this.scanningDeclarations = other.scanningDeclarations;
        this.typeDeclarationsScanned = other.typeDeclarationsScanned;
        this.fullyTyped = other.fullyTyped;
        this.refinementValidated = other.refinementValidated;
        this.fullyTyped = other.fullyTyped;
        this.flowAnalyzed = other.flowAnalyzed;
    }

    protected boolean shouldIgnoreOverload(Declaration overload,
            Declaration currentDeclaration) {
        return false;
    }

    protected boolean isAllowedToChangeModel(Declaration declaration) {
        return true;
    }

    public ModuleDescriptor findModuleDescriptor() {
        if (ModuleManager.MODULE_FILE.equals(fileName)) {
            rootNode.visit(new Visitor() {
                @Override
                public void visit(ModuleDescriptor that) {
                    moduleDescriptor = that;
                }
            });
        }
        return moduleDescriptor;
    }

    public Module visitSrcModulePhase() {
        boolean moduleFile = 
                ModuleManager.MODULE_FILE.equals(fileName);
        boolean packageFile = 
                ModuleManager.PACKAGE_FILE.equals(fileName);
        if (moduleFile || packageFile) {
            if (!moduleVisited) {
                moduleVisited = true;
                processLiterals();
                moduleVisitor = 
                        new ModuleVisitor(
                                moduleManagerRef.get(), 
                                moduleSourceMapperRef.get(), 
                                pkg, moduleFile);
                moduleVisitor.setExceptionHandler(this);
                moduleVisitor.setCompleteOnlyAST(!isAllowedToChangeModel(null));
                rootNode.visit(moduleVisitor);
                return moduleVisitor.getMainModule();
            }
        }
        return null;
    }

    protected ModuleSourceMapper getModuleSourceMapper() {
        return moduleSourceMapperRef.get();
    }
    
    protected TypecheckerUnit createUnit() {
        return new TypecheckerUnit(moduleSourceMapperRef.get());
    }
    
    public void visitRemainingModulePhase() {
        if ( moduleVisitor != null ) {
            moduleVisitor.setPhase(ModuleVisitor.Phase.REMAINING);
            rootNode.visit(moduleVisitor);
            moduleVisitor = null;
        }
    }
    
    public boolean isFullyTyped() {
        return fullyTyped;
    }

    public void setFullyTyped(boolean fullyTyped) {
        this.fullyTyped = fullyTyped;
    }
    
    public boolean isFlowAnalyzed() {
        return flowAnalyzed;
    }

    public void setFlowAnalyzed(boolean flowAnalyzed) {
        this.flowAnalyzed = flowAnalyzed;
    }

    public boolean isTreeValidated() {
        return treeValidated;
    }

    public void setTreeValidated(boolean treeValidated) {
        this.treeValidated = treeValidated;
    }

    public boolean isDeclarationsScanned() {
        return declarationsScanned;
    }

    public void setDeclarationsScanned(boolean declarationsScanned) {
        this.declarationsScanned = declarationsScanned;
    }

    public boolean isTypeDeclarationsScanned() {
        return typeDeclarationsScanned;
    }

    public void setTypeDeclarationsScanned(boolean typeDeclarationsScanned) {
        this.typeDeclarationsScanned = typeDeclarationsScanned;
    }

    public boolean isRefinementValidated() {
        return refinementValidated;
    }

    public void setRefinementValidated(boolean refinementValidated) {
        this.refinementValidated = refinementValidated;
    }

    public void validateTree() {
        //System.out.println("Validating tree for " + fileName);
        if (!treeValidated) {
            String fn = unit.getRelativePath();
            for (int i=0; 
                    i<fn.length(); 
                    i = fn.offsetByCodePoints(i, 1)) {
                int cp = fn.codePointAt(i);
                if (cp>127) {
                    rootNode.addUsageWarning(
                            Warning.filenameNonAscii,
                            "source file name has non-ASCII characters: " + 
                            fn);
                }
            }
            String ufn = unit.getFilename();
            for (Unit u: unit.getPackage().getUnits()) {
                if (!u.equals(unit) && 
                        u.getFilename().equalsIgnoreCase(ufn)) {
                    if (u.getFilename().equals(ufn)) {
                        String errorMessage = 
                                "identical source files: " +
                                unit.getFullPath() + " and " + 
                                u.getFullPath();
                        if (u.getFilename().equals(MODULE_FILE) ||
                            u.getFilename().equals(PACKAGE_FILE)) {
                            errorMessage += " (a module/package descriptor should be defined only once, even in case of multiple source directories)";
                        }
                        rootNode.addError(errorMessage);                        
                    }
                    else {
                        rootNode.addUsageWarning(
                                Warning.filenameCaselessCollision,
                                "source file names differ only by case: " +
                                unit.getFullPath() + " and " + 
                                u.getFullPath());
                    }
                }
            }
            rootNode.visit(new Validator().setExceptionHandler(this));
            rootNode.visit(new Visitor() {
                @Override
                public void visit(ModuleDescriptor that) {
                    super.visit(that);
                    ImportPath importPath = 
                            that.getImportPath();
                    if (importPath != null) {
                        String moduleName = 
                                formatPath(importPath.getIdentifiers());
                        ModuleSourceMapper moduleManagerUtil = 
                                moduleSourceMapperRef.get();
                        if (moduleManagerUtil != null) {
                            for (Module otherModule: 
                                    moduleManagerUtil.getCompiledModules()) {
                                String otherModuleName = 
                                        otherModule.getNameAsString();
                                if (moduleName.startsWith(otherModuleName + ".") || 
                                    otherModuleName.startsWith(moduleName + ".")) {
                                    StringBuilder error = 
                                            new StringBuilder()
                                                .append("Found two modules within the same hierarchy: '")
                                                .append(otherModule.getNameAsString())
                                                .append("' and '")
                                                .append(moduleName)
                                                .append("'");
                                    that.addError(error.toString());
                                }
                            }
                        }
                    }
                }
            }.setExceptionHandler(this));
            treeValidated = true;
        }
    }

    public void scanDeclarations() {
        Boolean enabled = 
                TypeCache.setEnabled(false);
        try {
            if (!declarationsScanned) {
                processLiterals();
                scanningDeclarations = true;
                //System.out.println("Scan declarations for " + fileName);
                DeclarationVisitor dv = new DeclarationVisitor(unit) {
                    @Override
                    protected boolean shouldIgnoreOverload
                    (Declaration overload, Declaration declaration) {
                        return PhasedUnit.this.shouldIgnoreOverload(overload, declaration);
                    }
                    @Override
                    protected boolean isAllowedToChangeModel
                    (Declaration declaration) {
                        return PhasedUnit.this.isAllowedToChangeModel(declaration);
                    }
                };
                rootNode.visit(dv.setExceptionHandler(this));

                rootNode.visit(new LocalDeclarationVisitor().setExceptionHandler(this));

                declarationsScanned = true;
                scanningDeclarations = false;
            }
        }
        finally {
            TypeCache.setEnabled(enabled);
        }
    }

    private void processLiterals() {
        if (!literalsProcessed) {
            rootNode.visit(new LiteralVisitor().setExceptionHandler(this));
            literalsProcessed = true;
        }
    }

    public void scanTypeDeclarations() {
        scanTypeDeclarations(null);
    }
    
    public void scanTypeDeclarations(Cancellable cancellable) {
        Boolean enabled = 
                TypeCache.setEnabled(false);
        try {
            if (!typeDeclarationsScanned) {
                //System.out.println("Scan type declarations for " + fileName);
                rootNode.visit(new ImportVisitor(cancellable).setExceptionHandler(this));
                rootNode.visit(new DefaultTypeArgVisitor().setExceptionHandler(this));
                rootNode.visit(new SupertypeVisitor(false).setExceptionHandler(this)); //TODO: move to a new phase!
                rootNode.visit(new TypeVisitor(cancellable).setExceptionHandler(this));
                typeDeclarationsScanned = true;
            }
        }
        finally {
            TypeCache.setEnabled(enabled);
        }
    }

    public synchronized void validateRefinement() {
        Boolean enabled = 
                TypeCache.setEnabled(false);
        try {
            if (!refinementValidated) {
                Type.resetDepth(0);
                //System.out.println("Validate member refinement for " + fileName);
                rootNode.visit(new AliasVisitor().setExceptionHandler(this));
                rootNode.visit(new SupertypeVisitor(true).setExceptionHandler(this)); //TODO: move to a new phase!
                rootNode.visit(new InheritanceVisitor().setExceptionHandler(this));
                rootNode.visit(new RefinementVisitor().setExceptionHandler(this));
                refinementValidated = true;
            }
        }
        finally {
            TypeCache.setEnabled(enabled);
        }
    }

    public synchronized void analyseTypes() {
        analyseTypes(null);
    }
    
    public synchronized void analyseTypes(Cancellable cancellable) {
        if (!fullyTyped) {
            Type.resetDepth(-100);
            //System.out.println("Run analysis phase for " + fileName);
            rootNode.visit(new ExpressionVisitor(cancellable).setExceptionHandler(this));
            rootNode.visit(new VisibilityVisitor().setExceptionHandler(this));
            rootNode.visit(new AnnotationVisitor().setExceptionHandler(this));
            rootNode.visit(new TypeArgumentVisitor().setExceptionHandler(this));
            fullyTyped = true;
        }
    }
    
    public synchronized void analyseFlow() {
        if (!flowAnalyzed) {
            rootNode.visit(new TypeHierarchyVisitor().setExceptionHandler(this));
            //System.out.println("Validate control flow for " + fileName);
            rootNode.visit(new ControlFlowVisitor().setExceptionHandler(this));
            //System.out.println("Validate self references for " + fileName);
            //System.out.println("Validate specification for " + fileName);
            for (Declaration d: unit.getDeclarations()) {
                if (d.getName()!=null || d instanceof Constructor) {
                    rootNode.visit(new SpecificationVisitor(d).setExceptionHandler(this));
                    if (d instanceof TypeDeclaration) {
                        TypeDeclaration td = 
                                (TypeDeclaration) d;
                        rootNode.visit(new SelfReferenceVisitor(td).setExceptionHandler(this));
                    }
                }
            }
            flowAnalyzed = true;
        }
    }

    public synchronized void analyseUsage() {
        if (!usageAnalyzed) {
            ReferenceCounter rc = new ReferenceCounter();
            rootNode.visit(rc.setExceptionHandler(this));
            rootNode.visit(new UsageVisitor(rc).setExceptionHandler(this));
            rootNode.visit(new DeprecationVisitor().setExceptionHandler(this));
            usageAnalyzed = true;
        }
    }

    public void generateStatistics(StatisticsVisitor statsVisitor) {
        rootNode.visit(statsVisitor);
    }
    
    public void runAssertions(AssertionVisitor av) {
        //System.out.println("Running assertions for " + fileName);
        rootNode.visit(av);
    }

    public void display() {
        System.out.println("Displaying " + fileName);
        rootNode.visit(new PrintVisitor());
    }
    
    public Package getPackage() {
        return pkg;
    }
    
    public TypecheckerUnit getUnit() {
        return unit;
    }

    public List<Declaration> getDeclarations() {
        if (!declarationsScanned) {
            scanDeclarations();
        }
        return unit.getDeclarations();
    }

    public String getPathRelativeToSrcDir() {
        return pathRelativeToSrcDir;
    }

    public VirtualFile getUnitFile() {
        return unitFile;
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append("PhasedUnit")
            .append("[filename=").append(fileName)
            .append(", compilationUnit=").append(unit)
            .append(", pkg=").append(pkg)
            .append(']')
            .toString();
    }

    public Tree.CompilationUnit getCompilationUnit() {
        return rootNode;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<CommonToken> getTokens() {
        return (List) tokens;
    }

    public boolean isScanningDeclarations() {
        return scanningDeclarations;
    }

    public void setSuppressedWarnings(EnumSet<Warning> suppressedWarnings) {
        this.suppressedWarnings = suppressedWarnings;
    }
    
    public EnumSet<Warning> getSuppressedWarnings() {
        return this.suppressedWarnings;
    }

    @Override
    public boolean handleException(Exception e, Node that) {
        return false;
    }
}
