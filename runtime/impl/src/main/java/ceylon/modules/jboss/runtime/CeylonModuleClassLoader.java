package ceylon.modules.jboss.runtime;

import org.jboss.modules.ModuleClassLoader;
import org.jboss.modules.ModuleClassLoaderFactory;

/**
 * Class used in the language module to be able to force metamodel registration from the transformer.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class CeylonModuleClassLoader extends ModuleClassLoader implements com.redhat.ceylon.common.runtime.CeylonModuleClassLoader {

    static {
        boolean parallelOk = true;
        try {
            parallelOk = ClassLoader.registerAsParallelCapable();
        } catch (Throwable ignored) {
        }
        if (! parallelOk) {
            throw new Error("Failed to register " + CeylonModuleClassLoader.class.getName() + " as parallel-capable");
        }
    }

    private UtilRegistryTransformer transformer;
    private volatile int registerThreadCount = 0;
    private final Object registerThreadLock = new Object();

    protected CeylonModuleClassLoader(Configuration configuration, UtilRegistryTransformer transformer) {
        super(configuration);
        this.transformer = transformer;
    }
    
    // Stef: Enable back when we update jboss modules
//    @Override
//    protected String getClassNotFoundExceptionMessage(String className, Module fromModule){
//        StringBuilder b = new StringBuilder(className);
//        b.append(" from module '").append(fromModule.getIdentifier()+"' whose dependencies are: [");
//        DependencySpec[] dependencies = fromModule.getDependencies();
//        boolean first = true;
//        for(DependencySpec dep : dependencies){
//            if(dep instanceof ModuleDependencySpec){
//                ModuleDependencySpec modDep = ((ModuleDependencySpec)dep);
//                if(first)
//                    first = false;
//                else
//                    b.append(", ");
//                b.append(modDep.getIdentifier());
//            }
//        }
//        b.append("]");
//        ModuleLoader moduleLoader = fromModule.getModuleLoader();
//        if(moduleLoader instanceof CeylonModuleLoader){
//            List<ModuleIdentifier> modules = ((CeylonModuleLoader) moduleLoader).findModuleForClass(className);
//            if(!modules.isEmpty()){
//                b.append(". That class can be found in the following modules: ");
//                first = true;
//                for(ModuleIdentifier module : modules){
//                    if(first)
//                        first = false;
//                    else
//                        b.append(", ");
//                    b.append(module);
//                }
//            }
//        }
//        return b.toString();
//    }
    
    @Override
    public void registerInMetaModel(){
        transformer.register(this);
    }

    public void registerThreadRunning() {
        synchronized(registerThreadLock){
            registerThreadCount++;
        }
    }

    public void registerThreadDone() {
        synchronized(registerThreadLock){
            registerThreadCount--;
            registerThreadLock.notifyAll();
        }
    }

    public void waitForRegisterThreads(){
        synchronized(registerThreadLock){
            while(registerThreadCount > 0){
                try {
                    registerThreadLock.wait();
                } catch (InterruptedException e) {
                }
            }
        }
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
