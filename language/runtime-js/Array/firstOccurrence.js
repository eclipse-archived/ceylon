function(e,from,len){
  if (from===undefined || from<0)from=0;
  else if (from>=this.arr$.length)return null;
  if (len===undefined)len=this.arr$.length-from;
  var lim=len+from;
  if(lim>this.arr$.length)lim=this.arr$.length;
  if (e===null) {
    for (var i=from;i<lim;i++) {
      if (this.arr$[i]===null)return i;
    }
  } else {
    for (var i=from;i<lim;i++) {
      if (this.arr$[i]!==null && e.equals(this.arr$[i]))return i;
    }
  }
  return null;
}
