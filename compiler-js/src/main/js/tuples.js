function Tuple(first, rest) {
    var that = new Tuple.$$;
    that.f$ = first;
    that.r$ = rest;
    return that;
}
initTypeProto(Tuple, 'ceylon.language::Tuple', IdentifiableObject, Sequence);
var Tuple$proto = Tuple.$$.prototype;

Tuple$proto.getFirst = function() {
    return this.f$;
}
Tuple$proto.getRest = function() {
    return this.r$;
}
Tuple$proto.item = function(index) {
    if (index > 0) {
        return this.r$.item(index-1);
    } else if (index === 0) {
        return this.f$;
    }
    return null;
}
Tuple$proto.getLastIndex = function() {
    var rli = this.r$.getLastIndex();
    return rli === null ? 0 : rli+1;
}
Tuple$proto.getReversed = function() {
    return this.r$.getReversed().withTrailing(this.f$);
}
Tuple$proto.segment = function(from, len) {
    return from<=0? this.r$.segment(0,len+from-1).withLeading(this.f$) : this.r$.segment(from-1,len);
}
Tuple$proto.span = function(from, to) {
    var end = to === null ? this.getSize() : to;
    return from<=end ? this.segment(from,end-from+1) : this.segment(end,from-end+1).getReversed().getSequence();
}
Tuple$proto.getClone = function() { return this; }
Tuple$proto.getString = function() {
    var sb = StringBuilder();
    sb.appendAll("Tuple(", this.f$.getString(), ",", this.r$.getString(), ")");
    return sb.getString();
}

exports.Tuple=Tuple;
