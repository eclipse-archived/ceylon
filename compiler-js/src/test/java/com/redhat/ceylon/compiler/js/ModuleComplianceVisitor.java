package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.io.VFS;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InterfaceDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ObjectDefinition;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Value;

/** This visitor finds all the shared class and interface definitions in the language module,
 * along with all their shared actual methods, and checks that they are also defined in the
 * JS version of the language module.
 * 
 * @author Enrique Zamudio
 */
public class ModuleComplianceVisitor extends Visitor {

    /** The complete JS language module as a String */
    private String js;
    /** The last class or interface definition that was visited. */
    private ClassOrInterface last;
    /** The last object definition visited. */
    private Value lastObject;

    public ModuleComplianceVisitor(String jsLanguageModule) {
        js = jsLanguageModule;
    }

    @Override
    public void visit(ClassDefinition that) {
        if (!(that.getDeclarationModel().isShared() && that.getDeclarationModel().isToplevel()) || that.getDeclarationModel().isAlias()) {
            return;
        }
        last = that.getDeclarationModel();
        String def = String.format("exports.%s=", last.getName());
        if (js.indexOf(def) < 0) {
            System.out.println("Missing CLASS:     " + last.getName());
        }
        super.visit(that);
        last = null;
    }
    @Override
    public void visit(InterfaceDefinition that) {
        if (!(that.getDeclarationModel().isShared() && that.getDeclarationModel().isToplevel()) || that.getDeclarationModel().isAlias()) {
            return;
        }
        last = that.getDeclarationModel();
        String def = String.format("exports.%s=", last.getName());
        if (js.indexOf(def) < 0) {
            System.out.println("Missing INTERFACE: " + last.getName());
        }
        super.visit(that);
        last = null;
    }
    @Override
    public void visit(MethodDefinition that) {
        if (!that.getDeclarationModel().isShared() || that.getDeclarationModel().isFormal()) {
            return;
        }
        String def;
        String id = that.getIdentifier().getText().trim();
        if (that.getDeclarationModel().isToplevel()) {
            def = String.format("exports.%s=", id);
        } else if (that.getScope().getContainer() == last || that.getScope().getContainer() == lastObject) {
            def = String.format("%s$proto.%s = function(", last.getName(), id);
            if (js.indexOf(def) < 0) {
                def = String.format("%s.$$.prototype.%s = function", last.getName(), id);
            }
            if (js.indexOf(def) < 0) {
                System.out.println("Missing METHOD:    "
                    + (last == null || that.getDeclarationModel().isToplevel() ? "" : last.getName()+".")
                    + id);
            }
        }
        super.visit(that);
    }

    @Override
    public void visit(ObjectDefinition that) {
        if (!(that.getDeclarationModel().isShared() && that.getDeclarationModel().isToplevel())) {
            return;
        }
        lastObject = that.getDeclarationModel();
        //The object names in JS are like a getter
        String def = String.format("exports.get%s%s=", lastObject.getName().substring(0,1).toUpperCase(),
                lastObject.getName().substring(1));
        if (js.indexOf(def) < 0) {
            System.out.println("Missing OBJECT:    " + lastObject.getName());
        }
        super.visit(that);
        lastObject = null;
    }

    /** This is a convenience method to find either the JS language module or the .src file for the
     * Ceylon Language Module.
     * @param root The root directory to look for the file.
     * @param extension The file extension (.js or .src) */
    public static File findModule(String root, String extension) {
        File r = new File(root);
        if (r.exists() && r.isDirectory()) {
            r = new File(r, "ceylon");
            if (r.exists() && r.isDirectory()) {
                r = new File(r, "language");
            }
            if (r.exists() && r.isDirectory()) {
                //Get the lastest version
                File which = null;
                for (File s : r.listFiles()) {
                    if (which == null || s.lastModified() > which.lastModified()) {
                        which = s;
                    }
                }
                if (which != null) {
                    for (File s : which.listFiles()) {
                        if (s.getName().equals("ceylon.language-" + which.getName() + "." + extension)) {
                            return s;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Finding JS language module...");
        File jsmod = findModule("build/runtime", "js");
        if (jsmod == null && System.getenv("ceylon.repo") != null) {
            jsmod = findModule(System.getenv("ceylon.repo"), "js");
        }
        if (jsmod == null && System.getProperty("user.home") != null) {
            jsmod = findModule(System.getProperty("user.home"), "js");
        }
        if (jsmod == null) {
            throw new IllegalStateException("Cannot find JS language module");
        }
        System.out.println("Finding Ceylon Language Module");
        File mod = findModule("../ceylon.language/build/dist", "src");
        if (mod == null && System.getenv("ceylon.repo") != null) {
            mod = findModule(System.getenv("ceylon.repo"), "src");
        }
        if (mod == null && System.getProperty("user.home") != null) {
            mod = findModule(System.getProperty("user.home"), "src");
        }
        if (mod == null) {
            throw new IllegalStateException("Cannot find JVM language module");
        }
        System.out.println("Reading JS language module into memory...");
        FileReader reader = new FileReader(jsmod);
        char[] buf = new char[16384];
        StringWriter writer = new StringWriter();
        while (reader.ready()) {
            reader.read(buf);
            writer.write(buf);
        }
        reader.close();
        ModuleComplianceVisitor v = new ModuleComplianceVisitor(writer.toString());
        System.out.println("Checking Ceylon vs JS modules...");
        VFS vfs = new VFS();
        TypeChecker tc = new TypeCheckerBuilder().verbose(false)
                .addSrcDirectory(vfs.getFromZipFile(mod)).getTypeChecker();
        tc.process();
        for (PhasedUnit pu : tc.getPhasedUnits().getPhasedUnits()) {
            pu.getCompilationUnit().visit(v);
        }
    }
}
