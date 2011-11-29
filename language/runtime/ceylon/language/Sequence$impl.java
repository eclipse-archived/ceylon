package ceylon.language;


public class Sequence$impl {
    public static <Element> boolean getEmpty(Sequence<Element> $this){
        return false;
    }

    public static <Element> long getSize(Sequence<Element> $this){
        return $this.getLastIndex()+1;
    }

    public static <Element> Element getLast(Sequence<Element> $this){
        Element x = $this.item(Natural.instance($this.getLastIndex()));
        if (x != null) {
            return x;
        }
        else {
            return $this.getFirst(); //actually never occurs
        } 
    }

    public static <Element> boolean defines(Sequence<Element> $this, Natural index){
        return index.longValue() <= $this.getLastIndex();
    }

    public static <Element> Iterator<Element> getIterator(Sequence<Element> $this){
        return new Sequence.SequenceIterator<Element>($this, 0);
    }
    
    public static <Element> java.lang.String toString(Sequence<Element> $this) {
		java.lang.StringBuilder result = new java.lang.StringBuilder("{ ");
		for (Iterator<Element> iter=$this.getIterator(); iter!=null; iter=iter.getTail()) {
			result.append(iter.getHead())
				.append(", ");
		}
		result.setLength(result.length()-2);
		result.append(" }");
		return result.toString();
    }
    
}
