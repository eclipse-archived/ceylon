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
