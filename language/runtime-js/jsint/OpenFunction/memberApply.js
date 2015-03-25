function memberApply(cont,types,$mptypes){
  var mm=this.tipo.$crtmm$;
  if (!(cont.tipo && extendsType({t:cont.tipo},{t:mm.$cont}))&&cont!==nothingType$meta$model())
    throw IncompatibleTypeException$meta$model("Incompatible Container type argument");
  if (!extendsType(mm.$t,$mptypes.Return$memberApply))throw IncompatibleTypeException$meta$model("Incompatible Return type argument");
  validate$params(mm.ps,$mptypes.Arguments$memberApply,"Wrong number of Arguments for memberApply");
  var ta={t:this.tipo};
  validate$typeparams(ta,mm.tp,types);
  return AppliedMethod$jsint(this.tipo,types,{Container$AppliedMethod:$mptypes.Container$memberApply,
    Type$AppliedMethod:mm.$t,Arguments$AppliedMethod:tupleize$params(mm.ps,ta.a)});
}
