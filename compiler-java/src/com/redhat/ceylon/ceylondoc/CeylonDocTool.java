package com.redhat.ceylon.ceylondoc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Modules;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;

public class CeylonDocTool {

    private List<PhasedUnit> phasedUnits;
    private Modules modules;
    private String destDir;
    private Map<ClassOrInterface,List<ClassOrInterface>> subclasses = new HashMap<ClassOrInterface, List<ClassOrInterface>>();
    private Map<TypeDeclaration,List<ClassOrInterface>> satisfyingClassesOrInterfaces = new HashMap<TypeDeclaration, List<ClassOrInterface>>();   
    private boolean showPrivate;
    
    public CeylonDocTool(List<PhasedUnit> phasedUnits, Modules modules, boolean showPrivate) {
        this.phasedUnits = phasedUnits;
        this.modules = modules;
        this.showPrivate = showPrivate;
    }
    
    public void setDestDir(String destDir){
        this.destDir = destDir;
    }

    public void makeDoc() throws IOException{
    	
    	for (PhasedUnit pu : phasedUnits) {
            for(Declaration decl : pu.getUnit().getDeclarations()){
            	 if(decl instanceof ClassOrInterface){
            		 ClassOrInterface c = (ClassOrInterface) decl;            		 
            		 // subclasses map
            		 if (c instanceof Class) {
	            		 ClassOrInterface superclass = c.getExtendedTypeDeclaration();            		 
	            		 if (superclass != null) {
	                		 if (subclasses.get(superclass) ==  null) {
	                			 subclasses.put(superclass, new ArrayList<ClassOrInterface>());
	                		 }
	                		 subclasses.get(superclass).add(c);
	            		 }
            		 }
            		 
            		 List<TypeDeclaration> satisfiedTypes = new ArrayList<TypeDeclaration>(c.getSatisfiedTypeDeclarations());            		 
            		 if (satisfiedTypes != null && satisfiedTypes.isEmpty() == false) {
            			 // satisfying classes or interfaces map
            			for (TypeDeclaration satisfiedType : satisfiedTypes) {
                    		 if (satisfyingClassesOrInterfaces.get(satisfiedType) ==  null) {
                    			 satisfyingClassesOrInterfaces.put(satisfiedType, new ArrayList<ClassOrInterface>());
                    		 }
                    		 satisfyingClassesOrInterfaces.get(satisfiedType).add(c);
						}
            		 }
                 }
            }
        }

    	Module module = null;
        for (PhasedUnit pu : phasedUnits) {
        	if(module == null)
        		module = pu.getPackage().getModule();
        	else if(pu.getPackage().getModule() != module)
        		throw new RuntimeException("Documentation of multiple modules not supported yet");
            for(Declaration decl : pu.getUnit().getDeclarations()){            	
                doc(decl);
            }
        }    	
        
        for(Package pkg : module.getPackages()){
        	doc(pkg);
        }
        doc(module);
        copyResource("resources/style.css");
    }

    private void doc(Module module) throws IOException {
        new SummaryDoc(destDir, module, showPrivate).generate();
    }

    private void doc(Package pkg) throws IOException {
        new PackageDoc(destDir, pkg, showPrivate).generate();
    }

    private void copyResource(String path) throws IOException {
        InputStream resource = getClass().getResourceAsStream(path);
        OutputStream os = new FileOutputStream(new File(destDir, "style.css"));
        byte[] buf = new byte[1024];
        int read;
        while((read = resource.read(buf)) > -1){
            os.write(buf, 0, read);
        }
        os.flush();
        os.close();
    }

	private void doc(Declaration decl) throws IOException {
		if (decl instanceof ClassOrInterface) {
			if (showPrivate || decl.isShared()) {
				new ClassDoc(destDir, showPrivate, (ClassOrInterface) decl,
						subclasses.get(decl),
						satisfyingClassesOrInterfaces.get(decl)).generate();
			}
		}
	}

}
