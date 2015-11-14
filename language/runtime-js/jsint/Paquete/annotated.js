function($mpt){
  var _k = '$pkg$ans$' + this.name.replace(/\./g,'$');
  var anns = this.container.meta[_k];
  if (typeof(anns) === 'function') {
    anns = anns();
    this.container.meta[_k]=anns;
  } else if (anns === undefined) {
    return false;
  }
  for (var i=0; i<anns.length; i++) {
    var an = anns[i];
    if (is$(an, $mpt.Annotation$annotated))return true;
  }
  return false;
}
