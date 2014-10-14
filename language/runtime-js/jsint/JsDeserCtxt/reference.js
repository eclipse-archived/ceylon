function(id,model,$mpt){
  var idx=this.ids.indexOf(id);
  if (idx>=0) {
    var ref=this.refs[idx];
    if (ref.clazz.equals(model))return ref;
    throw AssertionError("reference already made to instance with a different class");
  }
  if (model.declaration.abstract)throw AssertionError("class is abstract: " + model.string);
  return DeserRefImpl(this, id, model, {Instance$DeserRefImpl:$mpt.Instance$reference});
}
