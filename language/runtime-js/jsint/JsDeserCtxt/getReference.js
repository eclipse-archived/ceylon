function(id){
  var idx=this.ids.indexOf(id);
  if (idx<0) {
    throw AssertionError("cannot obtain reference to unregistered id: " + id);
  }
  return this.refs[idx];
}
