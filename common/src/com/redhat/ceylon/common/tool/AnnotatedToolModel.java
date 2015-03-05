package com.redhat.ceylon.common.tool;

public class AnnotatedToolModel<T extends Tool> extends ToolModel<T> {
    private Class<T> toolClass;
    
    public AnnotatedToolModel(String name) {
        super(name);
    }

    public Class<T> getToolClass() {
        return toolClass;
    }

    public void setToolClass(Class<T> toolClass) {
        this.toolClass = toolClass;
    }

    @Override
    public boolean isPorcelain() {
        return super.isPorcelain() && (toolClass == null || toolClass.getAnnotation(Hidden.class) == null);
    }

    @Override
    public boolean isPlumbing() {
        return super.isPorcelain() && (toolClass == null || toolClass.getAnnotation(Hidden.class) != null);
    }
}
