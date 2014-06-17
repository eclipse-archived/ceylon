function annotations($$$mptypes){
  var _k = '$pkg$ans$' + this.name.replace(/\./g,'$');
  var anns = this.container.meta[_k];
  if (typeof(anns) === 'function') {
    anns = anns();
    this.container.meta[_k]=anns;
  } else if (anns === undefined) {
    anns = [];
  }
  var r = [];
  for (var i=0; i < anns.length; i++) {
    var an = anns[i];
    if (is$(an, $$$mptypes.Annotation$annotations)) r.push(an);
  }
  return r.length===0?getEmpty():ArraySequence(r,{Element$ArraySequence:$$$mptypes.Annotation$annotations});
}

