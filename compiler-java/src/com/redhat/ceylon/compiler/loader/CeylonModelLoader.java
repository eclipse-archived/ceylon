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

package com.redhat.ceylon.compiler.loader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.type.TypeKind;
import javax.tools.JavaFileObject.Kind;

import com.redhat.ceylon.compiler.codegen.CeylonCompilationUnit;
import com.redhat.ceylon.compiler.tools.CeylonLog;
import com.redhat.ceylon.compiler.tools.LanguageCompiler;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.model.BottomType;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Element;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;
import com.redhat.ceylon.compiler.typechecker.model.Modules;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.model.ValueParameter;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.util.Util;
import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Attribute.Array;
import com.sun.tools.javac.code.Attribute.Compound;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Scope.Entry;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.CompletionFailure;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.PackageSymbol;
import com.sun.tools.javac.code.Symbol.TypeSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.jvm.ClassReader;
import com.sun.tools.javac.main.OptionName;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Convert;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Name.Table;
import com.sun.tools.javac.util.Options;

public class CeylonModelLoader implements ModelCompleter, ModelLoader {
    
    private Symtab symtab;
    private Table names;
    private Map<String, Declaration> declarationsByName = new HashMap<String, Declaration>();
    private Map<Package, Unit> unitsByPackage = new HashMap<Package, Unit>();
    private ClassReader reader;
    private PhasedUnits phasedUnits;
    private com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext;
    private TypeParser typeParser;
    private Log log;
    private boolean isBootstrap;
    private Types types;
    private TypeFactory typeFactory;
    private final Set<String> loadedPackages = new HashSet<String>();
    private final Map<String,LazyPackage> packagesByName = new HashMap<String,LazyPackage>();
    private boolean packageDescriptorsNeedLoading = false;
    
    public static CeylonModelLoader instance(Context context) {
        CeylonModelLoader instance = context.get(CeylonModelLoader.class);
        if (instance == null) {
            instance = new CeylonModelLoader(context);
            context.put(CeylonModelLoader.class, instance);
        }
        return instance;
    }

    public CeylonModelLoader(Context context) {
        phasedUnits = LanguageCompiler.getPhasedUnitsInstance(context);
        ceylonContext = LanguageCompiler.getCeylonContextInstance(context);
        symtab = Symtab.instance(context);
        names = Name.Table.instance(context);
        reader = CeylonClassReader.instance(context);
        log = CeylonLog.instance(context);
        types = Types.instance(context);
        typeFactory = TypeFactory.instance(context);
        typeParser = new TypeParser(this, typeFactory);
        isBootstrap = Options.instance(context).get(OptionName.BOOTSTRAPCEYLON) != null;
    }

    public void loadStandardModules(){
        // set up the type factory
        Module languageModule = findOrCreateModule("ceylon.language");
        Package languagePackage = findOrCreatePackage(languageModule, "ceylon.language");
        typeFactory.setPackage(languagePackage);
        /*
         * We start by loading java.lang and ceylon.language because we will need them no matter what.
         */
        PackageSymbol javaPkg = reader.enterPackage(names.fromString("java.lang"));
        javaPkg.complete();
        PackageSymbol modelPkg = reader.enterPackage(names.fromString("com.redhat.ceylon.compiler.metadata.java"));
        modelPkg.complete();

        /*
         * We do not load the ceylon.language module from class files if we're bootstrapping it
         */
        if(!isBootstrap){
            loadPackage("ceylon.language");
            loadPackage("ceylon.language.descriptor");
        }
    }
    
    public void setupSourceFileObjects(com.sun.tools.javac.util.List<JCCompilationUnit> trees) {
        for(final JCCompilationUnit tree : trees){
            CompilationUnit ceylonTree = ((CeylonCompilationUnit)tree).ceylonTree;
            final String pkgName = tree.getPackageName() != null ? tree.getPackageName().toString() : "";
            
            ceylonTree.visit(new Visitor(){
                
                void loadFromSource(Tree.Declaration decl) {
                    String name = Util.quoteIfJavaKeyword(decl.getIdentifier().getText());
                    String fqn = pkgName.isEmpty() ? name : pkgName+"."+name;
                    reader.enterClass(names.fromString(fqn), tree.getSourceFile());
                }
                
                @Override
                public void visit(Tree.ClassDefinition that) {
                    loadFromSource(that);
                }
                
                @Override
                public void visit(Tree.InterfaceDefinition that) {
                    loadFromSource(that);
                }
                
                @Override
                public void visit(Tree.ObjectDefinition that) {
                    loadFromSource(that);
                }

                @Override
                public void visit(Tree.MethodDefinition that) {
                    loadFromSource(that);
                }

                @Override
                public void visit(Tree.AttributeDeclaration that) {
                    loadFromSource(that);
                }

                @Override
                public void visit(Tree.AttributeGetterDefinition that) {
                    loadFromSource(that);
                }
            });
        }
        // If we're bootstrapping the Ceylon language now load the symbols from the source CU
        if(isBootstrap)
            symtab.loadCeylonSymbols();
    }

    void loadPackage(String packageName) {
        // abort if we already loaded it
        if(!loadedPackages.add(packageName))
            return;
        PackageSymbol ceylonPkg = packageName.equals("") ? syms().unnamedPackage : reader.enterPackage(names.fromString(packageName));
        ceylonPkg.complete();
        /*
         * Eventually this will go away as we get a hook from the typechecker to load on demand, but
         * for now the typechecker requires at least ceylon.language to be loaded 
         */
        for(Symbol m : ceylonPkg.members().getElements()){
            if(!(m instanceof ClassSymbol))
                return;
            ClassSymbol enclosingClass = getEnclosing((ClassSymbol) m);
            if(enclosingClass.classfile.getKind() != Kind.SOURCE)
                convertToDeclaration(lookupClassSymbol(m.getQualifiedName().toString()), DeclarationType.VALUE);
        }
    }
    
    private ClassSymbol getEnclosing(ClassSymbol c) {
        Symbol owner = c.owner;
        com.sun.tools.javac.util.List<Name> enclosing = Convert.enclosingCandidates(Convert.shortName(c.name));
        if(enclosing.isEmpty())
            return c;
        Name name = enclosing.head;
        Symbol encl = owner.members().lookup(name).sym;
        if (encl == null)
            encl = symtab.classes.get(TypeSymbol.formFlatName(name, owner));
        if(encl != null)
            return (ClassSymbol) encl;
        return c;
    }


    enum ClassType {
        ATTRIBUTE, METHOD, OBJECT, CLASS, INTERFACE;
    }
    
    private Declaration convertToDeclaration(ClassSymbol classSymbol, DeclarationType declarationType) {
        String className = classSymbol.className();
        ClassType type;
        String prefix;
        if(isCeylonToplevelAttribute(classSymbol)){
            type = ClassType.ATTRIBUTE;
            prefix = "V";
        }else if(isCeylonToplevelMethod(classSymbol)){
            type = ClassType.METHOD;
            prefix = "V";
        }else if(isCeylonToplevelObject(classSymbol)){
            type = ClassType.OBJECT;
            // depends on which one we want
            prefix = declarationType == DeclarationType.TYPE ? "C" : "V";
        }else if(classSymbol.isInterface()){
            type = ClassType.INTERFACE;
            prefix = "C";
        }else{
            type = ClassType.CLASS;
            prefix = "C";
        }
        String key = prefix + className;
        // see if we already have it
        if(declarationsByName.containsKey(key)){
            return declarationsByName.get(key);
        }
        
        // make it
        Declaration decl = null;
        List<Declaration> decls = new ArrayList<Declaration>(2);
        switch(type){
        case ATTRIBUTE:
            decl = makeToplevelAttribute(classSymbol);
            break;
        case METHOD:
            decl = makeToplevelMethod(classSymbol);
            break;
        case OBJECT:
            // we first make a class
            decl = makeLazyClassOrInterface(classSymbol, true);
            declarationsByName.put("C"+className, decl);
            decls.add(decl);
            // then we make a value for it
            decl = makeToplevelAttribute(classSymbol);
            key = "V"+className;
            break;
        case CLASS:
        case INTERFACE:
            decl = makeLazyClassOrInterface(classSymbol, false);
            break;
        }

        declarationsByName.put(key, decl);
        decls.add(decl);

        // find its module
        String pkgName = classSymbol.packge().getQualifiedName().toString();
        Module module = findOrCreateModule(pkgName);
        LazyPackage pkg = findOrCreatePackage(module, pkgName);

        // find/make its Unit
        Unit unit = getCompiledUnit(pkg);

        for(Declaration d : decls){
            d.setShared((classSymbol.flags() & Flags.PUBLIC) != 0);
        
            // add it to its package
            pkg.addMember(d);
            d.setContainer(pkg);

            // add it to its Unit
            d.setUnit(unit);
            unit.getDeclarations().add(d);
        }
        
        return decl;
    }

    private Unit getCompiledUnit(LazyPackage pkg) {
        Unit unit = unitsByPackage.get(pkg);
        if(unit == null){
            unit = new Unit();
            unit.setPackage(pkg);
            unitsByPackage.put(pkg, unit);
        }
        return unit;
    }

    private boolean isCeylonToplevelAttribute(ClassSymbol classSymbol) {
        return classSymbol.attribute(symtab.ceylonAtAttributeType.tsym) != null;
    }

    private boolean isCeylonToplevelObject(ClassSymbol classSymbol) {
        return classSymbol.attribute(symtab.ceylonAtObjectType.tsym) != null;
    }

    private Declaration makeToplevelAttribute(ClassSymbol classSymbol) {
        Value value = new LazyValue(classSymbol, this);
        return value;
    }

    private boolean isCeylonToplevelMethod(ClassSymbol classSymbol) {
        return classSymbol.attribute(symtab.ceylonAtMethodType.tsym) != null;
    }

    private Declaration makeToplevelMethod(ClassSymbol classSymbol) {
        LazyMethod method = new LazyMethod(classSymbol, this);
        return method;
    }
    
    private ClassOrInterface makeLazyClassOrInterface(ClassSymbol classSymbol, boolean forTopLevelObject) {
        if(!classSymbol.isInterface()){
            return new LazyClass(classSymbol, this, forTopLevelObject);
        }else{
            return new LazyInterface(classSymbol, this);
        }
    }

    private Declaration convertToDeclaration(Type type, Scope scope, DeclarationType declarationType) {
        String typeName;
        switch(type.getKind()){
        case VOID:    typeName = "ceylon.language.Void"; break;
        case BOOLEAN: typeName = "java.lang.Boolean"; break;
        case BYTE:    typeName = "java.lang.Byte"; break;
        case CHAR:    typeName = "java.lang.Character"; break;
        case SHORT:   typeName = "java.lang.Short"; break;
        case INT:     typeName = "java.lang.Integer"; break;
        case LONG:    typeName = "java.lang.Long"; break;
        case FLOAT:   typeName = "java.lang.Float"; break;
        case DOUBLE:  typeName = "java.lang.Double"; break;
        case ARRAY:
            Type componentType = ((Type.ArrayType)type).getComponentType();
            //throw new RuntimeException("Array type not implemented");
            //UnionType[Empty|Sequence<Natural>] casetypes 
            // producedtypes.typearguments: typeparam[element]->type[natural]
            TypeDeclaration emptyDecl = (TypeDeclaration)convertToDeclaration("ceylon.language.Empty", DeclarationType.TYPE);
            TypeDeclaration sequenceDecl = (TypeDeclaration)convertToDeclaration("ceylon.language.Sequence", DeclarationType.TYPE);
            UnionType unionType = new UnionType(typeFactory);
            List<ProducedType> caseTypes = new ArrayList<ProducedType>(2);
            caseTypes.add(emptyDecl.getType());
            List<ProducedType> typeArguments = new ArrayList<ProducedType>(1);
            typeArguments.add(getType(componentType, scope));
            caseTypes.add(sequenceDecl.getProducedType(null, typeArguments));
            unionType.setCaseTypes(caseTypes);
            return unionType;
        case DECLARED:
            typeName = type.tsym.getQualifiedName().toString();
            break;
        case TYPEVAR:
            return safeLookupTypeParameter(scope, type.tsym.getQualifiedName().toString());
        case WILDCARD:
            // FIXME: wtf?
            typeName = "ceylon.language.Nothing";
            break;
        default:
            throw new RuntimeException("Failed to handle type "+type);
        }
        return convertToDeclaration(typeName, declarationType);
    }
    
    Declaration convertToDeclaration(String typeName, DeclarationType declarationType) {
        if ("ceylon.language.Bottom".equals(typeName)) {
            return new BottomType(typeFactory);
        }
        ClassSymbol classSymbol = lookupClassSymbol(typeName);
        if (classSymbol == null) {
            throw new RuntimeException("Failed to resolve "+typeName);
        }
        return convertToDeclaration(classSymbol, declarationType);
    }

    private TypeParameter safeLookupTypeParameter(Scope scope, String name) {
        TypeParameter param = lookupTypeParameter(scope, name);
        if(param == null)
            throw new RuntimeException("Type param "+name+" not found in "+scope);
        return param;
    }
    
    private TypeParameter lookupTypeParameter(Scope scope, String name) {
        if(scope instanceof Method){
            Method m = (Method) scope;
            for(TypeParameter param : m.getTypeParameters()){
                if(param.getName().equals(name))
                    return param;
            }
            if (!m.isToplevel()) {
                // look it up in its class
                return lookupTypeParameter(scope.getContainer(), name);
            } else {
                // not found
                return null;
            }
        }else if(scope instanceof ClassOrInterface){
            for(TypeParameter param : ((ClassOrInterface) scope).getTypeParameters()){
                if(param.getName().equals(name))
                    return param;
            }
            // not found
            return null;
        }else
            throw new RuntimeException("Type param "+name+" lookup not supported for scope "+scope);
    }

    ClassSymbol lookupClassSymbol(String name) {
        ClassSymbol classSymbol;

        String outerName = name;
        /*
         * This madness here tries to look for a class, and if it fails, tries to resolve it 
         * from its parent class. This is required because a.b.C.D (where D is an inner class
         * of C) is not found in symtab.classes but in C's ClassSymbol.enclosedElements.
         */
        do{
            classSymbol = symtab.classes.get(names.fromString(outerName));
            if(classSymbol != null){
                if(outerName.length() == name.length())
                    return classSymbol;
                else
                    return lookupInnerClass(classSymbol, name.substring(outerName.length()+1).split("\\."));
            }
            int lastDot = outerName.lastIndexOf(".");
            if(lastDot == -1 || lastDot == 0)
                return null;
            outerName = outerName.substring(0, lastDot);
        }while(classSymbol == null);
        return null;
    }

    private ClassSymbol lookupInnerClass(ClassSymbol classSymbol, String[] parts) {
        PART:
            for(String part : parts){
                for(Symbol s : classSymbol.getEnclosedElements()){
                    if(s instanceof ClassSymbol 
                            && s.getSimpleName().toString().equals(part)){
                        classSymbol = (ClassSymbol) s;
                        continue PART;
                    }
                }
                // didn't find the inner class
                return null;
            }
        return classSymbol;
    }

    public Package findPackage(final String pkgName) {
        return packagesByName.get(pkgName);
    }

    public LazyPackage findOrCreatePackage(Module module, final String pkgName) {
        LazyPackage pkg = packagesByName.get(pkgName);
        if(pkg != null)
            return pkg;
        pkg = new LazyPackage(this, reader, names);
        packagesByName.put(pkgName, pkg);
        // FIXME: some refactoring needed
        pkg.setName(pkgName == null ? Collections.<String>emptyList() : Arrays.asList(pkgName.split("\\.")));
        
        // only load package descriptors for new packages after a certain phase
        if(packageDescriptorsNeedLoading)
            loadPackageDescriptor(pkg);

        // only bind it if we already have a module
        if(module != null){
            pkg.setModule(module);
            module.getPackages().add(pkg);
        }
        return pkg;
    }

    public Module findOrCreateModule(String pkgName) {
        java.util.List<String> moduleName;
        boolean isJava = false;
        // FIXME: this is a rather simplistic view of the world
        if(pkgName == null)
            moduleName = Arrays.asList(Util.DEFAULT_MODULE_NAME);
        else if(pkgName.startsWith("java.")){
            moduleName = Arrays.asList("java");
            isJava = true;
        }else if(pkgName.startsWith("sun.")){
            moduleName = Arrays.asList("sun");
            isJava = true;
        }else if(pkgName.startsWith("ceylon.language."))
            moduleName = Arrays.asList("ceylon","language");
        else
            moduleName = Arrays.asList(pkgName.split("\\."));
         Module module = phasedUnits.getModuleManager().getOrCreateModule(moduleName, null);
         // make sure that when we load the ceylon language module we set it to where
         // the typechecker will look for it
         if(pkgName != null
                 && pkgName.startsWith("ceylon.language.")
                 && ceylonContext.getModules().getLanguageModule() == null){
             ceylonContext.getModules().setLanguageModule(module);
         }
         ((CompilerModule)module).setJava(isJava);
         // FIXME: this can't be that easy.
         module.setAvailable(true);
         return module;
    }

    public Module loadCompiledModule(String pkgName) {
        if(pkgName.isEmpty())
            return null;
        String moduleClassName = pkgName + ".module";
        System.err.println("Trying to look up module from "+moduleClassName);
        ClassSymbol moduleClass = loadClass(pkgName, moduleClassName);
        if(moduleClass != null){
            // load its module annotation
            Module module = loadCompiledModule(moduleClass, moduleClassName);
            if(module != null)
                return module;
        }
        // keep looking up
        int lastDot = pkgName.lastIndexOf(".");
        if(lastDot == -1)
            return null;
        String parentPackageName = pkgName.substring(0, lastDot);
        return loadCompiledModule(parentPackageName);
    }

    private ClassSymbol loadClass(String pkgName, String className) {
        ClassSymbol moduleClass = null;
        try{
            PackageSymbol javaPkg = pkgName.equals("") ? syms().unnamedPackage : reader.enterPackage(names.fromString(pkgName));
            javaPkg.complete();
            moduleClass = lookupClassSymbol(className);
        }catch(CompletionFailure x){
            System.err.println("Failed to complete "+className);
        }
        return moduleClass;
    }

    private Module loadCompiledModule(ClassSymbol moduleClass, String moduleClassName) {
        ModuleManager moduleManager = phasedUnits.getModuleManager();
        String name = getAnnotationStringValue(moduleClass, symtab.ceylonAtModuleType, "name");
        String version = getAnnotationStringValue(moduleClass, symtab.ceylonAtModuleType, "version");
        // FIXME: validate the name?
        if(name == null || name.isEmpty()){
            log.warning("ceylon", "Module class "+moduleClassName+" contains no name, ignoring it");
            return null;
        }
        if(version == null || version.isEmpty()){
            log.warning("ceylon", "Module class "+moduleClassName+" contains no version, ignoring it");
            return null;
        }
        Module module = moduleManager.getOrCreateModule(Arrays.asList(name.split("\\.")), version);
        Modules modules = ceylonContext.getModules();

        List<Attribute> imports = getAnnotationArrayValue(moduleClass, symtab.ceylonAtModuleType, "dependencies").getValue();
        for (Attribute i : imports)
        {
            Attribute.Compound importAttribute = (Attribute.Compound) i.getValue();
            Attribute nameAttribute = importAttribute.member(Name.fromString(names, "name"));
            if (nameAttribute != null)
            {
                String dependencyName = (String) nameAttribute.getValue();
                if (! dependencyName.equals("java"))
                {
                    Attribute versionAttribute = importAttribute.member(Name.fromString(names, "version"));
                    String dependencyVersion = null;
                    if (versionAttribute != null) {
                        dependencyVersion = (String) versionAttribute.getValue();
                    }
                    else {
                        dependencyVersion = version;
                    }
                    
                    Module dependency = moduleManager.getOrCreateModule(ModuleManager.splitModuleName(dependencyName),version);
                    
                    Attribute optionalAttribute = importAttribute.member(Name.fromString(names, "optional"));
                    String optionalString = optionalAttribute == null ? null : (String) optionalAttribute.getValue();
                    
                    Attribute exportAttribute = importAttribute.member(Name.fromString(names, "export"));
                    String exportString = optionalAttribute == null ? null : (String) exportAttribute.getValue();
                    
                    ModuleImport moduleImport = moduleManager.findImport(module, dependency);
                    if (moduleImport == null) {
                        boolean optional = optionalString!=null && optionalString.equals("true");
                        boolean export = exportString!=null && exportString.equals("true");
                        moduleImport = new ModuleImport(dependency, optional, export);
                        module.getImports().add(moduleImport);
                    }
                }
            }
        }
        
        module.setAvailable(true);
        
        modules.getListOfModules().add(module);
        Module languageModule = modules.getLanguageModule();
        module.setLanguageModule(languageModule);
        ModuleImport moduleImport = moduleManager.findImport(module, languageModule);
        if (moduleImport == null) {
            moduleImport = new ModuleImport(languageModule, false, false);
            module.getImports().add(moduleImport);
        }
        
        return module;
    }  
    
    private Module findLoadedModule(Modules modules, String name) {
        for(Module module : modules.getListOfModules()){
            if(module.getNameAsString().equals(name)){
                return module;
            }
        }
        return null;
    }

    private ProducedType getType(Type type, Scope scope) {
        Declaration decl = convertToDeclaration(type, scope, DeclarationType.TYPE);
        TypeDeclaration declaration = (TypeDeclaration) decl;
        com.sun.tools.javac.util.List<Type> javacTypeArguments = type.getTypeArguments();
        if(!javacTypeArguments.isEmpty()){
            List<ProducedType> typeArguments = new ArrayList<ProducedType>(javacTypeArguments.size());
            for(Type typeArgument : javacTypeArguments){
                typeArguments.add((ProducedType) getType(typeArgument, scope));
            }
            return declaration.getProducedType(null, typeArguments);
        }
        return declaration.getType();
    }

    //
    // ModelCompleter
    
    @Override
    public void complete(LazyInterface iface) {
        complete(iface, iface.classSymbol);
    }

    @Override
    public void completeTypeParameters(LazyInterface iface) {
        completeTypeParameters(iface, iface.classSymbol);
    }

    @Override
    public void complete(LazyClass klass) {
        complete(klass, klass.classSymbol);
    }

    @Override
    public void completeTypeParameters(LazyClass klass) {
        completeTypeParameters(klass, klass.classSymbol);
    }

    private void completeTypeParameters(ClassOrInterface klass, ClassSymbol classSymbol) {
        setTypeParameters(klass, classSymbol);
    }

    private void complete(ClassOrInterface klass, ClassSymbol classSymbol) {
        HashSet<String> variables = new HashSet<String>();
        String qualifiedName = getQualifiedName(classSymbol);
        // FIXME: deal with toplevel methods and attributes
        int constructorCount = 0;
        // then its methods
        for(Symbol member : classSymbol.getEnclosedElements()){
            // We skip members marked with @Ignore
            if(getAnnotation(member, symtab.ceylonAtIgnore) != null)
                continue;
            
            // FIXME: deal with multiple constructors
            if(member instanceof MethodSymbol){
                MethodSymbol methodSymbol = (MethodSymbol) member;
                String methodName = methodSymbol.name.toString();
                
                if(methodSymbol.isStatic())
                    continue;
                // FIXME: temporary, because some private classes from the jdk are referenced in private methods but not
                // available
                if(qualifiedName.startsWith("java.")
                        && (methodSymbol.flags() & Flags.PUBLIC) == 0)
                    continue;

                if(methodSymbol.isConstructor()){
                    constructorCount++;
                    // ignore the non-first ones
                    if(constructorCount > 1){
                        // only warn once
                        if(constructorCount == 2)
                            log.rawWarning(0, "Has multiple constructors: "+qualifiedName);
                        continue;
                    }
                    if(!(klass instanceof LazyClass)
                            || !((LazyClass)klass).isTopLevelObjectType())
                        setParameters((Class)klass, methodSymbol);
                    continue;
                }
                
                if(isGetter(methodSymbol)) {
                    // simple attribute
                    addValue(klass, methodSymbol, getJavaAttributeName(methodName));
                } else if(isSetter(methodSymbol)) {
                    // We skip setters for now and handle them later
                    variables.add(getJavaAttributeName(methodName));
                } else if(isHashAttribute(methodSymbol)) {
                    // ERASURE
                    // Un-erasing 'hash' attribute from 'hashCode' method
                    addValue(klass, methodSymbol, "hash");
                } else if(isStringAttribute(methodSymbol)) {
                    // ERASURE
                    // Un-erasing 'string' attribute from 'toString' method
                    addValue(klass, methodSymbol, "string");
                } else {
                    // normal method
                    Method method = new Method();
                    
                    method.setContainer(klass);
                    method.setName(methodName);
                    method.setUnit(klass.getUnit());
                    setMethodOrValueFlags(klass, methodSymbol, method);
                    
                    // type params first
                    setTypeParameters(method, methodSymbol);
    
                    // now its parameters
                    if(isEqualsMethod(methodSymbol))
                        setEqualsParameters(method, methodSymbol);
                    else
                        setParameters(method, methodSymbol);
                    method.setType(obtainType(methodSymbol.getReturnType(), methodSymbol, method));
                    markUnboxed(method, methodSymbol.getReturnType());
                    klass.getMembers().add(method);
                }
            }
        }
        
        // Now mark all Values for which Setters exist as variable
        for(String var : variables){
            Declaration decl = klass.getMember(var);
            if (decl != null && decl instanceof Value) {
                ((Value)decl).setVariable(true);
            } else {
                log.rawWarning(0, "Has conflicting attribute and method name '" + var + "': "+qualifiedName);
            }
        }
        
        if(klass instanceof Class && constructorCount == 0){
            // must be a default constructor
            ((Class)klass).setParameterList(new ParameterList());
        }
        
        setExtendedType(klass, classSymbol);
        setSatisfiedTypes(klass, classSymbol);
        fillRefinedDeclarations(klass);
    }

    private String getQualifiedName(ClassSymbol classSymbol) {
        // as taken from ClassSymbol.className():
        if(classSymbol.name.len == 0)
            return classSymbol.flatname.toString();
        else
            return classSymbol.getQualifiedName().toString();
    }

    private void fillRefinedDeclarations(ClassOrInterface klass) {
        for(Declaration member : klass.getMembers()){
            if(member.isActual()){
                member.setRefinedDeclaration(findRefinedDeclaration(klass, member.getName()));
            }
        }
    }

    private Declaration findRefinedDeclaration(ClassOrInterface decl, String name) {
        Declaration refinedDeclaration = decl.getRefinedMember(name);
        if(refinedDeclaration == null)
            throw new RuntimeException("Failed to find refined declaration for "+name);
        return refinedDeclaration;
    }

    private MethodSymbol getOverriddenMethod(MethodSymbol method, Types types) {
        MethodSymbol impl = null;
        // interfaces have a different way to work
        if(method.owner.isInterface())
            return (MethodSymbol) method.implemented(method.owner.type.tsym, types);
        for (Type superType = types.supertype(method.owner.type);
                impl == null && superType.tsym != null;
                superType = types.supertype(superType)) {
            TypeSymbol i = superType.tsym;
            // never go above this type since it has no supertype in Ceylon (does in Java though)
            if(i.getQualifiedName().toString().equals("ceylon.language.Void"))
                break;
            for (Entry e = i.members().lookup(method.name);
                    impl == null && e.scope != null;
                    e = e.next()) {
                if (method.overrides(e.sym, (TypeSymbol)method.owner, types, true) &&
                        // FIXME: I suspect the following requires a
                        // subst() for a parametric return type.
                        types.isSameType(method.type.getReturnType(),
                                types.memberType(method.owner.type, e.sym).getReturnType())) {
                    impl = (MethodSymbol) e.sym;
                }
            }
            // try in the interfaces
            if(impl == null)
                impl = (MethodSymbol) method.implemented(i, types);
        }
        // try in the interfaces
        if(impl == null)
            impl = (MethodSymbol) method.implemented(method.owner.type.tsym, types);
        return impl;
    }

    private boolean isGetter(MethodSymbol methodSymbol) {
        String name = methodSymbol.name.toString();
        boolean matchesGet = name.length() > 3 && name.startsWith("get") && Character.isUpperCase(name.charAt(3));
        boolean matchesIs = name.length() > 2 && name.startsWith("is") && Character.isUpperCase(name.charAt(2));
        boolean hasNoParams = methodSymbol.getParameters().size() == 0;
        boolean hasNonVoidReturn = (methodSymbol.getReturnType().getKind() != TypeKind.VOID);
        return (matchesGet || matchesIs) && hasNoParams && hasNonVoidReturn;
    }
    
    private boolean isSetter(MethodSymbol methodSymbol) {
        String name = methodSymbol.name.toString();
        boolean matchesSet = name.length() > 3 && name.startsWith("set") && Character.isUpperCase(name.charAt(3));
        boolean hasOneParam = methodSymbol.getParameters().size() == 1;
        boolean hasVoidReturn = (methodSymbol.getReturnType().getKind() == TypeKind.VOID);
        return matchesSet && hasOneParam && hasVoidReturn;
    }

    private boolean isHashAttribute(MethodSymbol methodSymbol) {
        String name = methodSymbol.name.toString();
        boolean matchesName = "hashCode".equals(name);
        boolean hasNoParams = methodSymbol.getParameters().size() == 0;
        return matchesName && hasNoParams;
    }
    
    private boolean isStringAttribute(MethodSymbol methodSymbol) {
        String name = methodSymbol.name.toString();
        boolean matchesName = "toString".equals(name);
        boolean hasNoParams = methodSymbol.getParameters().size() == 0;
        return matchesName && hasNoParams;
    }

    private boolean isEqualsMethod(MethodSymbol methodSymbol) {
        String name = methodSymbol.name.toString();
        if(!"equals".equals(name)
                || methodSymbol.getParameters().size() != 1)
            return false;
        VarSymbol param = methodSymbol.getParameters().head;
        return sameType(param.type, symtab.objectType);
    }

    private void setEqualsParameters(Method decl, MethodSymbol methodSymbol) {
        ParameterList parameters = new ParameterList();
        decl.addParameterList(parameters);
        ValueParameter parameter = new ValueParameter();
        parameter.setUnit(decl.getUnit());
        parameter.setContainer((Scope) decl);
        parameter.setName("that");
        parameter.setType(getType(symtab.ceylonEqualityType, decl));
        parameter.setDeclaration((Declaration) decl);
        parameters.getParameters().add(parameter);
    }

    private String getJavaAttributeName(String getterName) {
        if (getterName.startsWith("get") || getterName.startsWith("set")) {
            return Character.toLowerCase(getterName.charAt(3)) + getterName.substring(4);
        } else if (getterName.startsWith("is")) {
            // Starts with "is"
            return Character.toLowerCase(getterName.charAt(2)) + getterName.substring(3);
        } else {
            throw new RuntimeException("Illegal java getter/setter name");
        }
    }
    
    private void addValue(ClassOrInterface klass, MethodSymbol methodSymbol, String methodName) {
        Value value = new Value();
        value.setContainer(klass);
        value.setName(methodName);
        value.setUnit(klass.getUnit());
        setMethodOrValueFlags(klass, methodSymbol, value);
        value.setType(obtainType(methodSymbol.getReturnType(), methodSymbol, klass));
        markUnboxed(value, methodSymbol.getReturnType());
        klass.getMembers().add(value);
    }

    private void setMethodOrValueFlags(ClassOrInterface klass, MethodSymbol methodSymbol, MethodOrValue decl) {
        decl.setShared((methodSymbol.flags() & Flags.PUBLIC) != 0);
        if((methodSymbol.flags() & Flags.ABSTRACT) != 0 || klass instanceof Interface) {
            decl.setFormal(true);
        } else {
            if ((methodSymbol.flags() & Flags.FINAL) == 0) {
                decl.setDefault(true);
            }
        }
        if(getOverriddenMethod(methodSymbol, types) != null){
            decl.setActual(true);
        }
    }
    
    private void setExtendedType(ClassOrInterface klass, ClassSymbol classSymbol) {
        // look at its super type
        Type superClass = classSymbol.getSuperclass();
        ProducedType extendedType = null;
        
        if(klass instanceof Interface){
            // interfaces need to have their superclass set to Object
            if(superClass.getKind() == TypeKind.NONE)
                extendedType = getType(symtab.ceylonObjectType, klass);
            else
                extendedType = getType(superClass, klass);
        }else{
            String className = getQualifiedName(classSymbol);
            if(className.equals("ceylon.language.Void")){
                // ceylon.language.Void has no super type
            }else if(className.equals("ceylon.language.Exception")){
                // we pretend its superclass is something else
                extendedType = getType(symtab.ceylonIdentifiableObjectType, klass);
            }else if(className.equals("java.lang.Object")){
                // we pretend its superclass is something else, but note that in theory we shouldn't 
                // be seeing j.l.Object at all due to unerasure
                extendedType = getType(symtab.ceylonIdentifiableObjectType, klass);
            }else{
                // make sure we don't throw and just report an error when this sort of weird thing happens
                if(superClass.tsym == null){
                    log.error("ceylon", "Object has no super class symbol: "+className);
                    return;
                }
                // read it from annotation first
                String superClassName = getAnnotationStringValue(classSymbol, symtab.ceylonAtClassType, "extendsType");
                if(superClassName != null && !superClassName.isEmpty()){
                    extendedType = decodeType(superClassName, klass);
                }else{
                    // read it from the Java super type
                    superClassName = superClass.tsym.getQualifiedName().toString();
                    // now deal with type erasure, avoid having Object as superclass
                    if(superClassName.equals("java.lang.Object")){
                        extendedType = getType(symtab.ceylonIdentifiableObjectType, klass);
                    }else{
                        extendedType = getType(superClass, klass);
                    }
                }
            }
        }
        if(extendedType != null)
            klass.setExtendedType(extendedType);
    }

    private void setParameters(Functional decl, MethodSymbol methodSymbol) {
        ParameterList parameters = new ParameterList();
        decl.addParameterList(parameters);
        for(VarSymbol paramSymbol : methodSymbol.params()){
            ValueParameter parameter = new ValueParameter();
            parameter.setContainer((Scope) decl);
            parameter.setUnit(((Element)decl).getUnit());
            if(decl instanceof Class){
                ((Class)decl).getMembers().add(parameter);
            }
            String paramName = getAnnotationStringValue(paramSymbol, symtab.ceylonAtNameType);
            // use whatever param name we find as default
            if(paramName == null)
                paramName = paramSymbol.name.toString();
            parameter.setName(paramName);
            parameter.setType(obtainType(paramSymbol.type, paramSymbol, (Scope) decl));
            if(getAnnotation(paramSymbol, symtab.ceylonAtSequencedType) != null)
                parameter.setSequenced(true);
            markUnboxed(parameter, paramSymbol.type);
            parameter.setDeclaration((Declaration) decl);
            parameters.getParameters().add(parameter);
        }
    }

    private void markUnboxed(TypedDeclaration decl, Type type) {
        if(type.isPrimitive() || sameType(type, symtab.stringType))
            Util.markUnBoxed(decl);
    }

    @Override
    public void complete(LazyValue value) {
        MethodSymbol meth = null;
        for (Symbol member : value.classSymbol.members().getElements()) {
            // We skip members marked with @Ignore
            if(getAnnotation(member, symtab.ceylonAtIgnore) != null)
                continue;
            
            if (member instanceof MethodSymbol) {
                MethodSymbol m = (MethodSymbol) member;
                if (m.name.toString().equals(
                        Util.getGetterName(value.getName()))
                        && m.isStatic() && m.params().size() == 0) {
                    meth = m;
                }
                if (m.name.toString().equals(
                        Util.getSetterName(value.getName()))
                        && m.isStatic() && m.params().size() == 1) {
                    value.setVariable(true);
                }
            }
        }
        if(meth == null || meth.getReturnType() == null)
            throw new RuntimeException("Failed to find toplevel attribute "+value.getName());
        
        value.setType(obtainType(meth.getReturnType(), meth, null));
        markUnboxed(value, meth.getReturnType());
    }

    @Override
    public void complete(LazyMethod method) {
        MethodSymbol meth = null;
        String lookupName = Util.quoteIfJavaKeyword(method.getName());
        for(Symbol member : method.classSymbol.members().getElements()){
            // We skip members marked with @Ignore
            if(getAnnotation(member, symtab.ceylonAtIgnore) != null)
                continue;
            
            if(member instanceof MethodSymbol){
                MethodSymbol m = (MethodSymbol) member;
                if(m.name.toString().equals(lookupName)){
                    meth = m;
                    break;
                }
            }
        }
        if(meth == null || meth.getReturnType() == null)
            throw new RuntimeException("Failed to find toplevel method "+method.getName());
        
        // type params first
        setTypeParameters(method, meth);

        // now its parameters
        setParameters(method, meth);
        method.setType(obtainType(meth.getReturnType(), meth, method));
        markUnboxed(method, meth.getReturnType());
     }
    
    //
    // Utils for loading type info from the model
    
    private Compound getAnnotation(Symbol symbol, Type type) {
        com.sun.tools.javac.util.List<Compound> annotations = symbol.getAnnotationMirrors();
        for(Compound annotation : annotations){
            if(annotation.type.tsym.equals(type.tsym))
                return annotation;
        }
        return null;
    }

    private Array getAnnotationArrayValue(Symbol symbol, Type type, String field) {
        return (Array) getAnnotationValue(symbol, type, field);
    }

    private Array getAnnotationArrayValue(Symbol symbol, Type type) {
        return (Array) getAnnotationValue(symbol, type);
    }

    private String getAnnotationStringValue(Symbol symbol, Type type) {
        return getAnnotationStringValue(symbol, type, "value");
    }
    
    private String getAnnotationStringValue(Symbol symbol, Type type, String field) {
        Attribute annotation = getAnnotationValue(symbol, type, field);
        if(annotation != null)
            return (String) annotation.getValue();
        return null;
    }

    private Boolean getAnnotationBooleanValue(Symbol symbol, Type type, String field) {
        Attribute annotation = getAnnotationValue(symbol, type, field);
        if(annotation != null)
            return (Boolean) annotation.getValue();
        return null;
    }

    private Attribute getAnnotationValue(Symbol symbol, Type type) {
        return getAnnotationValue(symbol, type, "value");
    }
    
    private Attribute getAnnotationValue(Symbol symbol, Type type, String fieldName) {
        Compound annotation = getAnnotation(symbol, type);
        if(annotation != null)
            return annotation.member(names.fromString(fieldName));
        return null;
    }

    //
    // Satisfied Types
    
    private Array getSatisfiedTypesFromAnnotations(Symbol symbol) {
        return getAnnotationArrayValue(symbol, symtab.ceylonAtSatisfiedTypes);
    }
    
    private void setSatisfiedTypes(ClassOrInterface klass, ClassSymbol classSymbol) {
        Array satisfiedTypes = getSatisfiedTypesFromAnnotations(classSymbol);
        if(satisfiedTypes != null){
            klass.getSatisfiedTypes().addAll(getSatisfiedTypes(satisfiedTypes, klass));
        }else{
            for(Type iface : classSymbol.getInterfaces()){
                klass.getSatisfiedTypes().add(getType(iface, klass));
            }
        }
    }

    private Collection<? extends ProducedType> getSatisfiedTypes(Array satisfiedTypes, Scope scope) {
        List<ProducedType> producedTypes = new LinkedList<ProducedType>();
        for(Attribute type : satisfiedTypes.values){
            producedTypes.add(decodeType((String) type.getValue(), scope));
        }
        return producedTypes;
    }

    //
    // Type parameters loading

    private Array getTypeParametersFromAnnotations(Symbol symbol) {
        return getAnnotationArrayValue(symbol, symtab.ceylonAtTypeParameters);
    }

    // from our annotation
    private void setTypeParameters(Scope scope, List<TypeParameter> params, Array typeParameters) {
        // We must first add every type param, before we resolve the bounds, which can
        // refer to type params.
        for(Attribute attribute : typeParameters.values){
            Compound typeParam = (Compound) attribute;
            TypeParameter param = new TypeParameter();
            param.setUnit(((Element)scope).getUnit());
            param.setContainer(scope);
            // let's not trigger the lazy-loading if we're completing a LazyClass/LazyInterface
            if(scope instanceof LazyElement)
                ((LazyElement)scope).addMember(param);
            else // must be a method
                scope.getMembers().add(param);
            param.setName((String)typeParam.member(names.fromString("value")).getValue());
            params.add(param);
            
            Attribute varianceAttribute = typeParam.member(names.fromString("variance"));
            if(varianceAttribute != null){
                VarSymbol variance = (VarSymbol) varianceAttribute.getValue();
                String varianceName = variance.name.toString();
                if(varianceName.equals("IN")){
                    param.setContravariant(true);
                }else if(varianceName.equals("OUT"))
                    param.setCovariant(true);
            }
        }
        // Now all type params have been set, we can resolve the references parts
        Iterator<TypeParameter> paramsIterator = params.iterator();
        for(Attribute attribute : typeParameters.values){
            Compound typeParam = (Compound) attribute;
            TypeParameter param = paramsIterator.next();
            Array satisfiesAttribute = (Array)typeParam.member(names.fromString("satisfies"));
            if(satisfiesAttribute != null){
                for (Attribute satisfyAttribute : satisfiesAttribute.values) {
                    String satisfy = (String) satisfyAttribute.getValue();
                    ProducedType satisfiesType = decodeType(satisfy, scope);
                    param.getSatisfiedTypes().add(satisfiesType);
                }
            }
        }
    }

    // from java type info
    private void setTypeParameters(Scope scope, List<TypeParameter> params, com.sun.tools.javac.util.List<TypeSymbol> typeParameters) {
        // We must first add every type param, before we resolve the bounds, which can
        // refer to type params.
        for(TypeSymbol typeParam : typeParameters){
            TypeParameter param = new TypeParameter();
            param.setUnit(((Element)scope).getUnit());
            param.setContainer(scope);
            // let's not trigger the lazy-loading if we're completing a LazyClass/LazyInterface
            if(scope instanceof LazyElement)
                ((LazyElement)scope).addMember(param);
            else // must be a method
                scope.getMembers().add(param);
            param.setName(typeParam.name.toString());
            params.add(param);
        }
        // Now all type params have been set, we can resolve the references parts
        Iterator<TypeParameter> paramsIterator = params.iterator();
        for(TypeSymbol typeParam : typeParameters){
            TypeParameter param = paramsIterator.next();
            com.sun.tools.javac.util.List<Type> bounds = typeParam.getBounds();
            for(Type bound : bounds){
                // we turn java's default upper bound java.lang.Object into ceylon.language.Object
                if(bound.tsym == symtab.objectType.tsym){
                    // avoid adding java's default upper bound if it's just there with no meaning
                    if(bounds.size() == 1)
                        break;
                    bound = symtab.ceylonObjectType;
                }
                param.getSatisfiedTypes().add(getType(bound, scope));
            }
        }
    }

    // method
    private void setTypeParameters(Method method, MethodSymbol methodSymbol) {
        List<TypeParameter> params = new LinkedList<TypeParameter>();
        method.setTypeParameters(params);
        Array typeParameters = getTypeParametersFromAnnotations(methodSymbol);
        if(typeParameters != null)
            setTypeParameters(method, params, typeParameters);
        else
            setTypeParameters(method, params, methodSymbol.getTypeParameters());
    }

    // class
    private void setTypeParameters(ClassOrInterface klass, ClassSymbol classSymbol) {
        List<TypeParameter> params = new LinkedList<TypeParameter>();
        klass.setTypeParameters(params);
        Array typeParameters = getTypeParametersFromAnnotations(classSymbol);
        if(typeParameters != null)
            setTypeParameters(klass, params, typeParameters);
        else
            setTypeParameters(klass, params, classSymbol.getTypeParameters());
    }        

    //
    // TypeParsing and ModelLoader

    private ProducedType decodeType(String value, Scope scope) {
        return typeParser.decodeType(value, scope);
    }
    
    private ProducedType obtainType(Type type, Symbol symbol, Scope scope) {
        String typeName = getAnnotationStringValue(symbol, symtab.ceylonAtTypeInfoType);
        if (typeName != null) {
            return decodeType(typeName, scope);
        } else {
            // ERASURE
            if (sameType(type, symtab.stringType)) {
                type = makeOptional(symtab.ceylonStringType);
            } else if (sameType(type, symtab.booleanType)) {
                type = symtab.ceylonBooleanType;
            } else if (sameType(type, symtab.booleanObjectType)) {
                type = makeOptional(symtab.ceylonBooleanType);
            } else if (sameType(type, symtab.intType)) {
                // FIXME Really needs "small" annotation
                type = symtab.ceylonIntegerType;
            } else if (sameType(type, symtab.integerObjectType)) {
                // FIXME Really needs "small" annotation
                type = makeOptional(symtab.ceylonIntegerType);
            } else if (sameType(type, symtab.longType)) {
                type = symtab.ceylonIntegerType;
            } else if (sameType(type, symtab.longObjectType)) {
                type = makeOptional(symtab.ceylonIntegerType);
            } else if (sameType(type, symtab.floatType)) {
                // FIXME Really needs "small" annotation
                type = symtab.ceylonFloatType;
            } else if (sameType(type, symtab.floatObjectType)) {
                // FIXME Really needs "small" annotation
                type = makeOptional(symtab.ceylonFloatType);
            } else if (sameType(type, symtab.doubleType)) {
                type = symtab.ceylonFloatType;
            } else if (sameType(type, symtab.doubleObjectType)) {
                type = makeOptional(symtab.ceylonFloatType);
            } else if (sameType(type, symtab.charType)) {
                type = symtab.ceylonCharacterType;
            } else if (sameType(type, symtab.characterObjectType)) {
                type = makeOptional(symtab.ceylonCharacterType);
            } else if (sameType(type, symtab.objectType)) {
                type = makeOptional(symtab.ceylonIdentifiableObjectType);
            }
            
            return getType(type, scope);
        }
    }
    
    private boolean sameType(Type t1, Type t2) {
        return t1.asElement().equals(t2.asElement());
    }
    
    private Type makeOptional(Type type) {
//        UnionType ut = new UnionType();
//        List<ProducedType> types = new ArrayList<ProducedType>();
//        addToUnion(types,getNothingDeclaration().getType());
//        addToUnion(types,pt);
//        ut.setCaseTypes(types);
//        return ut.getType();
        // FIXME Implement conversion to optional type!!
        return type;
    }

    @Override
    public Declaration getDeclaration(String typeName, DeclarationType declarationType) {
        return convertToDeclaration(typeName, declarationType);
    }

    @Override
    public ProducedType getType(String name, Scope scope) {
        if(scope != null){
            TypeParameter typeParameter = lookupTypeParameter(scope, name);
            if(typeParameter != null)
                return typeParameter.getType();
        }
        if(!isBootstrap || !name.startsWith("ceylon.language"))
            return ((TypeDeclaration)convertToDeclaration(name, DeclarationType.TYPE)).getType();
        // we're bootstrapping ceylon.language so we need to return the ProducedTypes straight from the model we're compiling
        Module languageModule = ceylonContext.getModules().getLanguageModule();
        String simpleName = name.substring(name.lastIndexOf(".")+1);
        for(Package pkg : languageModule.getPackages()){
            Declaration member = pkg.getDirectMember(simpleName);
            if(member != null)
                return ((TypeDeclaration)member).getType();
        }
        throw new RuntimeException("Failed to look up given type in language module while bootstrapping: "+name);
    }

    public void loadPackageDescriptors() {
        for(Package pkg : packagesByName.values()){
            loadPackageDescriptor(pkg);
        }
        packageDescriptorsNeedLoading  = true;
    }

    private void loadPackageDescriptor(Package pkg) {
        // let's not load package descriptors for Java modules
        if(pkg.getModule() != null 
                && ((CompilerModule)pkg.getModule()).isJava()){
            pkg.setShared(true);
            return;
        }
        String className = pkg.getQualifiedNameString() + ".$package";
        System.err.println("Trying to look up package from "+className);
        ClassSymbol packageClass = loadClass(pkg.getQualifiedNameString(), className);
        if(packageClass == null){
            System.err.println("Failed to complete "+className);
            // missing: leave it private
            return;
        }
        // did we compile it from source or class?
        if(packageClass.classfile.getKind() != Kind.CLASS){
            // must have come from source, in which case we walked it and
            // loaded its values already
            System.err.println("We're compiling the package "+className);
            return;
        }
        loadCompiledPackage(packageClass, pkg);
    }

    private void loadCompiledPackage(ClassSymbol packageClass, Package pkg) {
        String name = getAnnotationStringValue(packageClass, symtab.ceylonAtPackageType, "name");
        Boolean shared = getAnnotationBooleanValue(packageClass, symtab.ceylonAtPackageType, "shared");
        // FIXME: validate the name?
        if(name == null || name.isEmpty()){
            log.warning("ceylon", "Package class "+pkg.getQualifiedNameString()+" contains no name, ignoring it");
            return;
        }
        if(shared == null){
            log.warning("ceylon", "Package class "+pkg.getQualifiedNameString()+" contains no shared, ignoring it");
            return;
        }
        pkg.setShared(shared);
    }

    public Symtab syms() {
        return symtab;
    }
}
