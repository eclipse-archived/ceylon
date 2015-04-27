function find$ann(cont,ant) {
  var _m=getrtmm$$(cont);
  if (!(_m && _m.an))return null;
  if (typeof(_m.an)==='function')_m.an=_m.an();
  for (var i=0; i < _m.an.length; i++) {
    if (is$(_m.an[i],{t:ant}))return _m.an[i];
  }
  return null;
}
//Find the real declaration of something from its model definition
function _findTypeFromModel(pkg,mdl,cont) {
  var mod = pkg.container;
  //TODO this is very primitive needs a lot of rules replicated from the JsIdentifierNames
  var nm = mdl.nm;
  var mt = mdl['mt'];
  if (mt === 'a' || mt === 'g' || mt === 'o' || mt === 's') {
    nm = '$prop$get' + nm[0].toUpperCase() + nm.substring(1);
  }
  if (cont) {
    var imm=getrtmm$$(cont);
    if (mt==='c'||mt==='i')nm=nm+'$'+imm.d[imm.d.length-1];
  }else if (pkg.suffix) {
    nm+=pkg.suffix;
  }
  var out=cont?cont.$$.prototype:mod.meta;
  var rv=out[nm];
  if (rv===undefined)rv=out['$_'+nm];
  if (rv===undefined){
    rv=out['$init$'+nm];
    if (typeof(rv)==='function')rv=rv();
  }
  return rv;
}
//Pass a {t:Bla} and get a FreeClass,FreeInterface,etc (OpenType).
//second arg is the container for the second argument, if any
function _openTypeFromTarg(targ,o) {
  if (targ.t==='u' || targ.t==='i') {
    var tl=[];
    for (var i=0; i < targ.l.length; i++) {
      var _ct=targ.l[i];
      if (typeof(_ct)==='string') {
        tl.push(OpenTvar$jsint(OpenTypeParam$jsint(o,_ct)));
      } else {
        tl.push(_ct.t?_openTypeFromTarg(_ct,o):_ct);
      }
    }
    return (targ.t==='u'?FreeUnion$jsint:FreeIntersection$jsint)(tl.rt$({t:OpenType$meta$declaration}));
  } else if (targ.t==='T') {
    var mm=getrtmm$$(Tuple);
    var lit = typeLiteral$meta({Type$typeLiteral:targ});
  } else {
    var mm=getrtmm$$(targ.t);
    var lit = typeLiteral$meta({Type$typeLiteral:targ.t});
  }
  if (targ.a && lit)lit._targs=targ.a;
  var mdl = get_model(mm);
  if (mdl.mt==='i') {
    return FreeInterface$jsint(lit);
  } else if (mdl.mt==='c' || mdl.mt==='o') {
    return FreeClass$jsint(lit);
  }
  console.log("Don't know WTF to return for " + lit + " metatype " + mdl.mt);
}
//Basically the same as clsparamtypes but for functions
function funparamtypes(fun) {
  var ps=fun.tipo.$crtmm$.ps;
  if (!ps || ps.length==0)return empty();
  var r=[];
  for (var i=0; i < ps.length; i++) {
    var pt=ps[i].$t;
    if (typeof(pt)==='string'){
      if (!fun.$targs)throw TypeApplicationException$meta$model("This function model needs type parameters: " +fun.string);
      pt=fun.$targs[pt];
      if (!pt)throw TypeApplicationException$meta$model("Function model is missing type argument for "
        + fun.string + "<" + ps[i].$t + ">");
    }
    r.push(typeLiteral$meta({Type$typeLiteral:pt},fun.$targs));
  }
  return r.length===0?empty():ArraySequence(r,{Element$ArraySequence:{t:Type$meta$model,a:{t:Anything}}});
}
//FunctionModel.string
function funmodstr$(fun) {
  var mm=fun.tipo.$crtmm$;
  var qn;
  if (mm.$cont) {
    qn=qname$(mm.$cont);
    if (mm.$cont.$crtmm$.tp) {
      var cnt=fun.$$targs$$&&fun.$$targs$$.Container$Function&&fun.$$targs$$.Container$Function.a;
      if (!cnt)cnt=fun.$$targs$$&&fun.$$targs$$.Container$Method&&fun.$$targs$$.Container$Method.a;
      qn+="<";var first=true;
      for (var tp in mm.$cont.$crtmm$.tp) {
        if (first)first=false;else qn+=",";
        var _ta=cnt&&cnt[tp];
        qn+=qname$(_ta||Anything);
      }
      qn+=">";
    }
    qn+="."+mm.d[mm.d.length-1];
  } else {
    qn=qname$(mm);
  }
  if (mm.tp) {
    qn+="<";
    var first=true;
    for (var tp in mm.tp) {
      if (first)first=false; else qn+=",";
      var targ=fun.$targs[tp];
      if (targ.t) {
        var _m=getrtmm$$(targ.t);
        qn+=qname$(_m);
      } else {
        qn+=tp;
      }
    }
    qn+=">";
  }
  return qn;
}
//Function.typeArguments
function funtypearg$(fun) {
  var mm = fun.tipo.$crtmm$;
  if (mm) {
    if (mm.tp) {
      if (fun.$targs===undefined)throw TypeApplicationException$meta$model("Missing type arguments for "+fun.string);
      var targs={};
      var ord=[];
      for (var tp in mm.tp) {
        var param = OpenTypeParam$jsint(fun.tipo,tp);
        var targ = fun.$targs[tp];
        if (targ) {
          targ=typeLiteral$meta({Type$typeLiteral:targ});
        } else {
          targ=typeLiteral$meta({Type$typeLiteral:{t:Anything}});
        }
        targs[param.qualifiedName]=[param,targ];
        ord.push(param.qualifiedName);
      }
      return TpMap$jsint(targs,ord,{V$TpMap:{t:Type$meta$model,a:{Type$Type:{t:Anything}}}});
    }
    return empty();
  }
  throw Exception("FunctionModel.typeArguments-we don't have a metamodel!");
}
//Function.typeArguments
function funtypeargl$(fun) {
  var mm = fun.tipo.$crtmm$;
  if (mm) {
    if (mm.tp) {
      if (fun.$targs===undefined)throw TypeApplicationException$meta$model("Missing type arguments for "+fun.string);
      var ord=[];
      for (var tp in mm.tp) {
        var targ = fun.$targs[tp];
        if (targ) {
          targ=typeLiteral$meta({Type$typeLiteral:targ});
        } else {
          targ=typeLiteral$meta({Type$typeLiteral:{t:Anything}});
        }
        ord.push(targ);
      }
      return ArraySequence(ord,{Element$ArraySequence:{t:Type$meta$model,a:{Target$Type:Anything}}});
    }
    return empty();
  }
  throw Exception("FunctionModel.typeArguments-we don't have a metamodel!");
}
function allann$(mm) {
  if (typeof(mm.an)==='function')mm.an=mm.an();
  var a=getAnnotationsForBitmask(mm.pa);
  if (a) {
    if (mm.an) {
      for (var i=0;i<mm.an.length;i++)a.push(mm.an[i]);
    }
  } else {
    a=mm.an;
  }
  return a||[];
}
function memberDeclaringType$($$member){
  var mm = getrtmm$$($$member.tipo);
  var m2 = get_model(mm);
  var _m = typeof(mm.mod)==='function'?mm.mod():mm.mod;
  return (m2['mt']==='c'?OpenClass$jsint:OpenInterface$jsint)(fmp$(_m['$mod-name'],_m['$mod-version'],mm.d[0]), $$member.tipo);
}

