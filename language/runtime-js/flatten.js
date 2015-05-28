function flatten(tf, $$$mptypes) {
  if (tf.$unflattened$)return tf.$unflattened$;
  if (tf.jsc$)tf=tf.jsc$;
  var mm=getrtmm$$(tf);
  var t0,iadic,argx;
  if (mm && mm.ps) {
    if (mm.ps.length===1) {
      t0=mm.ps[0].$t;
      iadic=variadicness(t0);
      argx = t0.t==='T' ? t0.l.length : 1;
    } else {
      throw new TypeError("Invalid argument to flatten: " +tf);
    }
  } else {
    throw new TypeError("Missing metamodel for " + tf);
  }
  var rf;
  if (t0.t==='T') {
    //Tuple
    rf=function rtf(){
      var argc = arguments.length;
      var mptypes = argc>argx ? arguments[argc-1] : undefined;
      if (mptypes)argc--;
      var t = [];
      if (iadic)argc--;
      for (var i=0;i<argx-(iadic?1:0);i++) {
        t.push(arguments[i]);
      }
      if (iadic) {
        var seqarg=arguments[argx-1];
        if (seqarg===undefined || seqarg.length===0) {
          seqarg=empty();
        } else if (seqarg !== null && !is$(seqarg,{t:Sequence})) {
          seqarg=ArraySequence(seqarg,{Element$ArraySequence:seqarg._elemTarg()});
        }
        if (argx===1&&t.length==0)return tf(seqarg);
      }
      return tf(tpl$(t,undefined,seqarg));
    };
    rf.$crtmm$={$t:mm.$t,ps:[]};
    for(var i=0;i<t0.l.length;i++){
      rf.$crtmm$.ps.push({$t:t0.l[i]});
    }
  } else {
    //Single variadic argument
    rf=function rfs(s,$mpt){
      return tf(s?tpl$(s):empty(),$mpt);
    };
    rf.$crtmm$={$t:mm.$t,ps:[{$t:{t:t0.t,a:t0.a,seq:iadic}}]};
  }
  rf.$$targs$$={Return$Callable:$$$mptypes.Return$flatten,Arguments$Callable:$$$mptypes.Args$flatten};
  rf.$flattened$=tf;
  return rf;
}
