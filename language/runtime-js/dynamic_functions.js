//Dress up a native JS object as the specified type
//(which should be a Ceylon dynamic interface)
function dre$$(object, type, loc, stack) {
  //If it's already dressed up as the type, leave it alone
  if (is$(object, type))return object;
  //If it's already of another type, throw
  if (object===null || object===undefined)return object;
  if (object.$$ !== undefined && object.getT$name()!=='<Dynamic>') {
    if (loc===false)return object;
    throw new TypeError("Cannot modify the type of an object to "+qname$(type)+" at runtime " + loc);
  }
  //If it's frozen, throw because we won't be able to dress it up
  if (typeof(object)==='object' && Object.isFrozen(object)) {
    if (loc===false)return object;
    throw new Error("Cannot add Ceylon type information to a frozen object");
  }
  //If it's a TypeScript enum, accept number values and nothing else
  if (type.t.$$.$tsenum) {
    if (typeof(object)==='number') {
      return object;
    } else {
      if (loc===false)return object;
      throw new Error("Native object cannot be a TypeScript enum");
    }
  }
  function memberTypeIsDynamicInterface$(t) {
    if (t.t && t.t.dynmem$) {
      return t;
    }
    if (t.t==='u') {
      var c=0,st;
      for (var i=0; i<t.l.length;i++) {
        if (t.l[i].t.dynmem$) {
          c++;
          st=t.l[i];
        }
      }
      if (c===1) {
        return st;
      }
    }
    return undefined;
  }
  //Check members, non-invasively first
  var actual = typeof(object)==='object'?Object.getOwnPropertyNames(object):[];
  var sats = type.t.$$.prototype.getT$all();
  var tname=type.t.$$.T$name;
  var t_all=sats;
  if (stack===undefined) {
    stack=[object];
  } else if (stack.indexOf(object)<0) {
    stack.push(object);
  }
  if (object.$$===undefined) {
    //Make anonymous class
    object.$$=$_Object.$$;
    object.getT$name=function(){return "<Dynamic>"};
    object.T$all={};
    object.getT$all=function(){return this.T$all};
    object.T$all['ceylon.language::Object']=$_Object;
    object.$crtmm$={mod:$CCMM$,$t:{t:$_Object},sts:[],d:{mt:'c'}};
  }
  //Add type
  object.T$all[tname]=type;
  object.$crtmm$.sts.push(type);
  //Initialize object with type info
  if (type.a) {
    type.t(type.a,object);
    if (object.$$targs$$===undefined) {
      object.$$targs$$={};
    }
    for (targ in type.a) {
      if (!object.$$targs$$[targ])object.$$targs$$[targ]=type.a[targ];
    }
  } else {
    type.t(object);
  }
  for (var sat in sats) {
    var expected = sats[sat].dynmem$;
    if (expected) {
      for (var i=0; i < expected.length; i++) {
        var propname="$prop$get"+expected[i][0].uppercased+expected[i].substring(1);
        var proptype=type.t.$$.prototype[propname];
        if (proptype) {
          proptype=getrtmm$$(proptype);
        }
        if (actual.indexOf(expected[i])<0 && object[expected[i]]===undefined) {
          if (proptype && extendsType({t:Null},proptype.$t)) {
            object[expected[i]]=null;
          } else {
            if (loc===false)return object;
            throw new Error("Native object is missing property '" + expected[i] + "' " + loc);
          }
        } else {
          var val=object[expected[i]],dynmemberType;
          if (val===object) {
            //avoid instance circularity
            if (!is$(val,proptype.$t)) {
              //add satisfied type
              object.$crtmm$.sts.push(proptype.$t);
              object.T$all[proptype.$t.t.$$.prototype.getT$name()]=proptype.$t.t;
              if (proptype.$t.a) {
                proptype.$t.t(proptype.$t.a,object);
              } else {
                proptype.$t.t(object);
              }
            }
          } else if (proptype && proptype.$t && !is$(val,proptype.$t)) {
            if (proptype.$t.t===$_Array) {
              object[expected[i]]=natc$(val,proptype.$t.a.Element$Array,loc,stack);
            } else if (proptype.$t.t===Integer) {
              object[expected[i]]=ndnc$(val,'i',loc);
            } else if (proptype.$t.t===Float) {
              object[expected[i]]=ndnc$(val,'f',loc);
            } else if ((dynmemberType=memberTypeIsDynamicInterface$(proptype.$t))!==undefined) {
              //If the member type is a dynamic interface, dress up the value
              if (stack.indexOf(val)<0) {
                dre$$(val,dynmemberType,loc,stack);
              }
            } else {
              var _t=proptype.$t;
              if (typeof(_t)==='string') {
                if (object.$$targs$$ && object.$$targs$$[_t]) {
                  _t=object.$$targs$$[_t];
                } else {
                  var mm=getrtmm$$(type.t);
                  if (mm && mm.sts) {
                    for (var j=0;j<mm.sts.length;j++) {
                      if (mm.sts[j].a && mm.sts[j].a[_t]) {
                        object.$$targs$$[_t]=mm.sts[j].a[_t];
                        _t=mm.sts[j].a[_t]; break;
                      }
                    }
                  }
                }
              }
              object[expected[i]]=ndtc$(val,_t,loc);
            }
          }
        }
      }
    }
    object.T$all[sats[sat].$$.prototype.getT$name()]=sats[sat];
  }
  if (typeof(object)==='object') {
    if (actual.indexOf('string')<0) {
      atr$(object,'string',function(){return object.toString();},undefined,
           $_Object.$$.prototype.$prop$getString.$crtmm$);
    }
    if (actual.indexOf('hash')<0) {
      atr$(object,'hash',function(){return identityHash(object);},undefined,
           $_Object.$$.prototype.$prop$getHash.$crtmm$);
    }
  }
  return object;
}
ex$.dre$$=dre$$;
//print native dynamic object
function pndo$(o) {
  if (o === undefined)print("<undefined>");
  else if (o === null)print("<null>");
  else if (is$(o,{t:Anything}))print(o);
  else if (o.string)print(o.string);
  else {
    print(o.toString());
  }
}
ex$.pndo$=pndo$;
//check if numbers are really numbers
function ndnc$(n,t,loc) {
  if (t==='f') {
    if (typeof(n)==='number')return Float(n);
    if (is$(n,{t:Float}))return n;
  } else if (t==='i') {
    if (typeof(n)==='number')return Math.floor(n);
    if (is$(n,{t:Integer}))return n;
  }
  if (loc===false)return false;
  throw new TypeError('Expected ' + (t==='f'?'Float':'Integer') + ' (' + loc + ')');
}
ex$.ndnc$=ndnc$;
//Check if an object if really of a certain type
function ndtc$(o,t,loc) {
  if (t==='$U' || is$(o,t))return o;
  if (loc===false)return false;
  throw new TypeError('Expected ' + qname$(t) + ' (' + loc + ')');
}
ex$.ndtc$=ndtc$;
//Box native array, checking elements' type
function natc$(a,t,loc,stack) {
  if (a===empty())return $arr$([],t);
  if (Array.isArray(a)) {
    for (var i=0;i<a.length;i++) {
      if (!is$(a[i],t) && (a[i] && a[i].getT$all)===undefined) {
        a[i]=dre$$(a[i],t,loc,stack);
      }
    }
    return $arr$(a,t);
  }
  if(loc===false)return false;
  throw new TypeError('Expected ' + qname$(t) + ' (' + loc + ')');
}
ex$.natc$=natc$;

//A special kind of "require" for non-standard npm modules
//that return a single function instead of a proper exports object
function npm$req(name,mod,req) {
  var x=req(mod);
  if (typeof(x)==='function') {
    var k=Object.keys(x);
    if (k.length===0) {
      var o={};
      o[name]=x;
      x=o;
    } else if (k.indexOf(name)<0) {
      x[name]=x;
    }
  }
  return x;
}
ex$.npm$req=npm$req;
