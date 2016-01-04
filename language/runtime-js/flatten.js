function flatten(tf, $$$mptypes) {
  if (tf.$unflattened$)return tf.$unflattened$;
  var tf_targs;
  if (tf.jsc$) {
    tf_targs=tf.$$targs$$;
    tf=tf.jsc$;
  } else if (tf.c2$) {
    tf_targs=tf.$$targs$$;
    tf=tf.c2$;
  }
  var mm=getrtmm$$(tf);
  var t0,iadic,argx;
  if (mm && mm.ps) {
    if (mm.ps.length===1) {
      t0=mm.ps[0].$t;
      if (!(t0.t===Sequential || t0.t===Sequence)) {
        var ps0t=mm.ps[0].$t;
        if (typeof(ps0t)==='string') {
          ps0t=tf_targs[ps0t];
        }
        t0=detpl$(ps0t);
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
          seqarg=seqarg.$sa$(seqarg._elemTarg());
        }
        if (argx===1&&t.length==0)return tf(seqarg);
        return tf(tpl$(t,seqarg));
      }
    } else {
      rf=function fft1(){
        var t=[];
        if (arguments.length===1 && is$(arguments[0],t0)) {
          //Spread tuple
          return tf(arguments[0]);
        } else if (arguments.length===1 && argx===1 && is$(arguments[0],{t:Sequential,a:{Element$Sequential:t0.l[0]}})) {
          //This definitely stinks
          return tf(arguments[0]);
        } else if (arguments.length>0){
          for (var i=0;i<argx;i++) {
            t.push(arguments[i]);
          }
        }
        if (t.length===1 && t0.l && t0.l.length===1 && is$(t[0],{t:Sequential}) && (t0.l[0].t===Sequence||t0.l[0].t===Sequential||t0.l[0].t===Tuple||t0.l[0].t==='T')) {
          //special case: 1-arg with tuple against 1-param 1x tuple with 1 Sequential element
          return tf(t[0]);
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
      if (s===empty()||is$(s,{t:Tuple}))return tf(s,$mpt);
      if (is$(s,{t:ArraySequence})){
        //This is awful but it's the only way to get the array directly out of its sequence
        //Don't forget to update the property name when it changes
        return tf(tpl$(s.$g1.arr$),$mpt);
      }
      if (is$(s,{t:$_Array}))return tf(tpl$(s.arr$),$mpt);
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
