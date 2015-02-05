function(id){
  var idx=this.ids.indexOf(id);
  if (idx>0) {
    return this.refs[idx].leak();
  }
  throw AssertionError("cannot obtain reference to unregistered id: " + id.string);
}
