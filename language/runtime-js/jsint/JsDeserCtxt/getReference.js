function(id,mp$){
  var idx=this.ids.indexOf(id);
  if (idx<0) {
    return null;
  }
  var r=this.refs[idx];
  //and this is as ugly as it gets
  //This is for singletons in jsonlib
  if (mp$.Instance$getReference.t!==Nothing){
    r.$$targs$$.Instance$Reference=mp$.Instance$getReference;
  }
  return r;
}
