function(id){
  var idx=this.ids.indexOf(id);
  if (idx<0) {
    throw AssertionError("cannot obtain reference to unregistered id: " + id);
  }
  var r=this.refs[idx];
  //ok a little less ugly now
  //This is for singletons in jsonlib
  if (r.realmpt_) {
    r.$$targs$$.Instance$Reference=r.realmpt_;
  }
  return r;
}
