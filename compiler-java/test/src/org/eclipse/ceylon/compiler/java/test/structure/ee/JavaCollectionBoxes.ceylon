@noanno
class JavaCollectionBoxes<T>(listString, mapString, setString) {
    shared variable List<String> listString;
    shared variable Map<String,String> mapString;
    shared variable Set<String> setString;
    
    shared variable List<Integer>? listInteger = null;
    shared variable Map<Integer,Integer>? mapInteger = null;
    shared variable Set<Integer>? setInteger = null;
    
    shared variable List<Byte>? listByte = null;
    shared variable Map<Byte,Byte>? mapByte = null;
    shared variable Set<Byte>? setByte = null;
    
    shared variable List<Boolean>? listBoolean = null;
    shared variable Map<Boolean,Boolean>? mapBoolean = null;
    shared variable Set<Boolean>? setBoolean = null;
    
    shared variable List<Character>? listCharacter = null;
    shared variable Map<Character,Character>? mapCharacter = null;
    shared variable Set<Character>? setCharacter = null;
    
    shared variable List<Float?>? listFloat = null;
    shared variable Map<Float?,Float?>? mapFloat = null;
    shared variable Set<Float?>? setFloat = null;
    
    shared variable List<List<Float>>? listNested = null;
    shared variable Map<Map<Float,Float>,Map<Float,Float>>? mapNested = null;
    shared variable Set<Set<Float>>? setNested = null;
    
    shared variable List<T>? listT = null;
    shared variable Map<T,T>? mapT = null;
    shared variable Set<T>? setT = null;
}