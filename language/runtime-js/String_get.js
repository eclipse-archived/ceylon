function get(i){
  if (i<0 || i>=this.length)
    return null;
  return this.elementAt(i);
}
