package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.common.ModuleUtil.isMavenModule;
import static com.redhat.ceylon.compiler.typechecker.parser.CeylonLexer.STRING_LITERAL;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.buildAnnotations;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.formatPath;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.getAnnotation;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.getAnnotationArgument;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.getAnnotationArgumentCount;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.getNativeBackend;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.hasAnnotation;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.name;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.setRestrictionArgument;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.formatPath;
import static com.redhat.ceylon.model.typechecker.model.Module.DEFAULT_MODULE_NAME;
import static com.redhat.ceylon.model.typechecker.model.Module.LANGUAGE_MODULE_NAME;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.CommonToken;

import com.redhat.ceylon.cmr.impl.DefaultRepository;
import com.redhat.ceylon.cmr.impl.MavenRepository;
import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.Backends;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.ModuleImport;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.util.ModuleManager;

/**
 * Detect and populate the list of imports for modules. In 
 * theory should only be called on module.ceylon and
 * package.ceylon files
 *
 * Put restrictions on how module.ceylon files are built 
 * today:
 * 
 *  - names and versions must be string literals or else 
 *    the visitor cannot extract them
 *  - imports must be "explicitly" defined, ie not imported 
 *    as List<Import> or else the module names cannot be 
 *    extracted
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class ModuleVisitor extends Visitor {
    
    /**
     * Instance of the visited module which will receive
     * the dependencies declaration
     */
    private Module mainModule;
    private final ModuleManager moduleManager;
    private final ModuleSourceMapper moduleManagerUtil;
    private final Package pkg;
    private Tree.CompilationUnit unit;
    private Phase phase = Phase.SRC_MODULE;
    private boolean completeOnlyAST = false;
    private Backends moduleBackends = Backends.ANY;
    private boolean moduleFile;

    public void setCompleteOnlyAST(boolean completeOnlyAST) {
        this.completeOnlyAST = completeOnlyAST;
    }

    public boolean isCompleteOnlyAST() {
        return completeOnlyAST;
    }

    public ModuleVisitor(ModuleManager moduleManager, 
            ModuleSourceMapper moduleManagerUtil, Package pkg, 
            boolean moduleFile) {
        this.moduleManager = moduleManager;
        this.moduleManagerUtil = moduleManagerUtil;
        this.pkg = pkg;
        this.moduleFile = moduleFile;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }
    
    private Map<String,String> constants = new HashMap<String,String>(0);

    @Override
    public void visit(Tree.AttributeDeclaration that) {
        Tree.Identifier id = that.getIdentifier();
        if (id!=null) {
            String name = id.getText();
            Tree.SpecifierOrInitializerExpression sie = 
                    that.getSpecifierOrInitializerExpression();
            if (sie!=null) {
                Tree.Expression ex = sie.getExpression();
                if (ex!=null) {
                    Tree.Term term = ex.getTerm();
                    if (term instanceof Tree.StringLiteral) {
                        String value = term.getText();
                        constants.put(name, "\"" + value + "\"");
                    }
                    else {
                        ex.addError("not a string literal");
                    }
                }
            }
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.AttributeGetterDefinition that) {
        that.getBlock().addError("not a constant");
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.CompilationUnit that) {
        unit = that;
        if (moduleFile && that.getModuleDescriptors().isEmpty()) {
            that.addError("missing module descriptor");
        }
        if (!moduleFile && that.getPackageDescriptors().isEmpty()) {
            that.addError("missing package descriptor");
        }
        super.visit(that);
    }
    
    private static String removeQuotes(String quoted) {
        return quoted.substring(1, quoted.length()-1);
    }

    private String getVersionString(
            Tree.QuotedLiteral quoted, 
            Tree.BaseMemberExpression constantVersion,
            Node that) {
        if (constantVersion!=null) {
            String name = 
                    constantVersion.getIdentifier().getText();
            String constVers = constants.get(name);
            if (constVers==null) {
                constantVersion.addError("constant not defined");
                return "0";
            }
            else {
                return toVersionString(constantVersion, constVers);
            }
        }
        else if (quoted!=null) {
            return toVersionString(quoted, quoted.getText());
        }
        else {
            that.addError("missing version");
            return "0";
        }
    }

    private static String toVersionString(Node node, String versionString) {
        for (int i=0; i<versionString.length();) {
            int codePoint = versionString.codePointAt(i);
            i += Character.charCount(codePoint);
            if (Character.isWhitespace(codePoint)) {
                node.addError("module version may not contain whitespace");
                break;
            }
        }
        if (versionString.length()<2) {
            return "";
        }
        else {
            if (versionString.charAt(0)=='\'') {
                node.addError("module version should be double-quoted");
            }
            String version = removeQuotes(versionString);
            if (version.isEmpty()) {
                node.addError("empty module version");
            }
            return version;
        }
    }

    private static String getNameString(Tree.QuotedLiteral quoted) {
        return getNameString(quoted, true);
    }

    private static String getNameString(Tree.QuotedLiteral quoted, 
            boolean addErrorOnInvalidQuotes) {
        String nameString = quoted.getText();
        if (nameString.length()<2) {
            return "";
        }
        else {
            for (int i=0; i<nameString.length();) {
                int codePoint = nameString.codePointAt(i);
                i += Character.charCount(codePoint);
                if (Character.isWhitespace(codePoint)) {
                    quoted.addError("module name may not contain whitespace");
                    break;
                }
            }
            if (addErrorOnInvalidQuotes && 
                    nameString.charAt(0)=='\'') {
                quoted.addError("module name should be double-quoted");
            }
            return removeQuotes(nameString);
        }
    }

    @Override
    public void visit(Tree.ModuleDescriptor that) {
        Tree.AnnotationList al = that.getAnnotationList();
        Unit u = unit.getUnit();
        moduleBackends = getNativeBackend(al, u);
        super.visit(that);
        if (phase==Phase.SRC_MODULE) {
            String version = 
                    getVersionString(
                            that.getVersion(), 
                            null, 
                            that);
            Tree.ImportPath importPath = that.getImportPath();
            for (Tree.Identifier id: importPath.getIdentifiers()) {
                if (containsDiscouragedChar(id)) {
                    id.addUsageWarning(Warning.packageName, 
                            "all-lowercase ASCII module names are recommended");
                }
            }
            List<String> name = getNameAsList(importPath);
            if (pkg.getNameAsString().isEmpty()) {
                that.addError("module descriptor encountered in root source directory");
            }
            else if (name.isEmpty()) {
                that.addError("missing module name");
            }
            else {
                String initialName = name.get(0);
                Backends unitBackends = 
                        u.getSupportedBackends();
                if (initialName.equals(DEFAULT_MODULE_NAME)) {
                    importPath.addError("reserved module name: 'default'");
                }
                else if (name.size()==1 && 
                         initialName.equals("ceylon")) {
                    importPath.addError("reserved module name: 'ceylon'");
                }
                else if (!moduleBackends.none()
                        && moduleBackends.header()) {
                    that.addError("missing backend argument for native annotation on module: "
                        + formatPath(importPath.getIdentifiers()));
                }
                else if (!moduleBackends.none()
                        && !unitBackends.none()
                        && !unitBackends.supports(moduleBackends)) {
                    that.addError("module not meant for this backend: "
                        + formatPath(importPath.getIdentifiers()));
                }
                else {
                    if (initialName.equals("ceylon")) {
                        importPath.addUsageWarning(
                                Warning.ceylonNamespace,
                                "discouraged module name: this namespace is used by Ceylon platform modules");
                    }
                    else if (initialName.equals("java") || 
                             initialName.equals("javax")) {
                        importPath.addUnsupportedError("unsupported module name: this namespace is used by Java platform modules");
                    }
                    mainModule = 
                            moduleManager.getOrCreateModule(
                                    name, version);
                    importPath.setModel(mainModule);
                    if (!completeOnlyAST) {
                        mainModule.setUnit(u);
                        mainModule.setVersion(version);
//                        if (hasAnnotation(al, "label", u)) {
//                            mainModule.setLabel(getAnnotationArgument(
//                                    getAnnotation(al, "label", u), 
//                                    0, u));
//                        }
                    }
                    String nameString = 
                            formatPath(importPath.getIdentifiers());
                    if ( !pkg.getNameAsString().equals(nameString) ) {
                        importPath
                            .addError("module name does not match descriptor location: '" + 
                                    nameString + "' should be '" + 
                                    pkg.getNameAsString() + "'", 
                                    8000);
                    }
                    if (!completeOnlyAST) {
                        moduleManagerUtil.addLinkBetweenModuleAndNode(
                                mainModule, that);
                        mainModule.setAvailable(true);
                        mainModule.getAnnotations().clear();
                        buildAnnotations(al, 
                                mainModule.getAnnotations());
                        mainModule.setNativeBackends(moduleBackends);
                        Tree.QuotedLiteral classifier = that.getClassifier();
                        if (classifier != null) {
                            mainModule.setClassifier(getNameString(classifier));
                            classifier.addUnsupportedError("classifiers not yet supported");
                        }
                        Tree.QuotedLiteral artifact = that.getArtifact();
                        if (artifact != null) {
                            mainModule.setArtifactId(getNameString(artifact));
                        }
                        Tree.ImportPath groupImportPath = that.getGroupImportPath();
                        Tree.QuotedLiteral groupQuotedLiteral = that.getGroupQuotedLiteral();
                        if (groupImportPath != null) {
                            mainModule.setGroupId(formatPath(groupImportPath.getIdentifiers()));
                        }
                        else if (groupQuotedLiteral != null) {
                            mainModule.setGroupId(getNameString(groupQuotedLiteral));
                        }
                    }
                }
            }
            HashSet<String> set = new HashSet<String>();
            Tree.ImportModuleList iml = 
                    that.getImportModuleList();
            if (iml!=null) {
                for (Tree.ImportModule im: 
                        iml.getImportModules()) {
                    String path = im.getName();
                    if (path!=null) {
                        if (!set.add(path)) {
                            im.addError("duplicate module import: '" + 
                                    path + "'");
                        }
                    }
                }
            }
        }
        moduleBackends = Backends.ANY;
    }

    private static boolean containsDiscouragedChar(Tree.Identifier id) {
        for (char ch: id.getText().toCharArray()) {
            if ((ch<'a' || ch>'z') && (ch<'0' || ch>'9')) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void visit(Tree.PackageDescriptor that) {
        super.visit(that);
        if (phase==Phase.REMAINING) {
            Tree.ImportPath importPath = that.getImportPath();
            for (Tree.Identifier id: importPath.getIdentifiers()) {
                if (containsDiscouragedChar(id)) {
                    id.addUsageWarning(Warning.packageName, 
                            "all-lowercase ASCII package names are recommended");
                }
            }
            List<String> name = getNameAsList(importPath);
            if (pkg.getNameAsString().isEmpty()) {
                that.addError("package descriptor encountered in root source directory");
            }
            else if (name.isEmpty()) {
                that.addError("missing package name");
            }
            else if (name.get(0).equals(DEFAULT_MODULE_NAME)) {
                importPath.addError("reserved module name: 'default'");
            }
            else if (name.size()==1 && 
                     name.get(0).equals("ceylon")) {
                importPath.addError("reserved module name: 'ceylon'");
            }
            else {
                if (name.get(0).equals("ceylon")) {
                    importPath.addUsageWarning(
                            Warning.ceylonNamespace,
                            "discouraged package name: this namespace is used by Ceylon platform modules");
                }
                else if (name.get(0).equals("java") ||
                         name.get(0).equals("javax")) {
                    importPath.addUsageWarning(
                            Warning.javaNamespace,
                            "discouraged package name: this namespace is used by Java platform modules");
                }
                importPath.setModel(pkg);
                Unit u = unit.getUnit();
                if (!completeOnlyAST) {
                    pkg.setUnit(u);
                }
                String nameString = 
                        formatPath(importPath.getIdentifiers());
                if ( !pkg.getNameAsString().equals(nameString) ) {
                    importPath
                        .addError("package name does not match descriptor location: '" + 
                                nameString + "' should be '" + 
                                pkg.getNameAsString() + "'", 
                                8000);
                }
                
                Tree.AnnotationList al = 
                        that.getAnnotationList();
                
                if (!completeOnlyAST) {
                    pkg.setShared(hasAnnotation(al, "shared", u));
                    pkg.getAnnotations().clear();
                    buildAnnotations(al, pkg.getAnnotations());
                }
                
            
                if (hasAnnotation(al, "restricted", u)) {
                    Tree.Annotation ann = getAnnotation(al, "restricted", u);
                    int len = getAnnotationArgumentCount(ann);
                    List<String> modules = new ArrayList<String>(len);
                    for (int i=0; i<len; i++) {
                        setRestrictionArgument(ann, i, u);
                        String arg = getAnnotationArgument(ann, i, u);
                        if (arg!=null) {
                            modules.add(arg);
                        }
                    }
                    
                    if (!completeOnlyAST) {
                        if (modules.isEmpty()) {
                            pkg.setShared(false);
                        }
                        else {
                            pkg.setRestrictions(modules);
                        }
                    }
                }

            }
        }
    }
    
    @Override
    public void visit(Tree.ImportModule that) {
        super.visit(that);
        String version = 
                getVersionString(
                        that.getVersion(),
                        that.getConstantVersion(),
                        that);
        if (that.getVersion()==null 
                && version!=null) {
            that.setVersion(new Tree.QuotedLiteral(
                    new CommonToken(STRING_LITERAL, 
                            "\"" + version + "\"")));
        }
        List<String> name;
        Node node;

        Tree.ImportPath importPath = 
                that.getImportPath();
        Tree.QuotedLiteral quotedLiteral = 
                that.getQuotedLiteral();
        if (importPath!=null) {
            name = getNameAsList(importPath);
            node = importPath;
        }
        else if (quotedLiteral!=null) {
            String nameString = 
                    getNameString(quotedLiteral);
            name = asList(nameString.split("\\."));
            node = quotedLiteral;
        }
        else {
            name = Collections.emptyList();
            node = null;
        }
        
        if (node!=null) {
            Tree.QuotedLiteral artifact = 
                    that.getArtifact();
            if (artifact!=null) {
                name = new ArrayList<String>(name);
                String nameString = getNameString(artifact);
                name.add("");
                name.addAll(asList(nameString.split("\\.")));
            }
            Tree.QuotedLiteral classifier = 
                    that.getClassifier();
            if (classifier!=null) {
                String nameString = getNameString(classifier);
                name.add("");
                name.addAll(asList(nameString.split("\\.")));
            }
        }
        if (phase==Phase.SRC_MODULE){
            String path = formatPath(name);
            that.setName(path);
        }
        else if (phase==Phase.REMAINING) {
            // set in previous phase
            String path = that.getName();

            Tree.Identifier ns = that.getNamespace();
            String namespace = ns!=null ? ns.getText() : null;
            boolean hasMavenName = 
                    isMavenModule(path);
            boolean forCeylon 
                     = (importPath != null && namespace == null)
                    || (importPath == null && namespace == null && !hasMavenName)
                    || DefaultRepository.NAMESPACE.equals(namespace);
            if (name.isEmpty()) {
                that.addError("missing module name");
            }
            else if (name.get(0).equals(DEFAULT_MODULE_NAME)) {
                if (forCeylon) {
                    node.addError("reserved module name: 'default'");
                }
            }
            else if (name.size()==1 && 
                     name.get(0).equals("ceylon")) {
                if (forCeylon) {
                    node.addError("reserved module name: 'ceylon'");
                }
            }
            else if (name.size()>1 && 
                     name.get(0).equals("ceylon") && 
                     name.get(1).equals("language")) {
                if (forCeylon) {
                    node.addError("the language module is imported implicitly");
                }
            }
            else {
                if (namespace == null && hasMavenName) {
                    namespace = MavenRepository.NAMESPACE;
                    node.addUsageWarning(Warning.missingImportPrefix,
                            "use of old style Maven imports is deprecated, prefix with 'maven:'");
                }
                Tree.AnnotationList al =
                        that.getAnnotationList();
                Unit u = unit.getUnit();
                Backends bs = getNativeBackend(al, u);
                if (!bs.none()) {
                    for (Backend b : bs) {
                        if (!b.isRegistered()) {
                            node.addError("illegal native backend name: '\"" + 
                                    b.nativeAnnotation + "\"' (must be either '\"jvm\"' or '\"js\"')");
                        }
                    }
                    if (!moduleBackends.none() && !moduleBackends.supports(bs)) {
                        node.addError("native backend name on import conflicts with module descriptor: '\"" + 
                                bs.names() + "\"' is not in '\"" + moduleBackends.names() + "\"'");
                    }
                }
                Module importedModule = 
                        moduleManager.getOrCreateModule(
                                name, version);
                if (importPath!=null) {
                    importPath.setModel(importedModule);
                }
                if (!completeOnlyAST && mainModule != null) {
                    if (importedModule.getVersion() == null) {
                        importedModule.setVersion(version);
                    }
                    ModuleImport moduleImport =
                            moduleManager.findImport(
                                    mainModule, importedModule);
                    if (moduleImport == null) {
                        boolean optional =
                                hasAnnotation(al, "optional", u);
                        boolean export =
                                hasAnnotation(al, "shared", u);
                        moduleImport =
                                new ModuleImport(namespace,
                                        importedModule,
                                        optional, export, bs);
                        moduleImport.getAnnotations().clear();
                        buildAnnotations(al,
                                moduleImport.getAnnotations());
                        mainModule.addImport(moduleImport);
                    }
                    moduleManagerUtil.addModuleDependencyDefinition(
                            moduleImport, that);
                }
            }
        }
    }

    private List<String> getNameAsList(Tree.ImportPath that) {
        List<String> name = new ArrayList<String>();
        for (Tree.Identifier i: that.getIdentifiers()) {
           name.add(i.getText()); 
        }
        return name;
    }
    
    public enum Phase {
        SRC_MODULE,
        REMAINING
    }
    
    public Module getMainModule() {
        return mainModule;
    }
    
    @Override
    public void visit(Tree.Import that) {
        super.visit(that);
        Tree.ImportPath path = that.getImportPath();
        if (path!=null && 
                formatPath(path.getIdentifiers())
                    .equals(LANGUAGE_MODULE_NAME)) {
            Tree.ImportMemberOrTypeList imtl = 
                    that.getImportMemberOrTypeList();
            if (imtl!=null) {
                for (Tree.ImportMemberOrType imt: 
                        imtl.getImportMemberOrTypes()) {
                    if (imt.getAlias()!=null && 
                            imt.getIdentifier()!=null) {
                        String name = 
                                name(imt.getIdentifier());
                        String alias = 
                                name(imt.getAlias().getIdentifier());
                        Map<String, String> mods = 
                                unit.getUnit().getModifiers();
                        if (mods.containsKey(name)) {
                            String curr = mods.get(alias);
                            if (curr!=null && curr.equals(alias)) {
                                mods.remove(alias);
                            }
                            mods.put(name, alias);
                        }
                    }
                }
            }
        }
    }
    
}
