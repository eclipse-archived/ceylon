function(e){
  if (e===null) {
    for (var i=0;i<this.length;i++) {
      if (this[i]===null)return i;
    }
  } else {
    for (var i=0;i<this.length;i++) {
      if (this[i]!==null && e.equals(this[i]))return i;
    }
  }
  return null;
}
