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
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import com.redhat.ceylon.ceylondoc.Util.ReferenceableComparatorByName;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.TypeAlias;
import com.redhat.ceylon.model.typechecker.model.Value;

public class PackageDoc extends ClassOrPackageDoc {

    private final Package pkg;
    private final boolean sharingPageWithModule;
    private final SortedMap<String,Class> classes = new TreeMap<String,Class>();
    private final SortedMap<String,Interface> interfaces = new TreeMap<String,Interface>();
    private final SortedMap<String,FunctionOrValue> attributes = new TreeMap<String,FunctionOrValue>();
    private final SortedMap<String,Function> methods = new TreeMap<String,Function>();
    private final SortedMap<String,Class> exceptions = new TreeMap<String,Class>();
    private final SortedMap<String,TypeAlias> aliases = new TreeMap<String,TypeAlias>();
    private final SortedMap<String,Class> annotationTypes = new TreeMap<String,Class>();
    private final SortedMap<String,Function> annotationConstructors = new TreeMap<String,Function>();

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
                addTo(interfaces, (Interface) m);
            } else if (m instanceof Class) {
                Class c = (Class) m;
                if( c.isAnnotation() ) {
                    addTo(annotationTypes, c);
                } else  if (Util.isThrowable(c)) {
                    addTo(exceptions, c);
                } else {
                    addTo(classes, c);
                }
            } else if (m instanceof Value) {
                addTo(attributes, (Value) m);
            } else if (m instanceof Function) {
                Function method = (Function) m;
                if( m.isAnnotation() ) {
                    addTo(annotationConstructors, method);
                } else {
                    addTo(methods, method);
                }
            } else if (m instanceof TypeAlias) {
                addTo(aliases, (TypeAlias) m);
            }
        }
    }

    private <T extends Declaration> void addTo(SortedMap<String, T> map, T decl) {
        map.put(Util.getDeclarationName(decl), decl);
        for(String alias : decl.getAliases())
            map.put(alias, decl);
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
        writeTagged(pkg);
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
        for (Entry<String, TypeAlias> a : aliases.entrySet()) {
            doc(a.getKey(), a.getValue());
        }
        closeTable();
    }
    
    private void writeAnnotations() throws IOException {
        if (annotationTypes.isEmpty() && annotationConstructors.isEmpty()) {
            return;
        }
        openTable("section-annotations", "Annotations", 2, true);
        for (Entry<String, Function> annotationConstructor : annotationConstructors.entrySet()) {
            doc(annotationConstructor.getKey(), annotationConstructor.getValue());
        }
        for (Entry<String, Class> annotationType : annotationTypes.entrySet()) {
            doc(annotationType.getKey(), annotationType.getValue());
        }
        closeTable();
    }

    private void writeAttributes() throws IOException {
        if (attributes.isEmpty()) {
            return;
        }
        openTable("section-attributes", "Values", 2, true);
        for (Entry<String, FunctionOrValue> v : attributes.entrySet()) {
            doc(v.getKey(), v.getValue());
        }
        closeTable();
    }
    
    private void writeMethods() throws IOException {
        if (methods.isEmpty()) {
            return;
        }
        openTable("section-methods", "Functions", 2, true);
        for (Entry<String, Function> m : methods.entrySet()) {
            doc(m.getKey(), m.getValue());
        }
        closeTable();
    }

    private void writeInterfaces() throws IOException {
        if (interfaces.isEmpty()) {
            return;
        }
        openTable("section-interfaces", "Interfaces", 2, true);
        for (Entry<String, Interface> i : interfaces.entrySet()) {
            doc(i.getKey(), i.getValue());
        }
        closeTable();
    }

    private void writeClasses() throws IOException {
        if (classes.isEmpty()) {
            return;
        }
        openTable("section-classes", "Classes", 2, true);
        for (Entry<String, Class> c : classes.entrySet()) {
            doc(c.getKey(), c.getValue());
        }
        closeTable();
    }
    
    private void writeExceptions() throws IOException {
        if (exceptions.isEmpty()) {
            return;
        }
        openTable("section-exceptions", "Exceptions", 2, true);
        for (Entry<String, Class> e : exceptions.entrySet()) {
            doc(e.getKey(), e.getValue());
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
