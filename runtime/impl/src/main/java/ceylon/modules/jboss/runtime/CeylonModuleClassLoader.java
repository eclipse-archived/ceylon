package ceylon.modules.jboss.runtime;

import org.jboss.modules.ModuleClassLoader;
import org.jboss.modules.ModuleClassLoaderFactory;

/**
 * Class used in the language module to be able to force metamodel registration from the transformer.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class CeylonModuleClassLoader extends ModuleClassLoader implements com.redhat.ceylon.common.runtime.CeylonModuleClassLoader {

    private UtilRegistryTransformer transformer;

    protected CeylonModuleClassLoader(Configuration configuration, UtilRegistryTransformer transformer) {
        super(configuration);
        this.transformer = transformer;
    }
    
    @Override
    public void registerInMetaModel(){
        transformer.register(this);
    }
    
    /**
     * Believe it or not, but ModuleClassLoader.Configuration is protected so it can only be seen in subclasses of
     * ModuleClassLoader, which is why this factory is located here.
     */
    static class CeylonModuleClassLoaderFactory implements ModuleClassLoaderFactory {
        private UtilRegistryTransformer transformer;

        /*
         * Remember the transformer for when we create the class loader
         */
        public CeylonModuleClassLoaderFactory(UtilRegistryTransformer transformer) {
            this.transformer = transformer;
        }
        
        @Override
        public ModuleClassLoader create(Configuration configuration) {
            return new CeylonModuleClassLoader(configuration, transformer);
        }
    }
}
