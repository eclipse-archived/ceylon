function List(wat) {
    Collection(wat);
    Correspondence(wat);
    Ranged(wat);
    return wat;
}
function $init$List() {
    if (List.$$===undefined) {
        initTypeProto(List, 'ceylon.language::List', $init$Collection(), $init$Correspondence(),
            $init$Ranged());
    }
var List$proto = List.$$.prototype;
List$proto.getSize = function() {
    var li = this.getLastIndex();
    return li === null ? 0 : li.getSuccessor();
}
List$proto.defines = function(idx) {
    var li = this.getLastIndex();
    if (li === null) li = -1;
    return li.compare(idx) !== smaller;
}
List$proto.equals = function(other) {
    if (isOfType(other, {t:List}) && other.getSize().equals(this.getSize())) {
        for (var i = 0; i < this.getSize(); i++) {
            var mine = this.get(i);
            var theirs = other.get(i);
            if (((mine === null) && theirs) || !(mine && mine.equals(theirs))) {
                return false;
            }
        }
        return true;
    }
    return false;
}
List$proto.getHash = function() {
    var hc=1;
    var iter=this.getIterator();
    var e; while ((e = iter.next()) != getFinished()) {
        hc*=31;
        if (e !== null) {
            hc += e.getHash();
        }
    }
    return hc;
}
List$proto.findLast = function(select) {
    var li = this.getLastIndex();
    if (li !== null) {
        while (li>=0) {
            var e = this.get(li);
            if (e !== null && select(e)) {
                return e;
            }
            li = li.getPredecessor();
        }
    }
    return null;
}
List$proto.withLeading = function(other, $$$mptypes) {
    var sb = SequenceBuilder({Element:{t:'u',l:[this.$$targs$$.Element, $$$mptypes.Other]}});
    sb.append(other);
    sb.appendAll(this);
    return sb.getSequence();
}
List$proto.withTrailing = function(other, $$$mptypes) {
    var sb = SequenceBuilder({Element:{t:'u',l:[this.$$targs$$.Element, $$$mptypes.Other]}});
    sb.appendAll(this);
    sb.append(other);
    return sb.getSequence();
}
exports.List=List;

List$proto.getIterator = function() {
    return ListIterator(this);
}
    return List;
}
$init$List();
function ListIterator(list) {
    var that = new ListIterator.$$;
    Iterator(that);
    that.list=list;
    that.$$targs$$=list.$$targs$$;
    that.index=0;
    that.lastIndex=list.getLastIndex();
    if (that.lastIndex === null) {
        that.lastIndex = -1;
    } else {
        that.lastIndex = that.lastIndex;
    }
    return that;
}
initTypeProto(ListIterator, 'ceylon.language::ListIterator', $init$Iterator());
ListIterator.$$.prototype.next = function() {
    if (this.index <= this.lastIndex) {
        return this.list.get(this.index++);
    }
    return getFinished();
}
