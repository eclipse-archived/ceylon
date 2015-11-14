function(id,ref){
  var idx=this.ids.indexOf(id);
  var or=null;
  if (idx>=0) {
    or=this.refs[idx];
  } else {
    idx=this.ids.length;
    this.ids[idx]=id;
  }
  this.refs[idx]=ref;
  return or;
}
