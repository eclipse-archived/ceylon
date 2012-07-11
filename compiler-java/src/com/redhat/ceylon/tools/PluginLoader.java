package com.redhat.ceylon.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import com.redhat.ceylon.tools.annotation.OptionArgument;
import com.redhat.ceylon.tools.annotation.Argument;
import com.redhat.ceylon.tools.annotation.ArgumentStyle;
import com.redhat.ceylon.tools.annotation.GnuStyle;
import com.redhat.ceylon.tools.annotation.Option;
import com.redhat.ceylon.tools.annotation.Rest;
import com.redhat.ceylon.tools.annotation.Style;

/**
 * Responsible for locating a Class for a given tool name and constucting a 
 * {@link PluginModel} by reflection on that class. 
 * @author tom
 */
public class PluginLoader {

    private final ClassLoader loader;
    
    private final ArgumentParserFactory argParserFactory;
    
    public PluginLoader(ArgumentParserFactory argParserFactory) {
        this(argParserFactory, PluginLoader.class.getClassLoader());
    }
    
    public PluginLoader(ArgumentParserFactory argParserFactory, ClassLoader loader) {
        this.argParserFactory = argParserFactory;
        this.loader = loader;
    }
    
    public PluginLoader(ArgumentParserFactory argParserFactory, File[] path) {
        this.argParserFactory = argParserFactory;
        if (path != null) {
            loader = new URLClassLoader(makeClasspath(path), 
                    getClass().getClassLoader());   
        } else {
            loader = getClass().getClassLoader();
        }
    }
    
    private <T extends Plugin> Class<T> loadToolClass(final String toolName) {
        String className = null;
        if (toolName.isEmpty()) {
            className = Tool.class.getName();
        } else {
            List<String> classNames = iterateToolNames(new Handler<String>() {
                @Override
                public String handle(String cls) {
                    if (toolName.equals(getToolName(cls))) {
                        return cls;
                    }
                    return null;
                }
            });
               
            if (classNames.size() > 1) {
                // TODO Allow fully qualified tool names to avoid ambiguities?
                throw new PluginException("Ambiguous tool name " + toolName + ", classes: " + classNames);
            }
            if (classNames.isEmpty()) {
                return null;
            }
            className = classNames.get(0);
        }
        try {
            Class<T> toolClass = (Class<T>)loader.loadClass(className);
            return toolClass;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    
    /**
     * Returns a ToolModel given the name of the tool, or null if no such tool is 
     * know to this tool loader.
     */
    public <T extends Plugin> PluginModel<T> loadToolModel(String toolName) {
        Class<T> toolClass = loadToolClass(toolName);
        if (toolClass != null) {
            final PluginModel<T> toolModel = loadModel(toolClass);
            toolModel.setToolLoader(this);
            return toolModel;
        }
        return null;
    }
    
    private static URL[] makeClasspath(File... jars) {
        URL[] urls = new URL[jars.length];
        for (int ii = 0; ii <jars.length; ii++) {
            try {
                urls[ii] = jars[ii].toURI().toURL();
            } catch (MalformedURLException e) {
                throw new PluginException(e);
            }
        }
        return urls;
    }
    
    private <T extends Plugin> PluginModel<T> loadModel(Class<T> cls) {
        checkClass(cls);
        PluginModel<T> model = new PluginModel<T>();
        model.setToolClass(cls);
        
        String name = getToolName(cls);
        model.setName(name);
        
        final ArgumentStyle style = cls.getAnnotation(ArgumentStyle.class);
        if (style != null) {
            boolean ok = false;
            final Class<? extends Style> styleClass = style.value();
            try {
                final Constructor<? extends Style> ctor = styleClass.getConstructor();
                ok = !Modifier.isAbstract(styleClass.getModifiers())
                        && !Modifier.isInterface(styleClass.getModifiers())
                        && ctor.getParameterTypes().length == 0;
            } catch (NoSuchMethodException e) {
                ok = false;
            }
            if (!ok) {
                throw new ModelException("Style class referred to by @ArgumentStyle() should have a public nullary constructor");
            }
            model.setArgumentStyle(styleClass);
        } else {
            model.setArgumentStyle(GnuStyle.class);
        }
        // We use this Map because Java doesn't define the order that the 
        // declared methods will be returned in, but the order matters 
        TreeMap<Integer, ArgumentModel<?>> argumentModels = new TreeMap<Integer, ArgumentModel<?>>();
        for (Method method : cls.getDeclaredMethods()) {
            addMethod(cls, model, method, argumentModels);
        }
        
        for (ArgumentModel<?> argumentModel : argumentModels.values()) {
            model.addArgument(argumentModel);
        }
        return model;
    }

    private <T extends Plugin, A> void addMethod(Class<T> cls, PluginModel<T> model,
            Method method, Map<Integer, ArgumentModel<?>> argumentModels) {
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
            model.setRest(method);
        }
        
        OptionModel optionModel = buildOption(method);
        OptionModel optionArgumentModel = buildOptionArgument(method);
        ArgumentModel<A> argumentModel = buildArgument(method, argumentModels);
        if (optionModel!= null) {
            if (argumentModel != null) {
                throw new ModelException(method + " is annotated with both @Option and @Argument");
            } 
            if (optionArgumentModel != null) {
                throw new ModelException(method + " is annotated with both @Option and @OptionArgument");
            }
            checkDuplicateOption(cls, model, optionModel);
            model.addOption(optionModel);
        } else if (optionArgumentModel != null) {
            if (argumentModel != null) {
                throw new ModelException(method + " is annotated with both @OptionArgument and @Argument");
            }
            checkDuplicateOption(cls, model, optionArgumentModel);
            model.addOption(optionArgumentModel);
        } else if (argumentModel != null) {
            
            // We don't add it to the model here
        } 
    }

    private <T extends Plugin> void checkDuplicateOption(Class<T> cls,
            PluginModel<T> model, OptionModel optionModel) {
        if (model.getOption(optionModel.getName()) != null) {
            throw new ModelException(cls + " has more than one binding for option " + optionModel.getName());
        }
        if (optionModel.getShortName() != null
                && model.getOptionByShort(optionModel.getShortName()) != null) {
            throw new ModelException(cls + " has more than one binding for short option " + optionModel.getShortName());
        }
    }

    private ArgumentModel<Boolean> buildPureOption(Method method) {
        ArgumentModel<Boolean> argumentModel;
        argumentModel = new ArgumentModel<Boolean>();
        argumentModel.setSetter(method);
        argumentModel.setType(boolean.class);
        argumentModel.setMultiplicity(Multiplicity.NONE);
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

    private void checkClass(Class<? extends Plugin> cls) throws ModelException {
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
        try {
            cls.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new ModelException("Tool " + cls + " does not have a public 0 argument constructor");            
        }
    }

    private boolean isSetter(Method method) {
        return method.getName().matches("set[A-Z0-9].*")
                && Modifier.isPublic(method.getModifiers())
                && method.getReturnType().equals(void.class)
                && method.getParameterTypes().length == 1;
    }

    private String getToolName(Class<? extends Plugin> toolClass) {
        String name = toolClass.getSimpleName();
        return getToolName(name);
    }

    private String camelCaseToDashes(String name) {
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

    private OptionModel buildOption(final Method setter) {
        Option option = setter.getAnnotation(Option.class);
        if (option == null) {
            return null;
        }
        if (!isSetter(setter)) {
            throw new ModelException("Method" + setter + " is annotated with @Option but is not a setter");
        }
        OptionModel optionModel = new OptionModel();
        optionModel.setName(getOptionName(option.longName(), setter));
        char shortName = option.shortName();
        if (shortName != Option.NO_SHORT) {
            optionModel.setShortName(shortName);
        }
        optionModel.setArgument(buildPureOption(setter));
        return optionModel;
    }

    private <A> OptionModel buildOptionArgument(final Method setter) {
        OptionArgument option = setter.getAnnotation(OptionArgument.class);
        if (option == null) {
            return null;
        }
        if (!isSetter(setter)) {
            throw new ModelException("Method " + setter + " is annotated with @OptionArgument but is not a setter");
        }
        OptionModel optionModel = new OptionModel();
        optionModel.setName(getOptionName(option.longName(), setter));
        char shortName = option.shortName();
        if (shortName != OptionArgument.NO_SHORT) {
            optionModel.setShortName(shortName);
        }
        ArgumentModel<A> argumentModel = new ArgumentModel<A>();
        
        Class<A> argumentType = (Class<A>)getSimpleTypeOrCollectionType(setter, OptionArgument.class);
        argumentModel.setType(argumentType);
        argumentModel.setMultiplicity(isSimpleType(setter) ? Multiplicity.QUESTION : Multiplicity.STAR);
        argumentModel.setName(option.argumentName());
        argumentModel.setSetter(setter);
        optionModel.setArgument(argumentModel);
        return optionModel;
    }
    
    private <A> ArgumentModel<A> buildArgument(final Method setter, Map<Integer, ArgumentModel<?>> argumentModels) {
        Argument argument = setter.getAnnotation(Argument.class);
        if (argument == null) {
            return null;
        }
        if (!isSetter(setter)) {
            throw new ModelException("Method " + setter + " is annotated with @Argument but is not a setter");
        }
        ArgumentModel<A> argumentModel = new ArgumentModel<A>();
        argumentModel.setMultiplicity(Multiplicity.fromString(argument.multiplicity()));
        argumentModel.setName(argument.argumentName());
        
        Class<A> argumentType = (Class<A>)getSimpleTypeOrCollectionType(setter, Argument.class);
        argumentModel.setType(argumentType);
                
        final ArgumentParser<?> parser = argParserFactory.getParser(argumentModel.getType());
        if (parser == null) {
            throw new ModelException("Unable to parse arguments of " + argumentModel.getType());
        }
        argumentModel.setSetter(setter);
        
        final ArgumentModel<?> clash = argumentModels.put(argument.order(), argumentModel);
        if (clash != null) {
            throw new ModelException("Two setters annotated with @Argument with the same order");
        }
        return argumentModel;
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
        if (!List.class.equals(rt)) {
            throw new ModelException("Method " + setter + " is annotated with " + annoType.getSimpleName() + " but the parameter type is not java.util.List");
        }
        Type ta = pt.getActualTypeArguments()[0];
        if (!(ta instanceof Class)) {
            throw new ModelException("Method " + setter + " is annotated with " + annoType.getSimpleName() + " but the type parameter to the java.util.List parameter is not a class");
        }
        Class<?> argumentType = (Class<?>)ta;
        return argumentType;
    }
    
    private String getToolName(String className) {
        String toolName = className.replaceAll("Tool$", "").replaceAll("^.*\\.", "");
        return camelCaseToDashes(toolName);
    }
    
    /**
     * Returns an iterable of all the tools names known to this tool loader.
     */
    public Iterable<String> getToolNames() {
        List<String> result = iterateToolNames(new Handler<String>() {
            @Override
            public String handle(String className) {
                return getToolName(className);
            }
        });
        Collections.sort(result);
        return result;
    }
    static interface Handler<T> {
        public T handle(String className);
    }
    
    private <T> List<T> iterateToolNames(Handler<T> handler) {
        List<T> result = new ArrayList<T>();
        /* Use the same conventions as java.util.ServiceLoader but without 
         * requiring us to load the Service classes
         */
        Enumeration<URL> resources;
        try {
            resources = loader.getResources("META-INF/services/"+Plugin.class.getName());
        } catch (IOException e) {
            throw new PluginException(e);
        }
        while (resources.hasMoreElements()) {
            parseServiceInfo(handler, result, resources);
        }
        // Nasty hack to work around the Eclipse putting all the generated 
        // classes and resources in the same place: 
        try {
            resources = loader.getResources("META-INF/services/"+Plugin.class.getName()+"-test");
        } catch (IOException e) {
            throw new PluginException(e);
        }
        while (resources.hasMoreElements()) {
            parseServiceInfo(handler, result, resources);
        }
        return result;
    }

    private <T> void parseServiceInfo(Handler<T> handler, List<T> result,
            final Enumeration<URL> resources) {
        URL url = resources.nextElement();
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            try {
                String className = reader.readLine();
                while (className != null) {
                    className = className.trim().replaceAll("#.*", "");
                    if (!className.isEmpty()) {
                        final T value = handler.handle(className);
                        if (value != null) {
                            result.add(value);
                        }
                    }
                    className = reader.readLine();
                }
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            throw new PluginException("Error reading service file " + url, e);
        }
    }

    public List<String> typo(final String command) {
        return iterateToolNames(new Handler<String>() {
            @Override
            public String handle(String className) {
                String toolName = getToolName(className);
                if (levenshteinDistance(toolName, command) < 3) {
                    return toolName;
                }
                return null;
            }
        });
    }
    
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
    
}
