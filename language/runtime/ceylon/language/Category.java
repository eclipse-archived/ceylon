package ceylon.language;

public interface Category {
    public boolean contains(Object value);
    
    public boolean containsEvery(Object... objects);/* {
        for (Object obj in objects) {
            if (!contains(obj)) {
                return false;
            }
        }
        fail {
            return true;
        }
    }*/

    public boolean containsAny(Object... objects); /*{
        for (Object obj in objects) {
            if (contains(obj)) {
                return true;
            }
        }
        fail {
            return false;
        }
    }*/

}
