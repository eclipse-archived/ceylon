package ceylon.language;


public class Sequence$impl {
    public static <Element> boolean getEmpty(Sequence<Element> $this){
        return false;
    }

    public static <Element> Natural getSize(Sequence<Element> $this){
        return Natural.instance($this.getLastIndex().longValue()+1);
    }

    public static <Element> Element getLast(Sequence<Element> $this){
        Element x = $this.item($this.getLastIndex());
        if (x != null) {
            return x;
        }
        else {
            return $this.getFirst(); //actually never occurs
        } 
    }

    public static <Element> boolean defines(Sequence<Element> $this, Natural index){
        return index.longValue() <= $this.getLastIndex().longValue();
    }

    public static <Element> Iterator<Element> getIterator(Sequence<Element> $this){
        return new Sequence.SequenceIterator<Element>($this, 0);
    }
    
    public static <Element> java.lang.String toString(Sequence<Element> $this){
        return "{" + getElementsString($this) + "}";
    }
    
    public static <Element> java.lang.String getElementsString(final Sequence<Element> $this){
        final class getFirstString$getter {
            
            java.lang.String getFirstString() {
                Element first = $this.getFirst();
                if(first instanceof Object)
                    return first.toString();
                else if(first == null)
                    return "null";
                throw new ceylon.language.Exception(null, null);
            }
        }
        final getFirstString$getter getFirstString$getter = new getFirstString$getter();
        Sequence<? extends Element> rest = $this.getRest();
        if(rest != null){
            return getFirstString$getter.getFirstString() + ", " + rest.getElementsString();
        }else{
            return getFirstString$getter.getFirstString();
        }
    }
}
