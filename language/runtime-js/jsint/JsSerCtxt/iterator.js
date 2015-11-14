function(){
  var idx=1;
  var sc$=this;
  return for$iter(function(){
    if (idx<sc$.refs.length) {
      idx++;
      return sc$.refs[idx-1];
    }
    return finished();
  },{Element$Iterator:{t:SerializableReference$serialization,a:{t:Anything}}});
}
