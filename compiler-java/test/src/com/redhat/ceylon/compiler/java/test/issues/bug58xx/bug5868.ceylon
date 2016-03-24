import javax.sound.midi {
    MidiSystem
}
import java.util { Arrays { asList }}

shared void bug5868(){
    value aInfos = MidiSystem.midiDeviceInfo;
    //variable value i = 0;
    value l = asList(*aInfos.array);
    //for (info in CeylonIterable(asList(*aInfos.array))) {
    //    print("``i``  ``info.name``");
    //    ++i;
    //}
    Arrays.asList<String>(*{""});
}