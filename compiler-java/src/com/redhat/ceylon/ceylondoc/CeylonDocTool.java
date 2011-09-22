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
    private Map<ClassOrInterface,List<ClassOrInterface>> subclassesMap = new HashMap<ClassOrInterface, List<ClassOrInterface>>();
    private Map<TypeDeclaration,List<ClassOrInterface>> implementingClassesMap = new HashMap<TypeDeclaration, List<ClassOrInterface>>();
    private Map<ClassOrInterface,List<ClassOrInterface>> superInterfacesMap = new HashMap<ClassOrInterface, List<ClassOrInterface>>();
    
    public CeylonDocTool(List<PhasedUnit> phasedUnits, Modules modules) {
        this.phasedUnits = phasedUnits;
        this.modules = modules;
    }
    
    public void setDestDir(String destDir){
        this.destDir = destDir;
    }

    public void makeDoc() throws IOException{
    	
    	for (PhasedUnit pu : phasedUnits) {
            for(Declaration decl : pu.getUnit().getDeclarations()){
            	 if(decl instanceof ClassOrInterface){
            		 ClassOrInterface c = (ClassOrInterface) decl;    
            		 
            		 // superclass map
            		 if (c instanceof Class) {
	            		 ClassOrInterface superclass = c.getExtendedTypeDeclaration();            		 
	            		 if (superclass != null) {
	                		 if (subclassesMap.get(superclass) ==  null) {
	                			 subclassesMap.put(superclass, new ArrayList<ClassOrInterface>());
	                		 }
	                		 subclassesMap.get(superclass).add(c);
	            		 }
            		 }
            		 
            		 List<TypeDeclaration> satisfiedTypes = new ArrayList<TypeDeclaration>(c.getSatisfiedTypeDeclarations());            		 
            		 if (satisfiedTypes != null && satisfiedTypes.isEmpty() == false) {
            			 
                		// implementing classes or interfaces map
            			for (TypeDeclaration satisfiedType : satisfiedTypes) {
                    		 if (implementingClassesMap.get(satisfiedType) ==  null) {
                    			 implementingClassesMap.put(satisfiedType, new ArrayList<ClassOrInterface>());
                    		 }
                    		 implementingClassesMap.get(satisfiedType).add(c);
						}
            			
            			// implemented interfaces map
            			List<ClassOrInterface> superInterfaces = new ArrayList<ClassOrInterface>();
            			superInterfacesMap.put(c, superInterfaces);
            			while (satisfiedTypes.isEmpty() == false) {
            				 List<TypeDeclaration> superSatisfiedTypes = new ArrayList<TypeDeclaration>(); 
            				 for (TypeDeclaration satisfiedType : satisfiedTypes) {
            					 superInterfaces.add((ClassOrInterface)satisfiedType);
            					 if (satisfiedType.getSatisfiedTypeDeclarations().isEmpty() == false) {
            						 for (TypeDeclaration superSatisfiedType: satisfiedType.getSatisfiedTypeDeclarations() ) {
            							 if (superInterfaces.contains(superSatisfiedType) == false && superSatisfiedTypes.contains(superSatisfiedType) == false) {
            								 superSatisfiedTypes.add(superSatisfiedType);
            							 }
            						 }
            					 }
            					 
            				 }
            				 satisfiedTypes = superSatisfiedTypes; 
            			}
            			
            		 }
            		 
                 }
            }
        }

        for (PhasedUnit pu : phasedUnits) {
            for(Declaration decl : pu.getUnit().getDeclarations()){
                doc(decl);
            }
        }    	
        
        for(Module module : modules.getListOfModules()){
            for(Package pkg : module.getPackages()){
                doc(pkg);
            }
        }
        doc(modules);
        copyResource("resources/style.css");
    }

    private void doc(Modules modules) throws IOException {
        new SummaryDoc(destDir, modules).generate();
    }

    private void doc(Package pkg) throws IOException {
        new PackageDoc(destDir, pkg).generate();
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
        if(decl instanceof ClassOrInterface){   
        	System.out.println(decl.getName() + " " +  superInterfacesMap.get(decl));
            new ClassDoc(destDir, (ClassOrInterface)decl,subclassesMap.get(decl), implementingClassesMap.get(decl), superInterfacesMap.get(decl)).generate();
        }
    }

}
