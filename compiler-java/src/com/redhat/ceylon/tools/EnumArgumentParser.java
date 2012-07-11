package com.redhat.ceylon.tools;

class EnumArgumentParser<E extends Enum<E>> implements ArgumentParser<E> {

    private final boolean denormalize;
    private final Class<E> enumClass;
    
    public EnumArgumentParser(Class<E> enumClass, boolean denormalize) {
        this.enumClass = enumClass;
        this.denormalize = denormalize;
    }
    
    @Override
    public E parse(String argument) {
        if (denormalize) {
            argument = denormalize(argument);
        }
        return Enum.valueOf(enumClass, argument);
    }
    
    protected String denormalize(String argument) {
        return argument.toUpperCase().replace('-', '_');
    }

    
    
}
