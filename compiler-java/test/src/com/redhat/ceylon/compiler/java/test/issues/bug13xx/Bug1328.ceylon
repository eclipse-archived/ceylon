@noanno
interface Bug1328_Factory<Things>
        given Things satisfies Object {
    shared formal Thing make<Thing>(Thing provided)
            given Thing satisfies Things;
}

@noanno
class Bug1328() satisfies Bug1328_Factory<Integer> {
    shared actual Thing2 make<Thing2>(Thing2 provided) {
        return nothing;
    }
}
