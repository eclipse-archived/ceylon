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

package com.redhat.ceylon.compiler.java.loader;

import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject.Kind;

import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.JDKUtils;
import com.redhat.ceylon.compiler.java.codegen.CeylonCompilationUnit;
import com.redhat.ceylon.compiler.java.codegen.Naming;
import com.redhat.ceylon.compiler.java.loader.mirror.JavacClass;
import com.redhat.ceylon.compiler.java.loader.mirror.JavacMethod;
import com.redhat.ceylon.compiler.java.loader.model.CompilerModuleManager;
import com.redhat.ceylon.compiler.java.tools.CeylonLog;
import com.redhat.ceylon.compiler.java.tools.LanguageCompiler;
import com.redhat.ceylon.compiler.java.util.Timer;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.AbstractModelLoader;
import com.redhat.ceylon.compiler.loader.ModelLoaderFactory;
import com.redhat.ceylon.compiler.loader.SourceDeclarationVisitor;
import com.redhat.ceylon.compiler.loader.TypeParser;
import com.redhat.ceylon.compiler.loader.mirror.ClassMirror;
import com.redhat.ceylon.compiler.loader.mirror.MethodMirror;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Declaration;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.Scope.Entry;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.CompletionFailure;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.PackageSymbol;
import com.sun.tools.javac.code.Symbol.TypeSymbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.jvm.ClassReader;
import com.sun.tools.javac.main.OptionName;
import com.sun.tools.javac.util.Abort;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Convert;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;
import com.sun.tools.javac.util.Options;

public class CeylonModelLoader extends AbstractModelLoader {
    
    private Symtab symtab;
    private Names names;
    private ClassReader reader;
    private PhasedUnits phasedUnits;
    private com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext;
    private Log log;
    private Types types;
    private Options options;
    
    public static AbstractModelLoader instance(Context context) {
        AbstractModelLoader instance = context.get(AbstractModelLoader.class);
        if (instance == null) {
            ModelLoaderFactory factory = context.get(ModelLoaderFactory.class);
            if (factory != null) {
                instance = factory.createModelLoader(context);
            }
            else {
                instance = new CeylonModelLoader(context);
            }
            context.put(AbstractModelLoader.class, instance);
        }
        return instance;
    }

    public CeylonModelLoader(Context context) {
        phasedUnits = LanguageCompiler.getPhasedUnitsInstance(context);
        ceylonContext = LanguageCompiler.getCeylonContextInstance(context);
        symtab = Symtab.instance(context);
        names = Names.instance(context);
        reader = CeylonClassReader.instance(context);
        log = CeylonLog.instance(context);
        types = Types.instance(context);
        typeFactory = TypeFactory.instance(context);
        typeParser = new TypeParser(this, typeFactory);
        options = Options.instance(context);
        timer = Timer.instance(context);
        isBootstrap = options.get(OptionName.BOOTSTRAPCEYLON) != null;
        moduleManager = phasedUnits.getModuleManager();
        modules = ceylonContext.getModules();
    }

    @Override
    public void addModuleToClassPath(Module module, ArtifactResult artifact){
        if(artifact != null)
            ((CompilerModuleManager)phasedUnits.getModuleManager()).getCeylonEnter().addModuleToClassPath(module, true, artifact);
    }

    public void setupSourceFileObjects(java.util.List<?> treeHolders) {
        for(Object treeHolder : treeHolders){
            if (!(treeHolder instanceof CeylonCompilationUnit)) {
                continue;
            }
            final CeylonCompilationUnit tree = (CeylonCompilationUnit)treeHolder;
            CompilationUnit ceylonTree = tree.ceylonTree;
            final String pkgName = tree.getPackageName() != null ? Util.quoteJavaKeywords(tree.getPackageName().toString()) : "";
            ceylonTree.visit(new SourceDeclarationVisitor(){
                @Override
                public void loadFromSource(Declaration decl) {
                    String fqn = Naming.toplevelClassName(pkgName, decl);
                    try{
                        reader.enterClass(names.fromString(fqn), tree.getSourceFile());
                    }catch(AssertionError error){
                        // this happens when we have already registered a source file for this decl, so let's
                        // print out a helpful message
                        // see https://github.com/ceylon/ceylon-compiler/issues/250
                        ClassMirror previousClass = lookupClassMirror(fqn);
                        log.error("ceylon", "Duplicate declaration error: "+fqn+" is declared twice: once in "
                                +tree.getSourceFile()+" and again in: "+
                                (previousClass != null ? ((JavacClass)previousClass).classSymbol.classfile : "another file"));
                    }
                }
            });
        }
        // If we're bootstrapping the Ceylon language now load the symbols from the source CU
        if(isBootstrap)
            symtab.loadCeylonSymbols();
    }
    
    @Override
    public synchronized boolean loadPackage(String packageName, boolean loadDeclarations) {
        // abort if we already loaded it, but only record that we loaded it if we want
        // to load the declarations, because merely calling complete() on the package
        // is OK
        packageName = Util.quoteJavaKeywords(packageName);
        if(loadDeclarations && !loadedPackages.add(packageName)){
            return true;
        }
        PackageSymbol ceylonPkg = packageName.equals("") ? syms().unnamedPackage : reader.enterPackage(names.fromString(packageName));
        ceylonPkg.complete();
        if(loadDeclarations){
            /*
             * Eventually this will go away as we get a hook from the typechecker to load on demand, but
             * for now the typechecker requires at least ceylon.language to be loaded 
             */
            for(Symbol m : ceylonPkg.members().getElements()){
                // FIXME: that's just plain weird, why do we return rather than throw?
                if(!(m instanceof ClassSymbol))
                    return true;
                ClassSymbol enclosingClass = getEnclosing((ClassSymbol) m);
                if(enclosingClass.classfile.getKind() != Kind.SOURCE){
                    m.complete();
                    // avoid anonymous and local classes
                    if(isAnonymousOrLocal((ClassSymbol) m))
                        continue;
                    // avoid member classes
                    if(((ClassSymbol)m).getNestingKind() != NestingKind.TOP_LEVEL)
                        continue;
                    convertToDeclaration(lookupClassMirror(m.getQualifiedName().toString()), DeclarationType.VALUE);
                }
            }
        }
        // a bit complicated, but couldn't find better. PackageSymbol.exists() seems to be set only by Enter which
        // might be too late
        return ceylonPkg.members().getElements().iterator().hasNext();
    }

    private boolean isAnonymousOrLocal(ClassSymbol m) {
        switch(m.getNestingKind()){
        case ANONYMOUS: return true;
        case LOCAL: return true;
        case TOP_LEVEL: return false;
        case MEMBER: return isAnonymousOrLocal((ClassSymbol) m.owner);
        }
        // can't reach?
        return false;
    }

    private ClassSymbol getEnclosing(ClassSymbol c) {
        Symbol owner = c.owner;
        com.sun.tools.javac.util.List<Name> enclosing = Convert.enclosingCandidates(Convert.shortName(c.name));
        if(enclosing.isEmpty())
            return c;
        Name name = enclosing.head;
        Symbol encl = owner.members().lookup(name).sym;
        if (encl == null || !(encl instanceof ClassSymbol))
            encl = symtab.classes.get(TypeSymbol.formFlatName(name, owner));
        if(encl != null)
            return (ClassSymbol) encl;
        return c;
    }
    
    @Override
    public synchronized ClassMirror lookupNewClassMirror(String name) {
        ClassSymbol classSymbol = null;

        String outerName = name;
        /*
         * This madness here tries to look for a class, and if it fails, tries to resolve it 
         * from its parent class. This is required because a.b.C.D (where D is an inner class
         * of C) is not found in symtab.classes but in C's ClassSymbol.enclosedElements.
         */
        do{
            String suffix = "";
            if (lastPartHasLowerInitial(outerName) && !outerName.endsWith("_")) {
                suffix = "_";
            }
            classSymbol = symtab.classes.get(names.fromString(Util.quoteJavaKeywords(outerName+suffix)));
            if(classSymbol != null){
                // if we got a source symbol for something non-Java it's a slipery slope
                if(Util.isLoadedFromSource(classSymbol) && !Util.isJavaSource(classSymbol))
                    return null;
                if(outerName.length() != name.length())
                    classSymbol = lookupInnerClass(classSymbol, name.substring(outerName.length()+1).split("\\."));
                if(classSymbol != null && classSymbol.classfile == null && classSymbol.sourcefile == null){
                    // try to complete it if that changes anything
                    try{
                        classSymbol.complete();
                    }catch(CompletionFailure x){
                        // if we can't complete it it doesn't exist, its classfile will remain null
                    }
                    if(classSymbol.classfile == null){
                        PackageSymbol pkg = classSymbol.packge();
                        // do not log an error for missing oracle jdk stuff
                        if(pkg == null || !JDKUtils.isOracleJDKAnyPackage(pkg.getQualifiedName().toString())){
                            log.error("ceylon", "Unable to find class file for "+name);
                        }
                        return null;
                    }
                }
                return classSymbol != null ? new JavacClass(classSymbol) : null;
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
                            && (s.getSimpleName().toString().equals(part)
                                    || (Util.isInitialLowerCase(part) 
                                            && s.getSimpleName().toString().equals(part + "_")))){
                        classSymbol = (ClassSymbol) s;
                        continue PART;
                    }
                }
                // didn't find the inner class
                return null;
            }
        return classSymbol;
    }

    private MethodSymbol getOverriddenMethod(MethodSymbol method, Types types) {
        try{
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
        }catch(CompletionFailure x){
            if(method.owner != null){
                PackageSymbol methodPackage = method.owner.packge();
                if(methodPackage != null){
                    String methodPackageName = methodPackage.getQualifiedName().toString();
                    if(JDKUtils.isJDKAnyPackage(methodPackageName)){
                        if(x.sym != null && x.sym instanceof ClassSymbol){
                            PackageSymbol pkg = ((ClassSymbol)x.sym).packge();
                            if(pkg != null){
                                String pkgName = pkg.getQualifiedName().toString();
                                if(JDKUtils.isOracleJDKAnyPackage(pkgName)){
                                    // the JDK tried to use some Oracle JDK stuff, just log it
                                    logMissingOracleType(x.getMessage());
                                    return null;
                                }
                            }
                        }
                    }
                }
            }
            // in every other case, rethrow
            throw x;
        }
    }

    public Symtab syms() {
        return symtab;
    }

    @Override
    protected void logVerbose(String message) {
        if(options.get(OptionName.VERBOSE) != null || options.get(OptionName.VERBOSE + ":loader") != null){
            Log.printLines(log.noticeWriter, message);
        }
    }

    @Override
    protected void logWarning(String message) {
        log.warning("ceylon", message);
    }

    @Override
    protected void logError(String message) {
        log.error("ceylon", message);
    }

    @Override
    protected boolean isOverridingMethod(MethodMirror methodSymbol) {
        final MethodSymbol method = ((JavacMethod)methodSymbol).methodSymbol;
        if (method.owner.getQualifiedName().contentEquals("ceylon.language.Identifiable")) {
            if (method.name.contentEquals("equals") || method.name.contentEquals("hashCode")) {
                return true;
            }
        }
        return getOverriddenMethod(method, types) != null;
    }

    @Override
    public void logDuplicateModuleError(Module module, Module loadedModule) {
        super.logDuplicateModuleError(module, loadedModule);
        // let's just give up, otherwise typechecking will likely throw some more and confuse the users
        throw new Abort();
    }
}
