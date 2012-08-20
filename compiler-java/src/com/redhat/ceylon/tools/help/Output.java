package com.redhat.ceylon.tools.help;

import org.tautua.markdownpapers.ast.Node;


interface Output {

    interface Options {
        public void option(String shortName, String longName, String argumentName, Node descriptionMd);
        public void endOptions();
    }
    
    interface Synopsis {
        
        public void appendSynopsis(String s);
        public void longOptionSynopsis(String string);
        public void shortOptionSynopsis(String string);
        public void argumentSynopsis(String name);
        public void endSynopsis();
        public void nextSynopsis();
                
    }
    
    public void title(String title);
    public Options startOptions(String title);
    public Synopsis startSynopsis(String title);
    public void section(Node paraMd);
    
    public void end();
    
}
