package com.redhat.ceylon.tools.antdoc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.github.rjeschke.txtmark.BlockEmitter;
import com.github.rjeschke.txtmark.Configuration;
import com.github.rjeschke.txtmark.Processor;
import com.redhat.ceylon.common.tool.CeylonBaseTool;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Hidden;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.OptionModel;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tool;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tools.CeylonToolLoader;
import com.redhat.ceylon.common.tools.help.DocBuilder;
import com.redhat.ceylon.common.tools.help.Markdown;
import com.redhat.ceylon.launcher.LauncherUtil;

/**
 * Generates documentation about ceylon ant tasks.
 * 
 * It's a bit like CeylonDocToolTool, but for ant tasks.
 */
@Hidden
@Summary( 
value="A tool generates documentation about the Ceylon ant tasks")
@Description("")
public class CeylonAntTaskDocTool extends CeylonBaseTool {

    private static final String DEFAULT_TOOLS_URL = "../ceylon/subcommands";

    private final Class<? extends Annotation> OPTION_EQUIV_ANNOTATION;

    private final Class<? extends Annotation> REQUIRED_ANNOTATION;

    private final Class<? extends Annotation> TOOL_EQUIV_ANNOTATION;

    private final Class<? extends Annotation> DOC_ANNOTATION;

    private final Class<? extends Annotation> IGNORE_ANNOTATION;
    
    private final Class<? extends Annotation> ATTRIBUTE_ANNOTATION;

    /** Annotation on add*() methods to know about mutiplicity */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    static @interface Multiplicity {}
    
    private static final String NL = System.lineSeparator();
    
    private final CeylonToolLoader toolLoader;
    private final ToolModel<Tool> rootToolModel;

    private final URLClassLoader loader;

    private Class<?> antTaskClass;
    
    private String toolsUrl = DEFAULT_TOOLS_URL;
    
    @SuppressWarnings("unchecked")
    public CeylonAntTaskDocTool() {
        toolLoader = new CeylonToolLoader();
        rootToolModel = toolLoader.loadToolModel("");
        try {
            File module = new File(LauncherUtil.determineHome(), "lib/ceylon-ant.jar");
            File antJar = new File("/home/tom/apache-ant-1.8.2/lib/ant.jar");
            System.err.println(module.getAbsolutePath() + " " + module.exists());
            System.err.println(antJar.getAbsolutePath() + " " + antJar.exists());
            loader = new URLClassLoader(new URL[]{
                            module.toURI().toURL(),
                            antJar.toURI().toURL()
                        }, 
                    null);
            antTaskClass = Class.forName("org.apache.tools.ant.Task", false, loader);
            
            OPTION_EQUIV_ANNOTATION = (Class<? extends Annotation>)Class.forName("com.redhat.ceylon.ant.OptionEquivalent", false, loader);
            REQUIRED_ANNOTATION  = (Class<? extends Annotation>)Class.forName("com.redhat.ceylon.ant.Required", false, loader);
            TOOL_EQUIV_ANNOTATION  = (Class<? extends Annotation>)Class.forName("com.redhat.ceylon.ant.ToolEquivalent", false, loader);
            DOC_ANNOTATION  = (Class<? extends Annotation>)Class.forName("com.redhat.ceylon.ant.AntDoc", false, loader);
            IGNORE_ANNOTATION = (Class<? extends Annotation>)Class.forName("com.redhat.ceylon.ant.AntDocIgnore", false, loader);
            ATTRIBUTE_ANNOTATION = (Class<? extends Annotation>)Class.forName("com.redhat.ceylon.ant.AntAttribute", false, loader);
            
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Description("URL at which the ceylon tool documentation is available (default: "+DEFAULT_TOOLS_URL+")")
    @OptionArgument
    public void setToolsUrl(String toolsUrl) {
        this.toolsUrl = toolsUrl;
    }

    protected String getAnnotationStringValue(Class<? extends Annotation> annotation, AnnotatedElement element) {
        return getAnnotationStringValue(annotation, element, "value");
    }
    
    protected String getAnnotationStringValue(Class<? extends Annotation> annotation, AnnotatedElement element, String name) {
        Annotation a = element.getAnnotation(annotation);
        if (a != null) {
            try {
                return (String)a.annotationType().getMethod(name).invoke(a);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                    | SecurityException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }
    
    protected Boolean getAnnotationBooleanValue(Class<? extends Annotation> annotation, AnnotatedElement element, String name) {
        Annotation a = element.getAnnotation(annotation);
        if (a != null) {
            try {
                return (Boolean)a.annotationType().getMethod(name).invoke(a);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                    | SecurityException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }
    
    public void documentTask(Appendable out, Class<?> taskClass) {
        try {
            String taskName = getTaskName(taskClass);
            docClass(out, taskName, taskClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** 
     * The name of the ant task without the angle brackets
     *  e.g. "ceylon-doc"
     */
    protected String getTaskName(Class<?> taskClass) {
        String taskName = taskClass.getSimpleName();
        taskName = taskName.replaceAll("^Ceylon", "");
        taskName = taskName.replaceAll("AntTask$", "");
        taskName = taskName.replaceAll("Task$", "");
        taskName = "ceylon-"+camelCaseToDashes(taskName.substring(0, 1).toLowerCase(Locale.ENGLISH) + taskName.substring(1));
        return taskName;
    }
    
    private void docClass(Appendable out, String task, Class<?> taskClass) throws IOException {
        String elementName = "<"+task+">";
        out.append("---").append(NL);
        out.append("layout: ").append("reference12").append(NL);
        out.append("title_md: '`").append(elementName).append("` Ant task'").append(NL);
        out.append("tab: ").append("documentation").append(NL);
        out.append("unique_id: ").append("docspage").append(NL);
        out.append("doc_root: ").append("../../..").append(NL);
        out.append("---").append(NL);
        out.append(NL);
        out.append("<!-- this file was auto-generated by " + getClass().getName() + " -->").append(NL);
        out.append(NL);
        out.append("# #{page.title_md}").append(NL);
        out.append(NL);
        out.append("## Usage").append(NL);
        out.append(NL);
        out.append("**Note**: You must [declare the tasks with a `<typedef>`](../ant).").append(NL);
        out.append(NL);
        if (taskClass.isAnnotationPresent(DOC_ANNOTATION)) {
            out.append(getAnnotationStringValue(DOC_ANNOTATION, taskClass)).append(NL);
            out.append(NL);
        }
        out.append("## Description").append(NL);
        out.append(NL);
        
        
        String toolName = getAnnotationStringValue(TOOL_EQUIV_ANNOTATION, taskClass);
        ToolModel<Tool> toolModel;
        if (toolName != null) {
            toolModel = toolLoader.loadToolModel(toolName);
            out.append(DocBuilder.getSummaryValue(toolModel)).append(NL);
            // TODO Generate this as a link to the relevent tool doc option
            out.append(NL);
            out.append("The `").append(elementName).append("` ant task wraps the [`ceylon " + toolName+"`]("+getToolHref(toolName)+") command.").append(NL);
            out.append(NL);
        } else {
            toolModel = null;
        }
        
        documentAttributes(out, taskClass, toolModel);
        
        documentNestedElements(out, taskClass, toolModel);
        
        out.append("## See also").append(NL);
        out.append(NL);
        out.append("* How to [declare the tasks with a `<typedef>`](../ant).").append(NL);
    }


    protected String getToolHref(String toolName) {
        if (toolName.isEmpty()) {
            return toolsUrl+"/ceylon.html";
        } else {
            return toolsUrl+"/ceylon-"+toolName+".html";
        }
    }
    
    protected String getToolOptionHref(String toolName, String longOption) {
        if (!longOption.startsWith("--")) {
            throw new IllegalArgumentException(longOption);
        }
        return getToolHref(toolName)+"#option" + longOption;
    }

    protected void documentNestedElements(Appendable out, Class<?> taskClass, ToolModel<Tool> toolModel) throws IOException {
        documentNestedElementsRecursive(3, new HashSet<Class<?>>(), out, taskClass, toolModel);
    }
    protected void documentNestedElementsRecursive(int depth, Set<Class<?>> classes, Appendable out, Class<?> taskClass, ToolModel<Tool> toolModel) throws IOException {
        if (classes.contains(taskClass)) {
            return;
        }
        classes.add(taskClass);
        if (hasAdder(taskClass)) {
            out.append(NL);
            out.append("### Nested elements").append(NL);
            out.append(NL);
            
            Map<Object, String> attrs = new TreeMap<Object, String>();
            
            for (Method method : taskClass.getMethods()) {
                
                if (isDocumentableAdder(method)) {
                    String elementName = getElementName(method);
                    StringBuilder sb = new StringBuilder();
                    if (method.getName().startsWith("add")) {
                        sb.append("#### `<").append(elementName).append(">`").append(NL);
                        sb.append(NL);
                        
                        docElement(sb, toolModel, taskClass, method);
                        
                        if (!method.getParameterTypes()[0].getPackage().getName().startsWith("org.apache.tools.ant")) {
                            documentNestedElementClass(depth, classes, sb, method, method.getParameterTypes()[0]);
                        }
                    }else {
                        // It's some other added
                        System.err.println("Different kind of adder "+ method);
                    }
                    
                    sb.append(NL);
                    
                    boolean required = isRequired(method);
                    attrs.put(new Key(required, elementName), sb.toString());
                }
            }
            for (String md : attrs.values()) {
                out.append(md);
            }
            
            
            out.append(NL);
        }
    }
    
    protected String getElementName(Method method) {
        String elementName = method.getName().replaceAll("^" + (method.getName().startsWith("addConfigured") ? "addConfigured" : "add"), "");
        elementName = elementName.substring(0, 1).toLowerCase(Locale.ENGLISH) + elementName.substring(1);
        return elementName;
    }
    
    protected void documentNestedElementClass(int depth, Set<Class<?>> classes, Appendable out, Method method, Class<?> elementClass) throws IOException {
        if (elementClass.isAnnotationPresent(DOC_ANNOTATION)) {
            out.append(getAnnotationStringValue(DOC_ANNOTATION, elementClass)).append(NL);
            out.append(NL);
        }
        
        if (hasSetters(elementClass)) {
            out.append("<table class=\"ant-elements\">").append(NL);
            out.append("<tbody>").append(NL);
            out.append("<tr>").append(NL);
            out.append("<th>Element</th>").append(NL);
            out.append("<th>Description</th>").append(NL);
            out.append("<th>Required</th>").append(NL);
            out.append("</tr>").append(NL);
            
            Map<Object, String> attrs = new TreeMap<Object, String>();
            
            for (Method elementMethod : elementClass.getMethods()) {
                if (isDocumentableSetter(elementClass, elementMethod)) {
                    String attributeName = getAttributeName(elementMethod);
                    StringBuilder sb = new StringBuilder();
                    sb.append("<tr>").append(NL);
                    sb.append("<td><code>").append(attributeName).append("</code></td>").append(NL);
                    sb.append("<td>");
                    if (elementMethod.isAnnotationPresent(DOC_ANNOTATION)) {
                        sb.append(markdown(getAnnotationStringValue(DOC_ANNOTATION, elementMethod)));
                    } else {
                        sb.append("Not documented");
                        System.err.println("Not documented: " + elementMethod + " for nested element " + method);
                    }
                    sb.append("</td>").append(NL);
                    boolean required = isRequired(elementMethod);
                    sb.append("<td>").append(getRequiredText(elementMethod)).append("</td>").append(NL);
                    sb.append("</tr>").append(NL);
                    
                    attrs.put(new Key(required, attributeName), sb.toString());
                }
            }
            // append in sorted order
            for (String md : attrs.values()) {
                out.append(md);
            }
            
            out.append("</tbody>").append(NL);
            out.append("</table>").append(NL);
        }

        //documentNestedElementsRecursive(depth++, classes, out, elementClass, null);
    }

    protected boolean hasAdder(Class<?> taskClass) {
        boolean hasAdders = false;
        for (Method method : taskClass.getMethods()) {
            if (isDocumentableAdder(method)) {
                hasAdders = true;
                break;
            }
        }
        return hasAdders;
    }

    protected void documentAttributes(Appendable out, Class<?> taskClass, 
            ToolModel<Tool> toolModel) throws IOException {
        if (hasSetters(taskClass)) {
            out.append("### Attributes").append(NL);
            out.append(NL);
            
            out.append("<table class=\"ant-parameters\">").append(NL);
            out.append("<tbody>").append(NL);
            out.append("<tr>").append(NL);
            out.append("<th>Attribute</th>").append(NL);
            out.append("<th>Description</th>").append(NL);
            out.append("<th>Required</th>").append(NL);
            out.append("</tr>").append(NL);
            
            // sort attributes into case insensitive order
            Map<Object, String> attrs = new TreeMap<Object, String>();
            for (final Method method : taskClass.getMethods()) {
                if (isDocumentableSetter(taskClass, method)) {
                    String attributeName = getAttributeName(method);
                    StringBuilder sb = new StringBuilder();
                    sb.append("<tr id=\"attribute-"+attributeName+"\">").append(NL);
                    sb.append("<td><code>").append(attributeName).append("</code></td>").append(NL);
                    sb.append("<td>");
                    docElement(sb, toolModel, taskClass, method);
                    sb.append("</td>").append(NL);
                    boolean required = isRequired(method);
                    sb.append("<td>").append(getRequiredText(method)).append("</td>").append(NL);
                    sb.append("</tr>").append(NL);
                    sb.append(NL);

                    attrs.put(new Key(required, attributeName), sb.toString());
                }
            }
            // append in sorted order
            for (String md : attrs.values()) {
                out.append(md);
            }

            out.append("</tbody>").append(NL);
            out.append("</table>").append(NL);
            
        }
    }

    protected boolean isRequired(final Method method) {
        String reqd = getAnnotationStringValue(REQUIRED_ANNOTATION, method);
        if (reqd == null) {
            return false;
        } else if (reqd.isEmpty() || reqd.toLowerCase(Locale.ENGLISH).startsWith("yes")) {
            return true;
        } else {
            return false;
        }
    }
    
    protected String getRequiredText(final Method method) {
        String reqd = getAnnotationStringValue(REQUIRED_ANNOTATION, method);
        if (reqd == null) {
            if (isRequired(method)) {
                throw new RuntimeException();
            }
            return "No";
        } else if (reqd.isEmpty()) {
            return "Yes";
        } else {
            return reqd;
        }
    }
    
    static class Key implements Comparable<Key> {
        final boolean required;
        final String attributeName;
        public Key(boolean required, String attributeName) {
            this.required = required;
            this.attributeName = attributeName;
        }
        @Override
        public int compareTo(Key o) {
            if (required && !o.required) {
                return -1;
            } else if (!required && o.required) {
                return 1;
            }
            return attributeName.compareToIgnoreCase(o.attributeName);
        }
    }
    String markdown(String md) {
        Configuration config = Configuration.builder().forceExtentedProfile().setCodeBlockEmitter(new BlockEmitter() {
            // Because this gets rendered on the website, and because there
            // all code blocks are assumed to be Ceylon (and thus get Try buttons)
            // we need to override the block emitter to prevent that
            @Override
            public void emitBlock(StringBuilder out, List<String> lines, String meta) {
                if (lines.isEmpty())
                    return;
                out.append("<!-- lang: none -->").append(NL);
                out.append("<pre><code>");
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
                    out.append(NL);
                }
                out.append("</code></pre>\n");
            }
        }).build();
        return Processor.process(md, config);
    }

    protected void docElement(Appendable out, ToolModel<Tool> toolModel, Class<?> taskClass, Method method) throws IOException {
        String antDoc = getAnnotationStringValue(DOC_ANNOTATION, method);
        String option = getAnnotationStringValue(OPTION_EQUIV_ANNOTATION, method);
        StringBuilder sb = new StringBuilder();
        if (antDoc != null) {
            sb.append(antDoc).append(NL);
        }
        
        if (option != null) {
            if (option.isEmpty()) {
                option = isDocumentableSetter(taskClass, method) ? inferOptionNameFromSetter(method) : inferOptionNameFromAdder(method);
            }
            boolean found = false;
            if (toolModel == null) {
                System.err.println("@" + OPTION_EQUIV_ANNOTATION.getName() + " but no toolModel on " + method);
            } else {
                for (OptionModel<?> opt : toolModel.getOptions()) {
                    if (("--"+opt.getLongName()).equals(option)) {
                        found = true;
                        if (getAnnotationBooleanValue(OPTION_EQUIV_ANNOTATION, method, "link")) {
                            sb.append("Equivalent to the [`--" + opt.getLongName() +"`]("+getToolOptionHref(toolModel.getName(), option)+") command line option.").append(NL);
                        }
                        if (getAnnotationBooleanValue(OPTION_EQUIV_ANNOTATION, method, "transclude")) {
                            sb.append(DocBuilder.getOptionDescription(toolModel, opt)).append(NL);
                        }
                        
                        break;
                    }
                    
                }
            }
            if (!found) {
                // Try looking it up as an option on the ceylon tool itself
                for (OptionModel<?> opt : rootToolModel.getOptions()) {
                    if (("--"+opt.getLongName()).equals(option)) {
                        found = true;
                        if (getAnnotationBooleanValue(OPTION_EQUIV_ANNOTATION, method, "link")) {
                            sb.append("Equivalent to the [`--" + opt.getLongName() +"`]("+getToolOptionHref(toolModel.getName(), option)+") command line option.").append(NL);
                        }
                        if (getAnnotationBooleanValue(OPTION_EQUIV_ANNOTATION, method, "transclude")) {
                            sb.append(DocBuilder.getOptionDescription(toolModel, opt)).append(NL);
                        }
                        break;
                    }
                    
                }
            }
            if (!found) {
                System.err.println("Option " + option + " for " + method + " not found");
            }
        } 
        
        if (sb.length() == 0){
            sb.append("Not documented");
            System.err.println("Not documented: " + method);
        }
        out.append(markdown(sb.toString()));
    }


    protected String inferOptionNameFromSetter(Method setter) {
        if (setter.getName().startsWith("set")) {
            return "--" + camelCaseToDashes(setter.getName().substring(3));
        } else {
            throw new RuntimeException(setter.getName());
        }
    }
    
    protected String inferOptionNameFromAdder(Method adder) {
        if (adder.getName().startsWith("addConfigured")) {
            return "--" + camelCaseToDashes(adder.getName().substring(13));
        } else if (adder.getName().startsWith("add")) {
            return "--" + camelCaseToDashes(adder.getName().substring(3));
        } else {
            throw new RuntimeException(adder.getName());
        }
    }

    protected String camelCaseToDashes(String name) {
        StringBuilder sb = new StringBuilder();
        for (char ch : name.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                if (sb.length() != 0) {
                    sb.append('-');
                }
                sb.append(Character.toLowerCase(ch));
            } else {
                sb.append(ch);
            }
        }
        final String toolName = sb.toString();
        return toolName;
    }


    protected boolean hasSetters(Class<?> elementClass) {
        boolean hasSetters = false;
        for (Method elementMethod : elementClass.getMethods()) {
            if (isDocumentableSetter(elementClass, elementMethod)) {
                hasSetters = true;
                break;
            }
        }
        return hasSetters;
    }

    protected boolean isDocumentableAdder(Method method) {
        return Modifier.isPublic(method.getModifiers())
                && method.getName().startsWith("add")
                && method.getParameterTypes().length == 1
                && !method.isAnnotationPresent(IGNORE_ANNOTATION)
                && !method.getDeclaringClass().isAssignableFrom(antTaskClass);
    }

    protected boolean isDocumentableSetter(Class<?> taskClass, Method method) {
        try {
            return (Modifier.isPublic(method.getModifiers())
                        && method.getName().startsWith("set")
                        && method.getParameterTypes().length == 1
                        && !method.isAnnotationPresent(IGNORE_ANNOTATION)
                        && taskClass.getMethod(method.getName(), method.getParameterTypes()).equals(method)
                        && !method.getDeclaringClass().isAssignableFrom(antTaskClass))
                    || (Modifier.isPublic(method.getModifiers())
                        && method.getName().startsWith("setDynamicAttribute")
                        && method.getParameterTypes().length == 2
                        && !method.isAnnotationPresent(IGNORE_ANNOTATION)
                        && taskClass.getMethod(method.getName(), method.getParameterTypes()).equals(method)
                        && !method.getDeclaringClass().isAssignableFrom(antTaskClass));
            
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getAttributeName(Method method) {
        String name = getAnnotationStringValue(ATTRIBUTE_ANNOTATION, method);
        if (name != null) {
            return name;
        } else {
            name = method.getName().replaceFirst("^set", "");
            return name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }
    }
    
    @Override
    public void run() throws Exception {
        for (Class<?> t : Arrays.asList(
                Class.forName("com.redhat.ceylon.ant.CeylonCompileAntTask", false, loader),
                Class.forName("com.redhat.ceylon.ant.CeylonCompileJsAntTask", false, loader),
                Class.forName("com.redhat.ceylon.ant.CeylonRunAntTask", false, loader),
                Class.forName("com.redhat.ceylon.ant.CeylonRunJsAntTask", false, loader),
                Class.forName("com.redhat.ceylon.ant.CeylonTestAntTask", false, loader),
                Class.forName("com.redhat.ceylon.ant.CeylonTestJsAntTask", false, loader),
                Class.forName("com.redhat.ceylon.ant.CeylonDocAntTask", false, loader),
                Class.forName("com.redhat.ceylon.ant.CeylonWarAntTask", false, loader),
                Class.forName("com.redhat.ceylon.ant.CeylonImportJarAntTask", false, loader),
                Class.forName("com.redhat.ceylon.ant.CeylonCopyAntTask", false, loader),
                Class.forName("com.redhat.ceylon.ant.CeylonModuleDescriptorTask", false, loader),
                Class.forName("com.redhat.ceylon.ant.CeylonPluginAntTask", false, loader)
                )) {
            if (!antTaskClass.isAssignableFrom(t)) {
                System.err.println(t+" is not a "+antTaskClass.getName());
                continue;
            }
            Class task = (Class)t;
            String taskName = getTaskName(task);
            Writer o = new OutputStreamWriter(new FileOutputStream(new File("ant-"+taskName+".md")), "UTF-8");
            try {
                documentTask(o, task);
            } finally {
                o.close();
            }
        }
    }
    
    public static void main(String[] a) throws Exception {
        new CeylonAntTaskDocTool().run();
    }
}
