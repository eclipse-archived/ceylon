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
import static com.redhat.ceylon.ceylondoc.Util.normalizeSpaces;
import static com.redhat.ceylon.compiler.typechecker.util.ProducedTypeNamePrinter.abbreviateCallable;
import static com.redhat.ceylon.compiler.typechecker.util.ProducedTypeNamePrinter.abbreviateEntry;
import static com.redhat.ceylon.compiler.typechecker.util.ProducedTypeNamePrinter.abbreviateIterable;
import static com.redhat.ceylon.compiler.typechecker.util.ProducedTypeNamePrinter.abbreviateOptional;
import static com.redhat.ceylon.compiler.typechecker.util.ProducedTypeNamePrinter.abbreviateSequence;
import static com.redhat.ceylon.compiler.typechecker.util.ProducedTypeNamePrinter.abbreviateSequential;
import static com.redhat.ceylon.compiler.typechecker.util.ProducedTypeNamePrinter.abbreviateTuple;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.java.codegen.Decl;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Element;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.NothingType;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Referenceable;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeAlias;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.typechecker.util.ProducedTypeNamePrinter;

public class LinkRenderer {
    
    private Object to;
    private Object from;
    private CeylonDocTool ceylonDocTool;
    private Writer writer;
    private String customText;
    private Referenceable scope;
    private Declaration anchor;
    private boolean printAbbreviated = true;
    private boolean printTypeParameters = true;
    private boolean printTypeParameterDetail = false;
    private boolean printWikiStyleLinks = false;
    private boolean printLinkDropdownMenu = true;
    
    private final ProducedTypeNamePrinter producedTypeNamePrinter = new ProducedTypeNamePrinter() {
        
        @Override
        public String getSimpleDeclarationName(Declaration declaration, Unit unit) {
            String result = null;
            
            if (declaration instanceof ClassOrInterface || declaration instanceof NothingType) {
                TypeDeclaration type = (TypeDeclaration) declaration;
                String typeUrl = getUrl(type, null);
                if (typeUrl != null) {
                    result = buildLinkElement(typeUrl, getLinkText(type), "Go to " + type.getQualifiedNameString());

                } else {
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
    
    public LinkRenderer useScope(Module module) {
        scope = module;
        return this;
    }

    public LinkRenderer useScope(Package pkg) {
        scope = pkg;
        return this;
    }

    public LinkRenderer useScope(Declaration decl) {
        scope = decl;
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

    public String getLink() {
        String link = null;
        if (to instanceof String) {
            if (printWikiStyleLinks) {
                link = processWikiLink((String) to);
            } else {
                link = processAnnotationParam((String) to);
            }
        } else if (to instanceof ProducedType) {
            link = processProducedType((ProducedType) to);
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
        String link = getLink();
        writer.write(link);
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

    private String processProducedType(ProducedType producedType) {
        String result = producedTypeNamePrinter.getProducedTypeName(producedType, null);
        result = decodeResult(result);
        result = decorateWithLinkDropdownMenu(result, producedType);
        return result;
    }
    
    private String processTypedDeclaration(TypedDeclaration decl) {
        String declName = decl.getName();
        Scope declContainer = decl.getContainer();
        
        String url = getUrl(declContainer, decl);
        if( url != null ) {
            return buildLinkElement(url, getLinkText(decl), "Go to " + decl.getQualifiedNameString());
        } else {
            return declName;
        }
    }
    
    private String processTypeAlias(TypeAlias alias) {
        String aliasName = alias.getName();
        Scope aliasContainer = alias.getContainer();

        String url = getUrl(aliasContainer, alias);
        if (url != null) {
            return buildLinkElement(url, aliasName, "Go to " + alias.getQualifiedNameString());
        } else {
            return buildSpanElementWithNameAndTooltip(alias);
        }
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
                return processDeclaration(docLink.getBase());
            } else if (docLink.getModule() != null) {
                return processModule(docLink.getModule());
            } else if (docLink.getPkg() != null) {
                return processPackage(docLink.getPkg());
            }
        }

        ceylonDocTool.warningBrokenLink(docLinkText, scope);

        return getUnresolvableLink(docLinkText);
    }

    private String processAnnotationParam(String declLink) {
        String declName;
        Scope currentScope;
        
        int pkgSeparatorIndex = declLink.indexOf("::");
        if( pkgSeparatorIndex == -1 ) {
            declName = declLink;
            currentScope = resolveScope(scope);
        } else {
            String pkgName = declLink.substring(0, pkgSeparatorIndex);
            declName = declLink.substring(pkgSeparatorIndex+2, declLink.length());
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
            return getUnresolvableLink(declLink);
        }
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
        return decl instanceof MethodOrValue
                && ((MethodOrValue)decl).isParameter();
    }
    
    private Declaration resolveDeclaration(Scope scope, String declName, boolean isNested) {
        Declaration decl = null;

        if (scope != null) {
            decl = scope.getMember(declName, null, false);

            if (decl == null && !isNested && scope instanceof Element) {
                decl = ((Element) scope).getUnit().getImportedDeclaration(declName, null, false);
            }

            if (decl == null && !isNested && !scope.getQualifiedNameString().equals("ceylon.language") ) {
                decl = resolveDeclaration(scope.getContainer(), declName, isNested);
            }
            
            if (decl == null && declName.equals("Nothing") && scope.getQualifiedNameString().equals("ceylon.language")) {
                decl = new NothingType(((Package) scope).getUnit());
            }
        } else {
            Package pkg = ceylonDocTool.getCurrentModule().getPackage("ceylon.language");
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
        if( printWikiStyleLinks ) {
            unresolvable.append("[");
        }
        if (customText != null && !customText.equals(docLinkText)) {
            unresolvable.append(customText);
            unresolvable.append("|");
        }
        unresolvable.append(docLinkText);
        if( printWikiStyleLinks ) {
            unresolvable.append("]");
        }
        unresolvable.append("</span>");
        return unresolvable.toString();
    }
    
    private String getLinkText(Declaration decl) {
        String text;
        if( customText != null  ) {
            text = customText;
        } else if (to instanceof String) {
            String name = removeTypeLiteralPrefix((String) to);
            if (from instanceof Element) {
                String aliasedName = ((Element) from).getUnit().getAliasedName(decl);
                if (aliasedName != null && !aliasedName.equals(decl.getName()) && aliasedName.equals(name)) {
                    text = decl.getQualifiedNameString();
                } else {
                    text = name;
                }
            } else {
                text = name;
            }
        } else {
            text = decl.getName();
        }
        return text;
    }
    
    private String removeTypeLiteralPrefix(String text) {
        if( text.startsWith("module ") ) {
            return text.substring(7);
        } else if( text.startsWith("package ") ) {
            return text.substring(8);
        } else if( text.startsWith("class ") ) {
            return text.substring(6);                        
        } else if( text.startsWith("interface ") ) {
            return text.substring(10);
        } else if( text.startsWith("function ") ) {
            return text.substring(9);
        } else if( text.startsWith("value ") ) {
            return text.substring(6);
        } else if( text.startsWith("alias ") ) {
            return text.substring(6);
        } else {
            return text;
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
        
        Method method = null;
        if(to instanceof Method){
            method = (Method) to;
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
            String fragment;
            if(method == null)
                fragment = anchor.getName();
            else
                fragment = method.getName() + "-" + anchor.getName();
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
        if( customText != null ) {
            linkBuilder.append("class='link-custom-text'");
        } else {
            linkBuilder.append("class='link'");
        }
        linkBuilder.append(" href='").append(url).append("'");
        if (toolTip != null) {
        	linkBuilder.append(" title='").append(toolTip).append("'");
        }
        linkBuilder.append(">");
        linkBuilder.append(text);
        linkBuilder.append("</a>");
        return linkBuilder.toString();
    }
    
    private String buildSpanElementWithNameAndTooltip(Declaration d) {
        StringBuilder spanBuilder = new StringBuilder();
        spanBuilder.append("<span title='");
        spanBuilder.append(d.getQualifiedNameString());
        spanBuilder.append("'>");
        spanBuilder.append(getLinkText(d));
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
        moduleUrlBuilder.append("/module-doc/");
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
                HttpURLConnection con = (HttpURLConnection) new URL(moduleUrl + "index.html").openConnection();
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

    private String decorateWithLinkDropdownMenu(String link, ProducedType producedType) {
        if( !printLinkDropdownMenu || !printAbbreviated || !canLinkToCeylonLanguageModule() ) {
            return link;
        }
        
        List<ProducedType> producedTypes = new ArrayList<ProducedType>();
        decompose(producedType, producedTypes);
        
        boolean containsOptional = false;
        boolean containsSequential = false;
        boolean containsSequence = false;
        boolean containsIterable = false;
        boolean containsEntry = false;
        boolean containsCallable = false;
        boolean containsTuple = false;
        
        for (ProducedType pt : producedTypes) {
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
            sb.append(link);
            sb.append("<span class='dropdown'>");
            sb.append("<a class='dropdown-toggle' data-toggle='dropdown' href='#'><b title='Show more links' class='caret'></b></a>");
            sb.append("<ul class='dropdown-menu'>");    
            if( containsOptional ) {
                sb.append(getLinkMenuItem(unit.getNothingDeclaration(), "abbreviations X? means X|Nothing"));
            }
            if( containsSequential ) {
                sb.append(getLinkMenuItem(unit.getSequentialDeclaration(), "abbreviations X[] or [X*] means Sequential&lt;X&gt;"));
            }
            if( containsSequence ) {
                sb.append(getLinkMenuItem(unit.getSequenceDeclaration(), "abbreviations [X+] means Sequence&lt;X&gt;"));
            }
            if( containsIterable ) {
                sb.append(getLinkMenuItem(unit.getIterableDeclaration(), "abbreviations {X+} or {X*} means Iterable&lt;X,Nothing&gt; or Iterable&lt;X,Null&gt;"));
            }
            if( containsEntry ) {
                sb.append(getLinkMenuItem(unit.getEntryDeclaration(), "abbreviations X-&gt;Y means Entry&lt;X,Y&gt;"));
            }
            if( containsCallable ) {
                sb.append(getLinkMenuItem(unit.getCallableDeclaration(), "abbreviations X(Y,Z) means Callable&lt;X,[Y,Z]&gt;"));
            }
            if( containsTuple ) {
                sb.append(getLinkMenuItem(unit.getTupleDeclaration(), "abbreviations [X,Y] means Tuple&lt;X|Y,X,Tuple&lt;Y,Y,[]&gt;&gt;"));
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

    private void decompose(ProducedType pt, List<ProducedType> producedTypes) {
        if (!producedTypes.contains(pt)) {
            producedTypes.add(pt);
            TypeDeclaration decl = pt.getDeclaration();
            if (decl instanceof IntersectionType) {
                for (ProducedType satisfiedType : pt.getSatisfiedTypes()) {
                    decompose(satisfiedType, producedTypes);
                }
            } else if (decl instanceof UnionType) {
                for (ProducedType caseType : pt.getCaseTypes()) {
                    decompose(caseType, producedTypes);
                }
            }
            if (!pt.getTypeArgumentList().isEmpty()) {
                for (ProducedType typeArgument : pt.getTypeArgumentList()) {
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
    
}