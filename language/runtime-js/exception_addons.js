function $init$native$Exception$before(exc) {
  var _caller=arguments.callee.caller.caller;
  exc.stack_trace=[];
  while(_caller) {
    exc.stack_trace.push(_caller);
    _caller = _caller.caller;
  }
}
Exception.$$.prototype.printStackTrace = function() {
  var _c = className(this);
  if (this.message.size > 0) {
    _c += ' "' + this.message + '"';
  }
  print(_c);
  for (var i=0; i<this.stack_trace.length; i++) {
    var f = this.stack_trace[i];
    var mm = f.$$metamodel$$;
    if (typeof(mm)==='function') {
      mm = mm();
      f.$$metamodel$$=mm;
    }
    if (mm) {
        print("    at " + mm.pkg + "::" + mm.d.$nm);
    }
  }
}
