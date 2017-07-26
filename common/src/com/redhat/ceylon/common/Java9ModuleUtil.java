package com.redhat.ceylon.common;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * Utility which deals with Java 9 modules using reflection so we can avoid depending on it
 * at compile-time.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class Java9ModuleUtil {

	public static Object getModule(Class<?> klass) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method getModule = Class.class.getMethod("getModule");
		return getModule.invoke(klass);
	}
	
	public static Object findModule(Object fromModule, String name) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        Class<?> moduleClass = ClassLoader.getSystemClassLoader().loadClass("java.lang.Module");
		Method getLayer = moduleClass.getMethod("getLayer");
		Object layer = getLayer.invoke(fromModule);
		return findModuleFromLayer(layer, name);
	}
	
	private static Object findModuleFromLayer(Object layer, String name) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, SecurityException {
        Class<?> layerClass = ClassLoader.getSystemClassLoader().loadClass("java.lang.ModuleLayer");
        Method findModule = layerClass.getMethod("findModule", String.class);
        Object optionalModule = findModule.invoke(layer, name);
        Class<?> optionalClass = ClassLoader.getSystemClassLoader().loadClass("java.util.Optional");
        Method get = optionalClass.getMethod("get");
        try{
        	return get.invoke(optionalModule);
        }catch(InvocationTargetException x){
        	if(x.getTargetException() instanceof NoSuchElementException)
        		return null;
        	throw x;
        }
	}

	public static ClassLoader getClassLoader(Object module) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, SecurityException{
        Class<?> moduleClass = ClassLoader.getSystemClassLoader().loadClass("java.lang.Module");
        Method getClassLoader = moduleClass.getMethod("getClassLoader");
        Object classLoader = getClassLoader.invoke(module);
        return (ClassLoader) classLoader;
	}

	public static boolean isNamedModule(Object module) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, SecurityException {
        Class<?> moduleClass = ClassLoader.getSystemClassLoader().loadClass("java.lang.Module");
        Method isNamed = moduleClass.getMethod("isNamed");
        Boolean ret = (Boolean) isNamed.invoke(module);
        return ret.booleanValue();
	}

	public static Object loadModuleDynamically(String module) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException{
        String modulePath = System.getProperty("jdk.module.path");
        String[] modulePathEntries = modulePath.split(File.pathSeparator);
        return loadModulesDynamically(modulePathEntries, module);
	}
	
	public static Object loadModulesDynamically(String[] modulePath, String... modules) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException{
        Path[] paths = new Path[modulePath.length];
        int i=0;
        for(String moduleFolder : modulePath){
        	paths[i++] = Paths.get(moduleFolder);
        }

        // ModuleFinder finder = ModuleFinder.of(paths);
        Class<?> moduleFinderClass = ClassLoader.getSystemClassLoader().loadClass("java.lang.module.ModuleFinder");
        Method ofMethod = moduleFinderClass.getMethod("of", Path[].class);
        Object moduleFinder = ofMethod.invoke(null, (Object)paths);
        
        // Configuration parent = ModuleLayer.boot().configuration();
        Class<?> layerClass = ClassLoader.getSystemClassLoader().loadClass("java.lang.ModuleLayer");
        Method bootMethod = layerClass.getMethod("boot");
        Object bootLayer = bootMethod.invoke(null);

        Class<?> configurationClass = ClassLoader.getSystemClassLoader().loadClass("java.lang.module.Configuration");
        Method configurationMethod = layerClass.getMethod("configuration");
        Object parent = configurationMethod.invoke(bootLayer);
        
        // Configuration cf = parent.resolveAndBind(finder,
        //         ModuleFinder.of(),
        //         finder,
        //         Arrays.asList(modules));
        Method resolveMethod = configurationClass.getMethod("resolveAndBind", moduleFinderClass, moduleFinderClass, Collection.class);

        Object emptyModuleFinder = ofMethod.invoke(null, (Object)new Path[0]);

        Object configuration = resolveMethod.invoke(parent, moduleFinder, emptyModuleFinder, Arrays.asList(modules));

        // reify the configuration as a Layer
        // ModuleLayer layer = ModuleLayer.defineModulesWithOneLoader(cf, ClassLoader.getSystemClassLoader());
        Method createMethod = layerClass.getMethod("defineModulesWithOneLoader", configurationClass, ClassLoader.class);
        
        Object newLayer = createMethod.invoke(bootLayer, configuration, ClassLoader.getSystemClassLoader());
        return findModuleFromLayer(newLayer, modules[0]);
	}
}
