function(targs,$mptypes) {
  var mm=this.tipo.$crtmm$;
  if (mm.tp) {
    if (!targs)throw TypeApplicationException$meta$model("This class requires type arguments");
    //TODO generate targs
  }
  validate$params(mm.ps,$mptypes.Arguments$classApply,"Wrong number of Arguments for classApply");
  return this.$_apply(targs,{Type$apply:$mptypes.Type$classApply});
}
