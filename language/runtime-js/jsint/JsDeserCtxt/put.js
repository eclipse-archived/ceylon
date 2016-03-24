function(id,ref){
  var idx=this.ids.indexOf(id);
  if (idx<0){
    idx=this.ids.length;
    this.ids[idx]=id;
  }
  this.refs[idx]=ref;
}
