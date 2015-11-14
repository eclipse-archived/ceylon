function(cont,targs,$mptypes){
  var mm=this.tipo.$crtmm$;
  if (cont!==nothingType$meta$model() && !extendsType({t:cont.tipo},{t:mm.$cont}))
    throw IncompatibleTypeException$meta$model("Incompatible Container specified");
  var _t={t:this.tipo};
  if ($mptypes.Type$memberInterfaceApply.a)_t.a=$mptypes.Type$memberInterfaceApply.a;
  validate$typeparams(_t,mm.tp,targs);
  var rv=AppliedMemberInterface$jsint(this.tipo,
    {Container$AppliedMemberInterface:$mptypes.Container$memberInterfaceApply,Type$AppliedMemberInterface:_t});
  if (_t.a)rv.$targs=_t.a;
  return rv;
}
