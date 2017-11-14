

package org.eclipse.ceylon.common.log;

/**
 * Simple logger impl.
 *
 * @author Stephane Epardaud
 */
public abstract class JULLogger implements Logger {

    protected abstract java.util.logging.Logger logger();
    
    @Override
    public void error(String str) {
        logger().severe(str);
    }

    @Override
    public void warning(String str) {
        logger().warning(str);
    }

    @Override
    public void info(String str) {
        logger().info(str);
    }

    @Override
    public void debug(String str) {
        logger().fine(str);
    }

}
