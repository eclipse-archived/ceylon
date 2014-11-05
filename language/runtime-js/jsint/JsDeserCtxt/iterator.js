function(){
  var idx=0;
  var dc$=this;
  return for$iter(function(){
    if (dc$.refs.length<idx) {
      idx++;
      return dc$.refs[idx-1];
    }
    return getFinished();
  },{Element$Iterator:{t:Reference$serialization,a:{t:Anything}}});
}
