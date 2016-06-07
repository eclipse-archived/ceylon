@noanno
interface ClassMembers<X> {
    X x => nothing;
    shared class Member() {
        shared X y => x;
        shared X z => outer.x;
        //shared Member self => this;
    }
}