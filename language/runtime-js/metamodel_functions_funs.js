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
  return r.$sa$({t:Type$meta$model,a:{t:Anything}});
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
        if (_ta && _ta.dv)qn+=_ta.dv+' ';
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
function _funtypearg_$(fun,makeItem,maptarg){
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
        targs[param.qualifiedName]=[param,makeItem(fun,tp,targ)];
        ord.push(param.qualifiedName);
      }
      return TpMap$jsint(targs,ord,{V$TpMap:maptarg});
    }
    return empty();
  }
  throw Exception("FunctionModel.typeArguments-we don't have a metamodel!");
}
function funtypearg$(fun) {
  return _funtypearg_$(fun,function(c,t,a){return a;},
    {t:Type$meta$model,a:{Type$Type:{t:Anything}}});
}
//Function.typeArgumentWithVariance
function funtypeargv$(fun) {
  return _funtypearg_$(fun,function(c,t,a){
    var iance,usv=fun.tipo.$crtmm$.tp[t].uv;
    if (usv==='out'){
      iance=covariant$meta$declaration();
    } else if (usv==='in'){
      iance=contravariant$meta$declaration();
    } else {
      iance=invariant$meta$declaration();
    }
    return tpl$([a,iance]);
  },{t:'T',l:[{t:Type$meta$model,a:{Type$Type:{t:Anything}}},{t:Variance$meta$declaration}]});
}
//Function.typeArgumentList
function _funtypeargl_$(fun,makeItem,listarg){
  var mm = fun.tipo.$crtmm$;
  if (mm) {
    if (mm.tp) {
      if (fun.$targs===undefined)throw TypeApplicationException$meta$model("Missing type arguments for "+fun.string);
      var ord=[];
      for (var tp in mm.tp) {
        var targ,_targ = fun.$targs[tp];
        if (_targ) {
          targ=typeLiteral$meta({Type$typeLiteral:_targ});
        } else {
          targ=typeLiteral$meta({Type$typeLiteral:{t:Anything}});
        }
        ord.push(makeItem(fun,_targ||0,targ));
      }
      return ord.$sa$(listarg);
    }
    return empty();
  }
  throw Exception("FunctionModel.typeArguments-we don't have a metamodel!");
}
function funtypeargl$(fun) {
  return _funtypeargl_$(fun,function(c,t,a){return a;},
    {t:Type$meta$model,a:{Target$Type:Anything}});
}
//Function.typeArgumentWithVarianceList
function funtypeargvl$(fun) {
  return _funtypeargl_$(fun,function(c,t,a){
    var iance;
    if (t.uv==='out')iance=covariant$meta$declaration();
    else if (t.uv==='in')iance=contravariant$meta$declaration();
    else iance=invariant$meta$declaration();
    return tpl$([a,iance]);
  },{t:'T',l:[{t:Type$meta$model,a:{Target$Type:Anything}},{t:Variance$meta$declaration}]});
}
