if (this.m$['$pks$'] === undefined) {
  this.m$['$pks$'] = {};
  for (mem in this.meta.$CCMM$) {
    if (typeof(mem) === 'string' && mem[0]!=='$') {
      this.m$['$pks$'][mem] = Paquete$jsint(mem, this, this.m$[mem]);
    }
  }
}
var m = [];
for (mem in this.m$['$pks$']) {
  if (typeof(mem) === 'string') {
    m.push(this.m$['$pks$'][mem]);
  }
}
return m.length===0?getEmpty():ArraySequence(m,{Element$ArraySequence:{t:Package$meta$declaration}});
