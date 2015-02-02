function(id){
  var idx=this.ids.indexOf(id);
  if (idx<0) {
    throw AssertionError("cannot obtain reference to unregistered id: " + id);
  }
  var r=this.refs[idx];
  //what an ugly fucking hack.
  //This is for singletons in jsonlib
  if (r.$$targs$$.Instance$Reference.t===Anything) {
    r.$$targs$$.Instance$Reference=$$mp.Instance$getReference;
  }
  return r;
}
