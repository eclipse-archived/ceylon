package ceylon.language;

import com.redhat.ceylon.compiler.java.language.ArraySequence;
import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
public final class Empty$impl {
    private final Empty $this;
    public Empty$impl(Empty $this) {
        this.$this = $this;
    }

    public long getSize() {
        return _getSize($this);
    }
    private static long _getSize(Empty $this){
        return 0;
    }

    public Empty getReversed() {
        return _getReversed($this);
    }
    private static Empty _getReversed(Empty $this){
        return $this;
    }

    @SuppressWarnings("rawtypes")
    public Iterator getIterator() {
        return _getIterator($this);
    }
    @SuppressWarnings("rawtypes")
    private static Iterator _getIterator(Empty $this){
        return emptyIterator_.getEmptyIterator$();
    }

    public java.lang.Object item(java.lang.Object key){
        return _item($this, key);
    }
    private static java.lang.Object _item(Empty $this, java.lang.Object key){
        return null;
    }

    public Empty segment(Integer from, long length) {
        return _segment($this, from, length);
    }
    private static Empty _segment(Empty $this, Integer from, long length) {
        return $this;
    }

    public Empty span(Integer from, Integer to) {
        return _span($this, from, to);
    }
    private static Empty _span(Empty $this, Integer from, Integer to) {
        return $this;
    }

    public long count(java.lang.Object element) {
        return _count($this, element);
    }
    private static long _count(Empty $this, java.lang.Object element) {
        return 0;
    }

    public boolean contains(java.lang.Object element) {
        return _contains($this, element);
    }
    private static boolean _contains(Empty $this, java.lang.Object element) {
        return false;
    }

    public boolean defines(Integer key) {
        return _defines($this, key);
    }
    private static boolean _defines(Empty $this, Integer key) {
        return false;
    }

    public Empty getClone() {
        return _getClone($this);
    }
    private static Empty _getClone(Empty $this) {
        return $this;
    }

    public Empty getSequence() {
        return _getSequence($this);
    }
    private static Empty _getSequence(Empty $this) {
        return $this;
    }

    public java.lang.String toString() {
        return _toString($this);
    }
    private static java.lang.String _toString(Empty $this) {
        return "{}";
    }

    public Integer getLastIndex(){
        return _getLastIndex($this);
    }
    private static Integer _getLastIndex(Empty $this){
        return null;
    }

    public Integer getFirst(){
        return _getFirst($this);
    }
    private static Integer _getFirst(Empty $this){
        return null;
    }

    public Integer getLast(){
        return _getLast($this);
    }
    private static Integer _getLast(Empty $this){
        return null;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <Other> Sequence withLeading(Other e) {
        return new ArraySequence<Other>(e);
    }
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <Element,Other> Sequence _withLeading(Empty orig, Other e) {
        return new ArraySequence<Other>(e);
    }
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <Other> Sequence withTrailing(Other e) {
        return new ArraySequence<Other>(e);
    }
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <Element,Other> Sequence _withTrailing(Empty orig, Other e) {
        return new ArraySequence<Other>(e);
    }

}
