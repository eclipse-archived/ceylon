package com.redhat.ceylon.compiler.codegen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.type.TypeKind;

import com.redhat.ceylon.compiler.tools.LanguageCompiler;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.ValueParameter;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.PackageSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.jvm.ClassReader;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Name.Table;

public class CeylonModelLoader implements ModelCompleter {
    
    private Symtab symtab;
    private Table names;
    private Map<String, Declaration> declarationsByName = new HashMap<String, Declaration>();
    private ClassReader reader;
    private PhasedUnits phasedUnits;
    private com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext;
    
    public CeylonModelLoader(Context context) {
        phasedUnits = LanguageCompiler.getPhasedUnitsInstance(context);
        ceylonContext = LanguageCompiler.getCeylonContextInstance(context);
        symtab = Symtab.instance(context);
        names = Name.Table.instance(context);
        reader = ClassReader.instance(context);
    }

    public void loadRequiredModules() {
        /*
         * We start by loading java.lang and ceylon.language because we will need them no matter what.
         */
        PackageSymbol ceylonPkg = reader.enterPackage(names.fromString("ceylon.language"));
        ceylonPkg.complete();
        PackageSymbol javaPkg = reader.enterPackage(names.fromString("java.lang"));
        javaPkg.complete();
        /*
         * Eventually this will go away as we get a hook from the typechecker to load on demand, but
         * for now the typechecker requires at least ceylon.language to be loaded 
         */
        for(Symbol m : ceylonPkg.members().getElements()){
            System.err.println("Convert: "+m.getQualifiedName().toString());
            convertToDeclaration(lookupClassSymbol(m.getQualifiedName().toString()));
        }
    }

    private Declaration convertToDeclaration(ClassSymbol classSymbol) {
        String className = classSymbol.className();
        if(declarationsByName.containsKey(className)){
            return declarationsByName.get(className);
        }
        System.err.println("convertToDeclaration: "+className);
        com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface klass = makeLazyClassOrInterface(classSymbol);
        declarationsByName.put(className, klass);
        
        klass.setName(classSymbol.getSimpleName().toString());
        klass.setShared((classSymbol.flags() & Flags.PUBLIC) != 0);
        
        // find its module
        String pkgName = classSymbol.packge().getQualifiedName().toString();
        Module module = findOrCreateModule(pkgName);
        Package pkg = findOrCreatePackage(module, pkgName);
        // and add it there
        pkg.getMembers().add(klass);
        klass.setContainer(pkg);
        
        return klass;
    }

    private ClassOrInterface makeLazyClassOrInterface(ClassSymbol classSymbol) {
        if(!classSymbol.isInterface()){
            LazyClass klass = new LazyClass(classSymbol, this);
            klass.setAbstract((classSymbol.flags() & Flags.ABSTRACT) != 0);
            return klass;
        }else{
            LazyInterface iface = new LazyInterface(classSymbol, this);
            return iface;
        }
    }

    private Declaration convertToDeclaration(Type type) {
        String typeName;
        switch(type.getKind()){
        case VOID:    typeName = "java.lang.Void";    break;
        case BOOLEAN: typeName = "java.lang.Boolean"; break;
        case BYTE:    typeName = "java.lang.Byte"; break;
        case CHAR:    typeName = "java.lang.Character"; break;
        case SHORT:   typeName = "java.lang.Short"; break;
        case INT:     typeName = "java.lang.Integer"; break;
        case LONG:    typeName = "java.lang.Long"; break;
        case FLOAT:   typeName = "java.lang.Float"; break;
        case DOUBLE:  typeName = "java.lang.Double"; break;
        case ARRAY:
            // ((Type.ArrayType)type).getComponentType()
            throw new RuntimeException("Array type not implemented");
        case DECLARED:
            typeName = type.tsym.getQualifiedName().toString();
            break;
        case TYPEVAR:
            throw new RuntimeException("Type var not implemented");
        default:
            throw new RuntimeException("Failed to handle type "+type);
        }
        ClassSymbol classSymbol = lookupClassSymbol(typeName);
        if(classSymbol == null)
            throw new RuntimeException("Failed to resolve "+typeName);
        return convertToDeclaration(classSymbol);
    }
    
    private ClassSymbol lookupClassSymbol(String name) {
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

    private Package findOrCreatePackage(Module module, String pkgName) {
        for(Package pkg : module.getPackages()){
            if(pkg.getNameAsString().equals(pkgName))
                return pkg;
        }
        Package pkg = new Package();
        pkg.setModule(module);
        pkg.setName(Arrays.asList(pkgName.split("\\.")));
        module.getPackages().add(pkg);
        return pkg;
    }

    private Module findOrCreateModule(String pkgName) {
        java.util.List<String> moduleName;
        // FIXME: this is a rather simplistic view of the world
        if(pkgName.startsWith("java."))
            moduleName = Arrays.asList("java");
        else if(pkgName.startsWith("sun."))
            moduleName = Arrays.asList("sun");
        else
            moduleName = Arrays.asList(pkgName.split("\\."));
         Module module = phasedUnits.getModuleBuilder().getOrCreateModule(moduleName);
         // make sure that when we load the ceylon language module we set it to where
         // the typechecker will look for it
         if(pkgName.startsWith("ceylon.language.")
                 && ceylonContext.getModules().getLanguageModule() == null){
             ceylonContext.getModules().setLanguageModule(module);
         }
         // FIXME: this can't be that easy.
         module.setAvailable(true);
         return module;
    }

    private ProducedType getType(Type type) {
        return ((LazyClass)convertToDeclaration(type)).getType();
    }

    @Override
    public void complete(LazyInterface iface) {
        complete(iface, iface.classSymbol);
    }

    @Override
    public void complete(LazyClass klass) {
        complete(klass, klass.classSymbol);
    }

    private void complete(ClassOrInterface klass, ClassSymbol classSymbol) {
        System.err.println("Lazy loading class "+klass);
        for(Symbol member : classSymbol.members().getElements()){
            if(member instanceof MethodSymbol){
                MethodSymbol methodSymbol = (MethodSymbol) member;
                
                if(methodSymbol.isStatic()
                        || methodSymbol.isConstructor()
                        /* Temporary: if it's not public drop it. */
                        || (methodSymbol.flags() & Flags.PUBLIC) == 0)
                    continue;
                
                Method method = new Method();
                
                method.setShared((methodSymbol.flags() & Flags.PUBLIC) != 0);
                method.setContainer(klass);
                method.setName(methodSymbol.name.toString());
                klass.getMembers().add(method);
                
                System.err.println(" Found method "+method.getName());
                
                // now its parameters
                ParameterList parameters = new ParameterList();
                method.addParameterList(parameters);
                for(VarSymbol paramSymbol : methodSymbol.params()){
                    ValueParameter parameter = new ValueParameter();
                    parameter.setContainer(method);
                    parameter.setType(getType(paramSymbol.type));
                    parameters.getParameters().add(parameter);
                }
                method.setType(getType(methodSymbol.getReturnType()));
            }
        }
        // look at its super type
        Type superClass = classSymbol.getSuperclass();
        // ceylon.language.Void has no super type. java.lang.Object neither
        if(!classSymbol.getQualifiedName().toString().equals("language.ceylon.Void")
                && superClass.getKind() != TypeKind.NONE)
            klass.setExtendedType(getType(superClass));
    }
}
