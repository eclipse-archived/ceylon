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
//Function.typeArgumentWithVariance
function funtypeargv$(fun) {
  throw Exception("FunctionModel.typeArgumentWithVariances not implemented");
}
//Function.typeArgumentList
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
//Function.typeArgumentWithVarianceList
function funtypeargvl$(fun) {
  throw Exception("FunctionModel.typeArgumentWithVarianceList not implemented");
}
