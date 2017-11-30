"A simple example plugin for the `ceylon` tool"
by("Tako Schotanus")
license("http://www.apache.org/licenses/LICENSE-2.0")
native("jvm")
module com.example.plugin "1.0" {
    shared import java.base "7";
    shared import org.eclipse.ceylon.cli "1.3.4-SNAPSHOT"/*@CEYLON_VERSION@*/;
}
