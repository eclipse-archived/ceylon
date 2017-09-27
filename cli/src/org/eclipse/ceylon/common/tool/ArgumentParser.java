package org.eclipse.ceylon.common.tool;

public interface ArgumentParser<A> {

    public A parse(String argument, Tool tool) throws Exception;
    
}
