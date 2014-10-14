function(id,inst,$mpt){
  var cur=this.instances.indexOf(inst);
  if (cur >= 0) {
    throw AssertionError('A different instance has already been registered with id '+id.string+': "'
                         +(cur?cur.string:"null")+'", "'+(inst?inst.string:"null"));
  }
  cur=this.instances.length;
  var prev=this.refs[cur];
  if (prev) {
    var pi=prev.instance();
    throw AssertionError('A different instance has already been registered with id '+id.string+': "'
                         +(pi?pi.string:"null")+'", "'+(inst?inst.string:"null"));
  }
  this.instances[cur]=inst;
  var ref=SerRefImpl$jsint(this,id,inst,{Instance$SerRefImpl:$mpt.Instance$reference});
  this.refs[cur]=ref;
  return ref;
}
