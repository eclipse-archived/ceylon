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

import static com.redhat.ceylon.ceylondoc.CeylondMessages.msg;
import static com.redhat.ceylon.ceylondoc.Util.getAnnotation;
import static com.redhat.ceylon.ceylondoc.Util.isAbbreviatedType;
import static com.redhat.ceylon.ceylondoc.Util.normalizeSpaces;
import static com.redhat.ceylon.model.typechecker.util.TypePrinter.abbreviateCallable;
import static com.redhat.ceylon.model.typechecker.util.TypePrinter.abbreviateEntry;
import static com.redhat.ceylon.model.typechecker.util.TypePrinter.abbreviateIterable;
import static com.redhat.ceylon.model.typechecker.util.TypePrinter.abbreviateOptional;
import static com.redhat.ceylon.model.typechecker.util.TypePrinter.abbreviateSequence;
import static com.redhat.ceylon.model.typechecker.util.TypePrinter.abbreviateSequential;
import static com.redhat.ceylon.model.typechecker.util.TypePrinter.abbreviateTuple;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.config.DefaultToolOptions;
import com.redhat.ceylon.compiler.java.codegen.Decl;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.typechecker.model.Annotated;
import com.redhat.ceylon.model.typechecker.model.Annotation;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Element;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.NothingType;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.Referenceable;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.TypeAlias;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.model.Value;
import com.redhat.ceylon.model.typechecker.util.TypePrinter;

public class LinkRenderer {
    
    private Object to;
    private Object from;
    private CeylonDocTool ceylonDocTool;
    private Writer writer;
    private String customText;
    private boolean withinText;
    private Referenceable scope;
    private Declaration anchor;
    private boolean printAbbreviated = true;
    private boolean printTypeParameters = true;
    private boolean printTypeParameterDetail = false;
    private boolean printWikiStyleLinks = false;
    private boolean printLinkDropdownMenu = true;
    private boolean printParenthesisAfterMethodName = true;
    private boolean printMemberContainerName = true;
    
    private final TypePrinter producedTypeNamePrinter = new TypePrinter() {
        
        @Override
        public String getSimpleDeclarationName(Declaration declaration, Unit unit) {
            String result = null;
            
            if (declaration instanceof ClassOrInterface || declaration instanceof NothingType) {
                TypeDeclaration type = (TypeDeclaration) declaration;
                if (isLinkable(type)) {
                    String typeUrl = getUrl(type, null);
                    if (typeUrl != null) {
                        result = buildLinkElement(typeUrl, getLinkText(type), "Go to " + type.getQualifiedNameString());
                    }
                }
                if( result == null ) {
                    result = buildSpanElementWithNameAndTooltip(declaration);
                }
            } else if (declaration instanceof TypeParameter) {
                result = "<span class='type-parameter'>" + declaration.getName(unit) + "</span>";
            } else if (declaration instanceof TypedDeclaration) {
                result = processTypedDeclaration((TypedDeclaration) declaration);
            } else if (declaration instanceof TypeAlias) {
                result = processTypeAlias((TypeAlias) declaration);
            } else {
                result = buildSpanElementWithNameAndTooltip(declaration);
            }
            
            return encodeResult(result);
        }

        @Override
        public boolean printAbbreviated() {
            return printAbbreviated;
        }

        @Override
        public boolean printTypeParameters() {
            return printTypeParameters;
        }

        @Override
        public boolean printTypeParameterDetail() {
            return printTypeParameterDetail;
        }

        @Override
        public boolean printQualifyingType() {
            return false;
        }
        
    };
    
    public LinkRenderer(CeylonDocTool ceylonDocTool, Writer writer, Object from) {
        this.ceylonDocTool = ceylonDocTool;
        this.writer = writer;
        this.from = from;
    }
    
    public LinkRenderer(LinkRenderer linkRenderer) {
        this.to = linkRenderer.to;
        this.from = linkRenderer.from;
        this.ceylonDocTool = linkRenderer.ceylonDocTool;
        this.writer = linkRenderer.writer;
        this.customText = linkRenderer.customText;
        this.scope = linkRenderer.scope;
        this.anchor = linkRenderer.anchor;
        this.printAbbreviated = linkRenderer.printAbbreviated;
        this.printTypeParameters = linkRenderer.printTypeParameters;
        this.printTypeParameterDetail = linkRenderer.printTypeParameterDetail;
        this.printLinkDropdownMenu = linkRenderer.printLinkDropdownMenu;
    }
    
    public LinkRenderer to(Object to) {
        this.to = to;
        return this;
    }

    public LinkRenderer useCustomText(String customText) {
        this.customText = customText;
        return this;
    }
    
    public LinkRenderer withinText(boolean text) {
        withinText = text;
        return this;
    }
    
    public LinkRenderer useScope(Referenceable scope) {
        this.scope = scope;
        return this;
    }
    
    public LinkRenderer useAnchor(Declaration anchor) {
        this.anchor = anchor;
        return this;
    }
    
    public LinkRenderer printAbbreviated(boolean printAbbreviated) {
        this.printAbbreviated = printAbbreviated;
        return this;
    }

    public LinkRenderer printTypeParameters(boolean printTypeParameters) {
        this.printTypeParameters = printTypeParameters;
        return this;
    }
    
    public LinkRenderer printTypeParameterDetail(boolean printTypeParameterDetail) {
        this.printTypeParameterDetail = printTypeParameterDetail;
        return this;
    }
    
    public LinkRenderer printWikiStyleLinks(boolean printWikiStyleLinks) {
        this.printWikiStyleLinks = printWikiStyleLinks;
        return this;
    }
    
    public LinkRenderer printLinkDropdownMenu(boolean printLinkDropdownMenu) {
        this.printLinkDropdownMenu = printLinkDropdownMenu;
        return this;
    }
    
    public LinkRenderer printParenthesisAfterMethodName(boolean printParenthesisAfterMethodName) {
        this.printParenthesisAfterMethodName = printParenthesisAfterMethodName;
        return this;
    }
    
    public LinkRenderer printMemberContainerName(boolean printMemberContainerName) {
        this.printMemberContainerName = printMemberContainerName;
        return this;
    }

    public String getLink() {
        String link = null;
        if (to instanceof String) {
            if (printWikiStyleLinks) {
                link = processWikiLink((String) to);
            } else {
                link = processAnnotationParam((String) to);
            }
        } else if (to instanceof Type) {
            link = processProducedType((Type) to);
        } else if (to instanceof Declaration) {
            link = processDeclaration(((Declaration) to));
        } else if (to instanceof Module) {
            link = processModule((Module) to);
        } else if (to instanceof Package) {
            link = processPackage((Package) to);
        }
        return link;
    }
    
    public String getUrl() {
        return getUrl(to, anchor);
    }
    
    public String getResourceUrl(String to) throws IOException {
        return ceylonDocTool.getResourceUrl(from, to);
    }

    public String getSrcUrl(Object to) throws IOException {
        return ceylonDocTool.getSrcUrl(from, to);
    }

    public void write() throws IOException {
        writer.write(getLink());
    }

    private String processModule(Module module) {
        String moduleUrl = getUrl(module, anchor);
        if (moduleUrl != null) {
            return buildLinkElement(moduleUrl, module.getNameAsString(), "Go to module");
        } else {
            return module.getNameAsString();
        }
    }
    
    private String processPackage(Package pkg) {
        String pkgUrl = getUrl(pkg, anchor);
        if (pkgUrl != null) {
            return buildLinkElement(pkgUrl, customText != null ? customText : pkg.getNameAsString(), "Go to package " + pkg.getNameAsString());
        } else {
            return pkg.getNameAsString();
        }
    }
    
    private String processDeclaration(Declaration decl) {
        if (decl instanceof TypeDeclaration) {
            return processProducedType(((TypeDeclaration) decl).getType());
        } else {
            return processTypedDeclaration((TypedDeclaration) decl);
        }
    }

    private String processProducedType(Type producedType) {
        String result;
        boolean wasWithinText = withinText;
        withinText = false;
        try {
            result = producedTypeNamePrinter.print(producedType, null);
        }
        finally {
            withinText = wasWithinText;
        }
        result = decodeResult(result);
        if (withinText && customText==null) {
            result = "<code>" + result + "</code>";
        }
        result = decorateWithLinkDropdownMenu(result, producedType);
        return result;
    }
    
    private String processTypedDeclaration(TypedDeclaration decl) {
        String declName = Util.getDeclarationName(decl);
        Scope declContainer = decl.getContainer();
        
        if( isLinkable(decl) ) {
            String url = getUrl(declContainer, decl);
            if( url != null ) {
                return buildLinkElement(url, getLinkText(decl), "Go to " + decl.getQualifiedNameString());
            }
        }
        
        String result = declName;
        if (withinText) {
            result = "<code>" + result + "</code>";
        }
        if (customText != null) {
            result = customText + " (" + result + ")";
        }
        return result;
    }
    
    private String processTypeAlias(TypeAlias alias) {
        String aliasName = alias.getName();
        Scope aliasContainer = alias.getContainer();
        
        if (isLinkable(alias)) {
            String url = getUrl(aliasContainer, alias);
            if (url != null) {
                return buildLinkElement(url, aliasName, "Go to " + alias.getQualifiedNameString());
            }
        }
        return buildSpanElementWithNameAndTooltip(alias);
    }

    private String processWikiLink(final String docLinkText) {
        Tree.DocLink docLink = findDocLink(docLinkText, scope);
        if (docLink == null && scope instanceof Declaration) {
            Declaration refinedDeclaration = ((Declaration) scope).getRefinedDeclaration();
            if (refinedDeclaration != scope) {
                docLink = findDocLink(docLinkText, refinedDeclaration);
            }
        }

        if (docLink != null) {
            if (docLink.getQualified() != null && docLink.getQualified().size() > 0) {
                return processDeclaration(docLink.getQualified().get(docLink.getQualified().size() - 1));
            } else if (docLink.getBase() != null) {
                printAbbreviated = !isAbbreviatedType(docLink.getBase());
                return processDeclaration(docLink.getBase());
            } else if (docLink.getModule() != null) {
                return processModule(docLink.getModule());
            } else if (docLink.getPkg() != null) {
                return processPackage(docLink.getPkg());
            }
        }
        
        if (docLink != null && scope instanceof Annotated) {
            Annotation docAnnotation = getAnnotation(scope.getUnit(), ((Annotated) scope).getAnnotations(), "doc");
            if (docAnnotation != null) {
                ceylonDocTool.warningBrokenLink(docLinkText, docLink, scope);
            }
        }

        return getUnresolvableLink(docLinkText);
    }

    private String processAnnotationParam(String text) {
        if( text.equals("module")) {
            Module mod = getCurrentModule();
            if( mod != null) {
                return processModule(mod);
            }
        }
        if (text.startsWith("module ")) {
            String modName = text.substring(7);
            for (Module m : ceylonDocTool.getTypeChecker().getContext().getModules().getListOfModules()) {
                if (m.getNameAsString().equals(modName)) {
                    return processModule(m);
                }
            }
        }
        if( text.equals("package")) {
            Package pkg = getCurrentPackage();
            if (pkg != null) {
                return processPackage(pkg);
            }
        }
        if (text.startsWith("package ")) {
            String pkgName = text.substring(8);
            for (Module m : ceylonDocTool.getTypeChecker().getContext().getModules().getListOfModules()) {
                if (pkgName.startsWith(m.getNameAsString() + ".")) {
                    Package pkg = m.getPackage(pkgName);
                    if (pkg != null) {
                        return processPackage(pkg);
                    }
                }
            }
        }
        if( text.equals("interface") ) {
            Interface interf = getCurrentInterface();
            if( interf != null ) {
                return processProducedType(interf.getType());
            }
        }
        if( text.equals("class") ) {
            Class clazz = getCurrentClass();
            if( clazz != null ) {
                return processProducedType(clazz.getType());
            }
        }
        
        
        String declName;
        Scope currentScope;
        
        int pkgSeparatorIndex = text.indexOf("::");
        if( pkgSeparatorIndex == -1 ) {
            declName = text;
            currentScope = resolveScope(scope);
        } else {
            String pkgName = text.substring(0, pkgSeparatorIndex);
            declName = text.substring(pkgSeparatorIndex+2, text.length());
            currentScope = ceylonDocTool.getCurrentModule().getPackage(pkgName);
        }
        
        String[] declNames = declName.split("\\.");
        Declaration currentDecl = null;
        boolean isNested = false;
        for (String currentDeclName : declNames) {
            currentDecl = resolveDeclaration(currentScope, currentDeclName, isNested);
            if (currentDecl != null) {
                if( isValueWithTypeObject(currentDecl) ) {
                    TypeDeclaration objectType = ((Value)currentDecl).getTypeDeclaration();
                    currentScope = objectType;
                    currentDecl = objectType;
                } else {
                    currentScope = resolveScope(currentDecl);
                }
                isNested = true;
            } else {
                break;
            }
        }
        
        // we can't link to parameters yet, unless they're toplevel
        if (currentDecl != null && !isParameter(currentDecl)) {
            if (currentDecl instanceof TypeDeclaration) {
                return processProducedType(((TypeDeclaration) currentDecl).getType());
            } else {
                return processTypedDeclaration((TypedDeclaration) currentDecl);
            }
        } else {
            return getUnresolvableLink(text);
        }
    }

    private boolean isLinkable(Declaration decl) {
        if( decl == null ) {
            return false;
        }
        if( decl.isParameter() ) {
            return true;
        }
        if( !ceylonDocTool.isIncludeNonShared() ) {
            if( !decl.isShared() ) {
                return false;
            }
            
            Scope c = decl.getContainer();
            while(c != null) {
                boolean isShared = true;
                if( c instanceof Declaration ) {
                    isShared = ((Declaration) c).isShared();
                }
                if( c instanceof Package ) {
                    isShared = ((Package) c).isShared();
                }
                if( !isShared ) {
                    return false;
                }
                c = c.getContainer();
            }
        }
        return true;
    }

    private boolean isValueWithTypeObject(Declaration decl) {
        if (Decl.isValue(decl)) {
            TypeDeclaration typeDeclaration = ((Value) decl).getTypeDeclaration();
            if (typeDeclaration instanceof Class && typeDeclaration.isAnonymous()) {
                return true;
            }
        }
        return false;
    }

    private boolean isParameter(Declaration decl) {
        return decl instanceof FunctionOrValue
                && ((FunctionOrValue)decl).isParameter();
    }
    
    private Declaration resolveDeclaration(Scope scope, String declName, boolean isNested) {
        Declaration decl = null;

        if (scope != null) {
            decl = scope.getMember(declName, null, false);

            if (decl == null && !isNested && scope instanceof Element) {
                decl = ((Element) scope).getUnit().getImportedDeclaration(declName, null, false);
            }

            if (decl == null && !isNested && !scope.getQualifiedNameString().equals(AbstractModelLoader.CEYLON_LANGUAGE) ) {
                decl = resolveDeclaration(scope.getContainer(), declName, isNested);
            }
            
            if (decl == null && declName.equals("Nothing") && scope.getQualifiedNameString().equals(AbstractModelLoader.CEYLON_LANGUAGE)) {
                decl = new NothingType(((Package) scope).getUnit());
            }
        } else {
            Package pkg = ceylonDocTool.getCurrentModule().getPackage(AbstractModelLoader.CEYLON_LANGUAGE);
            if (pkg != null) {
                decl = resolveDeclaration(pkg, declName, isNested);
            }
        }

        return decl;
    }

    private Scope resolveScope(Referenceable referenceable) {
        if (referenceable instanceof Module) {
            return ((Module) referenceable).getPackage(referenceable.getNameAsString());
        } else if (referenceable instanceof Scope) {
            return (Scope) referenceable;
        } else if (referenceable instanceof Declaration) {
            return ((Declaration) referenceable).getContainer();
        } else {
            return null;
        }
    }

    private boolean isInCurrentModule(Object obj) {
        Module objModule = null;
        if (obj instanceof Module) {
            objModule = (Module) obj;
        } else if (obj instanceof Scope) {
            objModule = getPackage((Scope) obj).getModule();
        } else if (obj instanceof Element) {
            objModule = getPackage(((Element) obj).getScope()).getModule();
        }
        
        Module currentModule = ceylonDocTool.getCurrentModule();
        if (currentModule != null && objModule != null) {
            return currentModule.equals(objModule);
        }
        
        return false;
    }
    
    private String getUnresolvableLink(final String docLinkText) {
        StringBuilder unresolvable = new StringBuilder();
        unresolvable.append("<span class='link-unresolvable'>");
        boolean hasCustomText = 
                customText != null && !customText.equals(docLinkText);
        if (hasCustomText) {
            unresolvable.append(customText);
            unresolvable.append(" (");
        }
        if (withinText) unresolvable.append("<code>");
        int index = docLinkText.indexOf('|')+1;
        unresolvable.append(docLinkText.substring(index));
        if (withinText) unresolvable.append("</code>");
        if (hasCustomText) {
            unresolvable.append(")");
        }
        unresolvable.append("</span>");
        return unresolvable.toString();
    }
    
    private String getLinkText(Declaration decl) {
        if (customText != null) {
            return customText;
        }
        else {
            String name = Util.getDeclarationName(decl);
            if( scope != null && scope.getUnit() != null ) {
                name = scope.getUnit().getAliasedName(decl, name);
            }

            String result;
            if (decl instanceof TypeDeclaration) {
                result = "<span class='type-identifier'>" + name + "</span>";
            }
            else {
                if (decl instanceof Function && printParenthesisAfterMethodName) {
                    name = name + "()";
                }
                result = "<span class='identifier'>" + name + "</span>";
            }
            if (printMemberContainerName && decl.isMember() && decl.getContainer() != from) {
                result = getLinkText((Declaration) decl.getContainer()) + '.' + result;
            }
            return result;
        }
    }
    
    private Package getPackage(Scope scope) {
        while (!(scope instanceof Package)) {
            scope = scope.getContainer();
        }
        return (Package) scope;
    }   

    private String getUrl(Object to, Declaration anchor) {
        String url;
        
        List<Function> methods = new ArrayList<Function>();
        while(to instanceof Function){
            Function method = (Function) to;
            methods.add(method);
            to = method.getContainer();
        }
        
        if (isInCurrentModule(to)) {
            url = getLocalUrl(to);
        } else {
            url = getExternalUrl(to);
        }        
            
        if (url != null && anchor != null) {
            String sectionPackageAnchor = "#section-package";
            if (url.endsWith(sectionPackageAnchor)) {
                url = url.substring(0, url.length() - sectionPackageAnchor.length());
            }
            StringBuilder fragment = new StringBuilder();
            if(!methods.isEmpty()) {
                Collections.reverse(methods);
                for(Function method : methods) {
                    fragment.append(method.getName());
                    fragment.append("-");
                }
            }
            fragment.append(anchor.getName());
            url = url + "#" + fragment;
        }            
            
        return url;
    }
    
    private String getLocalUrl(Object to) {
        try {
            return ceylonDocTool.getObjectUrl(from, to);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getExternalUrl(Object to) {
        String url = null;
        if (to instanceof Module) {
            url = getExternalModuleUrl((Module)to);
            if (url != null) {
                url += "index.html";
            }
        } else if (to instanceof Package) {
            Package pkg = (Package)to;
            url = getExternalModuleUrl(pkg.getModule());
            if (url != null) {
                url += buildPackageUrlPath(pkg);
                url += "index.html";
            }
        } else if (to instanceof ClassOrInterface) {
            ClassOrInterface klass = (ClassOrInterface) to;
            Package pkg = getPackage(klass);
            url = getExternalModuleUrl(pkg.getModule());
            if (url != null) {
                url += buildPackageUrlPath(pkg);
                url += ceylonDocTool.getFileName(klass);
            }
        }
        return url;
    }
    
    private String getExternalModuleUrl(Module module) {
        if( ceylonDocTool.getLinks() != null ) {
            String moduleName = module.getNameAsString();
            
            for (String link : ceylonDocTool.getLinks()) {
                String[] linkParts = divideToPatternAndUrl(link);
                String moduleNamePattern = linkParts[0];
                String moduleRepoUrl = linkParts[1];
                
                if (moduleNamePattern == null) {
                    String moduleDocUrl = buildModuleUrl(moduleRepoUrl, module);
                    if (isHttpProtocol(moduleDocUrl) && checkHttpUrlExist(moduleDocUrl)) {
                        return moduleDocUrl;
                    }
                    if (isFileProtocol(moduleDocUrl) && checkFileUrlExist(moduleDocUrl)) {
                        return moduleDocUrl;
                    }
                } else if (moduleName.startsWith(moduleNamePattern)) {
                    return buildModuleUrl(moduleRepoUrl, module);
                }
            }
        }
        return null;
    }
  
    private String buildLinkElement(String url, String text, String toolTip) {
        StringBuilder linkBuilder = new StringBuilder();
        linkBuilder.append("<a ");
        if (customText != null) {
            linkBuilder.append("class='link-custom-text'");
        } else {
            linkBuilder.append("class='link'");
        }
        linkBuilder.append(" href='").append(url).append("'");
        if (toolTip != null) {
        	linkBuilder.append(" title='").append(toolTip).append("'");
        }
        linkBuilder.append(">");
        if (customText==null && withinText) {
            linkBuilder.append("<code>");
        }
        linkBuilder.append(text);
        if (customText==null && withinText) {
            linkBuilder.append("</code>");
        }
        linkBuilder.append("</a>");
        return linkBuilder.toString();
    }
    
    private String buildSpanElementWithNameAndTooltip(Declaration d) {
        StringBuilder spanBuilder = new StringBuilder();
        spanBuilder.append("<span title='");
        spanBuilder.append(d.getQualifiedNameString());
        spanBuilder.append("'>");
        if (withinText) spanBuilder.append("<code>");
        spanBuilder.append(getLinkText(d));
        if (withinText) spanBuilder.append("</code>");
        spanBuilder.append("</span>");
        return spanBuilder.toString();
    }

    private String buildModuleUrl(String moduleRepoUrl, Module module) {
        StringBuilder moduleUrlBuilder = new StringBuilder();
        moduleUrlBuilder.append(moduleRepoUrl);
        if (!moduleRepoUrl.endsWith("/")) {
            moduleUrlBuilder.append("/");
        }
        moduleUrlBuilder.append(Util.join("/", module.getName()));
        moduleUrlBuilder.append("/");
        moduleUrlBuilder.append(module.getVersion());
        moduleUrlBuilder.append("/module-doc/api/");
        return moduleUrlBuilder.toString();
    }
    
    private String buildPackageUrlPath(Package pkg) {
        List<String> packagePath = pkg.getName().subList(pkg.getModule().getName().size(), pkg.getName().size());
        if (!packagePath.isEmpty()) {
            return Util.join("/", packagePath) + "/";
        }
        return "";
    }
    
    public static String[] divideToPatternAndUrl(String link) {
        String moduleRepoUrl = null;
        String moduleNamePattern = null;

        int indexOfSeparator = link.indexOf("=");
        if (indexOfSeparator != -1) {
            moduleNamePattern = link.substring(0, indexOfSeparator);
            moduleRepoUrl = link.substring(indexOfSeparator + 1);
        } else {
            moduleRepoUrl = link;
        }

        return new String[] { moduleNamePattern, moduleRepoUrl };
    }
    
    public static boolean isHttpProtocol(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    public static boolean isFileProtocol(String url) {
        return url.startsWith("file://");
    }

    private boolean checkHttpUrlExist(String moduleUrl) {
        Boolean result = ceylonDocTool.getModuleUrlAvailabilityCache().get(moduleUrl);
        if( result == null ) {
            try {
                URL url = new URL(moduleUrl + "index.html");
                HttpURLConnection con;
                Proxy proxy = DefaultToolOptions.getDefaultProxy();
                if (proxy != null) {
                    con = (HttpURLConnection) url.openConnection(proxy);
                } else {
                    con = (HttpURLConnection) url.openConnection();
                }
                con.setConnectTimeout((int) DefaultToolOptions.getDefaultTimeout());
                con.setReadTimeout((int) DefaultToolOptions.getDefaultTimeout() * Constants.READ_TIMEOUT_MULTIPLIER);
                con.setRequestMethod("HEAD");
                int responseCode = con.getResponseCode();
    
                if( responseCode == HttpURLConnection.HTTP_OK ) {
                    result = Boolean.TRUE;                
                } else {
                    ceylonDocTool.getLogger().warning(msg("info.urlDoesNotExist", moduleUrl));
                    result = Boolean.FALSE;
                }
            }
            catch (IOException e) {
                ceylonDocTool.getLogger().warning(msg("info.urlDoesNotExist", moduleUrl));
                result = Boolean.FALSE;
            }
            ceylonDocTool.getModuleUrlAvailabilityCache().put(moduleUrl, result);
        }
        return result.booleanValue();
    }
    
    private boolean checkFileUrlExist(String moduleUrl) {
        Boolean result = ceylonDocTool.getModuleUrlAvailabilityCache().get(moduleUrl);
        if( result == null ) {
            File moduleDocDir = new File(moduleUrl.substring("file://".length()));
            if (moduleDocDir.isDirectory() && moduleDocDir.exists()) {
                result = Boolean.TRUE;
            } else {
                ceylonDocTool.getLogger().warning(msg("info.urlDoesNotExist", moduleUrl));
                result = Boolean.FALSE;
            }
            ceylonDocTool.getModuleUrlAvailabilityCache().put(moduleUrl, result);
        }
        return result.booleanValue();
    }
    
    private static String encodeResult(String text) {
        if (text != null) {
            text = text.replaceAll("<", "#LT;");
            text = text.replaceAll(">", "#GT;");
        }
        return text;
    }
    
    private static String decodeResult(String text) {
        if (text != null) {
            text = text.replaceAll("&", "&amp;");
            text = text.replaceAll("<", "&lt;");
            text = text.replaceAll(">", "&gt;");
            text = text.replaceAll("#LT;", "<");
            text = text.replaceAll("#GT;", ">");

            text = text.replaceAll("&lt;in ", "&lt;<span class='type-parameter-keyword'>in </span>");
            text = text.replaceAll(",in ", ",<span class='type-parameter-keyword'>in </span>");

            text = text.replaceAll("&lt;out ", "&lt;<span class='type-parameter-keyword'>out </span>");
            text = text.replaceAll(",out ", ",<span class='type-parameter-keyword'>out </span>");
        }
        return text;
    }

    private String decorateWithLinkDropdownMenu(String link, Type producedType) {
        if( !printLinkDropdownMenu || !printAbbreviated || !canLinkToCeylonLanguageModule() ) {
            return link;
        }
        
        List<Type> producedTypes = new ArrayList<Type>();
        decompose(producedType, producedTypes);
        
        boolean containsOptional = false;
        boolean containsSequential = false;
        boolean containsSequence = false;
        boolean containsIterable = false;
        boolean containsEntry = false;
        boolean containsCallable = false;
        boolean containsTuple = false;
        
        for (Type pt : producedTypes) {
            if (abbreviateOptional(pt)) {
                containsOptional = true;
            } else if (abbreviateSequential(pt) && !link.contains("'Go to ceylon.language::Sequential'")) {
                containsSequential = true;
            } else if (abbreviateSequence(pt) && !link.contains("'Go to ceylon.language::Sequence'")) {
                containsSequence = true;
            } else if (abbreviateIterable(pt) && !link.contains("'Go to ceylon.language::Iterable'")) {
                containsIterable = true;
            } else if (abbreviateEntry(pt) && !link.contains("'Go to ceylon.language::Entry'")) {
                containsEntry = true;
            } else if (abbreviateCallable(pt) && !link.contains("'Go to ceylon.language::Callable'")) {
                containsCallable = true;
            } else if (abbreviateTuple(pt) && !link.contains("'Go to ceylon.language::Tuple'")) {
                containsTuple = true;
            }
        }
        
        Unit unit = producedType.getDeclaration().getUnit();
        
        if( containsOptional || containsSequential || containsSequence || containsIterable || containsEntry || containsCallable || containsTuple ) {
            StringBuilder sb = new StringBuilder();
            sb.append("<span class='link-dropdown'>");
            sb.append(link.replaceAll("class='link'", "class='link type-identifier'"));
            sb.append("<span class='dropdown'>");
            sb.append("<a class='dropdown-toggle' data-toggle='dropdown' href='#'><b title='Show more links' class='caret'></b></a>");
            sb.append("<ul class='dropdown-menu'>");    
            if( containsOptional ) {
                sb.append(getLinkMenuItem(unit.getNullDeclaration(), "abbreviations X? means Null|X"));
            }
            if( containsSequential ) {
                sb.append(getLinkMenuItem(unit.getSequentialDeclaration(), "abbreviation X[] or [X*] means Sequential&lt;X&gt;"));
            }
            if( containsSequence ) {
                sb.append(getLinkMenuItem(unit.getSequenceDeclaration(), "abbreviation [X+] means Sequence&lt;X&gt;"));
            }
            if( containsIterable ) {
                sb.append(getLinkMenuItem(unit.getIterableDeclaration(), "abbreviation {X+} or {X*} means Iterable&lt;X,Nothing&gt; or Iterable&lt;X,Null&gt;"));
            }
            if( containsEntry ) {
                sb.append(getLinkMenuItem(unit.getEntryDeclaration(), "abbreviation X-&gt;Y means Entry&lt;X,Y&gt;"));
            }
            if( containsCallable ) {
                sb.append(getLinkMenuItem(unit.getCallableDeclaration(), "abbreviation X(Y,Z) means Callable&lt;X,[Y,Z]&gt;"));
            }
            if( containsTuple ) {
                sb.append(getLinkMenuItem(unit.getTupleDeclaration(), "abbreviation [X,Y] means Tuple&lt;X|Y,X,Tuple&lt;Y,Y,[]&gt;&gt;"));
            }
            sb.append("</ul>"); // dropdown-menu
            sb.append("</span>"); // dropdown
            sb.append("</span>"); // link-dropdown
            
            return sb.toString();
        }
        
        return link;
    }

    private boolean canLinkToCeylonLanguageModule() {
        Module currentModule = ceylonDocTool.getCurrentModule();
        if (currentModule.getNameAsString().equals(Module.LANGUAGE_MODULE_NAME)) {
            return true;
        } else {
            Module languageModule = currentModule.getLanguageModule();
            String languageModuleUrl = getExternalModuleUrl(languageModule);
            return languageModuleUrl != null;
        }
    }

    private void decompose(Type pt, List<Type> producedTypes) {
        if (!producedTypes.contains(pt)) {
            producedTypes.add(pt);
            if (pt.isIntersection()) {
                for (Type satisfiedType : pt.getSatisfiedTypes()) {
                    decompose(satisfiedType, producedTypes);
                }
            }
            else if (pt.isUnion()) {
                for (Type caseType : pt.getCaseTypes()) {
                    decompose(caseType, producedTypes);
                }
            }
            if (!pt.getTypeArgumentList().isEmpty()) {
                for (Type typeArgument : pt.getTypeArgumentList()) {
                    decompose(typeArgument, producedTypes);
                }
            }
        }
    }

    private String getLinkMenuItem(Declaration decl, String description) {
        String url = new LinkRenderer(this).
                to(decl).
                useCustomText("").
                printLinkDropdownMenu(false).
                printAbbreviated(false).
                printTypeParameters(false).
                getUrl();

        StringBuilder sb = new StringBuilder();
        sb.append("<li>");
        sb.append("<a class='link' href='").append(url).append("'>");
        sb.append("Go to ").append(decl.getName()).append(" ");
        sb.append("<small>").append(description).append("</small>");
        sb.append("</a>");
        sb.append("</li>");

        return sb.toString();
    }
    
    private Tree.DocLink findDocLink(final String docLinkText, Referenceable referenceable) {
        final Tree.DocLink[] docLinks = new Tree.DocLink[1];
        Node scopeNode = ceylonDocTool.getNode(referenceable);
        if (scopeNode != null) {
            scopeNode.visit(new Visitor() {
                @Override
                public void visit(Tree.DocLink docLink) {
                    String s1 = normalizeSpaces(docLinkText);
                    String s2 = normalizeSpaces(docLink.getText());
                    if (s1.equals(s2)) {
                        docLinks[0] = docLink;
                        return;
                    }
                }
            });
        }
        return docLinks[0];
    }
    
    private Module getCurrentModule() {
        if (scope instanceof Module) {
            return (Module) scope;
        } else if (scope instanceof Package) {
            return ((Package) scope).getModule();
        } else if (scope instanceof Declaration) {
            return scope.getUnit().getPackage().getModule();
        }
        return null;
    }

    private Package getCurrentPackage() {
        if (scope instanceof Module) {
            return ((Module) scope).getRootPackage();
        } else if (scope instanceof Package) {
            return (Package) scope;
        } else if (scope instanceof Declaration) {
            return scope.getUnit().getPackage();
        }
        return null;
    }
    
    private Interface getCurrentInterface() {
        Object o = scope;
        while (o != null) {
            if (o instanceof Interface) {
                return (Interface) o;
            } else if (o instanceof Declaration) {
                o = ((Declaration) o).getContainer();
            } else {
                o = null;
            }
        }
        return null;
    }

    private Class getCurrentClass() {
        Object o = scope;
        while (o != null) {
            if (o instanceof Class) {
                return (Class) o;
            } else if (o instanceof Declaration) {
                o = ((Declaration) o).getContainer();
            } else {
                o = null;
            }
        }
        return null;
    }

}