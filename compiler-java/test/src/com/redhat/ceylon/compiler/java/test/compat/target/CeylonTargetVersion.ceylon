import ceylon.language.serialization{deserialization}
import com.redhat.ceylon.common {
    Versions
}

shared void run() {
    print("Running on ``language.version`` according to language.version");
    print("Compiled against com.redhat.ceylon.common/``Versions.\iCEYLON_VERSION`` according to com.redhat.ceylon.common::Versions");
    deserialization();
}