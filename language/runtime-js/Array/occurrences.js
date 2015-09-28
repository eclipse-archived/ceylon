function(e,from,len){
  if (from===undefined||from<0)from=0;
  if (len===undefined)len=this.length;
  var r=[];
  if (e===null) {
    for (var i=from;i<len;i++) {
      if (this[i]===null)r.push(i);
    }
  } else {
    for (var i=from;i<len;i++) {
      if (this[i]!==null && this[i].equals(e))r.push(i);
    }
  }
  return r.length>0?r.rt$({t:Integer}):empty();
  
}
