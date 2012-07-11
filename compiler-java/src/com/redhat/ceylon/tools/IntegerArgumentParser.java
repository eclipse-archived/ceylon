package com.redhat.ceylon.tools;

class IntegerArgumentParser implements ArgumentParser<Integer> {

    @Override
    public Integer parse(String argument) {
        return Integer.valueOf(argument);
    }

}
