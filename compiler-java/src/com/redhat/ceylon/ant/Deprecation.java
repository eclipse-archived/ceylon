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

package com.redhat.ceylon.ant;

import org.apache.tools.ant.Task;

class Deprecation {

    static void message(Task task, Class<?> newClass) {
        Class<?> oldClass = task.getClass();
        String oldTaskName = oldClass.getSimpleName().toLowerCase();
        
        String newTaskName = newClass.getSimpleName();
        newTaskName = newTaskName.replaceAll("AntTask$", "");
        StringBuilder sb = new StringBuilder();
        for (char ch : newTaskName.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                sb.append("-");
            }
            sb.append(Character.toLowerCase(ch));
        }
        newTaskName = sb.substring(1);
        
        task.log("********** IMPORTANT NOTICE **********");
        task.log("The task class known as " + oldClass.getName() + 
        " is deprecated has been replaced by " + newClass.getName() + ".");
        task.log(oldClass.getName() + " will be removed in a future release");
        task.log("Instead being declared as");
        
        task.log("  <taskdef name=\"" + oldTaskName + "\" classname=\""+ oldClass.getName() + "\" classpathref=\"ant-tasks\"/>");
        task.log("and used like this");
        task.log("  <" + oldTaskName +" ...>...</" + oldTaskName+">");
        task.log("It should be declared as");
        task.log("  <typedef resource=\"com/redhat/ceylon/ant/antlib.xml\" classpathref=\"ant-tasks\"/>");
        task.log("and used like this");
        
        task.log("  <" + newTaskName +" ...>...</" + newTaskName+">");
        task.log("********** /IMPORTANT NOTICE *********");
    }
    
}
