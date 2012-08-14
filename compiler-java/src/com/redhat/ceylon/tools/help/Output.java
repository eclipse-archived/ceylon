package com.redhat.ceylon.tools.help;


interface Output {

    interface Section {
        public void paragraph(String paraMd);
        public void endSection();
    }
    
    interface Options {
        public void option(String shortName, String longName, String argumentName, String descriptionMd);
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
    public Section section(String title);
    
    public void end();
    
}
