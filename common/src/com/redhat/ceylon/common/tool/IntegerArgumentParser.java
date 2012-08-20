package com.redhat.ceylon.common.tool;

class IntegerArgumentParser implements ArgumentParser<Integer> {

    @Override
    public Integer parse(String argument) {
        return Integer.valueOf(argument);
    }

}
