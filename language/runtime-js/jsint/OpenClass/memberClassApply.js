function(cont,targs,$mptypes){
  var mm=this.tipo.$crtmm$;
  if (cont!==getNothingType$meta$model() && !extendsType({t:cont.tipo},{t:mm.$cont}))
    throw IncompatibleTypeException$meta$model("Incompatible Container specified");
  if (!extendsType({t:this.tipo},$mptypes.Type$memberClassApply))
    throw IncompatibleTypeException$meta$model("Incompatible Type specified");
  var _t={t:this.tipo};
  validate$typeparams(_t,mm.tp,targs);
  validate$params(mm.ps,$mptypes.Arguments$memberClassApply,"Wrong number of Arguments for classApply");
  var rv=AppliedMemberClass(this.tipo,{Container$MemberClass:{t:mm.$cont},Type$MemberClass:_t,Arguments$MemberClass:tupleize$params(mm.ps)});
  if (_t.a)rv.$targs=_t.a;
  return rv;
}
