function(){
  var idx=0;
  var sc$=this;
  return for$iter(function(){
    if (sc$.refs.length<idx) {
      idx++;
      return sc$.refs[idx-1].ref;
    }
    return getFinished();
  },{Element$Iterator:{t:SerializableReference$serialization,a:{t:'u',l:[{t:$_Object},{t:Null}]}});
}
