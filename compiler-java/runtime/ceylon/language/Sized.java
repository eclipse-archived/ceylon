package ceylon.language;

public interface Sized extends Container {
    public Natural getSize();
    
    public boolean getEmpty();/* {
        return size==0;
    }*/

}
