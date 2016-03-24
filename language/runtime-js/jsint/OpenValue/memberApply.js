function(cont,$mptypes) {
  var mm=getrtmm$$(this.tipo);
  if (!(cont.tipo && extendsType({t:cont.tipo},{t:mm.$cont}))&&cont!==nothingType$meta$model())
    throw IncompatibleTypeException$meta$model("Incompatible Container type argument");
  if (!extendsType(mm.$t,$mptypes.Get$memberApply))throw IncompatibleTypeException$meta$model("Incompatible Get type argument");
  if (!extendsType($mptypes.Set$memberApply,this.tipo.set?mm.$t:{t:Nothing}))throw IncompatibleTypeException$meta$model("Incompatible Set type argument");
  return AppliedAttribute(this.meta.nm,this.tipo,{Get$Attribute:$mptypes.Get$apply,Set$Attribute:$mptypes.Set$apply,
    Container$Attribute:$mptypes.Container$memberApply});
}
