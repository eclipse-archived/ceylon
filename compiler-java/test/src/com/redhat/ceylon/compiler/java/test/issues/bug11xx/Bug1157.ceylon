interface Bug1157_Anno<out Value> given Value satisfies Bug1157_Anno<Value> {}

interface Bug1157_AnnoSub<out Value>
        satisfies Bug1157_Anno<Value>
        given Value satisfies Bug1157_Anno<Value>
        {}

A[] bug1157_annotations<out A>()
        given A satisfies Bug1157_Anno<A> => nothing;

void bug1157_call() {
    bug1157_annotations<Bug1157_AnnoSub<Nothing>>();
}