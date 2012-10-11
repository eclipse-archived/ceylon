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
import static com.redhat.ceylon.ceylondoc.Util.getModifiers;
import static com.redhat.ceylon.ceylondoc.Util.getSuperInterfaces;
import static com.redhat.ceylon.ceylondoc.Util.isNullOrEmpty;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.Value;

public class ClassDoc extends ClassOrPackageDoc {

    private ClassOrInterface klass;
    /**
     * The {@linkplain #shouldInclude(Declaration) visible} 
     * methods
     */
    private List<Method> methods;
    /**
     * The {@linkplain #shouldInclude(Declaration) visible} 
     * attributes
     */
    private List<MethodOrValue> attributes;
    /**
     * The {@linkplain #shouldInclude(Declaration) visible} 
     * subclasses of the key
     */
    private List<ClassOrInterface> subclasses;
    /**
     * The {@linkplain #shouldInclude(Declaration) visible} classes/interfaces 
     * that satisfy the key
     */
    private List<ClassOrInterface> satisfyingClassesOrInterfaces;
    /**
     * The {@linkplain #shouldInclude(Declaration) visible} classes 
     * that satisfy the key
     */
    private List<Class> satisfyingClasses;
    /**
     * The {@linkplain #shouldInclude(Declaration) visible} 
     * inner interfaces
     */
    private List<Interface> innerInterfaces;
    /**
     * The {@linkplain #shouldInclude(Declaration) visible} 
     * inner classes
     */
    private List<Class> innerClasses;
    /**
     * The {@linkplain #shouldInclude(Declaration) visible} 
     * inner exceptions
     */
    private List<Class> innerExceptions;
    /**
     * The {@linkplain #shouldInclude(Declaration) visible} interfaces 
     * that satisfy the key
     */
    private List<Interface> satisfyingInterfaces;
    /**
     * The {@linkplain #shouldInclude(Declaration) visible} 
     * superinterfaces
     */
    private List<ProducedType> superInterfaces;
    /**
     * The {@linkplain #shouldInclude(Declaration) visible} 
     * superclasses
     */
    private List<TypeDeclaration> superClasses;
    private boolean inheritedSectionOpen;
    private String inheritedSectionCategory;
    private Map<MemberSpecification, Map<TypeDeclaration, List<Declaration>>> superclassInheritedMembers = new HashMap<ClassDoc.MemberSpecification, Map<TypeDeclaration,List<Declaration>>>(2);
    private Map<MemberSpecification, Map<TypeDeclaration, List<Declaration>>> interfaceInheritedMembers = new HashMap<ClassDoc.MemberSpecification, Map<TypeDeclaration,List<Declaration>>>(2);

    private Comparator<Declaration> comparator = new Comparator<Declaration>() {
        @Override
        public int compare(Declaration a, Declaration b) {
            return a.getName().compareTo(b.getName());
        }
    };

    private Comparator<ProducedType> producedTypeComparator = new Comparator<ProducedType>() {
        @Override
        public int compare(ProducedType a, ProducedType b) {
            return a.getDeclaration().getName().compareTo(b.getDeclaration().getName());
        }
    };

    interface MemberSpecification {
        boolean isSatisfiedBy(Declaration decl);
    }

    MemberSpecification attributeSpecification = new MemberSpecification() {
        @Override
        public boolean isSatisfiedBy(Declaration decl) {
            return decl instanceof Value || decl instanceof Getter;
        }
    };

    MemberSpecification methodSpecification = new MemberSpecification() {
        @Override
        public boolean isSatisfiedBy(Declaration decl) {
            return decl instanceof Method;
        }
    };

    /**
     * 
     * @param tool
     * @param klass
     * @param subclasses The {@linkplain #shouldInclude(Declaration) visible} 
     * subclasses of the key
     * @param satisfyingClassesOrInterfaces The 
     * {@linkplain #shouldInclude(Declaration) visible} class/interfaces 
     * that satisfy the key
     * @throws IOException
     */
    public ClassDoc(CeylonDocTool tool, Writer writer,
            ClassOrInterface klass, List<ClassOrInterface> subclasses, List<ClassOrInterface> satisfyingClassesOrInterfaces) throws IOException {
        super(tool.getModule(klass), tool, writer);
        if (subclasses != null) {
            this.subclasses = subclasses;
        } else {
            this.subclasses = new ArrayList<ClassOrInterface>();
        }
        if (satisfyingClassesOrInterfaces != null) {
            this.satisfyingClassesOrInterfaces = satisfyingClassesOrInterfaces;
        } else {
            this.satisfyingClassesOrInterfaces = new ArrayList<ClassOrInterface>();
        }

        this.klass = klass;
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
            if (shouldInclude(m)) {
                if (m instanceof Value) {
                    attributes.add((Value) m);
                }
                else if (m instanceof Getter) {
                    attributes.add((Getter) m);
                }
                else if (m instanceof Method) {
                    methods.add((Method) m);
                }
                else if (m instanceof Interface) {
                    innerInterfaces.add((Interface) m);
                }
                else if (m instanceof Class) {
                    Class c = (Class) m;
                    if( Util.isException(c) ) {
                        innerExceptions.add(c);
                    }
                    else {
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

        Collections.sort(methods, comparator);
        Collections.sort(attributes, comparator);
        Collections.sort(subclasses, comparator);
        Collections.sort(satisfyingClasses, comparator);
        Collections.sort(satisfyingInterfaces, comparator);
        Collections.sort(superInterfaces, producedTypeComparator);
        Collections.sort(innerInterfaces, comparator);
        Collections.sort(innerClasses, comparator);
        Collections.sort(innerExceptions, comparator);
        
        loadSuperclassInheritedMembers(attributeSpecification);
        loadSuperclassInheritedMembers(methodSpecification);
        loadInterfaceInheritedMembers(attributeSpecification);
        loadInterfaceInheritedMembers(methodSpecification);
    }

    public void generate() throws IOException {
        htmlHead();
        summary();
        innerInterfaces();
        innerClasses();
        innerExceptions();
        
        if (hasAnyAttributes()) {
            open("div id='section-attributes'");
            attributes();
            close("div");
            inheritedSectionCategory = "attributes";
            writeSuperclassInheritedMembers(attributeSpecification, "Attributes inherited from class: ");
            writeInterfaceInheritedMembers(attributeSpecification, "Attributes inherited from interface: ");
            makeSureInheritedSectionIsClosed();
        }

        if (hasConstructor()) {
            constructor((Class) klass);
        }
        
        if (hasAnyMethods()) {
            open("div id='section-methods'");
            methods();
            close("div");
            inheritedSectionCategory = "methods";
            writeSuperclassInheritedMembers(methodSpecification, "Methods inherited from class: ");
            writeInterfaceInheritedMembers(methodSpecification, "Methods inherited from interface: ");
            makeSureInheritedSectionIsClosed();
        }
        
        close("body");
        close("html");
    }

    private void htmlHead() throws IOException {
        htmlHead("Class for " + klass.getName());
    }

    private List<Declaration> getInheritedMembers(TypeDeclaration decl, MemberSpecification specification) {
        List<Declaration> members = new ArrayList<Declaration>();
        for (Declaration m : decl.getMembers())
            if (shouldInclude(m) && specification.isSatisfiedBy(m)) {
                members.add((MethodOrValue) m);
            }
        return members;
    }

    private void writeSuperclassInheritedMembers(MemberSpecification specification, String title) throws IOException {
        Map<TypeDeclaration, List<Declaration>> inheritedMembers = superclassInheritedMembers.get(specification);
        for (Map.Entry<TypeDeclaration, List<Declaration>> entry : inheritedMembers.entrySet()) {
            TypeDeclaration superClass = entry.getKey();
            List<Declaration> notRefined = entry.getValue();
            makeSureInheritedSectionIsOpen();
            open("table");
            open("tr class='TableHeadingColor'");
            open("th");
            write(title);
            open("code");
            linkToDeclaration(superClass);
            close("code");
            close("th");
            close("tr");

            open("tr class='TableRowColor'");
            open("td", "code");
            boolean first = true;
            for (Declaration member : notRefined) {
                if (!first) {
                    write(", ");
                } else {
                    first = false;
                }
                linkToDeclaration(member);
            }
            close("code", "td", "tr", "table");
        }
        
    }
    
    /**
     * Gets the attributes inherited from interfaces.
     * @return A map of interface to the attributes this type inherits from 
     * that interface.
     */
    private Map<TypeDeclaration, List<Declaration>> getInterfaceInheritedAttibutes() {
        return interfaceInheritedMembers.get(attributeSpecification);
    }
    
    /**
     * Gets the methods inherited from interfaces.
     * @return A map of interface to the methods this type inherits from 
     * that interface.
     */
    private Map<TypeDeclaration, List<Declaration>> getInterfaceInheritedMethods() {
        return interfaceInheritedMembers.get(methodSpecification);
    }
    
    /**
     * Gets the attributes inherited from superclasses.
     * @return A map of superclass to the attributes this type inherits from 
     * that superclass.
     */
    private Map<TypeDeclaration, List<Declaration>> getSuperclassInheritedAttibutes() {
        return superclassInheritedMembers.get(attributeSpecification);
    }
    
    /**
     * Gets the methods inherited from superclasses.
     * @return A map of superclass to the methods this type inherits from 
     * that superclass.
     */
    private Map<TypeDeclaration, List<Declaration>> getSuperclassInheritedMethods() {
        return superclassInheritedMembers.get(methodSpecification);
    }
    
    /**
     * Determines whether the type has any attributes (include any inherited from
     * superclasses or superinterfaces). 
     * @return true if the type has any attributes.
     */
    private boolean hasAnyAttributes() {
        return !(attributes.isEmpty() 
                    && getInterfaceInheritedAttibutes().isEmpty()
                    && getSuperclassInheritedAttibutes().isEmpty());
    }
    
    /**
     * Determines whether the type has any methods (include any inherited from
     * superclasses or superinterfaces). 
     * @return true if the type has any methods.
     */
    private boolean hasAnyMethods() {
        return !(methods.isEmpty() 
                    && getInterfaceInheritedMethods().isEmpty()
                    && getSuperclassInheritedMethods().isEmpty());
    }

    private void loadSuperclassInheritedMembers(
            MemberSpecification specification) {
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

    private void makeSureInheritedSectionIsOpen() throws IOException {
        if(inheritedSectionOpen)
            return;
        inheritedSectionOpen = true;
        open("div id='section-inherited-"+inheritedSectionCategory+"' class='collapsible'");
        around("div class='short'", "Show inherited "+inheritedSectionCategory);
        open("div class='long'");
    }

    private void makeSureInheritedSectionIsClosed() throws IOException {
        if(!inheritedSectionOpen)
            return;
        inheritedSectionOpen = false;
        close("div", "div");
    }

    private void writeInterfaceInheritedMembers(MemberSpecification memberSpecification, String title) throws IOException {
        Map<TypeDeclaration, List<Declaration>> inheritedMembers = interfaceInheritedMembers.get(memberSpecification);
        for (TypeDeclaration superInterface : inheritedMembers.keySet()) {
            List<Declaration> members = inheritedMembers.get(superInterface);
            if (members == null 
                    || members.isEmpty())
                continue;
            makeSureInheritedSectionIsOpen();
            open("table");
            open("tr class='TableHeadingColor'");
            open("th");
            write(title);
            open("code");
            linkToDeclaration(superInterface);
            close("code");
            close("th");
            close("tr");

            open("tr class='TableRowColor'");
            open("td", "code");
            boolean first = true;
            for (Declaration member : members) {
                if (!first) {
                    write(", ");
                } else {
                    first = false;
                }
                linkToDeclaration(member);
            }
            close("code", "td", "tr", "table");
        }

    }

    private void loadInterfaceInheritedMembers(
            MemberSpecification memberSpecification) {
        LinkedHashMap<TypeDeclaration, List<Declaration>> result = new LinkedHashMap<TypeDeclaration, List<Declaration>>();
        for (ProducedType superInterface : superInterfaces) {
            TypeDeclaration decl = superInterface.getDeclaration();
            List<Declaration> members = getInheritedMembers(decl, memberSpecification);
            for (Declaration member : members) {
                
                Declaration refined = member.getRefinedDeclaration();
                if (refined == null || refined == member || refined.getContainer() instanceof Class) {
                    List<Declaration> r = result.get(refined.getContainer());
                    if (r == null) {
                        r = new ArrayList<Declaration>();
                        result.put((TypeDeclaration)refined.getContainer(), r);
                    }
                    r.add(refined);
                }
            }
        }
        interfaceInheritedMembers.put(memberSpecification, result);
    }

    private void innerInterfaces() throws IOException {
        innerTypeDeclarations(innerInterfaces, "section-nested_interfaces", "Nested Interfaces");
    }

    private void innerClasses() throws IOException {
        innerTypeDeclarations(innerClasses, "section-nested_classes", "Nested Classes");
    }

    private void innerExceptions() throws IOException {
        innerTypeDeclarations(innerExceptions, "section-nested_exceptions", "Nested Exceptions");
    }

    private void innerTypeDeclarations(List<? extends ClassOrInterface> innerTypeDeclarations, String id, String title) throws IOException {
        if (!innerTypeDeclarations.isEmpty()) {
            openTable(id, title, "Modifiers", "Name and Description");
            for (ClassOrInterface innerTypeDeclaration : innerTypeDeclarations) {
                tool.doc(innerTypeDeclaration);
                doc(innerTypeDeclaration);
            }
            close("table");
        }
    }

    private void doc(ClassOrInterface c) throws IOException {
        open("tr class='TableRowColor'");
        open("td");
        around("span class='modifiers'", getModifiers(c));
        close("td");
        open("td");
        writeIcon(c);
        linkToDeclaration(c);
        writeTagged(c);
        tag("br");
        writeDescription(c);
        close("td");
        close("tr");
    }

    private void summary() throws IOException {
        Package pkg = tool.getPackage(klass);

        writeNav(pkg.getModule(), klass, DocType.TYPE);
        subMenu();
        open("div class='head summary'");

        // name
        around("div class='package'", "<code>" + pkg.getNameAsString() + "</code>");

        open("div class='type'");
        write(klass instanceof Class ? "Class " : "Interface ");
        writeIcon(klass);
        write("<code>", getClassName(), "</code>");
        writeSourceLink(klass);
        close("div");
        
        // hierarchy tree - only for classes
        if (klass instanceof Class) {
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
                writeIcon(superType.getDeclaration());
                linkRenderer().to(superType).write();
                i++;
            }
            while (i-- > 0) {
                close("li", "ul");
            }
        }

        tag("br");
        // interfaces
        writeListOnSummary2("satisfied", "Satisfied Interfaces: ", superInterfaces);

        // subclasses
        writeListOnSummary("subclasses", "Direct Known Subclasses: ", subclasses);

        // satisfying classes
        writeListOnSummary("satisfyingClasses", "All Known Satisfying Classes: ", satisfyingClasses);

        // satisfying interfaces
        writeListOnSummary("satisfyingClasses", "All Known Satisfying Interfaces: ", satisfyingInterfaces);
        
        // enclosing class or interface 
        writeEnclosingType();

        // description
        around("div class='doc'", getDoc(klass, linkRenderer()));

        writeBy(klass);
        writeSee(klass);
        writeTagged(klass);
        
        close("div");
    }

    protected void subMenu() throws IOException {
        if (hasAnyAttributes()
                || hasConstructor()
                || hasAnyMethods()) {
            open("div class='submenu'");
            if (hasAnyAttributes()) {
                printSubMenuItem("section-attributes", getAccessKeyed("Attributes", 'A', "Jump to attributes"));
            }
            if (hasConstructor()) {
                printSubMenuItem("section-constructor", getAccessKeyed("Constructor", 'C', "Jump to constructor"));
            }
            if (hasAnyMethods()) {
                printSubMenuItem("section-methods", getAccessKeyed("Methods", 'M', "Jump to methods"));
            }
            close("div");
        }
    }

    private boolean hasConstructor() {
        return (klass instanceof Class);
    }
    
    @Override
    protected Object getFromObject() {
        return klass;
    }

    private String getClassName() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(klass.getName());
        if (isNullOrEmpty(klass.getTypeParameters()) == false) {
            sb.append("&lt;");
            boolean first = true;
            for (TypeParameter typeParam : klass.getTypeParameters()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append(typeParam.getName());
            }
            sb.append("&gt;");
        }
        return sb.toString();
    }

    private void constructor(Class klass) throws IOException {
        openTable("section-constructor", "Constructor");
        open("tr", "td");
        
        open("code");
        writeIcon(klass);
        write(klass.getName());
        writeParameterList(klass);
        close("code");
        
        open("div class='description'", "p");
        writeParameters(klass);
        close("p", "div");
        
        close("td", "tr", "table");
    }

    private void methods() throws IOException {
        if (methods.isEmpty()){
            return;
        }
        openTable(null, "Methods", "Modifier and Type", "Method and Description");

        for (Method m : methods) {
            doc(m);
        }
        close("table");
    }

    private void attributes() throws IOException {
        if (attributes.isEmpty()){
            return;
        }
        openTable(null, "Attributes", "Modifier and Type", "Name and Description");
        for (MethodOrValue attribute : attributes) {
            doc(attribute);
        }
        close("table");
    }

    private void writeListOnSummary(String divClass, String label, List<? extends ClassOrInterface> list) throws IOException {
        if (isNullOrEmpty(list) == false) {
            boolean first = true;
            open("div class='" + divClass + "'");
            write(label);
            for (ClassOrInterface typeDeclaration : list) {
                if (!first) {
                    write(", ");
                } else {
                    first = false;
                }
                linkRenderer().to(typeDeclaration).skipTypeArguments().write();
            }
            close("div");
        }
    }

    private void writeListOnSummary2(String divClass, String label, List<ProducedType> list) throws IOException {
        if (isNullOrEmpty(list) == false) {
            boolean first = true;
            open("div class='" + divClass + "'");
            write(label);
            for (ProducedType typeDeclaration : list) {
                if (!first) {
                    write(", ");
                } else {
                    first = false;
                }
                linkRenderer().to(typeDeclaration).write();
            }
            close("div");
        }
    }
    
    private void writeEnclosingType() throws IOException {
        if (klass.isMember()) {
            ClassOrInterface enclosingType = (ClassOrInterface) klass.getContainer();
            open("div class='enclosingType'");
            write("Enclosing " + (enclosingType instanceof Class ? "class: " : "interface: "));
            writeIcon(enclosingType);
            linkToDeclaration(enclosingType);
            close("div");
        }
    }
    
    @Override
    protected void writeAdditionalKeyboardShortcuts() throws IOException {
        writeKeyboardShortcut('p', "index.html");
        if (hasAnyAttributes()) {
            writeKeyboardShortcut('a', "#section-attributes");
        }
        if (hasConstructor()) {
            writeKeyboardShortcut('c', "#section-constructor");
        }
        if (hasAnyMethods()) {
            writeKeyboardShortcut('m', "#section-methods");
        }
    }
}
