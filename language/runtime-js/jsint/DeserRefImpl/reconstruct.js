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
          r.inst_=this.clazz.tipo.deser$$(r.decons_, this.clazz);
          //TODO add outer ref if available
          r.state_=2;
        }
        if (r.state_==2) {
          //iterar por r.references()
          var referred;for(var iter=this.decons_.iterator();(referred=iter.next())!=getFinished();){
            while (!is$(referred,{t:'u',l:[{t:Finished},{t:Reference$serialization}]}))referred=iter.next();
            if (referred===getFinished())break;
            if (referred.state_==undefined) {
              throw AssertionError("reference " + referred.id.string + " has not been deserialized");
            }
            if (referred.state_!=3) {
              queue.push(referred);
            }
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
