package com.redhat.ceylon.common.tool;

import java.util.ArrayList;
import java.util.List;

public class EnumToolLoader<E extends Enum<E>> extends ToolLoader {

    private Class<E> enumClass;

    protected EnumToolLoader(Class<E> toolEnum) {
        this.enumClass = toolEnum;
    }

    @Override
    protected String getToolName(String className) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Iterable<String> toolClassNames() {
        Enum<?>[] elements;
        try {
            elements = (Enum<?>[])enumClass.getMethod("values").invoke(null);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
        List<String> result = new ArrayList<>(elements.length);
        for (Enum<?> element : elements) {
            result.add(element.getClass().getName());
        }
        return result;
    }
    
    public Tool instance(final String toolName) {
        return (Tool)Enum.valueOf(enumClass, toolName);
    }
    
    public static void main(String[] args) {
        System.out.println(new EnumToolLoader(Thread.State.class).toolClassNames());
    }

}
