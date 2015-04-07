function(e){
  if (e===null) {
    for (var i=0;i<this.length;i++) {
      if (this[i]===null)return true;
    }
  } else {
    for (var i=0;i<this.length;i++) {
      if (this[i]!==null && e.equals(this[i]))return true;
    }
  }
  return false;
}
