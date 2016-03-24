//Dress up a native JS object as the specified type
//(which should be a Ceylon dynamic interface)
function dre$$(object, type, loc) {
  //If it's already dressed up as the type, leave it alone
  if (is$(object, type))return object;
  //If it's already of another type, throw
  if (object.$$ !== undefined && object.getT$all()[object.getT$name()].dynmem$===undefined) {
    throw new TypeError("Cannot modify the type of an object to "+qname$(type)+" at runtime " + loc);
  }
  //If it's frozen, throw because we won't be able to dress it up
  if (typeof(object)==='object' && Object.isFrozen(object)) {
    throw new Error("Cannot add Ceylon type information to a frozen object");
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
  //Check members
  var actual = typeof(object)==='object'?Object.getOwnPropertyNames(object):[];
  var sats = type.t.$$.prototype.getT$all();
  object.$$=type.t.$$;
  object.getT$name=function(){return type.t.$$.T$name};
  object.getT$all=function(){return sats;}
  if (type.a) {
    object.$$targs$$=type.a;
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
          if (extendsType({t:Null},proptype.$t)) {
            object[expected[i]]=null;
          } else {
            throw new Error("Native object is missing property '" + expected[i] + "' " + loc);
          }
        } else {
          var val=object[expected[i]],dynmemberType;
          if (val===object) {
            //avoid instance circularity
            if (!is$(val,proptype.$t)) {
              //and make this an intersection type
              var tname=object.getT$name()+"|"+proptype.$t.t.$$.T$name;
              object.getT$name=function(){return tname;}
              //Copy the satisfied types and add the new one
              var _ts={};
              for (var _tn in object.getT$all()) {
                _ts[_tn]=object.getT$all()[_tn];
              }
              _ts[proptype.$t.t.$$.prototype.getT$name()]=proptype.$t.t;
              object.getT$all=function(){return _ts;}
              //type arguments
              object.$$=$_Object.$$;
            }
          } else if (proptype && proptype.$t && !is$(val,proptype.$t)) {
            if (proptype.$t.t===$_Array) {
              object[expected[i]]=natc$(val,proptype.$t.a.Element$Array,loc);
            } else if (proptype.$t.t===Integer) {
              object[expected[i]]=ndnc$(val,'i',loc);
            } else if (proptype.$t.t===Float) {
              object[expected[i]]=ndnc$(val,'f',loc);
            } else if ((dynmemberType=memberTypeIsDynamicInterface$(proptype.$t))!==undefined) {
              //If the member type is a dynamic interface, dress up the value
              dre$$(val,dynmemberType,loc);
            } else {
              var _t=proptype.$t;
              if (typeof(_t)==='string') {
                if (object.$$targs$$[_t]) {
                  _t=object.$$targs$$[_t];
                } else {
                  var mm=getrtmm$$(type.t);
                  if (mm && mm.sts) {
                    for (var i=0;i<mm.sts.length;i++) {
                      if (mm.sts[i].a && mm.sts[i].a[_t]) {
                        object.$$targs$$[_t]=mm.sts[i].a[_t];
                        _t=mm.sts[i].a[_t]; break;
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
  throw new TypeError('Expected ' + (t==='f'?'Float':'Integer') + ' (' + loc + ')');
}
ex$.ndnc$=ndnc$;
//Check if an object if really of a certain type
function ndtc$(o,t,loc) {
  if (is$(o,t))return o;
  throw new TypeError('Expected ' + qname$(t) + ' (' + loc + ')');
}
ex$.ndtc$=ndtc$;
//Box native array, checking elements' type
function natc$(a,t,loc) {
  if (a===empty())return $arr$([],t);
  if (Array.isArray(a)) {
    for (var i=0;i<a.length;i++) {
      if (!is$(a[i],t) && (a[i] && a[i].$$)===undefined) {
        a[i]=dre$$(a[i],t,loc);
      }
    }
    return $arr$(a,t);
  }
  throw new TypeError('Expected ' + qname$(t) + ' (' + loc + ')');
}
ex$.natc$=natc$;
