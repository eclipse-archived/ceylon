function getFromLast(i){
  if (i<0 || i>=this.length)
    return null;
  return this.elementAt(this.length-1-i);
}
