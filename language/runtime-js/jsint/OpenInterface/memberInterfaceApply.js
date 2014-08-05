function(cont,targs,$mptypes){
  var mm=this.tipo.$crtmm$;
  if (cont!==getNothingType$meta$model() && !extendsType({t:cont.tipo},{t:mm.$cont}))
    throw IncompatibleTypeException$meta$model("Incompatible Container specified");
  var _t={t:this.tipo};
  validate$typeparams(_t,mm.tp,targs);
  var rv=AppliedMemberInterface(this.tipo,{Container$MemberInterface:{t:mm.$cont},Type$MemberInterface:_t});
  if (_t.a)rv.$targs=_t.a;
  return rv;
}
