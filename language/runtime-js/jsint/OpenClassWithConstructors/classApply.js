function(targs,$mptypes) {
  var mm=this.tipo.$m$;
  if (mm.tp) {
    var needed=Object.keys(mm.tp).length;
    if (!targs || targs.size!=needed)throw TypeApplicationException$meta$model("Not enough type arguments provided: "+(targs?targs.size:0)+", but requires exactly "+needed);
    //TODO generate targs
  }
  validate$params(mm.ps,$mptypes.Arguments$classApply,"Wrong number of Arguments for classApply");
  return this.$_apply(targs,{Type$apply:$mptypes.Type$classApply});
}
