function($mpt){
  var anns = this.meta.$mod$ans$;
  if (typeof(anns) === 'function') {
    anns = anns();
    this.meta.$mod$ans$=anns;
  }
  if (anns) for (var i=0; i < anns.length; i++) {
    if (is$(anns[i],$mpt.Annotation$annotated))return true;
  }
  return false;
}
