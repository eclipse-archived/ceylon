function(){
  return this.length === 0 ? getEmptyIterator() : StringIterator(this);
}
