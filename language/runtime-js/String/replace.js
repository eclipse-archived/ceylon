function(sub, repl) {
    if (this.indexOf(sub) < 0) {
      return this;
    }
    var ns = this.replace(sub, repl);
    while (ns.indexOf(sub) >= 0) {
      ns = ns.replace(sub, repl);
    }
    return ns;
}
