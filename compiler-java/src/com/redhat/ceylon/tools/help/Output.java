package com.redhat.ceylon.tools.help;

import org.tautua.markdownpapers.ast.Node;


interface Output {

    interface Section {
        public void paragraph(Node paraMd);
        public void endSection();
    }
    
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
                
    }
    
    public void title(String title);
    public Options options(String title);
    public Synopsis synopsis(String title);
    public Section section();
    
    public void end();
    
}
