defineAttr(FunctionModel$meta$model.$$.prototype,'parameterTypes',function(){
  var ps=this.tipo.$$metamodel$$.$ps;
  if (!ps || ps.length==0)return getEmpty();
  var r=[];
  for (var i=0; i < ps.length; i++) {
    var pt=ps[i].$t;
    if (typeof(pt)==='string'){
      if (!this.$targs)throw TypeApplicationException$meta$model(String$("This function model needs type parameters"));
      pt=this.$targs[pt];
      if (!pt)throw TypeApplicationException$meta$model(String$("Function model is missing type argument for <" + ps[i].$t + ">"));
    }
    r.push(typeLiteral$meta({Type:pt}));
  }
  return r.reifyCeylonType({Element:{t:Type$meta$model,a:{t:Anything}},Absent:{t:Null}});
},undefined,function(){return{mod:$$METAMODEL$$,$cont:FunctionModel$meta$model,d:['ceylon.language.meta.model','FunctionModel','$at','parameterTypes'],$t:{t:Sequential,a:{Element:{t:Type$meta$model,a:{Type:{t:Anything}}},Absent:{t:Null}}}};});
defineAttr(FunctionModel$meta$model.$$.prototype,'typeArguments',function(){
  var mm = this.tipo.$$metamodel$$;
  if (mm) {
    if (mm.$tp) {
      if (this.$targs===undefined)throw TypeApplicationException$meta$model("Missing type arguments for "+this.string);
      var targs=[];
      for (var tp in mm.$tp) {
        var param = OpenTypeParam(this.tipo,tp);
        var targ = this.$targs[tp];
        if (targ) {
          targ=typeLiteral$meta({Type:targ});
        } else {
          targ=typeLiteral$meta({Type:{t:Anything}});
        }
        targs.push(Entry(param,targ,{Key:{t:TypeParameter$meta$declaration},Item:{t:Type$meta$model,a:{Type:{t:Anything}}}}));
      }
      return LazyMap(targs.reifyCeylonType({Absent:{t:Null},Element:{t:Entry,a:{Key:{t:TypeParameter$meta$declaration},Item:{t:Type$meta$model,a:{Type:{t:Anything}}}}}}), {Key:{t:TypeParameter$meta$declaration},Item:{t:Type$meta$model,a:{Type:{t:Anything}}}});
    }
    return getEmpty();
  }
  throw Exception(String$("FunctionModel.typeArguments-we don't have a metamodel!"));
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Map,a:{Key:{t:TypeParameter$meta$declaration},Item:{t:Type$meta$model,a:{Type:{t:Anything}}}}},$cont:FunctionModel$meta$model,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','Generic','$at','typeArguments']};});
defineAttr(FunctionModel$meta$model.$$.prototype,'string',function(){
  var mm=this.tipo.$$metamodel$$;
  var qn;
  if (mm.$cont) {
    qn=$qname(mm.$cont);
    if (mm.$cont.$$metamodel$$.$tp) {
      var cnt=this.$$targs$$&&this.$$targs$$.Container&&this.$$targs$$.Container.a;
      qn+="<";var first=true;
      for (var tp in mm.$cont.$$metamodel$$.$tp) {
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
        if (typeof(targ.t.$$metamodel$$)==='function')targ.t.$$metamodel$$=targ.t.$$metamodel$$();
        qn+=$qname(targ.t.$$metamodel$$);
      } else {
        qn+=tp;
      }
    }
    qn+=">";
  }
  return String$(qn);
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','Object','$at','string'],$cont:FunctionModel$meta$model};});
