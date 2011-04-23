shared object process {

    shared String[] args { throw; }
    
    shared Entry<String, String>[] switches { throw; }

    shared Correspondence<String,String> properties  { throw; }

    shared void write(String string) { throw; }
    
    shared void writeLine(String line) { write(line); write("\n"); }
    
    shared String readLine() { throw; }

}