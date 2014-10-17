function(){
  if (this.state_==4)throw AssertionError("broken graph");
  var i=[];
  try {
    if (this.state_!=3) {
      var queue=[];
      queue.push(this);
      while (queue.length>0) {
        var r=queue.shift();
        i.push(r);
        if (r.state_==1) {
          r.inst_.deser$$(r.decons_);
          r.state_=2;
        }
        if (r.state_==2) {
          //iterar por r.references()
            if (referred.state_==undefined) {
              throw AssertionError("reference " + referred.id.string + " has not been deserialized");
            }
            if (referred.state_!=3) {
              queue.push(referred);
            }
          r.state_=3;
          r.decons_=null;
        }
      }
    }
    return null;
  } catch (e) {
    for (var ii=0; ii<i.length;i++) {
      i[ii].state_=4;
    }
    throw e;
  }
}
