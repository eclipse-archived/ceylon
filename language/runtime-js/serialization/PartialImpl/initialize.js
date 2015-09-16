function initialize(context,$mpt){
  var me=this;

  function getReferredInstance2(ctxt, refid) {
    var referred = ctxt.leakInstance(refid);
    if (is$(referred,{t:Partial$serialization})) {
      referred = referred.instance_;
    }
    return referred;
  }
  function getReferredInstance(ctxt, st, rc) {
    var x=st.$_get(rc);
    return getReferredInstance2(ctxt, x);
  }

  function initializeObject(inst) {
    var reachables = inst.getT$all()[inst.getT$name()].ser$refs$(inst);
    var numLate = 0;
    for (var i=0;i<reachables.length;i++) {
      var r=reachables[i];
      if (is$(r,{t:Member$serialization}) && r.attribute.late) {
        numLate++;
      } else if (is$(r,{t:Outer$serialization})) {
        numLate++;
      }
    }
    if (me.state.size < reachables.length-numLate) {
      var missingNames=[];
      for (var i=0;i<reachables.length;i++) {
        if (!missingNames.contains(reachables[i])) {
          missingNames.push(reachables[i]);
        }
      }
      var next;for(var iter=me.state.keys.iterator;(next=iter.next())!==finished();) {
        for (var i=0;i<missingNames.length;i++) {
          if (missingNames[i].equals(next)) {
            missingNames.splice(i,1);
            break;
          }
        }
      }
      throw DeserializationException$serialization("lacking sufficient state for instance with id " + me.id + ": " + missingNames.string);
    }
    for (var i=0;i<reachables.length;i++) {
      var ref=reachables[i];
      if (is$(ref,{t:Member$serialization})) {
        if (ref.attribute.late && !me.state.contains(ref)
            || me.state.$_get(ref) === uninitializedLateValue$serialization()) {
          continue;
        }
        //TODO membertypecache
        var referredInstance = getReferredInstance(context, me.state, ref);
        inst.getT$all()[inst.getT$name()].ser$set$(ref, inst, referredInstance);
      } else if (is$(ref,{t:Outer$serialization})) {
        continue;
      } else {
        throw AssertionError("unexpected ReachableReference " + ref.string);
      }
    }
  }

  function initializeArray(inst) {
    var sizeAttr = $_Array.ser$refs$(inst)[0];
    var size = getReferredInstance(context, me.state, sizeAttr);
    if (size === null || size === undefined) {
      throw DeserializationException$serialization("lacking sufficient state for array with id " + me.id + ": " + sizeAttr.string);
    }
    var type=inst.getT$all()[inst.getT$name()];
    type.ser$set$(sizeAttr,inst,size);
    for (var i=0;i<size;i++) {
      var index=ElementImpl$impl(i);
      var id=me.state.$_get(index);
      if (id===null||id===undefined) {
        throw DeserializationException$serialization("lacking sufficient state for array with id " + me.id + " element " + i);
      }
      var elem=getReferredInstance2(context,id);
      type.ser$set$(i,inst,elem);
    }
    if (me.state.size != size+1) {
      throw DeserializationException$serialization("lacking sufficient state for array with id " + me.id);
    }
  }

  var inst=me.instance_;
  if (is$(inst,{t:$_Array})) {
    initializeArray(inst);
  } else if (is$(inst,{t:Tuple})) {
    console.log("TODO! initialize tuple");
  } else {
    initializeObject(inst);
  }
  me.state=null;
}
