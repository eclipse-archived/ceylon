function(e,from,len){
  if (from===undefined||from<0)from=0;
  else if (from>this.arr$.length)return null;
  if (len===undefined)len=this.arr$.length-from;
  var end=this.arr$.length-len-from;
  if (end<0)end=0;
  var start=this.arr$.length-from-1;
  if (start>=this.arr$.length)start=this.arr$.length-1;
  if (e===null) {
    for (var i=start;i>=end;i--) {
      if (this.arr$[i]===null)return i;
    }
  } else {
    for (var i=start;i>=end;i--) {
      if (this.arr$[i]!==null && $eq$(e,this.arr$[i]))return i;
    }
  }
  return null;
}
