package com.redhat.ceylon.ceylondoc;

import static com.redhat.ceylon.ceylondoc.Util.getAncestors;
import static com.redhat.ceylon.ceylondoc.Util.getDoc;
import static com.redhat.ceylon.ceylondoc.Util.getModifiers;
import static com.redhat.ceylon.ceylondoc.Util.getSuperInterfaces;
import static com.redhat.ceylon.ceylondoc.Util.isNullOrEmpty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
    private List<Method> methods;
    private List<MethodOrValue> attributes;
    private List<ClassOrInterface> subclasses;
    private List<ClassOrInterface> satisfyingClassesOrInterfaces;
    private List<Class> satisfyingClasses;
    private List<Class> innerClasses;
    private List<Interface> satisfyingInterfaces;
    private List<ProducedType> superInterfaces;
    private List<TypeDeclaration> superClasses;

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

    MemberSpecification atributeSpecification = new MemberSpecification() {
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

    public ClassDoc(CeylonDocTool tool,   
            ClassOrInterface klass, List<ClassOrInterface> subclasses, List<ClassOrInterface> satisfyingClassesOrInterfaces) throws IOException {
        super(tool.getModule(klass), tool, tool.getObjectFile(klass));
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
        innerClasses = new ArrayList<Class>();
        superClasses = getAncestors(klass);
        superInterfaces = getSuperInterfaces(klass);
        for (Declaration m : klass.getMembers()) {
            if (include(m)) {
                if (m instanceof Value)
                    attributes.add((Value) m);
                else if (m instanceof Getter)
                    attributes.add((Getter) m);
                else if (m instanceof Method)
                    methods.add((Method) m);
                else if (m instanceof Class)
                    innerClasses.add((Class) m);
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
        Collections.sort(innerClasses, comparator);
    }

    public void generate() throws IOException {
        setupWriter();
        open("html");
        open("head");
        around("title", "Class for " + klass.getName());
        tag("link href='" + getResourceUrl("style.css") +"' rel='stylesheet' type='text/css'");
        open("script type='text/javascript' src='"+getResourceUrl("jquery-1.7.min.js")+"'");
        close("script");
        open("script type='text/javascript' src='"+getResourceUrl("ceylond.js")+"'");
        close("script");
        close("head");
        open("body");
        summary();
        innerClasses();
        attributes();
        inheritedMembers(atributeSpecification, "Attributes inherited from class: ");
        inheritedMembersFromInterfaces(atributeSpecification, "Attributes inherited from interface: ");
        if (klass instanceof Class)
            constructor((Class) klass);
        methods();
        inheritedMembers(methodSpecification, "Methods inherited from class: ");
        inheritedMembersFromInterfaces(methodSpecification, "Methods inherited from interface: ");
        close("body");
        close("html");
        writer.flush();
        writer.close();
    }

    public List<Declaration> getConcreteSharedMembers(TypeDeclaration decl, MemberSpecification specification) {
        List<Declaration> members = new ArrayList<Declaration>();
        for (Declaration m : decl.getMembers())
            if ((m.isShared() && !m.isFormal()) && specification.isSatisfiedBy(m))
                members.add((MethodOrValue) m);
        return members;
    }

    private void inheritedMembers(MemberSpecification specification, String title) throws IOException {
        if (superClasses.isEmpty())
            return;
        TypeDeclaration subclass = klass;
        for (TypeDeclaration superClass : superClasses) {
            List<Declaration> methods = getConcreteSharedMembers(superClass, specification);
            if (methods.isEmpty())
                continue;
            List<Declaration> notRefined = new ArrayList<Declaration>();
            // clean already listed methods (refined in subclasses)
            // done in 2 phases to avoid empty tables
            for (Declaration method : methods) {
                if (subclass.getDirectMember(method.getName()) == null) {
                    notRefined.add(method);
                }
            }
            if (notRefined.isEmpty())
                continue;
            open("table");
            open("tr class='TableHeadingColor'");
            open("th");
            write(title);
            open("code");
            link(superClass.getType());
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
                linkToMember(member);
            }
            close("code", "td");
            close("tr");
            subclass = superClass;
        }
        close("table");
    }

    private void inheritedMembersFromInterfaces(MemberSpecification memberSpecification, String title) throws IOException {
        Map<TypeDeclaration, List<Declaration>> classMembers = new HashMap<TypeDeclaration, List<Declaration>>();
        for (ProducedType superInterface : superInterfaces) {
            TypeDeclaration decl = superInterface.getDeclaration();
            List<Declaration> members = getConcreteSharedMembers(decl, memberSpecification);
            classMembers.put(decl, members);
        }
        for (ProducedType superInterface : superInterfaces) {
            TypeDeclaration decl = superInterface.getDeclaration();
            List<Declaration> members = getConcreteSharedMembers(decl, memberSpecification);
            for (Declaration member : members) {
                Declaration refined = member.getRefinedDeclaration();
                if (refined != null && refined != member && !(refined.getContainer() instanceof Class)) {
                    classMembers.get(refined.getContainer()).remove(refined);
                }
            }
        }
        for (TypeDeclaration superInterface : classMembers.keySet()) {
            List<Declaration> members = classMembers.get(superInterface);
            if (members.isEmpty())
                continue;
            open("table");
            open("tr class='TableHeadingColor'");
            open("th");
            write(title);
            open("code");
            link(superInterface.getType());
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
                linkToMember(member);
            }
            close("code", "td");
            close("tr");
            close("table");
        }

    }

    private void innerClasses() throws IOException {
        if (innerClasses.isEmpty())
            return;
        openTable("Nested Classes", "Modifiers", "Name and Description");
        for (Class m : innerClasses) {
            doc(m);
        }
        close("table");
    }

    private void doc(Class c) throws IOException {
        open("tr class='TableRowColor'");
        open("td");
        around("span class='modifiers'", getModifiers(c));
        close("td");
        open("td");
        link(c.getType());
        tag("br");
        startPrintingLongDoc(c);
        writeSee(c);
        endLongDocAndPrintShortDoc(c);
        close("td");
        close("tr");
    }

    private void summary() throws IOException {
        Package pkg = tool.getPackage(klass);

        open("div class='nav menu'");
        open("div");
        around("a href='" + getObjectUrl(module) + "'", "Overview");
        close("div");
        open("div");
        around("a href='index.html'", "Package");
        close("div");
        open("div class='selected'");
        write("Class");
        close("div");
        open("div");
        write(pkg.getModule().getNameAsString() + "/" + pkg.getModule().getVersion());
        close("div");
        close("div");

        open("div class='head summary'");

        // name
        around("div class='package'", "<code>" + pkg.getNameAsString() + "</code>");

        around("div class='type'", klass instanceof Class ? "Class " : "Interface ", "<code>", getClassName(), "</code>");

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
                link(superType);
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

        // description
        around("div class='doc'", getDoc(klass));

        writeSee(klass);
        
        close("div");
    }

	@Override
	protected String getObjectUrl(Object to) {
	    return getObjectUrl(klass, to);
	}
	
	@Override
    protected String getResourceUrl(String to) {
        return getResourceUrl(klass, to);
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
        openTable("Constructor");
        open("tr", "td", "code");
        write(klass.getName());
        writeParameterList(klass.getParameterLists());
        close("code", "td", "tr", "table");
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
        if (attributes.isEmpty())
            return;
        openTable("Attributes", "Modifier and Type", "Name and Description");
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
                link(typeDeclaration, null);
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
                link(typeDeclaration);
            }
            close("div");
        }
    }
}
