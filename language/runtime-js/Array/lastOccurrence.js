function(e){
  if (e===null) {
    for (var i=this.length-1;i>=0;i--) {
      if (this[i]===null)return i;
    }
  } else {
    for (var i=this.length-1;i>=0;i--) {
      if (this[i]!==null && e.equals(this[i]))return i;
    }
  }
  return null;
}
