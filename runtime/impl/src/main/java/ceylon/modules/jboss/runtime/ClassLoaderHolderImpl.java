package ceylon.modules.jboss.runtime;

import ceylon.modules.spi.runtime.ClassLoaderHolder;
import org.jboss.modules.Module;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class ClassLoaderHolderImpl implements ClassLoaderHolder {
    private final Module module;

    ClassLoaderHolderImpl(Module module) {
        this.module = module;
    }

    public ClassLoader getClassLoader() {
        return SecurityActions.getClassLoader(module);
    }

    public String getVersion() {
        return module.getIdentifier().getSlot();
    }

    @Override
    public String toString() {
        return module.toString();
    }
}
