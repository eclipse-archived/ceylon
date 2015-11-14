function(id,model,$mpt){
  var idx=this.ids.indexOf(id);
  if (idx>=0) {
    var ref=this.refs[idx];
    if (ref.clazz.equals(model))return ref;
  }
  if (model.declaration.abstract)throw AssertionError("class is abstract: " + model.string);
  var vd=model.declaration.objectValue;
  var r;
  if (vd != null) {
    vd=vd.$_get();
    r=DeserRefImpl$jsint(this, id, model, null, {Instance$DeserRefImpl:$mpt.Instance$reference,Outer$DeserRefImpl:{t:Nothing}});
    r.deserialize=function(){return r;}
    r.inst_=vd;
    r.state_=3;
    r.realmpt_=getrtmm$$(getrtmm$$(vd).$t.t)['super'];
  } else {
    r=DeserRefImpl$jsint(this, id, model, null, {Instance$DeserRefImpl:$mpt.Instance$reference,Outer$DeserRefImpl:{t:Nothing}});
  }
  this.ids.push(id);
  this.refs.push(r);
  return r;
}
