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

import static com.redhat.ceylon.ceylondoc.Util.getAncestors;
import static com.redhat.ceylon.ceylondoc.Util.getDoc;
import static com.redhat.ceylon.ceylondoc.Util.getSuperInterfaces;
import static com.redhat.ceylon.ceylondoc.Util.isNullOrEmpty;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.ceylondoc.Util.DeclarationComparatorByName;
import com.redhat.ceylon.ceylondoc.Util.ProducedTypeComparatorByName;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;

public class ClassDoc extends ClassOrPackageDoc {

    private ClassOrInterface klass;
    private List<Method> methods;
    private List<MethodOrValue> attributes;
    private List<ClassOrInterface> subclasses;
    private List<ClassOrInterface> satisfyingClassesOrInterfaces;
    private List<Class> satisfyingClasses;
    private List<Interface> innerInterfaces;
    private List<Class> innerClasses;
    private List<Class> innerExceptions;
    private List<Interface> satisfyingInterfaces;
    private List<ProducedType> superInterfaces;
    private List<TypeDeclaration> superClasses;
    private Map<MemberSpecification, Map<TypeDeclaration, List<Declaration>>> superclassInheritedMembers = new HashMap<ClassDoc.MemberSpecification, Map<TypeDeclaration,List<Declaration>>>(2);
    private Map<MemberSpecification, Map<TypeDeclaration, List<Declaration>>> interfaceInheritedMembers = new HashMap<ClassDoc.MemberSpecification, Map<TypeDeclaration,List<Declaration>>>(2);

    private interface MemberSpecification {
        boolean isSatisfiedBy(Declaration decl);
    }

    private MemberSpecification attributeSpecification = new MemberSpecification() {
        @Override
        public boolean isSatisfiedBy(Declaration decl) {
            return decl instanceof Value || decl instanceof Getter;
        }
    };

    private MemberSpecification methodSpecification = new MemberSpecification() {
        @Override
        public boolean isSatisfiedBy(Declaration decl) {
            return decl instanceof Method;
        }
    };

    public ClassDoc(CeylonDocTool tool, Writer writer, ClassOrInterface klass, List<ClassOrInterface> subclasses, List<ClassOrInterface> satisfyingClassesOrInterfaces) throws IOException {
        super(tool.getModule(klass), tool, writer);
        
        this.klass = klass;
        this.subclasses = (subclasses != null) ? subclasses : new ArrayList<ClassOrInterface>(); 
        this.satisfyingClassesOrInterfaces = (satisfyingClassesOrInterfaces != null) ? satisfyingClassesOrInterfaces : new ArrayList<ClassOrInterface>();
        
        loadMembers();
    }

    private void loadMembers() {
        methods = new ArrayList<Method>();
        satisfyingClasses = new ArrayList<Class>();
        satisfyingInterfaces = new ArrayList<Interface>();
        attributes = new ArrayList<MethodOrValue>();
        innerInterfaces = new ArrayList<Interface>();
        innerClasses = new ArrayList<Class>();
        innerExceptions = new ArrayList<Class>();
        superClasses = getAncestors(klass);
        superInterfaces = getSuperInterfaces(klass);
        
        for (Declaration m : klass.getMembers()) {
            if (tool.shouldInclude(m)) {
                if (m instanceof Value) {
                    attributes.add((Value) m);
                } else if (m instanceof Getter) {
                    attributes.add((Getter) m);
                } else if (m instanceof Method) {
                    methods.add((Method) m);
                } else if (m instanceof Interface) {
                    innerInterfaces.add((Interface) m);
                } else if (m instanceof Class) {
                    Class c = (Class) m;
                    if (Util.isException(c)) {
                        innerExceptions.add(c);
                    } else {
                        innerClasses.add(c);
                    }
                }
            }
        }

        for (ClassOrInterface classOrInterface : satisfyingClassesOrInterfaces) {
            if (classOrInterface instanceof Class) {
                satisfyingClasses.add((Class) classOrInterface);
            } else if (classOrInterface instanceof Interface) {
                satisfyingInterfaces.add((Interface) classOrInterface);
            }
        }

        Collections.sort(methods, DeclarationComparatorByName.INSTANCE);
        Collections.sort(attributes, DeclarationComparatorByName.INSTANCE);
        Collections.sort(subclasses, DeclarationComparatorByName.INSTANCE);
        Collections.sort(satisfyingClasses, DeclarationComparatorByName.INSTANCE);
        Collections.sort(satisfyingInterfaces, DeclarationComparatorByName.INSTANCE);
        Collections.sort(superInterfaces, ProducedTypeComparatorByName.INSTANCE);
        Collections.sort(innerInterfaces, DeclarationComparatorByName.INSTANCE);
        Collections.sort(innerClasses, DeclarationComparatorByName.INSTANCE);
        Collections.sort(innerExceptions, DeclarationComparatorByName.INSTANCE);
        
        loadSuperclassInheritedMembers(attributeSpecification);
        loadSuperclassInheritedMembers(methodSpecification);
        loadInterfaceInheritedMembers(attributeSpecification);
        loadInterfaceInheritedMembers(methodSpecification);
    }

    private void loadSuperclassInheritedMembers(MemberSpecification specification) {
        LinkedHashMap<TypeDeclaration, List<Declaration>> inheritedMembers = new LinkedHashMap<TypeDeclaration, List<Declaration>>();
        TypeDeclaration subclass = klass;
        for (TypeDeclaration superClass : superClasses) {
            List<Declaration> methods = getInheritedMembers(superClass, specification);
            if (methods.isEmpty())
                continue;
            List<Declaration> notRefined = new ArrayList<Declaration>();
            // clean already listed methods (refined in subclasses)
            // done in 2 phases to avoid empty tables
            for (Declaration method : methods) {
                if (subclass.getDirectMember(method.getName(), null, false) == null) {
                    notRefined.add(method);
                }
            }
            if (notRefined.isEmpty())
                continue;
            inheritedMembers.put(superClass, notRefined);
        }
        superclassInheritedMembers.put(specification, inheritedMembers);
    }

    private void loadInterfaceInheritedMembers(MemberSpecification memberSpecification) {
        LinkedHashMap<TypeDeclaration, List<Declaration>> result = new LinkedHashMap<TypeDeclaration, List<Declaration>>();
        for (ProducedType superInterface : superInterfaces) {
            TypeDeclaration decl = superInterface.getDeclaration();
            List<Declaration> members = getInheritedMembers(decl, memberSpecification);
            for (Declaration member : members) {
                
                Declaration refined = member.getRefinedDeclaration();
                if (refined == null
                        || refined == member
                        || (refined.getContainer() instanceof Class && refined.getContainer() != klass)) {
                    List<Declaration> r = result.get(member.getContainer());
                    if (r == null) {
                        r = new ArrayList<Declaration>();
                        result.put((TypeDeclaration)member.getContainer(), r);
                    }
                    r.add(member);
                }
            }
        }
        interfaceInheritedMembers.put(memberSpecification, result);
    }

    private List<Declaration> getInheritedMembers(TypeDeclaration decl, MemberSpecification specification) {
        List<Declaration> members = new ArrayList<Declaration>();
        for (Declaration m : decl.getMembers())
            if (tool.shouldInclude(m) && specification.isSatisfiedBy(m)) {
                members.add((MethodOrValue) m);
            }
        return members;
    }
    
    private boolean isObject() {
        return klass instanceof Class && klass.isAnonymous();
    }

    private boolean hasConstructor() {
        return klass instanceof Class && !isObject();
    }

    /**
     * Determines whether the type has any attributes (include any inherited from superclasses or superinterfaces). 
     * @return true if the type has any attributes.
     */
    private boolean hasAnyAttributes() {
        return !(attributes.isEmpty() 
                    && interfaceInheritedMembers.get(attributeSpecification).isEmpty()
                    && superclassInheritedMembers.get(attributeSpecification).isEmpty());
    }

    /**
     * Determines whether the type has any methods (include any inherited from superclasses or superinterfaces). 
     * @return true if the type has any methods.
     */
    private boolean hasAnyMethods() {
        return !(methods.isEmpty() 
                    && interfaceInheritedMembers.get(methodSpecification).isEmpty()
                    && superclassInheritedMembers.get(methodSpecification).isEmpty());
    }
    
    private String getClassLabel() {
        if (klass instanceof Interface) {
            return "interface";
        } else {
            return isObject() ? "object" : "class";
        }
    }

    public void generate() throws IOException {
        writeHeader(Util.capitalize(getClassLabel()) + " " + klass.getName());
        writeNavBar();
        writeSubNavBar();
        
        open("div class='container-fluid'");
        
        writeDescription();
        
        writeInnerTypes(innerInterfaces, "section-nested_interfaces", "Nested Interfaces");
        writeInnerTypes(innerClasses, "section-nested_classes", "Nested Classes");
        writeInnerTypes(innerExceptions, "section-nested_exceptions", "Nested Exceptions");
        
        if (hasConstructor()) {
            writeConstructor((Class) klass);
        }

        if (hasAnyAttributes()) {
            open("div id='section-attributes'");
            writeAttributes();
            writeInheritedMembers(attributeSpecification, "Inherited Attributes", "Attributes inherited from: ");
            close("div");
        }

        if (hasAnyMethods()) {
            open("div id='section-methods'");
            writeMethods();
            writeInheritedMembers(methodSpecification, "Inherited Methods", "Methods inherited from: ");
            close("div");
        }
        
        close("div");
        
        writeFooter();
    }

    private void writeSubNavBar() throws IOException {
        Package pkg = tool.getPackage(klass);
        
        open("div class='sub-navbar'");
        
        writeLinkSourceCode(klass);
        
        open("div class='sub-navbar-inner'");
        
        around("a class='sub-navbar-package' href='"+ linkRenderer().to(pkg).getUrl() +"'", pkg.getNameAsString());
        write("<br/>");
        writeClassName();
        close("div"); // sub-navbar-inner
        
        open("div class='sub-navbar-menu'");
        writeSubNavBarLink(linkRenderer().to(module).getUrl(), "Overview", 'O', "Jump to module documentation");
        writeSubNavBarLink(linkRenderer().to(pkg).getUrl(), "Package", 'P', "Jump to package documentation");
        if (hasAnyAttributes()) {
            writeSubNavBarLink("#section-attributes", "Attributes", 'A', "Jump to attributes");
        }
        if (hasConstructor()) {
            writeSubNavBarLink("#section-constructor", "Constructor", 'C', "Jump to constructor");
        }
        if (hasAnyMethods()) {
            writeSubNavBarLink("#section-methods", "Methods", 'M', "Jump to methods");
        }
        if (!innerInterfaces.isEmpty()) {
            writeSubNavBarLink("#section-interfaces", "Nested Interfaces", 'I', "Jump to nested interfaces");
        }
        if (!innerClasses.isEmpty()) {
            writeSubNavBarLink("#section-classes", "Nested Classes", 'C', "Jump to nested classes");
        }
        if (!innerExceptions.isEmpty()) {
            writeSubNavBarLink("#section-exceptions", "Nested Exceptions", 'E', "Jump to nested exceptions");
        }
        if( isObject() ) {
            writeSubNavBarLink(linkRenderer().to(pkg).useAnchor(klass).getUrl(), "Singleton object declaration", '\0', "Jump to singleton object declaration");
        }
        
        close("div"); // sub-navbar-menu
        
        close("div"); // sub-navbar
    }
    
    private void writeClassName() throws IOException {
        open("span class='sub-navbar-label'");
        write(getClassLabel());
        close("span");
        writeIcon(klass);
        open("span class='sub-navbar-name'");
        write(klass.getName());
        writeTypeParameters(klass.getTypeParameters());
        close("span");
    }    

    private void writeDescription() throws IOException {
        open("div class='class-description'");
        
        writeTagged(klass);
        writeTypeHierarchy();
        writeListOnSummary("satisfied", "Satisfied Interfaces: ", superInterfaces);
        writeListOnSummary("subclasses", "Direct Known Subclasses: ", subclasses);
        writeListOnSummary("satisfyingClasses", "All Known Satisfying Classes: ", satisfyingClasses);
        writeListOnSummary("satisfyingInterfaces", "All Known Satisfying Interfaces: ", satisfyingInterfaces);
        writeEnclosingType();
    
        around("div class='doc'", getDoc(klass, linkRenderer()));
        writeBy(klass);
        writeSee(klass);
        
        close("div"); // class-description
    }

    private void writeTypeHierarchy() throws IOException {
        if (klass instanceof Class) {
            open("div class='typeHierarchy section'");
            around("span class='title'", "Type Hierarchy:");
            
            LinkedList<ProducedType> superTypes = new LinkedList<ProducedType>();
            superTypes.add(klass.getType());
            ProducedType type = klass.getExtendedType();
            while (type != null) {
                superTypes.add(0, type);
                type = type.getDeclaration().getExtendedType();
            }
            int i = 0;
            for (ProducedType superType : superTypes) {
                open("ul class='inheritance'", "li");
                if( i != 0) {
                    around("i class='icon-indentation'");
                } else {
                    around("i class='icon-none'");
                }
                
                writeIcon(superType.getDeclaration());
                around("span class='decl' title='"+ superType.getProducedTypeQualifiedName() +"'", linkRenderer().to(superType).getLink());
                i++;
            }
            while (i-- > 0) {
                close("li", "ul");
            }
            
            close("div");
        }
    }

    private void writeListOnSummary(String cssClass, String title, List<?> types) throws IOException {
        if (!isNullOrEmpty(types)) {
            open("div class='" + cssClass + " section'");
            around("span class='title'", title);
            
            boolean first = true;
            for (Object type : types) {
                if (!first) {
                    write(", ");
                } else {
                    first = false;
                }
                
                if( type instanceof ClassOrInterface ) {
                    linkRenderer().to((ClassOrInterface)type).skipTypeArguments().write();
                } else {
                    linkRenderer().to((ProducedType)type).write(); 
                }
            }
            close("div");
        }
    }

    private void writeEnclosingType() throws IOException {
        if (klass.isMember()) {
            ClassOrInterface enclosingType = (ClassOrInterface) klass.getContainer();
            open("div class='enclosingType section'");
            around("span class='title'", "Enclosing " + (enclosingType instanceof Class ? "class: " : "interface: "));
            writeIcon(enclosingType);
            linkToDeclaration(enclosingType);
            close("div");
        }
    }

    private void writeInheritedMembers(MemberSpecification specification, String tableTitle, String rowTitle) throws IOException {
        boolean first = true;
        
        Map<TypeDeclaration, List<Declaration>> superClassInheritedMembersMap = superclassInheritedMembers.get(specification);
        List<TypeDeclaration> superClasses = new ArrayList<TypeDeclaration>(superclassInheritedMembers.get(specification).keySet());
        Collections.sort(superClasses, DeclarationComparatorByName.INSTANCE);
        
        for (TypeDeclaration superClass : superClasses) {
            List<Declaration> inheritedMembers = superClassInheritedMembersMap.get(superClass);
            Collections.sort(inheritedMembers, DeclarationComparatorByName.INSTANCE);

            if (first) {
                first = false;
                openTable(null, tableTitle, 1, false);
            }
            writeInheritedMembersRow(rowTitle, superClass, inheritedMembers);
        }

        Map<TypeDeclaration, List<Declaration>> superInterfaceInheritedMembersMap = interfaceInheritedMembers.get(specification);
        List<TypeDeclaration> superInterfaces = new ArrayList<TypeDeclaration>(superInterfaceInheritedMembersMap.keySet());
        Collections.sort(superInterfaces, DeclarationComparatorByName.INSTANCE);
        
        for (TypeDeclaration superInterface : superInterfaces) {
            List<Declaration> members = superInterfaceInheritedMembersMap.get(superInterface);
            if (members == null || members.isEmpty()) {
                continue;
            }
            Collections.sort(members, DeclarationComparatorByName.INSTANCE);

            if (first) {
                first = false;
                openTable(null, tableTitle, 1, false);
            }
            writeInheritedMembersRow(rowTitle, superInterface, members);
        }

        if (!first) {
            closeTable();
        }
    }

    private void writeInheritedMembersRow(String title, TypeDeclaration superType, List<Declaration> members) throws IOException {
        open("tr", "td");
        write(title);
        writeIcon(superType);
        linkToDeclaration(superType);
        open("div class='inherited-members'");
        
        boolean first = true;
        for (Declaration member : members) {
            if (!first) {
                write(", ");
            } else {
                first = false;
            }
            linkToDeclaration(member);
        }
        
        close("div");
        close("td", "tr");
    }
    
    private void writeInnerTypes(List<? extends ClassOrInterface> innerTypeDeclarations, String id, String title) throws IOException {
        if (!innerTypeDeclarations.isEmpty()) {
            openTable(id, title, 2, true);
            for (ClassOrInterface innerTypeDeclaration : innerTypeDeclarations) {
                tool.doc(innerTypeDeclaration);
                doc(innerTypeDeclaration);
            }
            closeTable();
        }
    }

    private void writeConstructor(Class klass) throws IOException {
        openTable("section-constructor", "Constructor", 1, true);
        open("tr", "td");
        
        writeIcon(klass);
        write(klass.getName());
        writeParameterList(klass);
        
        open("div class='description'");
        writeParameters(klass);
        close("div");
        
        close("td", "tr");
        closeTable();
    }

    private void writeAttributes() throws IOException {
        if (attributes.isEmpty()){
            return;
        }
        openTable(null, "Attributes", 2, true);
        for (MethodOrValue attribute : attributes) {
            doc(attribute);
        }
        closeTable();
    }

    private void writeMethods() throws IOException {
        if (methods.isEmpty()){
            return;
        }
        openTable(null, "Methods", 2, true);
        for (Method m : methods) {
            doc(m);
        }
        closeTable();
    }

    @Override
    protected void registerAdditionalKeyboardShortcuts() throws IOException {
        registerKeyboardShortcut('p', "index.html");
        if (hasAnyAttributes()) {
            registerKeyboardShortcut('a', "#section-attributes");
        }
        if (hasConstructor()) {
            registerKeyboardShortcut('c', "#section-constructor");
        }
        if (hasAnyMethods()) {
            registerKeyboardShortcut('m', "#section-methods");
        }
    }

    @Override
    protected Object getFromObject() {
        return klass;
    }
    
}