import com.google.common.collect { ImmutableList }

shared void run() {
    ImmutableList.\iof(""); // using the import doesn't affect anything
    BadType x = nothing; // non-existant type triggers guava annotation warnings
}