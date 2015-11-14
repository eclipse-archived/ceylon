function is$(obj,type,containers){
  if(type && type.t){
    if(type.t==='i'||type.t==='u'){
      return isOfTypes(obj, type);
    } else if (type.t===Finished) {
      return obj===finished();
    }
    if(obj===null||obj===undefined){
      return type.t===Null||type.t===Anything;
    }
    if(obj.getT$all===undefined || !obj.getT$all()){
      if(obj.$crtmm$){
        var _mm=getrtmm$$(obj);
        //We can navigate the metamodel
        if(_mm.d['mt']==='m'){
          if(type.t===Callable){
            //It's a callable reference
            if(type.a&&type.a.Return$Callable&&_mm['$t']){
              //Check if return type matches
              if(extendsType(_mm['$t'],type.a.Return$Callable)){
                if(type.a.Arguments$Callable&&_mm['ps']!==undefined){
                  var metaparams=_mm['ps'];
                  if(metaparams.length==0){
                    return type.a.Arguments$Callable.t === Empty;
                  }else{
                    //check if arguments match
                    var comptype=type.a.Arguments$Callable;
                    for(var i=0;i<metaparams.length;i++){
                      if(comptype.t!==Tuple||!extendsType(metaparams[i]['$t'],comptype.a.First$Tuple)){
                        return false;
                      }
                      comptype=comptype.a.Rest$Tuple;
                    }
                  }
                }
                return true;
              }
            }
          }
        }
      }
      return false;
    }
    //Native arrays with no reified type args are NOT Ceylon objects.
    if (obj.$$targs$$===undefined && Array.isArray(obj))return false;
    if (type.t==='T') {
      if (is$(obj,{t:Tuple})) {
          var last_type = type.l[type.l.length-1];
          if (type.l.length===obj.size || last_type.seq) {
            var lim=type.l.length; if (last_type.seq)lim--;
            for (var i=0;i<lim;i++) {
              if (!is$(obj.$_get(i),type.l[i]))return false;
            }
            //This is for tuples with sequenced tails
            if (last_type.seq==2 && obj.size<type.l.length) {
              return false;
            } else if (last_type.seq) {
              for (var i=lim;i<obj.size;i++) {
                if(!is$(obj.$_get(i),last_type)) {
                  return false;
                }
              }
            }
            return true;
          } else {
            return false;
          }
      } else {
        return false;
      }
    }
    type=value2type$(type);
    if(type.t.$$.T$name in obj.getT$all()){
      if(type.t==Callable&&!(obj.$$targs$$ && obj.$$targs$$.Return$Callable && obj.$$targs$$.Arguments$Callable)
          && getrtmm$$(obj)&&obj.$crtmm$.$t && obj.$crtmm$.ps!==undefined){
        //Callable with no $$targs$$, we can build them from metamodel
        add_type_arg(obj,'Return$Callable',obj.$crtmm$.$t);
        add_type_arg(obj,'Arguments$Callable',{t:'T',l:[]});
        for(var i=0;i<obj.$crtmm$.ps.length;i++){
          obj.$$targs$$.Arguments$Callable.l.push(obj.$crtmm$.ps[i].$t);
        }
        if (obj.$$targs$$.Arguments$Callable.l.length===0)obj.$$targs$$.Arguments$Callable={t:Empty};
      }
      if(type.a && obj.$$targs$$) {
        for(var i in type.a) {
          var cmptype=type.a[i];
          var tmpobj=obj;
          var iance=cmptype.uv||null;
          var _mm=getrtmm$$(type.t);
          if(!iance&&_mm&&_mm.tp&&_mm.tp[i])iance=_mm.tp[i].dv;
          if(iance===null) {
            //null means no i in _mm.tp
            //Type parameter may be in the outer type
            while(iance===null&&tmpobj.outer$!==undefined){
              tmpobj=tmpobj.outer$;
              var _tmpf = tmpobj.constructor.T$all[tmpobj.constructor.T$name];
              var _mmf = getrtmm$$(_tmpf);
              if(_mmf&&_mmf.tp&&_mmf.tp[i]){
                iance=_mmf.tp[i].dv;
              }
              if(iance===null&&_mmf&&_mmf['super']){
                //lookup the type parameter in the supertype
                var smm=getrtmm$$(_mmf['super'].t);
                if(smm&&smm.tp&&smm.tp[i])iance=smm.tp[i].dv;
              }
              if(iance===null&&_mmf&&_mmf.sts){
                var sats=_mmf.sts;
                for(var s=0;iance===null&&s<sats.length;s++){
                  var smm=getrtmm$$(sats[s].t);
                  if (smm&&smm.tp&&smm.tp[i])iance=smm.tp[i].dv;
                }
              }
            }
          }
          if(iance===null) {
            //if the type has a container it could be a method
            //in which case the type parameter could be defined there
            var _omm=_mm;
            while(iance===null&&_omm) {
              if(_omm.tp&&_omm.tp[i]!==undefined){
                iance=_omm.tp[i].dv;
                tmpobj=obj;
              }
              if(iance===null)_omm=getrtmm$$(_omm.$cont);
            }
          }
          if (iance === 'out') {
            if (!extendsType(tmpobj.$$targs$$[i], cmptype,true)) {
              return false;
            }
          } else if (iance === 'in') {
            if (!extendsType(cmptype, tmpobj.$$targs$$[i],true)) {
              return false;
            }
          } else if (iance === undefined) {
            var _targ=tmpobj.$$targs$$[i];
            if (!(_targ && _targ.t && (_targ.t.$$ || _targ.t==='i' || _targ.t==='u')))return false;
            if (_targ.t.$$) {
              if (cmptype && cmptype.t && cmptype.t.$$) {
                if (!(cmptype.t.$$.T$name && _targ.t.$$.T$name === cmptype.t.$$.T$name))return false;
              } else if (cmptype && cmptype.t && cmptype.t==='i') {
                //_targ must satisfy all types in cmptype
                if (cmptype.t!==_targ.t || !cmptype.l || cmptype.l.length!==_targ.l.length)return false;
                for (var i=0; i<_targ.l.length;i++) {
                  if (!extendsType(_targ.l[i],cmptype,true))return false;
                }
              } else if (cmptype && cmptype.t && cmptype.t==='u') {
                //_targ must satisfy at least one type in cmptype
                if (cmptype.t!==_targ.t || !cmptype.l || cmptype.l.length!==_targ.l.length)return false;
                for (var i=0; i<_targ.l.length;i++) {
                  if (!extendsType(_targ.l[i],cmptype,true))return false;
                }
              }
            } else {
              if (cmptype.t!==_targ.t || !cmptype.l || cmptype.l.length!==_targ.l.length)return false;
              for (var i=0; i<_targ.l.length;i++) {
                if (!extendsType(_targ.l[i],cmptype,true))return false;
              }
            }
          } else if (iance === null) {
            console.log("Possible missing metamodel for " + type.t.$$.T$name + "<" + i + ">");
          } else {
            console.log("Don't know what to do about variance '" + iance + "'");
          }
        }
      }
      //TODO If the object is a member of a type with type arguments,
      //check that the outer type arguments match
      if (containers) {
        var cnt=obj.outer$;
        if (cnt) {
          //Nested types, check all outers
          for (var i=0; i<containers.length; i++) {
            if (!is$(cnt,containers[i]))return false;
            cnt=cnt.outer$;
          }
        } else if (obj.$$targs$$) {
          //Method argument types
          for (var t in containers) {
            if (obj.$$targs$$[t] && containers[t] && !extendsType(obj.$$targs$$[t], containers[t], true))return false;
          }
        }
      }
      return true;
    }
  }
  return false;
}
function isOfTypes(obj, types) {
  if (obj===null) {
    for (var i=0; i < types.l.length; i++) {
      if(types.l[i].t===Null || types.l[i].t===Anything) return true;
      else if (types.l[i].t==='u') {
        if (isOfTypes(null, types.l[i])) return true;
      }
    }
    return false;
  }
  if (obj === undefined || obj.getT$all === undefined) { return false; }
  var unions = false;
  var inters = true;
  var _ints=false;
  var objTypes = obj.getT$all();
  for (var i = 0; i < types.l.length; i++) {
    var t = types.l[i];
    var partial = is$(obj, t);
    if (types.t==='u') {
      unions = partial || unions;
    } else {
      inters = partial && inters;
      _ints=true;
    }
  }
  return _ints ? inters||unions : unions;
}
//Tells whether t1 is a subtype of t2
//tparm indicates if the calculations are being done on type parameters
function extendsType(t1, t2,tparm) {
    if (t1 === undefined || t1.t === undefined || t1.t === Nothing || t2 === undefined || t2.t === undefined) {
      return true;//t2 === undefined;
    } else if (t2 && t2.t === Anything) {
      return true;
    } else if (t1 === null) {
      return t2.t === Null || t2.t === Anything;
    }
    if (t1.t === 'u' || t1.t === 'i') {
        if (t1.t==='i')removeSupertypes(t1.l);
        var unions = false;
        var inters = true;
        var _ints = false;
        for (var i = 0; i < t1.l.length; i++) {
            var partial = extendsType(t1.l[i],t2,tparm);
            if (t1.t==='u'&&!tparm) {
                unions = partial||unions;
            } else {
                inters = partial&&inters;
                _ints=true;
            }
        }
        return _ints ? inters||unions : unions;
    }
    if (t2.t === 'u' || t2.t === 'i') {
        if (t2.t==='i')removeSupertypes(t2.l);
        var unions = false;
        var inters = true;
        var _ints = false;
        for (var i = 0; i < t2.l.length; i++) {
            var partial = extendsType(t1, t2.l[i],tparm);
            if (t2.t==='u') {
                unions = partial||unions;
            } else {
                inters = partial&&inters;
                _ints=true;
            }
        }
        return _ints ? inters||unions : unions;
    }
    if (t1.t==='T') {
      if (t2.t==='T') {
        if (t1.l.length>=t2.l.length) {
          for (var i=0; i < t2.l.length;i++) {
            if (!extendsType(t1.l[i],t2.l[i],tparm))return false;
          }
          return true;
        } else return false;
      } else {
        t1=retpl$(t1);
      }
    } else if (t2.t==='T') {
      t2=retpl$(t2);
    }
    t1=value2type$(t1);
    for (t in t1.t.$$.T$all) {
        if (t === t2.t.$$.T$name || t === 'ceylon.language::Nothing') {
            if (t1.a && t2.a) {
                //Compare type arguments
                for (ta in t1.a) {
                    if (!extendsType(t1.a[ta], t2.a[ta],tparm)) return false;
                }
            }
            return true;
        }
    }
    return false;
}
ex$.is$=is$;
