function(){
  return this.length === 0 ? emptyIterator() : StringIterator(this);
}
