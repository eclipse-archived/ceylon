function references(o) {
  var isarr=is$(o,{t:$_Array});
  var refs=o.getT$all()[getT$name()].ser$refs$();
  var i=0,ii=0;
  return for$iter(function(){
    if (i<refs.length) {
      var r=refs[i];
      i++;
      return r;
    } else if (isarr && ii<o.size) {
      var r=ElementImpl$impl(ii);
      ii++;
      return r;
    }
    return finished();
  },{Element$Iterator:{t:ReachableReference$serialization}});
}
