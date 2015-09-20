function(e){
  var count=0;
  if (e===null) {
    for (var i=0;i<this.length;i++) {
      if (this[i]===null) count++;
    }
  } else {
    for (var i=0;i<this.length;i++) {
      if (this[i]!==null && e.equals(this[i])) count++;
    }
  }
  return count;
}
