/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
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
