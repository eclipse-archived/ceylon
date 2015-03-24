function($mpt){
  var anns = this.meta.$mod$ans$;
  if (typeof(anns) === 'function') {
    anns = anns();
    this.meta.$mod$ans$=anns;
  } else if (anns === undefined) {
    return false;
  }
  for (var i=0; i < anns.length; i++) {
    var an = anns[i];
    if (is$(an, $$$mptypes.Annotation$annotations))return true;
  }
  return false;
}
