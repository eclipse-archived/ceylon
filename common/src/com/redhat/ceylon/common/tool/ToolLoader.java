package com.redhat.ceylon.common.tool;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import com.redhat.ceylon.common.OSUtil;
import com.redhat.ceylon.common.tool.OptionModel.ArgumentType;

/**
 * Responsible for locating a Class for a given tool name and constucting a 
 * {@link ToolModel} by reflection on that class. 
 * @author tom
 */
public abstract class ToolLoader {

    protected static final String SCRIPT_PREFIX = "SCRIPT:";

    protected final ClassLoader loader;
    
    public ToolLoader() {
        this(ToolLoader.class.getClassLoader());
    }
    
    public ToolLoader(ClassLoader loader) {
        this.loader = loader == null ? ClassLoader.getSystemClassLoader() : loader;
    }
    
    protected <T extends Tool> Class<T> loadToolClass(final String toolName) {
        String className = getToolClassName(toolName);
        if (className == null) {
            return null;
        }
        try {
            Class<T> toolClass = (Class<T>)loader.loadClass(className);
            return toolClass;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    protected String getToolClassName(final String toolName) {
        List<String> classNames = new ArrayList<>();
        String className = null;
        for (String cls : toolClassNames()) {
            if (toolName.equals(getToolName(cls))) {
                classNames.add(cls);
            }
        }
        if (new HashSet<String>(classNames).size() > 1) {
            // TODO Allow fully qualified tool names to avoid ambiguities?
            throw new ToolException("Ambiguous tool name " + toolName + ", classes: " + classNames);
        }
        if (classNames.isEmpty()) {
            return null;
        }
        className = classNames.get(0);
        return className;
    }

    /**
     * Returns a ToolModel given the name of the tool, or null if no such tool is 
     * know to this tool loader.
     */
    public <T extends Tool> ToolModel<T> loadToolModel(String toolName) {
        String className = getToolClassName(toolName);
        if(className != null && className.startsWith(SCRIPT_PREFIX)){
            return loadScriptTool(className, toolName);
        }else{
            Class<T> toolClass = loadToolClass(toolName);
            if (toolClass != null) {
                final ToolModel<T> toolModel;
                try {
                    toolModel = loadModel(toolClass, toolName);
                } catch (ModelException e) {
                    throw e;
                } catch (RuntimeException e) {
                    throw new ModelException("Failed to load model for tool " + toolName +"(" + toolClass + ")", e);
                }
                toolModel.setToolLoader(this);
                return toolModel;
            }
        }
        return null;
    }
    
    private <T extends Tool> ToolModel<T> loadScriptTool(String className, String toolName) {
        ToolModel<T> model = new ToolModel<T>();
        model.setScript(true);
        model.setScriptName(className.substring(7));
        model.setName(toolName);
        model.setToolLoader(this);
        return model;
    }

    private static URL[] makeClasspath(File... jars) {
        URL[] urls = new URL[jars.length];
        for (int ii = 0; ii <jars.length; ii++) {
            try {
                urls[ii] = jars[ii].toURI().toURL();
            } catch (MalformedURLException e) {
                throw new ToolException(e);
            }
        }
        return urls;
    }
    
    private <T extends Tool> ToolModel<T> loadModel(Class<T> cls, String toolName) {
        checkClass(cls);
        ToolModel<T> model = new ToolModel<T>();
        model.setToolClass(cls);
        model.setName(toolName);
        
        // We use this Map because Java doesn't define the order that the 
        // declared methods will be returned in, but the order matters 
        TreeMap<Integer, ArgumentModel<?>> orderedArgumentModels = new TreeMap<Integer, ArgumentModel<?>>();
        for (Method method : cls.getMethods()) {
            addMethod(cls, model, method, orderedArgumentModels);
        }
        
        Entry<Integer, ArgumentModel<?>> last = orderedArgumentModels.lastEntry();
        if (last != null && last.getValue() instanceof SubtoolModel) {
            model.setSubtoolModel((SubtoolModel<?>)last.getValue());
            orderedArgumentModels.remove(last.getKey());
        }
        
        for (ArgumentModel<?> argumentModel : orderedArgumentModels.values()) {
            if (argumentModel instanceof SubtoolModel) {
                throw new ModelException("A @Subtool's order() must be greater than all @Argument order()s");
            }
            model.addArgument(argumentModel);
        }
        
        return model;
    }

    protected <T extends Tool> ArgumentParser<?> getArgumentParser(Method setter, Class<?> setterType) {
        Subtool subtool = setter.getAnnotation(Subtool.class);
        ParsedBy pf = setter.getAnnotation(ParsedBy.class);
        if (subtool != null) {
            if (subtool.classes().length > 0) {
                if (pf != null) {
                    throw new ModelException(setter + " annotated with both @Subtool(classes=...) and @ParsedBy");
                }
                return new ToolArgumentParser(MapToolLoader.fromClassNames(subtool.classes()));
            }
        }
        if (pf != null) {
            try {
                return pf.value().newInstance();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new ModelException("Error instantiating the given @ParserFactory", e);
            }
        }
        return StandardArgumentParsers.forClass(setterType, this);
    }
    
    private <T extends Tool, A> void addMethod(Class<T> cls, ToolModel<T> model,
            Method method, Map<Integer, ArgumentModel<?>> orderedArgumentModels) {
        final PostConstruct postConstructAnno = method.getAnnotation(PostConstruct.class);
        if (postConstructAnno != null) {
            checkPostConstructMethod(method);
            model.addPostConstruct(method);
        }
        final Rest rest = method.getAnnotation(Rest.class);
        if (rest != null) {
            if (!isSetter(method)) {
                throw new ModelException("Method " + method + " is annotated @Rest but is not a setter");
            }
            if (model.getRest() != null) {
                throw new ModelException("Only one method may be annotated @Rest: " + model.getRest() + " and " + method);
            }
            model.setRest(method);
        }
        
        OptionModel<Boolean> optionModel = buildOption(model, method);
        OptionModel<A> optionArgumentModel = buildOptionArgument(model, method);
        ArgumentModel<A> argumentModel = buildArgument(method, orderedArgumentModels);
        SubtoolModel<Tool> subtoolModel = buildSubtool(method, orderedArgumentModels);
        if (optionModel!= null) {
            if (argumentModel != null || subtoolModel != null) {
                throw new ModelException(method + " is annotated with both @Option and @Argument/@Subtool");
            } 
            if (optionArgumentModel != null) {
                throw new ModelException(method + " is annotated with both @Option and @OptionArgument");
            }
            checkDuplicateOption(cls, model, optionModel);
            model.addOption(optionModel);
        } else if (optionArgumentModel != null) {
            if (argumentModel != null || subtoolModel != null) {
                throw new ModelException(method + " is annotated with both @OptionArgument and @Argument/@Subtool");
            }
            checkDuplicateOption(cls, model, optionArgumentModel);
            model.addOption(optionArgumentModel);
        } else if (argumentModel != null) {
            if (subtoolModel != null) {
                throw new ModelException(method + " is annotated with both @Argument and @Subtool");
            }
            // We don't add it to the model here, it's in the orderedArgumentModels
        } 
    }

    private <T extends Tool> void checkDuplicateOption(Class<T> cls,
            ToolModel<T> model, OptionModel<?> optionModel) {
        if (model.getOption(optionModel.getLongName()) != null) {
            throw new ModelException(cls + " has more than one binding for option " + optionModel.getLongName());
        }
        if (optionModel.getShortName() != null
                && model.getOptionByShort(optionModel.getShortName()) != null) {
            throw new ModelException(cls + " has more than one binding for short option " + optionModel.getShortName());
        }
    }

    private ArgumentModel<Boolean> buildPureOption(ToolModel<?> toolModel, Method method) {
        ArgumentModel<Boolean> argumentModel;
        argumentModel = new ArgumentModel<Boolean>();
        argumentModel.setParser((ArgumentParser<Boolean>)getArgumentParser(method, boolean.class));
        argumentModel.setToolModel(toolModel);
        argumentModel.setSetter(method);
        argumentModel.setType(boolean.class);
        argumentModel.setMultiplicity(Multiplicity._0_OR_1);
        return argumentModel;
    }

    private void checkPostConstructMethod(Method method) {
        final int modifiers = method.getModifiers();
        if (!Modifier.isPublic(modifiers)) {
            throw new ModelException("Method " + method + " is annotated with @PostConstruct but is not public ");
        }
        if (method.getParameterTypes().length != 0) {
            throw new ModelException("Method " + method + " is annotated with @PostConstruct but requires a non-empty argument list");            
        }
    }

    private void checkClass(Class<? extends Tool> cls) throws ModelException {
        final int classModifiers = cls.getModifiers();
        if (Modifier.isAbstract(classModifiers)) {
            throw new ModelException("Tool " + cls + " is not concrete");            
        }
        if (Modifier.isInterface(classModifiers)) {
            throw new ModelException("Tool " + cls + " is not a class");            
        }
        if (!Modifier.isPublic(classModifiers)) {
            throw new ModelException("Tool " + cls + " is not public");            
        }
        if (cls.getEnclosingClass() != null
                && !Modifier.isStatic(cls.getModifiers())) {
            try {
                cls.getConstructor(cls.getEnclosingClass());
            } catch (NoSuchMethodException e) {
                throw new ModelException("Tool " + cls + " does not have a public 0 argument constructor");            
            }
        } else {
            try {
                cls.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new ModelException("Tool " + cls + " does not have a public 0 argument constructor");            
            }    
        }
        
    }

    private boolean hasDescription(Method setter) {
        return setter.getAnnotation(Description.class) != null;
    }
    
    private boolean isSetter(Method method) {
        return method.getName().matches("set[A-Z0-9].*")
                && Modifier.isPublic(method.getModifiers())
                && method.getReturnType().equals(void.class)
                && method.getParameterTypes().length == 1;
    }

    protected String classNameToToolName(String className){
        if(className.startsWith(SCRIPT_PREFIX)){
            String name = className.substring(7);
            int lastSep = className.lastIndexOf(File.separatorChar);
            if(lastSep != -1)
                name = name.substring(lastSep+1);
            if(OSUtil.isWindows()) // strip the .bat
                name = name.substring(0, name.length()-4);
            return name;
        }
        return camelCaseToDashes(className.replaceAll("^(.*\\.)?Ceylon(.*)Tool$", "$2"));
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
    
    private String getOptionName(String name, Method setter) {
        if (name == null || name.isEmpty()) {
            name = camelCaseToDashes(setter.getName().substring("set".length()));
        }
        return name;
    }

    private OptionModel<Boolean> buildOption(ToolModel<?> toolModel, final Method setter) {
        Option option = setter.getAnnotation(Option.class);
        if (option == null || setter.getAnnotation(OptionArgument.class) != null) {
            return null;
        }
        if (!isSetter(setter)) {
            throw new ModelException("Method " + setter + " is annotated with @Option but is not a setter");
        }
        if (!setter.getParameterTypes()[0].equals(Boolean.TYPE)) {
            throw new ModelException("Method " + setter + " is annotated with @Option but has a non-boolean parameter");
        }
        OptionModel<Boolean> optionModel = new OptionModel<Boolean>();
        optionModel.setLongName(getOptionName(option.longName(), setter));
        char shortName = option.shortName();
        if (shortName != Option.NO_SHORT) {
            optionModel.setShortName(shortName);
        }
        optionModel.setArgumentType(OptionModel.ArgumentType.NOT_ALLOWED);
        optionModel.setArgument(buildPureOption(toolModel, setter));
        optionModel.getArgument().setOption(optionModel);
        return optionModel;
    }

    private <A> OptionModel<A> buildOptionArgument(ToolModel<?> toolModel, final Method setter) {
        OptionArgument optionArgument = setter.getAnnotation(OptionArgument.class);
        if (optionArgument == null) {
            return null;
        }
        if (!isSetter(setter)) {
            throw new ModelException("Method " + setter + " is annotated with @OptionArgument but is not a setter");
        }
        Option option = setter.getAnnotation(Option.class);
        boolean argumentOptional = option != null;
        if (argumentOptional) {
            if (setter.getParameterTypes()[0].isPrimitive()) {
                throw new ModelException("Method " + setter + " is annotated with @OptionArgument and @Option has primitive parameter type");
            }
            if (optionArgument.shortName() != option.shortName()) {
                throw new ModelException("Method " + setter + " is annotated with @OptionArgument and @Option, but their shortName()s differ");
            }
            if (!optionArgument.longName().equals(option.longName())) {
                throw new ModelException("Method " + setter + " is annotated with @OptionArgument and @Option, but their longName()s differ");
            }
        }
        OptionModel<A> optionModel = new OptionModel<A>();
        
        optionModel.setLongName(getOptionName(optionArgument.longName(), setter));
        char shortName = optionArgument.shortName();
        if (shortName != OptionArgument.NO_SHORT) {
            if (argumentOptional) {
                throw new ModelException("Method " + setter + " is annotated with @OptionArgument and @Option, but has a shortName");
            }
            optionModel.setShortName(shortName);
        }
        ArgumentModel<A> argumentModel = new ArgumentModel<A>();
        
        Class<A> argumentType = (Class<A>)getSimpleTypeOrCollectionType(setter, OptionArgument.class);
        argumentModel.setParser((ArgumentParser<A>)getArgumentParser(setter, argumentType));
        argumentModel.setToolModel(toolModel);
        argumentModel.setType(argumentType);
        argumentModel.setMultiplicity(isSimpleType(setter) ? Multiplicity._0_OR_1 : Multiplicity._0_OR_MORE);
        argumentModel.setName(optionArgument.argumentName());
        argumentModel.setSetter(setter);
        optionModel.setArgumentType(argumentOptional ? ArgumentType.OPTIONAL : ArgumentType.REQUIRED);
        optionModel.setArgument(argumentModel);
        optionModel.getArgument().setOption(optionModel);
        return optionModel;
    }
    
    private <T extends Tool, A> ArgumentModel<A> buildArgument(
            final Method setter, Map<Integer, ArgumentModel<?>> orderedArgumentModels) {
        Argument argument = setter.getAnnotation(Argument.class);
        if (argument == null) {
            return null;
        }
        if (!isSetter(setter)) {
            throw new ModelException("Method " + setter + " is annotated with @Argument but is not a setter");
        }
        if (hasDescription(setter)) {
            throw new ModelException(
                    "Method " + setter + " is annotated with @Argument and @Description: " +
            		"Arguments should be documented in the class-level @Description");
        }
        if (isHidden(setter)) {
            throw new ModelException(
                    "Method " + setter + " is annotated with @Argument and @Hidden: " +
                    "You can't have @Hidden arguments");
        }
        ArgumentModel<A> argumentModel = new ArgumentModel<A>();
        Multiplicity multiplicity = Multiplicity.fromString(argument.multiplicity());
        String argumentName = argument.argumentName();
        int order = argument.order();
        Class<A> argumentType = (Class<A>)getSimpleTypeOrCollectionType(setter, Argument.class);
        
        populateArgumentModel(setter, orderedArgumentModels, argumentModel,
                argumentType, multiplicity, argumentName, order);
        return argumentModel;
    }
    
    private <T extends Tool, A, X extends Tool> SubtoolModel<X> buildSubtool(
            final Method setter, Map<Integer, ArgumentModel<?>> orderedArgumentModels) {
        Subtool argument = setter.getAnnotation(Subtool.class);
        if (argument == null) {
            return null;
        }
        if (!isSetter(setter)) {
            throw new ModelException("Method " + setter + " is annotated with @Subtool but is not a setter");
        }
        if (hasDescription(setter)) {
            throw new ModelException(
                    "Method " + setter + " is annotated with @Subtool and @Description: " +
                    "Subtools should be documented in the class-level @Description");
        }
        if (isHidden(setter)) {
            throw new ModelException(
                    "Method " + setter + " is annotated with @Subtool and @Hidden: " +
                    "You can't have @Hidden arguments");
        }
        SubtoolModel<X> argumentModel = new SubtoolModel<X>();
        Class<X> argumentType = (Class<X>)setter.getParameterTypes()[0];
        Multiplicity multiplicity = Multiplicity._1;
        String argumentName = argument.argumentName();
        int order = argument.order();
        
        populateArgumentModel(setter, orderedArgumentModels, argumentModel,
                argumentType, multiplicity, argumentName, order);
        return argumentModel;
    }

    private <A> void populateArgumentModel(final Method setter,
            Map<Integer, ArgumentModel<?>> orderedArgumentModels,
            ArgumentModel<A> argumentModel, Class<A> argumentType,
            Multiplicity multiplicity, String argumentName, int order) {
        argumentModel.setMultiplicity(multiplicity);
        argumentModel.setName(argumentName);
        argumentModel.setType(argumentType);
        
        final ArgumentParser<A> parser = (ArgumentParser<A>)getArgumentParser(setter, argumentType);
        if (parser == null) {
            throw new ModelException("Unable to parse arguments of " + argumentModel.getType());
        }
        argumentModel.setParser(parser);
        argumentModel.setSetter(setter);
        
        final ArgumentModel<?> clash = orderedArgumentModels.put(order, argumentModel);
        if (clash != null) {
            throw new ModelException("Two setters annotated with @Argument with the same order");
        }
    }

    private boolean isHidden(final Method setter) {
        return setter.getAnnotation(Hidden.class) != null;
    }

    private boolean isSimpleType(final Method setter) {
        Type t = setter.getGenericParameterTypes()[0];
        return t instanceof Class;
    }
    
    private Class<?> getSimpleTypeOrCollectionType(final Method setter, Class<? extends Annotation> annoType) {
        Type t = setter.getGenericParameterTypes()[0];
        if (t instanceof Class) {
            Class<?> type = (Class<?>)t;
            if (List.class.isAssignableFrom(type)) {
                throw new ModelException("Method " + setter + " is annotated with " + annoType.getSimpleName() + " but the parameter type is a raw List");
            }
            return (Class<?>)t;
        }
        if (!(t instanceof ParameterizedType)) {
            // TODO Maybe support wildcards
            throw new ModelException("");
        }
        ParameterizedType pt = (ParameterizedType)t;
        Type rt = pt.getRawType();// I.e. List
        if (ToolModel.class.equals(rt)) {
            return (Class)rt;
        }
        if (!List.class.equals(rt)) {
            throw new ModelException("Method " + setter + " is annotated with " + annoType.getSimpleName() + " but the parameter type is not java.util.List");
        }
        Type ta = pt.getActualTypeArguments()[0];
        if (ta instanceof ParameterizedType) {
            ta = ((ParameterizedType)ta).getRawType();
        }
        if (!(ta instanceof Class)) {
            throw new ModelException("Method " + setter + " is annotated with " + annoType.getSimpleName() + " but the type parameter to the java.util.List parameter is not a class but a " + ta);
        }
        Class<?> argumentType = (Class<?>)ta;
        return argumentType;
    }
    
    public abstract String getToolName(String className);
    
    /**
     * Returns an iterable of all the tools names known to this tool loader.
     */
    public Iterable<String> getToolNames() {
        List<String> result = new ArrayList<>();
        for (String className : toolClassNames()) {
            result.add(getToolName(className));
        }
        Collections.sort(result);
        return result;
    }
    static interface Handler<T> {
        public T handle(String className);
    }
    





    /**
     * Suggests tool names which are similar to something that was supposed 
     * to be a tool name, but wasn't.
     * @param badlySpelledCommand
     * @return A list of tool names which are similar to the give non tool 
     * name. 
     */
    public List<String> typo(final String badlySpelledCommand) {
        List<String> result = new ArrayList<>();
        for (String className : toolClassNames()) {
            String toolName = getToolName(className);
            if (levenshteinDistance(toolName, badlySpelledCommand) < 3) {
                if (loadToolModel(toolName).isPorcelain()) {
                    result.add(toolName);
                }
            }
        }
        return result;
    }
    
    protected abstract Iterable<String> toolClassNames();

    /* The following two methods taken from wikipedia */
    private static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    private static int levenshteinDistance(CharSequence str1,
                CharSequence str2) {
        int[][] distance = new int[str1.length() + 1][str2.length() + 1];

        for (int i = 0; i <= str1.length(); i++)
                distance[i][0] = i;
        for (int j = 0; j <= str2.length(); j++)
                distance[0][j] = j;

        for (int i = 1; i <= str1.length(); i++)
                for (int j = 1; j <= str2.length(); j++)
                        distance[i][j] = minimum(
                                        distance[i - 1][j] + 1,
                                        distance[i][j - 1] + 1,
                                        distance[i - 1][j - 1]
                                                        + ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0
                                                                        : 1));

        return distance[str1.length()][str2.length()];
    }

    public <T extends Tool> T instance(String toolName, Tool outer) {
        String toolClassName = getToolClassName(toolName);
        if (toolClassName == null) {
            return null;
        }
        try {
            Class<T> toolClass = (Class<T>)Class.forName(toolClassName, false, loader);
            if (toolClass.getEnclosingClass() != null
                    && !Modifier.isStatic(toolClass.getModifiers())) {
                if (outer == null) {
                    throw new ToolException("Cannot instantiate non-static innner class without a qualifier");
                }
                Constructor<T> ctor = toolClass.getConstructor(toolClass.getEnclosingClass());
                return ctor.newInstance(outer);
            } else {
                return toolClass.newInstance();
            }
        } catch (RuntimeException e) {
            throw new ToolException("Could not instantitate tool class " + toolClassName + " for tool " + toolName, e);
        } catch (ReflectiveOperationException e) {
            throw new ToolException("Could not instantitate tool class " + toolClassName + " for tool " + toolName, e);
        }
    }
    
}
