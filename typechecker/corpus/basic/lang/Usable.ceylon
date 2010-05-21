public interface Usable {

    doc "Called before entry into a |try| block."
    public void begin();
    
    doc "Called before normal exit from a |try| block."
    public void end();

    doc "Called before exit from a |try| block when an
         exception occurs."
    public void end(Exception e);
    
}