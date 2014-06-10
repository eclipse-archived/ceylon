function $init$native$Throwable$before(exc) {
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
