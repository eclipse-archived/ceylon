function(e,from,len){
  var count=0;
  if (from===undefined||from<0)from=0;
  else if (from>=this.arr$.length)return 0;
  if (len===undefined)len=this.arr$.length-from;
  var lim=from+len;
  if(lim>this.arr$.length)lim=this.arr$.length;
  if (e===null) {
    for (var i=from;i<lim;i++) {
      if (this.arr$[i]===null) count++;
    }
  } else {
    for (var i=from;i<lim;i++) {
      if (this.arr$[i]!==null && e.equals(this.arr$[i])) count++;
    }
  }
  return count;
}
