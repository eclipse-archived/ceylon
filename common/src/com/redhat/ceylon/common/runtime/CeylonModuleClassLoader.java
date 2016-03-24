package com.redhat.ceylon.common.runtime;

/**
 * This interface is here just so the language module can use a special hook in the ceylon-runtime project
 * without having to depend on it, which would be a circular dependency. Otherwise it would belong in the runtime
 * project.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface CeylonModuleClassLoader {
    public abstract void registerInMetaModel();
}
