function(){
  if (this.state_===4)throw AssertionError("broken graph");
  var i=[];
  try {
    if (this.state_!==3) {
      var queue=[this];
      while (queue.length>0) {
        var r=queue.shift();
        i.push(r);
        if (r.state_===1) {
          r.clazz.tipo.deser$$(r.decons_, r.clazz, r.leak());
          //TODO add outer ref if available
          r.state_=2;
        }
        if (r.state_===2) {
          //iterar por r.references()
          var referred;for(var iter=r.decons_.iterator();(referred=iter.next())!==finished();){
            while (!(referred===finished()||is$(referred.$_get(1),{t:Reference$serialization}))){
              referred=iter.next();
            }
            if (referred===finished())break;
            referred=referred.$_get(1);
            if (referred.state_===undefined) {
              throw AssertionError("reference " + referred.id.string + " has not been deserialized");
            }
            if (referred.state_!==3) {
              queue.push(referred);
            }
          }
          r.state_=3;
          r.decons_=null;
        }
      }
    }
  } catch (e) {
    for (var ii=0; ii<i.length;i++) {
      i[ii].state_=4;
    }
    throw e;
  }
}
