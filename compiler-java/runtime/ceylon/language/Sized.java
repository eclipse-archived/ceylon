package ceylon.language;

public interface Sized extends Container {
    public Natural getSize();
    
    public Boolean isEmpty();/* {
        return size==0;
    }*/

}
