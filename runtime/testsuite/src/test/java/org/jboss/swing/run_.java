/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.swing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class run_ {
    public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        System.out.println(run_.class.getName() + ": run_ 1 ...");
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

        System.out.println(run_.class.getName() + ": run_ 2 ...");
    }
}
