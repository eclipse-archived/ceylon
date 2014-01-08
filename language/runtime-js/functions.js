var larger = Comparison("larger");
function getLarger() { return larger }
var smaller = Comparison("smaller");
function getSmaller() { return smaller }
var equal = Comparison("equal");
function getEqual() { return equal }

exports.getLarger=getLarger;
exports.getSmaller=getSmaller;
exports.getEqual=getEqual;
exports.$prop$getLarger={get:getLarger,$$metamodel$$:function(){return{mod:$$METAMODEL$$,d:['ceylon.language','larger'],$t:{t:Comparison}};}};
exports.$prop$getSmaller={get:getSmaller,$$metamodel$$:function(){return{mod:$$METAMODEL$$,d:['ceylon.language','smaller'],$t:{t:Comparison}};}};
exports.$prop$getEqual={get:getEqual,$$metamodel$$:function(){return{mod:$$METAMODEL$$,d:['ceylon.language','equal'],$t:{t:Comparison}};}};

//These are operators for handling nulls
function exists(value) {
  return value !== null && value !== undefined;
}
function nonempty(value) {
  return value !== null && value !== undefined && !value.empty;
}

function isOfType(obj, type) {
  if (type && type.t) {
    if (type.t==='i' || type.t==='u') {
      return isOfTypes(obj, type);
    } else if (type.t==='T') {
      type=$retuple(type);
    }
    if (obj===null || obj===undefined) {
      return type.t===Null || type.t===Anything;
    }
    if (obj.getT$all === undefined) {
      if (obj.$$metamodel$$) {
        var _mm = obj.$$metamodel$$;
        if (typeof(_mm)==='function') {
          _mm=_mm();
          obj.$$metamodel$$=_mm;
        }
        //We can navigate the metamodel
        if (_mm.d['$mt'] === 'mthd') {
          if (type.t === Callable) { //It's a callable reference
            if (type.a && type.a.Return && _mm['$t']) {
              //Check if return type matches
              if (extendsType(_mm['$t'], type.a.Return)) {
                if (type.a.Arguments && _mm['$ps'] !== undefined) {
                  var metaparams = _mm['$ps'];
                  if (metaparams.length == 0) {
                    return type.a.Arguments.t === Empty;
                  } else {
                    //check if arguments match
                    var comptype = type.a.Arguments;
                    for (var i=0; i < metaparams.length; i++) {
                      if (comptype.t !== Tuple || !extendsType(metaparams[i]['$t'], comptype.a.First)) {
                        return false;
                      }
                      comptype = comptype.a.Rest;
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
    if (type.t.$$.T$name in obj.getT$all()) {
      if (type.a && obj.$$targs$$) {
        for (var i in type.a) {
          var cmptype = type.a[i];
          var tmpobj = obj;
          var iance = null;
          var _mm = type.t.$$metamodel$$;
          if (typeof(_mm)==='function') {
            _mm = _mm();
            type.t.$$metamodel$$=_mm;
          }
          if (_mm && _mm.$tp && _mm.$tp[i]) iance=_mm.$tp[i]['var'];
          if (iance===null) {//null means no i in _mm.$tp
            //Type parameter may be in the outer type
            while (iance===null && tmpobj.$$outer !== undefined) {
              tmpobj=tmpobj.$$outer;
              var _tmpf = tmpobj.constructor.T$all[tmpobj.constructor.T$name];
              var _mmf = typeof(_tmpf.$$metamodel$$)==='function'?_tmpf.$$metamodel$$():_tmpf.$$metamodel$$;
              if (_mmf && _mmf.$tp && _mmf.$tp[i]) {
                iance=_mmf.$tp[i]['var'];
              }
            }
          }
          if (iance === 'out') {
            if (!extendsType(tmpobj.$$targs$$[i], cmptype)) {
              return false;
            }
          } else if (iance === 'in') {
            if (!extendsType(cmptype, tmpobj.$$targs$$[i])) {
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
                  if (!extendsType(_targ.l[i],cmptype))return false;
                }
              } else if (cmptype && cmptype.t && cmptype.t==='u') {
                //_targ must satisfy at least one type in cmptype
                if (cmptype.t!==_targ.t || !cmptype.l || cmptype.l.length!==_targ.l.length)return false;
                for (var i=0; i<_targ.l.length;i++) {
                  if (!extendsType(_targ.l[i],cmptype))return false;
                }
              }
            } else {
              if (cmptype.t!==_targ.t || !cmptype.l || cmptype.l.length!==_targ.l.length)return false;
              for (var i=0; i<_targ.l.length;i++) {
                if (!extendsType(_targ.l[i],cmptype))return false;
              }
            }
          } else if (iance === null) {
            console.log("Possible missing metamodel for " + type.t.$$.T$name + "<" + i + ">");
          } else {
            console.log("Don't know what to do about variance '" + iance + "'");
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
    var partial = isOfType(obj, t);
    if (types.t==='u') {
      unions = partial || unions;
    } else {
      inters = partial && inters;
      _ints=true;
    }
  }
  return _ints ? inters||unions : unions;
}
function extendsType(t1, t2) { //true if t1 is subtype of t2
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
            var partial = extendsType(t1.l[i], t2);
            if (t1.t==='i') {
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
            var partial = extendsType(t1, t2.l[i]);
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
            if (!extendsType(t1.l[i],t2.l[i]))return false;
          }
          return true;
        } else return false;
      } else {
        t1=$retuple(t1);
      }
    } else if (t2.t==='T') {
      t2=$retuple(t2);
    }
    for (t in t1.t.$$.T$all) {
        if (t === t2.t.$$.T$name || t === 'ceylon.language::Nothing') {
            if (t1.a && t2.a) {
                //Compare type arguments
                for (ta in t1.a) {
                    if (!extendsType(t1.a[ta], t2.a[ta])) return false;
                }
            }
            return true;
        }
    }
    return false;
}
function removeSupertypes(list) {
    for (var i=0; i < list.length; i++) {
        for (var j=i; i < list.length; i++) {
            if (i!==j) {
                if (extendsType(list[i],list[j])) {
                    list[j]=list[i];
                } else if (extendsType(list[j],list[i])) {
                    list[i]=list[j];
                }
            }
        }
    }
}

function className(obj) {
    function _typename(t) {
        if (t.t==='i' || t.t==='u') {
            var _sep = t.t==='i'?'&':'|';
            var ct = '';
            for (var i=0; i < t.l.length; i++) {
                if (i>0) { ct+=_sep; }
                ct += _typename(t.l[i]);
            }
            return String$(ct);
        } else {
            var tn = t.t.$$.T$name;
            if (t.a) {
                tn += '<';
                for (var i = 0; i < t.a.length; i++) {
                    if (i>0) { tn += ','; }
                    tn += _typename(t.a[i]);
                }
                tn += '>';
            }
            return tn;
        }
    }
    if (obj === null) return String$('ceylon.language::Null');
    if (obj === undefined) return String$("JavaScript UNDEFINED");
    var tn = obj.getT$name === undefined ? 'UNKNOWN' : obj.getT$name();
    if (tn === 'UNKNOWN') {
        if (typeof obj === 'function') {
            tn = 'ceylon.language::Callable';
        }
    }
    else if (obj.$$targs$$) {
        /*tn += '<';
        for (var i=0; i < obj.$$targs$$.length; i++) {
            if (i>0) { tn += ','; }
            tn += _typename(obj.$$targs$$[i]);
        }
        tn += '>';*/
    }
    return String$(tn);
}
className.$$metamodel$$={$an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language','className']};

function identityHash(obj) {
    return obj.BasicID;
}
identityHash.$$metamodel$$={$an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language','identityHash']};

function set_type_args(obj, targs) {
    if (obj===undefined)return;
    if (obj.$$targs$$ === undefined) {
        obj.$$targs$$={};
    }
    for (x in targs) {
        obj.$$targs$$[x] = targs[x];
    }
}
function add_type_arg(obj, name, type) {
    if (obj===undefined)return;
    if (obj.$$targs$$ === undefined) {
        obj.$$targs$$={};
    }
    obj.$$targs$$[name]=type;
}
function wrapexc(e,loc,file) {
  if (loc !== undefined) e.$loc=loc;
  if (file !== undefined) e.$file=file;
  return e;
}
function throwexc(e,loc,file) {
  if (loc !== undefined) e.$loc=loc;
  if (file !== undefined) e.$file=file;
  throw e;
}
exports.set_type_args=set_type_args;
exports.add_type_arg=add_type_arg;
exports.exists=exists;
exports.nonempty=nonempty;
exports.isOfType=isOfType;
exports.className=className;
exports.identityHash=identityHash;
exports.throwexc=throwexc;
exports.wrapexc=wrapexc;
