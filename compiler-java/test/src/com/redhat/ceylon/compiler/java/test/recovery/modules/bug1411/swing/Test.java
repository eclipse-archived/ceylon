package swing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

public class Test {

    static void test() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        JPanel jPanel = new javax.swing.JPanel();
        UIDefaults defaults = UIManager.getDefaults();
        Object cl = defaults.get("ClassLoader");
        ClassLoader uiClassLoader =
          (cl != null) ? (ClassLoader)cl : jPanel.getClass().getClassLoader();
        try {
                String className = (String)defaults.get(jPanel.getUIClassID());
                if (className != null) {
                    Class cls = (Class)defaults.get(className);
                    if (cls == null) {
                        if (uiClassLoader == null) {
                            Method m = SwingUtilities.class.getDeclaredMethod("loadSystemClass", String.class);
                            m.setAccessible(true);
                            m.invoke(null, className);
                            // cls = SwingUtilities.loadSystemClass(className);
                            throw new RuntimeException("FAIL");
                        }
                        else {
                            cls = uiClassLoader.loadClass(className);
                        }
                    }
                }
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            catch (ClassCastException e) {
                e.printStackTrace();
            }
        
        jPanel.setVisible(true);
    }
}
