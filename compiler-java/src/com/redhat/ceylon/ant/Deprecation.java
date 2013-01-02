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
