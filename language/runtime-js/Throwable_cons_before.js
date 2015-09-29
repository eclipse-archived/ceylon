function(msg,cause,exc) {
  exc.stack_trace=[];
  exc.toString=function(){return this.string;}
  try {
    exc.stack_trace=new Error().stack.split('\n').slice(3);
    return;
  } catch(e){}
  try {
    var _caller=arguments.callee.caller.caller;
    var ilc=0;
    var ilf=null;
    var noms=[];
    while(_caller) {
      exc.stack_trace.push(_caller);
      noms.push(_caller.string);
      try {
        _caller = _caller.caller;
        if (!_caller)return;
        if (_caller.string===ilf) {
          ilc++;
          if (ilc>2) {
            exc.stack_trace.push("CIRCULAR");
            _caller = null;
          }
        } else if (ilc===0 && noms.contains(_caller.string)) {
          ilc=1;
          ilf=_caller.string;
        }
      } catch(e) { _caller = null; }
    }
  } catch(e) {}
}
