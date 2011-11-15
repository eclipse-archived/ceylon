package com.redhat.ceylon.ceylondoc;

import static com.redhat.ceylon.ceylondoc.Util.join;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Modules;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;

public class CeylonDocTool {

    private List<PhasedUnit> phasedUnits;
    private Modules modules;
    private String destDir;
    private Map<ClassOrInterface, List<ClassOrInterface>> subclasses = new HashMap<ClassOrInterface, List<ClassOrInterface>>();
    private Map<TypeDeclaration, List<ClassOrInterface>> satisfyingClassesOrInterfaces = new HashMap<TypeDeclaration, List<ClassOrInterface>>();
    private boolean showPrivate;

    public CeylonDocTool(List<PhasedUnit> phasedUnits, Modules modules, boolean showPrivate) {
        this.phasedUnits = phasedUnits;
        this.modules = modules;
        this.showPrivate = showPrivate;
    }

    public void setDestDir(String destDir) {
        this.destDir = destDir;
    }

    public String getDestDir() {
        return destDir;
    }

    public boolean isShowPrivate() {
        return showPrivate;
    }

    private String getFileName(Scope klass) {
        List<String> name = new LinkedList<String>();
        while(klass instanceof Declaration){
            name.add(0, ((Declaration)klass).getName());
            klass = klass.getContainer();
        }
        return join(".", name);
    }

    private File getFolder(Package pkg) {
        File dir = new File(destDir, join("/",pkg.getName()));
        dir.mkdirs();
        return dir;
    }

    private File getFolder(ClassOrInterface klass) {
        return getFolder(getPackage(klass));
    }
    
    private String kind(Object obj) {
        if (obj instanceof Class) {
            return Character.isUpperCase(((Class)obj).getName().charAt(0)) ? "class" : "object";
        } else if (obj instanceof Interface) {
            return "interface";
        } else if (obj instanceof AttributeDeclaration
                || obj instanceof Getter) {
            return "attribute";
        } else if (obj instanceof Method) {
            return "function";
        } else if (obj instanceof Value) {
            return "value";
        } else if (obj instanceof Package) {
            return "package";
        } else if (obj instanceof Module) {
            return "module";
        }
        throw new RuntimeException("Unexpected: " + obj);
    }
    
    File getObjectFile(Object modPgkOrDecl) {
        final File file;
        if (modPgkOrDecl instanceof ClassOrInterface) {
            ClassOrInterface klass = (ClassOrInterface)modPgkOrDecl;
            String filename = kind(modPgkOrDecl) + "_" + getFileName(klass) + ".html";
            file = new File(getFolder(klass), filename);
        } else if (modPgkOrDecl instanceof Package) {
            Package pkg = (Package)modPgkOrDecl;
            String filename = "index.html";
            file = new File(getFolder(pkg), filename);
        } else if (modPgkOrDecl instanceof Module) {
            String filename = "index.html";
            file = new File(new File(destDir), filename);
        } else {
            throw new RuntimeException("Unexpected: " + modPgkOrDecl);
        }
        return file;
    }

    public void makeDoc() throws IOException{
    	
    	for (PhasedUnit pu : phasedUnits) {
            for (Declaration decl : pu.getUnit().getDeclarations()) {
                if(!include(decl)) {
                    continue;
                }
                 if (decl instanceof ClassOrInterface) {
                     getObjectFile(decl);
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
            if (module == null) {
                module = pu.getPackage().getModule();
                getObjectFile(module);
        		for (Package pkg : module.getPackages()) {
                    getObjectFile(pkg);
        		}
            } else if (pu.getPackage().getModule() != module) {
                throw new RuntimeException("Documentation of multiple modules not supported yet");
            }
            for (Declaration decl : pu.getUnit().getDeclarations()) {
                doc(decl);
            }
        }

        for (Package pkg : module.getPackages()) {
            doc(pkg);
        }
        doc(module);
        copyResource("resources/style.css", "style.css");
        copyResource("resources/jquery-1.7.min.js", "jquery-1.7.min.js");
        copyResource("resources/ceylond.js", "ceylond.js");
    }

    private void doc(Module module) throws IOException {
        new SummaryDoc(this, module).generate();
    }

    private void doc(Package pkg) throws IOException {
        new PackageDoc(this, pkg).generate();
    }

    private void copyResource(String path, String target) throws IOException {
        InputStream resource = getClass().getResourceAsStream(path);
        OutputStream os = new FileOutputStream(new File(destDir, target));
        byte[] buf = new byte[1024];
        int read;
        while ((read = resource.read(buf)) > -1) {
            os.write(buf, 0, read);
        }
        os.flush();
        os.close();
    }

	private void doc(Declaration decl) throws IOException {
		if (decl instanceof ClassOrInterface) {
			 if (include(decl)) {
			    Scope scope = getPackage(decl);
				new ClassDoc(this,
                        (ClassOrInterface) decl,
						subclasses.get(decl),
						satisfyingClassesOrInterfaces.get(decl)).generate();
			}
		}
	}

    Package getPackage(Declaration decl) {
        Scope scope = decl.getContainer();
        while (!(scope instanceof Package)) {
            scope = scope.getContainer();
        }
        return (Package)scope;
    }
    
    Module getModule(Declaration decl) {
        return getPackage(decl).getModule();
    }


    protected boolean include(Declaration decl){
        return showPrivate || decl.isShared();
    }
}
