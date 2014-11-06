function(){
  var idx=1;
  var sc$=this;
  return for$iter(function(){
    if (idx<sc$.refs.length) {
      idx++;
      return sc$.refs[idx-1];
    }
    return getFinished();
  },{Element$Iterator:{t:SerializableReference$serialization,a:{t:Anything}}});
}
