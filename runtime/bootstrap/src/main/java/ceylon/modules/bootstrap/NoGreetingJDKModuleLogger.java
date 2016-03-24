package ceylon.modules.bootstrap;

import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.log.JDKModuleLogger;
import org.jboss.modules.log.ModuleLogger;

public class NoGreetingJDKModuleLogger implements ModuleLogger {

    private JDKModuleLogger forward;

    public NoGreetingJDKModuleLogger(){
        this.forward = new JDKModuleLogger();
    }
    
    @Override
    public void trace(String message) {
        forward.trace(message);
    }

    @Override
    public void trace(String format, Object arg1) {
        forward.trace(format, arg1);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        forward.trace(format, arg1, arg2);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2, Object arg3) {
        forward.trace(format, arg1, arg2, arg3);
    }

    @Override
    public void trace(String format, Object... args) {
        forward.trace(format, args);
    }

    @Override
    public void trace(Throwable t, String message) {
        forward.trace(t, message);
    }

    @Override
    public void trace(Throwable t, String format, Object arg1) {
        forward.trace(t, format, arg1);
    }

    @Override
    public void trace(Throwable t, String format, Object arg1, Object arg2) {
        forward.trace(t, format, arg1, arg2);
    }

    @Override
    public void trace(Throwable t, String format, Object arg1, Object arg2, Object arg3) {
        forward.trace(t, format, arg1, arg2, arg3);
    }

    @Override
    public void trace(Throwable t, String format, Object... args) {
        forward.trace(t, format, args);
    }

    @Override
    public void greeting() {
        // NOTHING!!!
    }

    @Override
    public void moduleDefined(ModuleIdentifier identifier, ModuleLoader moduleLoader) {
        forward.moduleDefined(identifier, moduleLoader);
    }

    @Override
    public void classDefineFailed(Throwable throwable, String className, Module module) {
        forward.classDefineFailed(throwable, className, module);
    }

    @Override
    public void classDefined(String name, Module module) {
        forward.classDefined(name, module);
    }

    @Override
    public void providerUnloadable(String name, ClassLoader loader) {
        forward.providerUnloadable(name, loader);
    }
}
