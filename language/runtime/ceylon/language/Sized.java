package ceylon.language;

public interface Sized extends Container {
    public Natural getSize();
    
    public Boolean getEmpty();/* {
        return size==0;
    }*/

}
