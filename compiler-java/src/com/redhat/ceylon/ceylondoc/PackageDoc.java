/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.ceylondoc;

import static com.redhat.ceylon.ceylondoc.Util.getDoc;
import static com.redhat.ceylon.ceylondoc.Util.isEmpty;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.ceylondoc.Util.ReferenceableComparatorByName;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.TypeAlias;
import com.redhat.ceylon.compiler.typechecker.model.Value;

public class PackageDoc extends ClassOrPackageDoc {

    private final Package pkg;
    private final boolean sharingPageWithModule;
    private final List<Class> classes = new ArrayList<Class>();
    private final List<Interface> interfaces = new ArrayList<Interface>();
    private final List<MethodOrValue> attributes = new ArrayList<MethodOrValue>();
    private final List<Method> methods = new ArrayList<Method>();
    private final List<Class> exceptions = new ArrayList<Class>();
    private final List<TypeAlias> aliases = new ArrayList<TypeAlias>();
    private final List<Class> annotationTypes = new ArrayList<Class>();
    private final List<Method> annotationConstructors = new ArrayList<Method>();

	public PackageDoc(CeylonDocTool tool, Writer writer, Package pkg) throws IOException {
		super(pkg.getModule(), tool, writer);
		this.pkg = pkg;
		this.sharingPageWithModule = tool.isRootPackage(module, pkg);
		loadMembers();
	}

    private void loadMembers() {
        for (Declaration m : pkg.getMembers()) {
            if (!tool.shouldInclude(m)) {
                continue;
            }
            if (m instanceof Interface) {
                interfaces.add((Interface) m);
            } else if (m instanceof Class) {
                Class c = (Class) m;
                if( c.isAnnotation() ) {
                    annotationTypes.add(c);
                } else  if (Util.isException(c)) {
                    exceptions.add(c);
                } else {
                    classes.add(c);
                }
            } else if (m instanceof Value) {
                attributes.add((MethodOrValue) m);
            } else if (m instanceof Method) {
                Method method = (Method) m;
                if( m.isAnnotation() ) {
                    annotationConstructors.add(method);
                } else {
                    methods.add(method);
                }
            } else if (m instanceof TypeAlias) {
                aliases.add((TypeAlias) m);                
            }
        }
        
        Collections.sort(classes, ReferenceableComparatorByName.INSTANCE);
        Collections.sort(interfaces, ReferenceableComparatorByName.INSTANCE);
        Collections.sort(attributes, ReferenceableComparatorByName.INSTANCE);
        Collections.sort(methods, ReferenceableComparatorByName.INSTANCE);
        Collections.sort(exceptions, ReferenceableComparatorByName.INSTANCE);
        Collections.sort(aliases, ReferenceableComparatorByName.INSTANCE);
        Collections.sort(annotationTypes, ReferenceableComparatorByName.INSTANCE);
        Collections.sort(annotationConstructors, ReferenceableComparatorByName.INSTANCE);
    }

    public void generate() throws IOException {
        if (!sharingPageWithModule) {
            writeHeader("Package " + pkg.getName());
            writeNavBar();
        }
        
        writeSubNav();
        
        open("div class='container-fluid'");
        
        writeDescription();
        writeSubpackages();
        writeAliases();
        writeAnnotations();
        writeAttributes();
        writeMethods();
        writeInterfaces();
        writeClasses();
        writeExceptions();
        
        close("div");
        
        if (!sharingPageWithModule) {
            writeFooter();
        }
    }

    private void writeSubNav() throws IOException {
        open("div class='sub-navbar'");
        
        writeLinkSourceCode(pkg);
        
        if (sharingPageWithModule) {
            open("div id='section-package' class='sub-navbar-inner'");
        } else {
            open("div class='sub-navbar-inner'");
        }
        
        around("span class='sub-navbar-label'", "package");
        writeIcon(pkg);
        open("span class='sub-navbar-name'");
        writePackageNavigation(pkg);
        close("span");
        
        close("div"); // sub-navbar-inner
        
        open("div class='sub-navbar-menu'");
        if (!sharingPageWithModule) {
            writeSubNavBarLink(linkRenderer().to(module).getUrl(), "Overview", 'O', "Jump to module documentation");
        }
        if (!aliases.isEmpty()) {
            writeSubNavBarLink("#section-aliases", "Aliases", 'l', "Jump to aliases");
        }
        if (!annotationTypes.isEmpty() || !annotationConstructors.isEmpty()) {
            writeSubNavBarLink("#section-annotations", "Annotations", 'n', "Jump to annotations");
        }
        if (!attributes.isEmpty()) {
            writeSubNavBarLink("#section-attributes", "Values", 'V', "Jump to values");
        }
        if (!methods.isEmpty()) {
            writeSubNavBarLink("#section-methods", "Functions", 'F', "Jump to functions");
        }
        if (!interfaces.isEmpty()) {
            writeSubNavBarLink("#section-interfaces", "Interfaces", 'I', "Jump to interfaces");
        }
        if (!classes.isEmpty()) {
            writeSubNavBarLink("#section-classes", "Classes", 'C', "Jump to classes");
        }
        if (!exceptions.isEmpty()) {
            writeSubNavBarLink("#section-exceptions", "Exceptions", 'E', "Jump to exceptions");
        }
        close("div"); // sub-navbar-menu
        
        close("div"); // sub-navbar
    }
    
    private void writeDescription() throws IOException {
        open("div class='package-description'");
        String doc = getDoc(pkg, linkRenderer());
        if (isEmpty(doc)) {
            tool.warningMissingDoc(pkg.getQualifiedNameString(), pkg);
        }
        around("div class='doc'", doc);
        writeAnnotations(pkg);
        writeBy(pkg);
        close("div");
    }
    
    private void writeSubpackages() throws IOException {
        if (!sharingPageWithModule) {
            List<Package> subpackages = new ArrayList<Package>();
            for (Package p : module.getPackages()) {
                if (p.getName().size() == pkg.getName().size() + 1
                        && p.getNameAsString().startsWith(pkg.getNameAsString())
                        && tool.shouldInclude(p)) {
                    subpackages.add(p);
                }
            }
            Collections.sort(subpackages, ReferenceableComparatorByName.INSTANCE);
            writePackagesTable("Subpackages", subpackages);
        }
    }
    
    private void writeAliases() throws IOException {
        if(aliases.isEmpty()) {
            return;
        }
        openTable("section-aliases", "Aliases", 2, true);
        for (TypeAlias a : aliases) {
            doc(a);
        }
        closeTable();
    }
    
    private void writeAnnotations() throws IOException {
        if (annotationTypes.isEmpty() && annotationConstructors.isEmpty()) {
            return;
        }
        openTable("section-annotations", "Annotations", 2, true);
        for (Method annotationConstructor : annotationConstructors) {
            doc(annotationConstructor);
        }
        for (Class annotationType : annotationTypes) {
            doc(annotationType);
        }
        closeTable();
    }

    private void writeAttributes() throws IOException {
        if (attributes.isEmpty()) {
            return;
        }
        openTable("section-attributes", "Values", 2, true);
        for (MethodOrValue v : attributes) {
            doc(v);
        }
        closeTable();
    }
    
    private void writeMethods() throws IOException {
        if (methods.isEmpty()) {
            return;
        }
        openTable("section-methods", "Functions", 2, true);
        for (Method m : methods) {
            doc(m);
        }
        closeTable();
    }

    private void writeInterfaces() throws IOException {
        if (interfaces.isEmpty()) {
            return;
        }
        openTable("section-interfaces", "Interfaces", 2, true);
        for (Interface i : interfaces) {
            doc(i);
        }
        closeTable();
    }

    private void writeClasses() throws IOException {
        if (classes.isEmpty()) {
            return;
        }
        openTable("section-classes", "Classes", 2, true);
        for (Class c : classes) {
            doc(c);
        }
        closeTable();
    }
    
    private void writeExceptions() throws IOException {
        if (exceptions.isEmpty()) {
            return;
        }
        openTable("section-exceptions", "Exceptions", 2, true);
        for (Class e : exceptions) {
            doc(e);
        }
        closeTable();
    }
    
    @Override
    protected void registerAdditionalKeyboardShortcuts() throws IOException {
        registerKeyboardShortcut('p', "index.html");
        if (!aliases.isEmpty()) {
            registerKeyboardShortcut('l', "#section-aliases");
        }
        if (!annotationTypes.isEmpty() || !annotationConstructors.isEmpty()) {
            registerKeyboardShortcut('n', "#section-annotations");
        } 
        if (!attributes.isEmpty()) {
            registerKeyboardShortcut('v', "#section-attributes");
            registerKeyboardShortcut('a', "#section-attributes");
        } 
        if (!methods.isEmpty()) {
            registerKeyboardShortcut('f', "#section-methods");
            registerKeyboardShortcut('m', "#section-methods");
        }
        if (!interfaces.isEmpty()) {
            registerKeyboardShortcut('i', "#section-interfaces");
        }
        if (!classes.isEmpty()) {
            registerKeyboardShortcut('c', "#section-classes");
        }
        if (!exceptions.isEmpty()) {
            registerKeyboardShortcut('e', "#section-exceptions");
        }
    }

    @Override
    protected Object getFromObject() {
        return pkg;
    }
}
