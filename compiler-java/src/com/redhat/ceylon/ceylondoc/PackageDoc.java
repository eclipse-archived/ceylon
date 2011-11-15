package com.redhat.ceylon.ceylondoc;

import static com.redhat.ceylon.ceylondoc.Util.getDoc;
import static com.redhat.ceylon.ceylondoc.Util.getModifiers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Value;

public class PackageDoc extends ClassOrPackageDoc {

    private Package pkg;
    private List<Class> classes;
    private List<Interface> interfaces;
    private List<MethodOrValue> attributes;
    private List<Method> methods;

    public PackageDoc(String destDir, Package pkg, boolean showPrivate) throws IOException {
        super(destDir, showPrivate);
        this.pkg = pkg;
        loadMembers();
    }

    private void loadMembers() {
        classes = new ArrayList<Class>();
        interfaces = new ArrayList<Interface>();
        attributes = new ArrayList<MethodOrValue>();
        methods = new ArrayList<Method>();
        for (Declaration m : pkg.getMembers()) {
            if (m instanceof Interface)
                interfaces.add((Interface) m);
            else if (m instanceof Class)
                classes.add((Class) m);
            else if (m instanceof Value || m instanceof Getter)
                attributes.add((MethodOrValue) m);
            else if (m instanceof Method)
                methods.add((Method) m);
        }
        Comparator<Declaration> comparator = new Comparator<Declaration>() {
            @Override
            public int compare(Declaration a, Declaration b) {
                return a.getName().compareTo(b.getName());
            }
        };
        Collections.sort(classes, comparator);
        Collections.sort(interfaces, comparator);
        Collections.sort(attributes, comparator);
        Collections.sort(methods, comparator);
    }

    public void generate() throws IOException {
        setupWriter();
        open("html");
        open("head");
        around("title", "Package " + pkg.getName());
        String pathToBase = pkg.getNameAsString().isEmpty() ? "" : getPathToBase(pkg) + "/";
        tag("link href='" + pathToBase + "style.css' rel='stylesheet' type='text/css'");
        open("script type='text/javascript' src='text/css' src='"+pathToBase+"jquery-1.7.min.js'");
        close("script");
        close("head");
        open("body");
        summary();
        attributes();
        methods();
        interfaces();
        classes();
        close("body");
        close("html");
        writer.flush();
        writer.close();
    }

    private void summary() throws IOException {
        open("div class='nav menu'");
        open("div");
        around("a href='" + getPathToBase() + "/overview-summary.html'", "Overview");
        close("div");
        open("div class='selected'");
        write("Package");
        close("div");
        open("div");
        write("Class");
        close("div");
        open("div");
        write(pkg.getModule().getNameAsString() + "/" + pkg.getModule().getVersion());
        close("div");
        close("div");

        open("div class='head summary'");
        around("h1", "Package ", "<code>", pkg.getNameAsString(), "</code>");
        close("div");
    }

    private void methods() throws IOException {
        if (methods.isEmpty())
            return;
        openTable("Methods", "Modifier and Type", "Method and Description");
        for (Method m : methods) {
            doc(m);
        }
        close("table");
    }

    private void attributes() throws IOException {
        openTable("Attributes", "Modifier and Type", "Name and Description");
        for (MethodOrValue v : attributes) {
            doc(v);
        }
        close("table");
    }

    private void interfaces() throws IOException {
        openTable("Interfaces", "Modifier and Type", "Description");
        for (Interface i : interfaces) {
            doc(i);
        }
        close("table");
    }

    private void classes() throws IOException {
        openTable("Classes", "Modifier and Type", "Description");
        for (Class c : classes) {
            doc(c);
        }
        close("table");
    }

    private void doc(ClassOrInterface d) throws IOException {
        open("tr class='TableRowColor category'");
        open("td", "code");
        around("span class='modifiers'", getModifiers(d));
        write(" ");
        link(d.getType());
        close("code", "td");
        open("td");
        write(getDoc(d));
        close("td");
        close("tr");
    }

    @Override
    protected String getPathToBase() {
        return getPathToBase(pkg);
    }

    @Override
    protected File getOutputFile() {
        return new File(getFolder(pkg), "index.html");
    }
}
