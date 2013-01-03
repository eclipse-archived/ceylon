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

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * Utility for managing task exit according to a tasks' 
 * {@code failonerror} and {@code errorproperty} attributes.
 * @author tom
 */
class ExitHandler {

    private String errorProperty;
    private boolean failOnError = true;
    
    String getErrorProperty() {
        return errorProperty;
    }
    void setErrorProperty(String errorProperty) {
        this.errorProperty = errorProperty;
    }
    boolean isFailOnError() {
        return failOnError;
    }
    void setFailOnError(boolean failOnError) {
        this.failOnError = failOnError;
    }
    void handleExit(Task task, int sc, String message) {
        if (errorProperty != null) {
            task.getProject().setNewProperty(
                errorProperty, "true");
        }
        if (failOnError) {
            throw new BuildException(message, task.getLocation());
        } else {
            task.log(message, Project.MSG_ERR);
        }
    }
    
    
}
