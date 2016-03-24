package com.redhat.ceylon.common;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        Class<?> moduleClass = ClassLoader.getSystemClassLoader().loadClass("java.lang.reflect.Module");
		Method getLayer = moduleClass.getMethod("getLayer");
		Object layer = getLayer.invoke(fromModule);
		return findModuleFromLayer(layer, name);
	}
	
	private static Object findModuleFromLayer(Object layer, String name) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, SecurityException {
        Class<?> layerClass = ClassLoader.getSystemClassLoader().loadClass("java.lang.reflect.Layer");
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
        Class<?> moduleClass = ClassLoader.getSystemClassLoader().loadClass("java.lang.reflect.Module");
        Method getClassLoader = moduleClass.getMethod("getClassLoader");
        Object classLoader = getClassLoader.invoke(module);
        return (ClassLoader) classLoader;
	}

	public static boolean isNamedModule(Object module) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, SecurityException {
        Class<?> moduleClass = ClassLoader.getSystemClassLoader().loadClass("java.lang.reflect.Module");
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
        
        // Configuration cf = Configuration.resolve(ModuleFinder.empty(),
        //         Layer.boot().configuration(),
        //         finder,
        //         appModuleName);
        Class<?> configurationClass = ClassLoader.getSystemClassLoader().loadClass("java.lang.module.Configuration");
        Class<?> layerClass = ClassLoader.getSystemClassLoader().loadClass("java.lang.reflect.Layer");
        Method resolveMethod = configurationClass.getMethod("resolve", moduleFinderClass, layerClass, moduleFinderClass, String[].class);

        Method emptyMethod = moduleFinderClass.getMethod("empty");
        Object emptyModuleFinder = emptyMethod.invoke(null);

        Method bootMethod = layerClass.getMethod("boot");
        Object bootLayer = bootMethod.invoke(null);
//        Method configurationMethod = layerClass.getMethod("configuration");
//        Object bootConfiguration = configurationMethod.invoke(bootLayer);
        
        Object configuration = resolveMethod.invoke(null, emptyModuleFinder, bootLayer, moduleFinder, modules);

        // cf = cf.bind();
        Method bindMethod = configurationClass.getMethod("bind");
        configuration = bindMethod.invoke(configuration);
        
        // choose a class loader
        // ModuleClassLoader loader = new ModuleClassLoader(cf);
        Class<?> moduleClassLoaderClass = ClassLoader.getSystemClassLoader().loadClass("java.lang.ModuleClassLoader");
        Constructor<?> moduleClassLoaderConstructor = moduleClassLoaderClass.getConstructor(configurationClass);
        final Object moduleClassLoader = moduleClassLoaderConstructor.newInstance(configuration);

        // reify the configuration as a Layer
        // Layer layer = Layer.create(cf, mn -> loader);
        Class<?> classLoaderFinderClass = ClassLoader.getSystemClassLoader().loadClass("java.lang.reflect.Layer$ClassLoaderFinder");
        Method createMethod = layerClass.getMethod("create", configurationClass, classLoaderFinderClass);
        
        InvocationHandler handler = new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				return moduleClassLoader;
			}
		};
		Object classLoaderFinder = Proxy.newProxyInstance(Java9ModuleUtil.class.getClassLoader(), new Class[]{classLoaderFinderClass}, handler );
        
        Object newLayer = createMethod.invoke(null, configuration, classLoaderFinder);
        return findModuleFromLayer(newLayer, modules[0]);
	}
}
