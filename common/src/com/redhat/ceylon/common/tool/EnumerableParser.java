package com.redhat.ceylon.common.tool;

public interface EnumerableParser<A> extends ArgumentParser<A>{

    public Iterable<String> possibilities();
    
}
