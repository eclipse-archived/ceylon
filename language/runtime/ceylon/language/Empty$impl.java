package ceylon.language;

import com.redhat.ceylon.compiler.java.language.ArraySequence;
import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
public final class Empty$impl {
    private final Empty $this;
    public Empty$impl(Empty $this) {
        this.$this = $this;
    }

    @SuppressWarnings("rawtypes")
    public Iterator getIterator() {
        return emptyIterator_.getEmptyIterator$();
    }

    public java.lang.Object item(java.lang.Object key){
        return null;
    }

    public Empty segment(Integer from, long length) {
        return $this;
    }

    public Empty span(Integer from, Integer to) {
        return $this;
    }

    public Empty spanTo(Integer to) {
        return $this;
    }

    public Empty spanFrom(Integer from) {
        return $this;
    }

    public long getSize() {
        return 0;
    }

    public Empty getReversed() {
        return $this;
    }

    public Empty getSequence() {
        return $this;
    }

    public java.lang.String toString() {
        return "{}";
    }

    public Integer getLastIndex(){
        return null;
    }

    public Integer getFirst(){
        return null;
    }

    public Integer getLast(){
        return null;
    }

    public Empty getClone() {
        return $this;
    }

    public Empty getCoalesced() {
        return $this;
    }

    public boolean contains(java.lang.Object element) {
        return false;
    }

    public long count(java.lang.Object element) {
        return 0;
    }
    
    public long count(Callable<? extends Boolean> f) {
        return 0;
    }

    public boolean defines(Integer key) {
        return false;
    }

    @SuppressWarnings("unchecked")
    public <Result> Iterable<Result> map(Callable<Result> collecting) {
        return (Iterable<Result>) $this;
    }
    
    public Empty filter(Callable<? extends Boolean> selecting) {
        return $this;
    }

    public <Result> Result fold(Result initial, Callable<? extends Result> accumulating) {
        return initial;
    }

    public java.lang.Object find(Callable<? extends Boolean> selecting) {
        return null;
    }

    public Empty sort(Callable<? extends Comparison> comparing) {
        return $this;
    }

    @SuppressWarnings("unchecked")
    public <Result> Sequential<? extends Result> collect(Callable<? extends Result> collecting) {
        return (Sequential<? extends Result>) $this;
    }
    
    public Empty select(Callable<? extends Boolean> selecting) {
        return $this;
    }

    public boolean any(Callable<? extends Boolean> selecting) {
        return false;
    }

    public boolean every(Callable<? extends Boolean> selecting) {
        return false;
    }

    public Empty skipping(long skip) {
        return $this;
    }

    public Empty taking(long take) {
        return $this;
    }

    public Empty by(long step) {
        return $this;
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <Other> Sequence withLeading(Other e) {
        return new ArraySequence<Other>(e);
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <Other> Sequence withTrailing(Other e) {
        return new ArraySequence<Other>(e);
    }
}
