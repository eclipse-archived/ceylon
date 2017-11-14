

package org.eclipse.ceylon.cmr.impl;

import java.util.logging.Logger;

import org.eclipse.ceylon.cmr.api.AbstractDependencyResolverAndModuleInfoReader;
import org.eclipse.ceylon.cmr.api.CmrRepository;
import org.eclipse.ceylon.cmr.api.DependencyResolver;
import org.eclipse.ceylon.cmr.api.DependencyResolvers;
import org.eclipse.ceylon.cmr.api.RepositoryManager;

/**
 * Simple config holder.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Configuration {
    private static DependencyResolver mavenResolver;
    private static AbstractDependencyResolverAndModuleInfoReader jsResolver;
    private static AbstractDependencyResolverAndModuleInfoReader npmResolver;

    public static final String MAVEN_RESOLVER_CLASS = "org.eclipse.ceylon.cmr.maven.MavenDependencyResolver";
    public static final String JS_RESOLVER_CLASS = "org.eclipse.ceylon.cmr.impl.JSUtils";
    public static final String NPM_RESOLVER_CLASS = "org.eclipse.ceylon.cmr.impl.NpmUtils";
    
    public static DependencyResolver getMavenResolver(){
        if (mavenResolver == null) {
            mavenResolver = getResolver(MAVEN_RESOLVER_CLASS);
        }
        return mavenResolver;
    }

    public static AbstractDependencyResolverAndModuleInfoReader getJavaScriptResolver(){
        if (jsResolver == null) {
            jsResolver = (AbstractDependencyResolverAndModuleInfoReader) getResolver(JS_RESOLVER_CLASS);
        }
        return jsResolver;
    }

    public static AbstractDependencyResolverAndModuleInfoReader getNpmResolver(){
        if (npmResolver == null) {
            npmResolver = (AbstractDependencyResolverAndModuleInfoReader) getResolver(NPM_RESOLVER_CLASS);
        }
        return npmResolver;
    }

    public static DependencyResolvers getResolvers(RepositoryManager manager) {
        DependencyResolvers resolvers = new DependencyResolvers();
        resolvers.addResolver(BytecodeUtils.INSTANCE);
        DependencyResolver jsResolver = getJavaScriptResolver();
        if (jsResolver != null) {
            resolvers.addResolver(jsResolver);
        }
        resolvers.addResolver(PropertiesDependencyResolver.INSTANCE);
        resolvers.addResolver(XmlDependencyResolver.INSTANCE);
        if (usesMaven(manager)) {
            DependencyResolver mavenResolver = getMavenResolver();
            if (mavenResolver != null) {
                resolvers.addResolver(mavenResolver);
            }
        }
        resolvers.addResolver(OSGiDependencyResolver.INSTANCE);
        DependencyResolver npmResolver = getNpmResolver();
        if (npmResolver != null) {
            resolvers.addResolver(npmResolver);
        }
        return resolvers;
    }

    private static boolean usesMaven(RepositoryManager manager) {
        if (manager != null) {
            for (CmrRepository repo : manager.getRepositories()) {
                if (repo instanceof MavenRepository) {
                    return true;
                }
            }
        }
        return false;
    }

    private static DependencyResolver getResolver(String className) {
        try {
            ClassLoader cl = Configuration.class.getClassLoader();
            DependencyResolver resolver = (DependencyResolver) cl.loadClass(className).newInstance();
            return resolver;
        } catch (Throwable t) {
            Logger.getLogger(Configuration.class.getName()).warning(String.format("Cannot add resolver %s - %s", className, t));
        }
        return null;
    }
}
