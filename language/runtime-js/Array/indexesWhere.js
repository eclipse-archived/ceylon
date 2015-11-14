function(f){
  var r=[];
  for(var i=0;i<this.length;i++){
    var e=this[i];
    if (nn$(e)&&f(e))r.push(i);
  }
  return r.length?r.rt$({t:Integer}):empty();
}
