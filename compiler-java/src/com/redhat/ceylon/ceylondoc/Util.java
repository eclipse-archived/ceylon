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

import java.io.File;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.github.rjeschke.txtmark.BlockEmitter;
import com.github.rjeschke.txtmark.Configuration;
import com.github.rjeschke.txtmark.Processor;
import com.github.rjeschke.txtmark.SpanEmitter;
import com.redhat.ceylon.compiler.java.codegen.Decl;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;

public class Util {
    
    private static final Set<String> abbreviatedTypes = new HashSet<String>();
    static {
        abbreviatedTypes.add("ceylon.language::Empty");
        abbreviatedTypes.add("ceylon.language::Entry");
        abbreviatedTypes.add("ceylon.language::Sequence");
        abbreviatedTypes.add("ceylon.language::Sequential");
        abbreviatedTypes.add("ceylon.language::Iterable");
    }

    public static boolean isAbbreviatedType(Declaration decl) {
        return abbreviatedTypes.contains(decl.getQualifiedNameString());
    }
	
	public static String join(String separator, List<String> parts) {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> iterator = parts.iterator();
        while(iterator.hasNext()){
            stringBuilder.append(iterator.next());
            if(iterator.hasNext())
                stringBuilder.append(separator);
        }
        return stringBuilder.toString();
    }

    private static final int FIRST_LINE_MAX_SIZE = 120;

    public static String getDoc(Declaration decl, LinkRenderer linkRenderer) {
        return wikiToHTML(getRawDoc(decl), linkRenderer.useScope(decl));
    }

    public static String getDoc(Module module, LinkRenderer linkRenderer) {
        return wikiToHTML(getRawDoc(module.getAnnotations()), linkRenderer.useScope(module));
    }
    
    public static String getDoc(ModuleImport moduleImport, LinkRenderer linkRenderer) {
        return wikiToHTML(getRawDoc(moduleImport.getAnnotations()), linkRenderer);
    }

    public static String getDoc(Package pkg, LinkRenderer linkRenderer) {
        return wikiToHTML(getRawDoc(pkg.getAnnotations()), linkRenderer.useScope(pkg));
    }

    public static String getDocFirstLine(Declaration decl, LinkRenderer linkRenderer) {
        return wikiToHTML(getFirstLine(getRawDoc(decl)), linkRenderer.useScope(decl));
    }

    public static String getDocFirstLine(Package pkg, LinkRenderer linkRenderer) {
        return wikiToHTML(getFirstLine(getRawDoc(pkg.getAnnotations())), linkRenderer.useScope(pkg));
    }

    public static String getDocFirstLine(Module module, LinkRenderer linkRenderer) {
        return wikiToHTML(getFirstLine(getRawDoc(module.getAnnotations())), linkRenderer.useScope(module));
    }
    
    public static List<String> getTags(Declaration decl) {
        List<String> tags = new ArrayList<String>();
        Annotation tagged = Util.getAnnotation(decl.getAnnotations(), "tagged");
        if (tagged != null) {
            tags.addAll(tagged.getPositionalArguments());
        }
        return tags;
    }
    
    public static String wikiToHTML(String text, LinkRenderer linkRenderer) {
        if( text == null || text.length() == 0 ) {
            return text;
        }
        
        Configuration config = Configuration.builder()
                .forceExtentedProfile()
                .setCodeBlockEmitter(CeylondocBlockEmitter.INSTANCE)
                .setSpecialLinkEmitter(new CeylondocSpanEmitter(linkRenderer))
                .build();
        
        return Processor.process(text, config);
    }

    private static String getFirstLine(String text) {
        // be lenient for Package and Module
        if(text == null)
            return "";
        // First try to get the first sentence
        BreakIterator breaker = BreakIterator.getSentenceInstance();
        breaker.setText(text);
        breaker.first();
        int dot = breaker.next();
        // First sentence is sufficiently short
        if (dot != BreakIterator.DONE
                && dot <= FIRST_LINE_MAX_SIZE) {
            return text.substring(0, dot).replaceAll("\\s*$", "");
        }
        if (text.length() <= FIRST_LINE_MAX_SIZE) {
            return text;
        }
        // First sentence is really long, to try to break on a word
        breaker = BreakIterator.getWordInstance();
        breaker.setText(text);
        int pos = breaker.first();
        while (pos < FIRST_LINE_MAX_SIZE
                && pos != BreakIterator.DONE) {
            pos = breaker.next();
        }
        if (pos != BreakIterator.DONE
                && breaker.previous() != BreakIterator.DONE) {
            return text.substring(0, breaker.current()).replaceAll("\\s*$", "") + "…";
        }
        return text.substring(0, FIRST_LINE_MAX_SIZE-1) + "…";
    }

    private static String getRawDoc(Declaration decl) {
        Annotation a = findAnnotation(decl, "doc");
        if (a != null) {
            return a.getPositionalArguments().get(0);
        }
        return "";
    }
    
    private static String getRawDoc(List<Annotation> anns) {
        for (Annotation a : anns) {
            if (a.getName().equals("doc") && a.getPositionalArguments() != null && !a.getPositionalArguments().isEmpty()) {
                return a.getPositionalArguments().get(0);
            }
        }
        return "";
    }

    public static Annotation getAnnotation(ModuleImport moduleImport, String name) {
        for (Annotation a : moduleImport.getAnnotations()) {
            if (a.getName().equals(name))
                return a;
        }
        return null;
    }
    
    public static Annotation getAnnotation(List<Annotation> annotations, String name) {
        if (annotations != null) {
            for (Annotation a : annotations) {
                if (a.getName().equals(name))
                    return a;
            }
        }
        return null;
    }
    
    public static Annotation findAnnotation(Declaration decl, String name) {
        Annotation a = getAnnotation(decl.getAnnotations(), name);
        if (a == null && decl.isActual()) {
            a = getAnnotation(decl.getRefinedDeclaration().getAnnotations(), name);
        }
        return a;
    }
    
    public static String capitalize(String text) {
        char[] buffer = text.toCharArray();
        boolean capitalizeNext = true;
        for (int i = 0; i < buffer.length; i++) {
            char ch = buffer[i];
            if (Character.isWhitespace(ch)) {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer[i] = Character.toTitleCase(ch);
                capitalizeNext = false;
            }
        }
        return new String(buffer);
    }

    public static String getModifiers(Declaration d) {
        StringBuilder modifiers = new StringBuilder();
        if (d.isShared()) {
            modifiers.append("shared ");
        }
        if (d.isFormal()) {
            modifiers.append("formal ");
        } else {
            if (d.isActual()) {
                modifiers.append("actual ");
            }
            if (d.isDefault()) {
                modifiers.append("default ");
            }
        }
        if (Decl.isValue(d)) {
            Value v = (Value) d;
            if (v.isVariable()) {
                modifiers.append("variable ");
            }
        } else if (d instanceof Class) {
            Class c = (Class) d;
            if (c.isAbstract()) {
                modifiers.append("abstract ");
            }
        }
        return modifiers.toString().trim();
    }

    public static List<TypeDeclaration> getAncestors(TypeDeclaration decl) {
        List<TypeDeclaration> ancestors = new ArrayList<TypeDeclaration>();
        TypeDeclaration ancestor = decl.getExtendedTypeDeclaration();
        while (ancestor != null) {
            ancestors.add(ancestor);
            ancestor = ancestor.getExtendedTypeDeclaration();
        }
        return ancestors;
    }

    public static List<ProducedType> getSuperInterfaces(TypeDeclaration decl) {
        Set<ProducedType> superInterfaces = new HashSet<ProducedType>();
        List<ProducedType> satisfiedTypes = decl.getSatisfiedTypes();
        for (ProducedType satisfiedType : satisfiedTypes) {
            superInterfaces.add(satisfiedType);
            superInterfaces.addAll(getSuperInterfaces(satisfiedType.getDeclaration()));
        }
        ArrayList<ProducedType> list = new ArrayList<ProducedType>();
        list.addAll(superInterfaces);
        removeDuplicates(list);
        return list;
    }

    private static void removeDuplicates(List<ProducedType> superInterfaces) {
        OUTER: for (int i = 0; i < superInterfaces.size(); i++) {
            ProducedType pt1 = superInterfaces.get(i);
            // compare it with each type after it
            for (int j = i + 1; j < superInterfaces.size(); j++) {
                ProducedType pt2 = superInterfaces.get(j);
                if (pt1.getDeclaration().equals(pt2.getDeclaration())) {
                    if (pt1.isSubtypeOf(pt2)) {
                        // we keep the first one because it is more specific
                        superInterfaces.remove(j);
                    } else {
                        // we keep the second one because it is more specific
                        superInterfaces.remove(i);
                        // since we removed the first type we need to stay at
                        // the same index
                        i--;
                    }
                    // go to next type
                    continue OUTER;
                }
            }
        }
    }
    
    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }
    
    public static boolean isException(Class c) {
        if (c != null) {
            if ("ceylon.language::Exception".equals(c.getQualifiedNameString())) {
                return true;
            } else {
                return isException(c.getExtendedTypeDeclaration());
            }
        }
        return false;
    }  

    public static void delete(File f){
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                delete(c);
        }
        if (!f.delete())
            throw new RuntimeException(CeylondMessages.msg("error.failedDeleteFile", f));
    }

    public static String getUnitPackageName(PhasedUnit unit) {
        // WARNING: TypeChecker VFS alyways uses '/' chars and not platform-dependent ones
        String path = unit.getPathRelativeToSrcDir();
        String file = unit.getUnitFile().getName();
        if(!path.endsWith(file)){
            throw new RuntimeException("Unit relative path does not end with unit file name: "+path+" and "+file);
        }
        path = path.substring(0, path.length() - file.length());
        if(path.endsWith("/"))
            path = path.substring(0, path.length() - 1);
        return path.replace('/', '.');
    }

    public static String getQuotedFQN(String pkgName, com.redhat.ceylon.compiler.typechecker.tree.Tree.Declaration decl) {
        String name = decl.getIdentifier().getText();
        String qualifiedName = pkgName.isEmpty() ? name : pkgName + "." + name;
        return com.redhat.ceylon.compiler.java.util.Util.quoteJavaKeywords(qualifiedName);
    }
    
    private static class CeylondocBlockEmitter implements BlockEmitter {
        
        private static final CeylondocBlockEmitter INSTANCE = new CeylondocBlockEmitter();

        @Override
        public void emitBlock(StringBuilder out, List<String> lines, String meta) {
            if (lines.isEmpty())
                return;
            
            if( meta == null || meta.length() == 0 ) {
                out.append("<pre>");
            }
            else {
                out.append("<pre class=\"brush: ").append(meta).append("\">");
            }

            for (final String s : lines) {
                for (int i = 0; i < s.length(); i++) {
                    final char c = s.charAt(i);
                    switch (c) {
                    case '&':
                        out.append("&amp;");
                        break;
                    case '<':
                        out.append("&lt;");
                        break;
                    case '>':
                        out.append("&gt;");
                        break;
                    default:
                        out.append(c);
                        break;
                    }
                }
                out.append('\n');
            }
            out.append("</pre>\n");
        }
        
    }
    
    private static class CeylondocSpanEmitter implements SpanEmitter {

        private final LinkRenderer linkRenderer;
        
        public CeylondocSpanEmitter(LinkRenderer linkRenderer) {
            this.linkRenderer = linkRenderer;
        }

        @Override
        public void emitSpan(StringBuilder out, String content) {
            String customName;
            String declName; 
            int indexOf = content.indexOf("|");
            if( indexOf == -1 ) {
                customName = null;
                declName = content;
            } else {
                customName = content.substring(0, indexOf);
                declName = content.substring(indexOf+1, content.length()); 
            }
            String link = new LinkRenderer(linkRenderer).
                    to(declName).
                    useCustomText(customName).
                    printTypeParameters(false).
                    printWikiStyleLinks(true).
                    getLink();
            out.append(link);
        }
        
    }
    
    public static class DeclarationComparatorByName implements Comparator<Declaration> {

        public static final DeclarationComparatorByName INSTANCE = new DeclarationComparatorByName();

        @Override
        public int compare(Declaration a, Declaration b) {
            return a.getName().compareTo(b.getName());
        }
        
    };
    
    public static class ProducedTypeComparatorByName implements Comparator<ProducedType> {
        
        public static final ProducedTypeComparatorByName INSTANCE = new ProducedTypeComparatorByName();
        
        @Override
        public int compare(ProducedType a, ProducedType b) {
            return a.getDeclaration().getName().compareTo(b.getDeclaration().getName());
        }
    };
    
    public static class ModuleImportComparatorByName implements Comparator<ModuleImport> {

        public static final ModuleImportComparatorByName INSTANCE = new ModuleImportComparatorByName();

        @Override
        public int compare(ModuleImport a, ModuleImport b) {
            return a.getModule().getNameAsString().compareTo(b.getModule().getNameAsString());
        }
    };
    
    public static class PackageComparatorByName implements Comparator<Package> {

        public static final PackageComparatorByName INSTANCE = new PackageComparatorByName();

        @Override
        public int compare(Package a, Package b) {
            return a.getModule().getNameAsString().compareTo(b.getModule().getNameAsString());
        }
    };    
   
}