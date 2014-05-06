atr$(FunctionModel$meta$model.$$.prototype,'parameterTypes',function(){
  var ps=this.tipo.$crtmm$.$ps;
  if (!ps || ps.length==0)return getEmpty();
  var r=[];
  for (var i=0; i < ps.length; i++) {
    var pt=ps[i].$t;
    if (typeof(pt)==='string'){
      if (!this.$targs)throw TypeApplicationException$meta$model(String$("This function model needs type parameters"));
      pt=this.$targs[pt];
      if (!pt)throw TypeApplicationException$meta$model(String$("Function model is missing type argument for <" + ps[i].$t + ">"));
    }
    r.push(typeLiteral$meta({Type$typeLiteral:pt}));
  }
  return ArraySequence(r,{Element$Iterable:{t:Type$meta$model,a:{t:Anything}}});
},undefined,function(){return{mod:$CCMM$,$cont:FunctionModel$meta$model,d:['ceylon.language.meta.model','FunctionModel','$at','parameterTypes'],$t:{t:Sequential,a:{Element$Sequential:{t:Type$meta$model,a:{Type:{t:Anything}}},Absent:{t:Null}}}};});
atr$(FunctionModel$meta$model.$$.prototype,'typeArguments',function(){
  var mm = this.tipo.$crtmm$;
  if (mm) {
    if (mm.$tp) {
      if (this.$targs===undefined)throw TypeApplicationException$meta$model("Missing type arguments for "+this.string);
      var targs={};
      for (var tp in mm.$tp) {
        var param = OpenTypeParam(this.tipo,tp);
        var targ = this.$targs[tp];
        if (targ) {
          targ=typeLiteral$meta({Type$typeLiteral:targ});
        } else {
          targ=typeLiteral$meta({Type$typeLiteral:{t:Anything}});
        }
        targs[param]=targ;
      }
      return Mapita(targs,{K$Mapita:{t:TypeParameter$meta$declaration},V$Mapita:{t:Type$meta$model,a:{Type$Type:{t:Anything}}}});
    }
    return getEmpty();
  }
  throw Exception(String$("FunctionModel.typeArguments-we don't have a metamodel!"));
},undefined,function(){return{mod:$CCMM$,$t:{t:Map,a:{Key:{t:TypeParameter$meta$declaration},Item:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$cont:FunctionModel$meta$model,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Generic','$at','typeArguments']};});
atr$(FunctionModel$meta$model.$$.prototype,'string',function(){
  var mm=this.tipo.$crtmm$;
  var qn;
  if (mm.$cont) {
    qn=$qname(mm.$cont);
    if (mm.$cont.$crtmm$.$tp) {
      var cnt=this.$$targs$$&&this.$$targs$$.Container$Function&&this.$$targs$$.Container$Function.a;
      if (!cnt)cnt=this.$$targs$$&&this.$$targs$$.Container$Method&&this.$$targs$$.Container$Method.a;
      qn+="<";var first=true;
      for (var tp in mm.$cont.$crtmm$.$tp) {
        if (first)first=false;else qn+=",";
        var _ta=cnt&&cnt[tp];
        qn+=$qname(_ta||Anything);
      }
      qn+=">";
    }
    qn+="."+mm.d[mm.d.length-1];
  } else {
    qn=$qname(mm);
  }
  if (mm.$tp) {
    qn+="<";
    var first=true;
    for (var tp in mm.$tp) {
      if (first)first=false; else qn+=",";
      var targ=this.$targs[tp];
      if (targ.t) {
        var _m=getrtmm$$(targ.t);
        qn+=$qname(_m);
      } else {
        qn+=tp;
      }
    }
    qn+=">";
  }
  return String$(qn);
},undefined,function(){return{mod:$CCMM$,$t:{t:String$},d:['$','Object','$at','string'],$cont:FunctionModel$meta$model};});
