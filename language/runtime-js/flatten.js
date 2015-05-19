function flatten(tf, $$$mptypes) {
  if (tf.$unflattened$)return tf.$unflattened$;
  if (tf.jsc$)tf=tf.jsc$;
  var mm=getrtmm$$(tf);
  function rf() {
    var argc = arguments.length;
    var last = argc>0 ? arguments[argc-1] : undefined;
    var tlast = last!=null && typeof(last)==='object';
    if (tlast && typeof(last.Args$flatten) === 'object' && (last.Args$flatten.t==='T'||typeof(last.Args$flatten.t) === 'function')) {
      argc--;
    } else if (tf.$$targs$$ && tlast) {
      var ks=Object.keys(tf.$$targs$$);
      var all=true;
      for (var i=0;i<ks.length;i++) {
        if (last[ks[i]]===undefined)all=false;
      }
      if (all)argc--;
    }
    var t = [];
    for (var i=0;i<argc;i++) {
      t.push(arguments[i]);
    }
    if (t.length===1 && is$(t[0],{t:Sequential}) &&mm&&mm.ps.length<=t[0].size){
      //It's already a Tuple
      return tf(t[0]);
    }
    return tf(tpl$(t));
  };
  if (mm) {
    rf.$crtmm$={$t:mm.$t,ps:[]};
    if (mm.ps.length===1 && mm.ps[0].$t.t==='T') {
      for(var i=0;i<mm.ps[0].$t.l.length;i++){
        rf.$crtmm$.ps.push(mm.ps[0].$t.l[i]);
      }
    }
  }
  rf.$$targs$$={Return$Callable:$$$mptypes.Return$flatten,Arguments$Callable:$$$mptypes.Args$flatten};
  rf.$flattened$=tf;
  return rf;
}
