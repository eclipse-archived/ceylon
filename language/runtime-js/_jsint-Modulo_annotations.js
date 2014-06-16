function annotations($$$mptypes){
  var anns = this.meta.$mod$ans$;
  if (typeof(anns) === 'function') {
    anns = anns();
    this.meta.$mod$ans$=anns;
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
