function(){
  var idx=1;
  var dc$=this;
  return for$iter(function(){
    if (idx<dc$.refs.length) {
      idx++;
      return dc$.refs[idx-1];
    }
    return finished();
  },{Element$Iterator:{t:Reference$serialization,a:{t:Anything}}});
}
