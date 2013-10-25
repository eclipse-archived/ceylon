//Addendum to ClassModel
defineAttr(ClassModel$meta$model.$$.prototype,'parameterTypes',function(){
  var ps=this.tipo.$$metamodel$$.$ps;
  if (!ps || ps.length==0)return getEmpty();
  var r=[];
  for (var i=0; i < ps.length; i++) {
    var pt=ps[i].$t;
    if (typeof(pt)==='string'){
      if (!this.$targs)throw TypeApplicationException$meta$model(String$("This class model needs type parameters"));
      pt=this.$targs[pt];
      if (!pt)throw TypeApplicationException$meta$model(String$("Class model is missing type argument for <" + ps[i].$t + ">"));
    }
    r.push(typeLiteral$meta({Type:pt}));
  }
  return r.reifyCeylonType({Element:{t:Type$meta$model,a:{t:Anything}},Absent:{t:Null}});
},undefined,function(){return{mod:$$METAMODEL$$,$cont:ClassModel$meta$model,d:['ceylon.language.meta.model','ClassModel','$at','parameterTypes'],$t:{t:Sequential,a:{Element:{t:Type$meta$model,a:{Type:{t:Anything}}},Absent:{t:Null}}}};});

defineAttr(ClassModel$meta$model.$$.prototype,'declaration',function(){
  var $$clase=this;
  if ($$clase._decl)return $$clase._decl;
  var mm = $$clase.tipo.$$metamodel$$;
  if (typeof(mm)==='function'){
    mm=mm();
    $$clase.tipo.$$metamodel$$=mm;
  }
  $$clase._decl = OpenClass(getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]), $$clase.tipo);
  return $$clase._decl;
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:ClassDeclaration$meta$declaration},$cont:ClassModel$meta$model,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.model','ClassModel','$at','declaration']};});
ClassModel$meta$model.$$.prototype.equals=function(o){
return isOfType(o,{t:AppliedClass}) && (o.tipo$2||o.tipo)==this.tipo && this.typeArguments.equals(o.typeArguments);
};
//TODO equals metamodel
