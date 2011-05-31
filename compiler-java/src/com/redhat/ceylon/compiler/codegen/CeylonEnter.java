package com.redhat.ceylon.compiler.codegen;

import com.redhat.ceylon.compiler.tools.LanguageCompiler;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleValidator;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

public class CeylonEnter extends Enter {

	public static Enter instance(Context context) {
		Enter instance = context.get(enterKey);
		if (instance == null)
			instance = new CeylonEnter(context);
		return instance;
	}

	private Gen2 gen;
	private boolean hasRun = false;
	private PhasedUnits phasedUnits;
	private com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext;

	protected CeylonEnter(Context context) {
		super(context);
		System.err.println("CeylonEnter created");
		try {
			gen = Gen2.getInstance(context);
		} catch (Exception e) {
			// FIXME
			e.printStackTrace();
		}
		phasedUnits = LanguageCompiler.getPhasedUnitsInstance(context);
        ceylonContext = LanguageCompiler.getCeylonContextInstance(context);
	}

	@Override
	public void main(List<JCCompilationUnit> trees) {
		System.err.println("CeylonEnter main");
		if(hasRun)
			throw new RuntimeException("Waaaaa, running twice!!!");
		hasRun = true;
		// let's see how far we can get the type checker now
		typeCheck();
		for(JCCompilationUnit tree : trees){
			if(tree instanceof CeylonCompilationUnit){
				CeylonCompilationUnit ceylonTree = (CeylonCompilationUnit)tree;
				gen.setMap(ceylonTree.lineMap);
				ceylonTree.defs = gen.convertAfterTypeChecking(ceylonTree.ceylonTree).toList();
				System.err.println(ceylonTree.defs);
			}
		}
		super.main(trees);
	}

	private void typeCheck() {
		final java.util.List<PhasedUnit> listOfUnits = phasedUnits.getPhasedUnits();
		for (PhasedUnit pu : listOfUnits) {
			pu.buildModuleImport();
		}

		final ModuleValidator moduleValidator = new ModuleValidator(ceylonContext);
		// FIXME: this breaks because it tries to load dependencies on its own
//		moduleValidator.verifyModuleDependencyTree();
		// FIXME: what's that for?
		java.util.List<PhasedUnits> phasedUnitsOfDependencies = moduleValidator.getPhasedUnitsOfDependencies();

		for (PhasedUnit pu : listOfUnits) {
			pu.validateTree();
			pu.scanDeclarations();
			pu.validateControlFlow();
			pu.validateSpecification();
		}
		// FIXME: this fails at this phase
		/*
		for (PhasedUnit pu : listOfUnits) {
			pu.scanTypeDeclarations();
		}
		for (PhasedUnit pu: listOfUnits) {
			pu.validateRefinement();
		}
		for (PhasedUnit pu : listOfUnits) {
			pu.analyseTypes();
		}
		for (PhasedUnit pu : listOfUnits) {
				pu.display();
				pu.runAssertions();
		}*/
	}
}
