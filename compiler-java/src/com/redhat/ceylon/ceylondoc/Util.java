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

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;

import com.github.rjeschke.txtmark.BlockEmitter;
import com.github.rjeschke.txtmark.Configuration;
import com.github.rjeschke.txtmark.Processor;
import com.github.rjeschke.txtmark.SpanEmitter;
import com.redhat.ceylon.compiler.java.codegen.Decl;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.model.typechecker.model.Annotated;
import com.redhat.ceylon.model.typechecker.model.Annotation;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Import;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.ModuleImport;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Referenceable;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.model.Value;

public class Util {
    
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("(?: |\\u00A0|\\s|[\\s&&[^ ]])\\s*");
    
    private static final Set<String> ABBREVIATED_TYPES = new HashSet<String>();
    static {
        ABBREVIATED_TYPES.add("ceylon.language::Empty");
        ABBREVIATED_TYPES.add("ceylon.language::Entry");
        ABBREVIATED_TYPES.add("ceylon.language::Sequence");
        ABBREVIATED_TYPES.add("ceylon.language::Sequential");
        ABBREVIATED_TYPES.add("ceylon.language::Iterable");
    }
    
    public static String normalizeSpaces(String str) {
        if (str == null) {
            return null;
        }
        return WHITESPACE_PATTERN.matcher(str).replaceAll(" ");
    } 

    public static boolean isAbbreviatedType(Declaration decl) {
        return ABBREVIATED_TYPES.contains(decl.getQualifiedNameString());
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
        return wikiToHTML(getRawDoc(module.getUnit(), module.getAnnotations()), linkRenderer.useScope(module));
    }
    
    public static String getDoc(ModuleImport moduleImport, LinkRenderer linkRenderer) {
        return wikiToHTML(getRawDoc(moduleImport.getModule().getUnit(), moduleImport.getAnnotations()), linkRenderer);
    }

    public static String getDoc(Package pkg, LinkRenderer linkRenderer) {
        return wikiToHTML(getRawDoc(pkg.getUnit(), pkg.getAnnotations()), linkRenderer.useScope(pkg));
    }

    public static String getDocFirstLine(Declaration decl, LinkRenderer linkRenderer) {
        return wikiToHTML(getFirstLine(getRawDoc(decl)), linkRenderer.useScope(decl));
    }

    public static String getDocFirstLine(Package pkg, LinkRenderer linkRenderer) {
        return wikiToHTML(getFirstLine(getRawDoc(pkg.getUnit(), pkg.getAnnotations())), linkRenderer.useScope(pkg));
    }

    public static String getDocFirstLine(Module module, LinkRenderer linkRenderer) {
        return wikiToHTML(getFirstLine(getRawDoc(module.getUnit(), module.getAnnotations())), linkRenderer.useScope(module));
    }
    
    public static <T extends Referenceable & Annotated> List<String> getTags(T decl) {
        List<String> tags = new ArrayList<String>();
        Annotation tagged = Util.getAnnotation(decl.getUnit(), decl.getAnnotations(), "tagged");
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
    
    public static String getRawDoc(Unit unit, List<Annotation> anns) {
        Annotation a = getAnnotation(unit, anns, "doc");
        if (a != null && a.getPositionalArguments() != null && !a.getPositionalArguments().isEmpty()) {
            return a.getPositionalArguments().get(0);
        }
        return "";
    }

    public static Annotation getAnnotation(ModuleImport moduleImport, String name) {
        return getAnnotation(moduleImport.getModule().getUnit(), moduleImport.getAnnotations(), name);
    }
    
    public static Annotation getAnnotation(Unit unit, List<Annotation> annotations, String name) {
        String aliasedName = resolveAliasedName(unit, name);
        
        // check that documentation annotation is not hidden by custom annotation
        if( name.equals(aliasedName) && unit != null ) {
            Declaration importedDeclaration = unit.getImportedDeclaration(name, null, false);
            if( importedDeclaration != null && !importedDeclaration.getNameAsString().startsWith("ceylon.language::") ) {
                return null;
            }
        }
        
        if (annotations != null) {
            for (Annotation a : annotations) {
                if (a.getName().equals(aliasedName))
                    return a;
            }
        }
        return null;
    }
    
    public static Annotation findAnnotation(Declaration decl, String name) {
        Annotation a = getAnnotation(decl.getUnit(), decl.getAnnotations(), name);
        if (a == null && decl.isActual() && decl.getRefinedDeclaration() != decl) {
            // keep looking up
            a = findAnnotation(decl.getRefinedDeclaration(), name);
        }
        return a;
    }
    
    private static String resolveAliasedName(Unit unit, String name) {
        if (unit != null) {
            for (Import i : unit.getImports()) {
                if (!i.isAmbiguous() && i.getDeclaration().getQualifiedNameString().equals("ceylon.language::" + name)) {
                    return i.getAlias();
                }
            }
        }
        return name;
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
            if (c.isFinal() && !c.isAnonymous()) {
                modifiers.append("final ");
            }
        }
        return modifiers.toString().trim();
    }

    public static List<TypeDeclaration> getAncestors(TypeDeclaration decl) {
        List<TypeDeclaration> ancestors = new ArrayList<TypeDeclaration>();
        Type ancestor = decl.getExtendedType();
        while (ancestor != null) {
            ancestors.add(ancestor.getDeclaration());
            ancestor = ancestor.getExtendedType();
        }
        return ancestors;
    }

    public static List<TypeDeclaration> getSuperInterfaces(TypeDeclaration decl) {
        Set<TypeDeclaration> superInterfaces = new HashSet<TypeDeclaration>();
        for (Type satisfiedType : decl.getSatisfiedTypes()) {
            superInterfaces.add(satisfiedType.getDeclaration());
            superInterfaces.addAll(getSuperInterfaces(satisfiedType.getDeclaration()));
        }
        List<TypeDeclaration> list = new ArrayList<TypeDeclaration>();
        list.addAll(superInterfaces);
        removeDuplicates(list);
        return list;
    }

    private static void removeDuplicates(List<TypeDeclaration> superInterfaces) {
        OUTER: for (int i = 0; i < superInterfaces.size(); i++) {
            TypeDeclaration decl1 = superInterfaces.get(i);
            // compare it with each type after it
            for (int j = i + 1; j < superInterfaces.size(); j++) {
                TypeDeclaration decl2 = superInterfaces.get(j);
                if (decl1.equals(decl2)) {
                    if (decl1.getType().isSubtypeOf(decl2.getType())) {
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
    
    public static boolean isThrowable(TypeDeclaration c) {
        if (c instanceof Class) {
            if ("ceylon.language::Throwable".equals(c.getQualifiedNameString())) {
                return true;
            } else if (c.getExtendedType()!=null) {
                return isThrowable(c.getExtendedType().getDeclaration());
            }
        }
        return false;
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
        // no need to quote the name itself as java keywords are lower-cased and we append a _ to every
        // lower-case toplevel so they can never be java keywords
        return pkgName.isEmpty() ? name : com.redhat.ceylon.compiler.java.util.Util.quoteJavaKeywords(pkgName) + "." + name;
    }
    
    public static Declaration findBottomMostRefinedDeclaration(TypedDeclaration d) {
        if (d.getContainer() instanceof TypeDeclaration) {
            Queue<TypeDeclaration> queue = new LinkedList<TypeDeclaration>();
            queue.add((TypeDeclaration) d.getContainer());
            return findBottomMostRefinedDeclaration(d, queue);
        }
        return null;
    }

    private static Declaration findBottomMostRefinedDeclaration(TypedDeclaration d, Queue<TypeDeclaration> queue) {
        TypeDeclaration type = queue.poll();
        if (type != null) {
            if (type != d.getContainer()) {
                Declaration member = type.getDirectMember(d.getName(), null, false);
                if (member != null && member.isActual()) {
                    return member;
                }
            }

            if (type.getExtendedType() != null) {
                queue.add(type.getExtendedType().getDeclaration());
            }
            for (Type satisfiedType: type.getSatisfiedTypes()) {
                queue.add(satisfiedType.getDeclaration());
            }

            return findBottomMostRefinedDeclaration(d, queue);
        }

        return null;
    }
    
    public static String getNameWithContainer(Declaration d) {
        return "<code><span class='type-identifier'>" + 
                ((TypeDeclaration)d.getContainer()).getName() + 
                "</span>.<span class='" + 
                (d instanceof TypeDeclaration ? "type-identifier" : "identifier") + 
                "'>" + d.getName() + "</span></code>";
    }    
    
    private static class CeylondocBlockEmitter implements BlockEmitter {
        
        private static final CeylondocBlockEmitter INSTANCE = new CeylondocBlockEmitter();

        @Override
        public void emitBlock(StringBuilder out, List<String> lines, String meta) {
            if (lines.isEmpty())
                return;
            
            if( meta == null || meta.length() == 0 ) {
                out.append("<pre data-language=\"ceylon\">");
            }
            else {
                out.append("<pre data-language=\"").append(meta).append("\">");
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
            int pipeIndex = content.indexOf("|");
            String customText = pipeIndex != -1 ? content.substring(0, pipeIndex) : null;
            String link = new LinkRenderer(linkRenderer).
                    to(content).
                    withinText(true).
                    useCustomText(customText).
                    printTypeParameters(false).
                    printWikiStyleLinks(true).
                    getLink();
            out.append(link);
        }
        
    }
    
    public static class ReferenceableComparatorByName implements Comparator<Referenceable> {

        public static final ReferenceableComparatorByName INSTANCE = new ReferenceableComparatorByName();

        @Override
        public int compare(Referenceable a, Referenceable b) {
            return nullSafeCompare(a.getNameAsString(), b.getNameAsString());
        }

    };
    
    public static class TypeComparatorByName implements Comparator<Type> {
        
        public static final TypeComparatorByName INSTANCE = new TypeComparatorByName();
        
        @Override
        public int compare(Type a, Type b) {
            return nullSafeCompare(a.getDeclaration().getName(), b.getDeclaration().getName());
        }
    };
    
    public static class ModuleImportComparatorByName implements Comparator<ModuleImport> {

        public static final ModuleImportComparatorByName INSTANCE = new ModuleImportComparatorByName();

        @Override
        public int compare(ModuleImport a, ModuleImport b) {
            return nullSafeCompare(a.getModule().getNameAsString(), b.getModule().getNameAsString());
        }
    }
  
    static int nullSafeCompare(final String s1, final String s2) {
        if (s1 == s2) {
            return 0;
        } else if (s1 == null) {
            return -1;
        } else if (s2 == null) {
            return 1;
        }
        return s1.compareTo(s2);
    }

    public static boolean isEnumerated(TypeDeclaration klass) {
        return klass.getCaseTypes() != null && !klass.getCaseTypes().isEmpty();
    }

    public static String getDeclarationName(Declaration decl) {
        String name = decl.getName();
        if( ModelUtil.isConstructor(decl) && name == null ) {
            name = ((TypeDeclaration)decl.getContainer()).getName();
        }
        return name;
    }
   
}