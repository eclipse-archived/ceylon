function(e,from,len){
  if (from===undefined||from<0)from=0;
  if (len===undefined)len=this.arr$.length;
  var r=[];
  if (e===null) {
    for (var i=from;i<len;i++) {
      if (this.arr$[i]===null)r.push(i);
    }
  } else {
    for (var i=from;i<len;i++) {
      if (this.arr$[i]!==null && this.arr$[i].equals(e))r.push(i);
    }
  }
  return r.length>0?$arr$(r,{t:Integer}):empty();
  
}
