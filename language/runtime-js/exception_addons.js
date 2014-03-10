function $init$native$Exception$before(exc) {
  var _caller=arguments.callee.caller.caller;
  exc.stack_trace=[];
  var ilc=0;
  var ilf=null;
  while(_caller) {
    exc.stack_trace.push(_caller);
    _caller = _caller.caller;
    if (_caller===ilf) {
      ilc++;
      if (ilc>2) {
        exc.stack_trace.push("CIRCULAR");
        _caller = null;
      }
    } else if (ilc===0 && _caller && exc.stack_trace.contains(_caller)) {
      ilc=1;
      ilf=_caller;
    }
  }
}
function printStackTrace(exc, _write) {
  if (_write===undefined)_write=getProcess().writeError;
  var _c = className(exc);
  if (exc.message.size > 0) {
    _c += ' "' + exc.message + '"';
  }
  _write(_c);_write(getOperatingSystem().newline);
  for (var i=0; i<exc.stack_trace.length; i++) {
    var f = exc.stack_trace[i];
    if (f == "CIRCULAR") {
      _write("    (Infinite loop detected)");
      _write(getOperatingSystem().newline);
      return;
    }
    var mm = f.$crtmm$;
    if (typeof(mm)==='function') {
      mm = mm();
      f.$crtmm$=mm;
    }
    if (mm) {
      var _src = '';
      if (i==0) {
        if (exc.$loc && exc.$file) _src = ' (' + exc.$file + " " + exc.$loc + ')';
      }
      _write("    at " + mm.d[0] + "::" + mm.d[mm.d.length-1] + _src);_write(getOperatingSystem().newline);
    }
  }
}
printStackTrace.$crtmm$=function(){
  return{mod:$CCMM$,d:['ceylon.language','printStackTrace'],$t:{t:Anything},$ps:[
    {$nm:'exception',$t:{t:Exception},$mt:'prm'},{$nm:'write',$t:{t:Callable,a:{Return:{t:Anything},Arguments:{t:'T',l:[{t:String$}]}}},$mt:'prm','$def':1}
  ]};};
exports.printStackTrace=printStackTrace;
