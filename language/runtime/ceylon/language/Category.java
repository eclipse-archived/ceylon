package ceylon.language;

public interface Category {
    public Boolean contains(Object value);
    
    public Boolean containsEvery(Object... objects);/* {
        for (Object obj in objects) {
            if (!contains(obj)) {
                return false;
            }
        }
        fail {
            return true;
        }
    }*/

    public Boolean containsAny(Object... objects); /*{
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
