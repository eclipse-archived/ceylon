@noanno
late String? uncheckedLateToplevel;
@noanno
class UncheckedLate() {
    shared late String? a1;
    shared late Integer a2;
    shared late variable Float a3;
    shared variable Boolean a4;
    a4 = true;
    // We do have an init check on this one, because we can
    // use its null-ness to track initialization
    shared late Object a5;
}