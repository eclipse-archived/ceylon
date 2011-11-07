package ceylon.language;

public class Ordered$Impl {
    public static <Element> boolean getEmpty(Ordered<Element> $this){
        return $this.getFirst() != null;
    }

    public static <Element> Element getFirst(Ordered<Element> $this){
        return $this.getIterator().getHead();
    }

    public static <Element> Ordered<Element> segment(Ordered<Element> $this, long skipping, final long finishingAfter){
    	if(finishingAfter == 0){
    		return new ArraySequence<Element>();
    	}else{
    		Iterator<Element> it = $this.getIterator();
    		long skipped = 0;
    		while(++skipped <= skipping){
    			it = it.getTail();
    		}
    		final Iterator<Element> capturedIt = it;
    		
    		class SegmentIterator implements Iterator<Element>{

    			private Iterator<Element> iterator;
				private long remaining;

				SegmentIterator(Iterator<Element> iterator, long remaining){
    				this.iterator = iterator;
    				this.remaining = remaining;
    			}
    			
				@Override
				public Element getHead() {
					if(remaining == 0){
						return null;
					}else{
						return iterator.getHead();
					}
				}

				@Override
				public Iterator<Element> getTail() {
					return new SegmentIterator(iterator.getTail(), remaining-1);
				}
    			
    		}
    		
    		Ordered<Element> segment = new Ordered<Element>(){

				@Override
				public Iterator<Element> getIterator() {
					return new SegmentIterator(capturedIt, finishingAfter);
				}

				@Override
				public boolean getEmpty() {
					return Ordered$Impl.getEmpty(this);
				}

				@Override
				public Element getFirst() {
					return Ordered$Impl.getFirst(this);
				}

				@Override
				public Ordered<Element> segment(long skipping,
						long finishingAfter) {
					return Ordered$Impl.segment(this, skipping, finishingAfter);
				}

				@Override
				public java.lang.String toString(){
					return "segment"; //todo
				}
    		};
    		
    		return segment;
    	}
    }

}
