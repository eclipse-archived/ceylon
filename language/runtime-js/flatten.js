function flatten(tf, $$$mptypes) {
  function rf() {
    var argc = arguments.length;
    var last = argc>0 ? arguments[argc-1] : undefined;
    var tlast = typeof(last)==='object';
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
    t=tpl$(t);
    return tf(t, t.$$targs$$);
  };
  rf.$$targs$$={Return$Callable:$$$mptypes.Return$flatten,Arguments$Callable:$$$mptypes.Args$flatten};
  return rf;
}
