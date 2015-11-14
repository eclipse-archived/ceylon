function($$$mptypes){
  var mm=getrtmm$$(this.tipo);
  if (!extendsType(mm.$t,$$$mptypes.Get$apply))throw IncompatibleTypeException$meta$model("Incompatible Get type argument");
  if (!extendsType($$$mptypes.Set$apply,this.tipo.set?mm.$t:{t:Nothing}))throw IncompatibleTypeException$meta$model("Incompatible Set type argument");
  return AppliedValue$jsint(undefined,this.tipo,{Get$Value:$$$mptypes.Get$apply,Set$Value:$$$mptypes.Set$apply});
}
