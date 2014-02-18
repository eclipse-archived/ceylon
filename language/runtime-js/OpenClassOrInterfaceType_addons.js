defineAttr(OpenClassOrInterfaceType$meta$declaration.$$.prototype,'typeArguments',function(){
  var tps=this.declaration.tipo.$$metamodel$$.$tp;
  if (tps) {
    var rtps = this.declaration._targs;
    var targs={};
    for (var tpn in tps) {
      var rtp=rtps&&rtps[tpn];
      var otp=OpenTypeParam(this.declaration.tipo,tpn);
      var targ;
      if (rtp===undefined) {
        targ = OpenTvar(otp);
      } else if (typeof(rtp)==='string') {
        targ = OpenTvar(OpenTypeParam(this.declaration.tipo,rtp));
      } else {
        if (rtp.t==='i'||rtp.t==='u') {
          //resolve case types
          var nrtp={t:rtp.t,l:[]};
          for (var i=0;i<rtp.l.length;i++) {
            var _ct=rtp.l[i];
            nrtp.l.push(typeof(_ct)==='string'?OpenTvar(OpenTypeParam(this.declaration.tipo,_ct)):_ct);
          }
          rtp=nrtp;
        }
        targ = _openTypeFromTarg(rtp);
      }
      targs[otp]=targ;
    }
    return Mapita(targs,{K$Mapita:{t:TypeParameter$meta$declaration},V$Mapita:{t:OpenType$meta$declaration}});
  }
  return getEmpty();
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Map,a:{Key:{t:TypeParameter$meta$declaration},Item:{t:OpenType$meta$declaration}}},$cont:OpenClassOrInterfaceType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenClassOrInterfaceType','$at','typeArguments']};});
defineAttr(OpenClassOrInterfaceType$meta$declaration.$$.prototype,'extendedType',function(){
  return this.declaration.extendedType;
},undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:OpenClassType$meta$declaration}]},$cont:OpenClassOrInterfaceType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenClassOrInterfaceType','$at','extendedType']};});
defineAttr(OpenClassOrInterfaceType$meta$declaration.$$.prototype,'declaration',function(){return this._decl;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:ClassDeclaration$meta$declaration},$cont:OpenClassOrInterfaceType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenClassOrInterfaceType','$at','declaration']};});
defineAttr(OpenClassOrInterfaceType$meta$declaration.$$.prototype,'satisfiedTypes',function(){
  return this.declaration.satisfiedTypes;
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:OpenInterfaceType$meta$declaration}}},$cont:OpenClassOrInterfaceType$meta$declaration,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenClassOrInterfaceType','$at','satisfiedTypes']};});
defineAttr(OpenClassOrInterfaceType$meta$declaration.$$.prototype,'string',function(){
  var s=this.declaration.string;
  var tps=this.declaration.tipo.$$metamodel$$.$tp;
  if (tps) {
    var rtps=this.declaration._targs;
    s+="<";
    var first=true;
    for (var t in tps) {
      var rtp=rtps&&rtps[t];
      if (first)first=false;else s+=",";
      if (rtp===undefined) {
        s+=t;
      } else {
        s+=_openTypeFromTarg(rtp).string;
      }
    }
    s+=">";
  }
  return s;
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','Object','$at','string']};});
