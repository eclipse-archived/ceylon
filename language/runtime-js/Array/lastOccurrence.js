function(e,to){
  if (to===undefined)to=this.length-1;
  if (e===null) {
    for (var i=to;i>=0;i--) {
      if (this[i]===null)return i;
    }
  } else {
    for (var i=to;i>=0;i--) {
      if (this[i]!==null && e.equals(this[i]))return i;
    }
  }
  return null;
}
