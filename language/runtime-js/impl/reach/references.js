function references(o) {
  var isarr=is$(o,{t:$_Array});
  var idx1=-1,idx2=0;
  var refs = o.getT$all()[o.getT$name()].ser$refs$(o);
  return for$iter(function(){
    idx1++;
    if (refs.length>idx1) {
      return refs[idx1];
    } else if (isarr && idx2<o.size) {
      var r=ElementImpl$impl(idx2);
      idx2++;
      return r;
    }
    return finished();
  },{Element$Iterator:{t:ReachableReference$serialization}});
}
