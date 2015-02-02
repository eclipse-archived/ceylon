function(id,inst,$mpt){
  //Check that the id hasn't been used
  for (var i=1;i<this.refs.length;i++){
    if (id.equals(this.refs[i].id)){
      var cur=this.refs[i].instance();
      throw AssertionError('A different instance has already been registered with id '+id.string+': "'
                         +(cur?cur.string:"null")+'", "'+(inst?inst.string:"null")+'"');
    }
  }
  //Check that the instance hasn't been registered with a different id
  var cur=this.instances.indexOf(inst);
  if (cur>0 && inst !== null) {
    throw AssertionError('The instance "'+(inst?inst.string:"null")+'" has already been registered with id '+id.string);
  }
  cur=this.instances.length;
  var pi=undefined;
  if (this.refs.length!==this.instances.length) {
    var prev=this.refs[cur];
    var pi=prev.instance();
  }
  if (pi===undefined) {
    for (var i=1;i<this.refs.length;i++) {
      if (this.refs[i].id.equals(id)) {
        pi=this.refs[i].instance();
        break;
      }
    }
  }
  if (pi!==undefined) {
    throw AssertionError('A different instance has already been registered with id '+id.string+': "'
                         +(pi?pi.string:"null")+'", "'+(inst?inst.string:"null")+'"');
  }
  var ref=SerRefImpl$jsint(this,id,inst,{Instance$SerRefImpl:$mpt.Instance$reference});
  this.instances[cur]=inst;
  this.refs[cur]=ref;
  return ref;
}
