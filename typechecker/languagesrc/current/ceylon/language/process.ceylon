shared object process {

    shared String[] arguments { throw; }
    
    shared Entries<String,String> switches { throw; }

    shared Entries<String,String> properties { throw; }

    shared void write(String string) { throw; }
    
    shared void writeLine(String line) { write(line); write("\n"); }
    
    shared String readLine() { throw; }

}