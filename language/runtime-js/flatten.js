function flatten(tf, $$$mptypes) {
  if (tf.$unflattened$)return tf.$unflattened$;
  if (tf.jsc$)tf=tf.jsc$;
  var mm=getrtmm$$(tf);
  var t0,iadic,argx;
  if (mm && mm.ps) {
    if (mm.ps.length===1) {
      t0=mm.ps[0].$t;
      if (!(t0.t===Sequential || t0.t===Sequence)) {
        t0=detpl$(mm.ps[0].$t);
      }
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
    if (iadic) {
      rf=function fftv(){
        var t = [];
        for (var i=0;i<argx-1;i++) {
          t.push(arguments[i]);
        }
        var seqarg=arguments[argx-1];
        if (seqarg===undefined || seqarg.length===0) {
          seqarg=empty();
        } else if (seqarg !== null && !is$(seqarg,{t:Sequence})) {
          seqarg=ArraySequence(seqarg,{Element$ArraySequence:seqarg._elemTarg()});
        }
        if (argx===1&&t.length==0)return tf(seqarg);
        return tf(tpl$(t,undefined,seqarg));
      }
    } else {
      rf=function fft1(){
        var t=[];
        if (arguments.length===1 && argx>1 && is$(arguments[0],{t:Sequential}) && arguments[0].size===argx) {
          //Because of spread we could get a Tuple here
          if (is$(arguments[0],{t:Tuple})) {
            return tf(arguments[0]);
          }
          for (var i=0;i<argx;i++) {
            t.push(arguments[0].$_get(i));
          }
        } else {
          for (var i=0;i<argx;i++) {
            t.push(arguments[i]);
          }
        }
        return tf(tpl$(t));
      }
    }
    rf.$crtmm$={$t:mm.$t,ps:[]};
    for(var i=0;i<t0.l.length;i++){
      rf.$crtmm$.ps.push({$t:t0.l[i],mt:'prm',nm:'flat'+i});
      if (t0.l[i].seq)rf.$crtmm$.ps[i].$t.seq=t0.l[i].seq;
    }
  } else {
    //Single variadic argument
    rf=function ffs(s,$mpt){
      return tf(s?tpl$(s):empty(),$mpt);
    };
    rf.$crtmm$={$t:mm.$t,ps:[{$t:{t:t0.t},nm:'flat#0',seq:iadic}]};
    if (t0.a)rf.$crtmm$.ps[0].$t.a=t0.a;
    if (extendsType(t0,{t:Empty}))rf.$crtmm$.ps=[];
  }
  rf.$$targs$$={Return$Callable:$$$mptypes.Return$flatten,Arguments$Callable:$$$mptypes.Args$flatten};
  rf.$flattened$=tf;
  return rf;
}
