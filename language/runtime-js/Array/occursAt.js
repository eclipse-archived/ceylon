function(i,e){
  if (i<0||i>=this.length)return false;
  if (e===null)return this[i]===null;
  return this[i]!==null && this[i].equals(e);
}
