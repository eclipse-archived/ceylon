
@noanno
shared interface Bug2305Interf {
    shared void initMultiEditChange(){}
}

@noanno
shared interface Bug2305InvocationCompletion {

    shared class Proposal() satisfies Bug2305Interf {
        
        shared void createChange() {
            initMultiEditChange();
        }
    }
}
