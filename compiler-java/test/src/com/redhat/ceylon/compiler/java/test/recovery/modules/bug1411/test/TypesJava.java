package test;

public class TypesJava {
    boolean return_boolean() { return true; };
    Boolean return_Boolean() { return true; };
    int return_int() { return 1; };
    Integer return_Integer() { return 1; };
    long return_long() { return 1L; };
    Long return_Long() { return 1L; };
    float return_float() { return 1.0f; };
    Float return_Float() { return 1.0f; };
    double return_double() { return 1.0d; };
    Double return_Double() { return 1.0d; };
    char return_char() { return 'a'; };
    Character return_Character() { return 'a'; };
    String return_String() { return ""; };

    void take_boolean(boolean val) { };
    void take_Boolean(Boolean val) { };
    void take_int(int val) { };
    void take_Integer(Integer val) { };
    void take_long(long val) { };
    void take_Long(Long val) { };
    void take_float(float val) { };
    void take_Float(Float val) { };
    void take_double(double val) { };
    void take_Double(Double val) { };
    void take_char(char val) { };
    void take_Character(Character val) { };
    void take_String(String val) { };

    boolean[] return_booleans() { return new boolean[] { true, false }; };
    Boolean[] return_Booleans() { return new Boolean[] { true, false }; };
    int[] return_ints() { return new int[] { 1, 2, 3 }; };
    Integer[] return_Integers() { return new Integer[] { 1, 2, 3 }; };
    long[] return_longs() { return new long[] { 1L, 2L, 3L }; };
    Long[] return_Longs() { return new Long[] { 1L, 2L, 3L }; };
    float[] return_floats() { return new float[] { 1.0f, 1.5f, 2.0f }; };
    Float[] return_Floats() { return new Float[] { 1.0f, 1.5f, 2.0f }; };
    double[] return_doubles() { return new double[] { 1.0d, 1.5d, 2.0d }; };
    Double[] return_Doubles() { return new Double[] { 1.0d, 1.5d, 2.0d }; };
    char[] return_chars() { return new char[] { 'a', 'b', 'z' }; };
    Character[] return_Characters() { return new Character[] { 'a', 'b', 'z' }; };
    String[] return_Strings() { return new String[] { "aap", "noot", "mies", "", null }; };

    void take_booleans(boolean[] val) { };
    void take_Booleans(Boolean[] val) { };
    void take_ints(int[] val) { };
    void take_Integers(Integer[] val) { };
    void take_longs(long[] val) { };
    void take_Longs(Long[] val) { };
    void take_floats(float[] val) { };
    void take_Floats(Float[] val) { };
    void take_doubles(double[] val) { };
    void take_Doubles(Double[] val) { };
    void take_chars(char[] val) { };
    void take_Characters(Character[] val) { };
    void take_Strings(String[] val) { };
    
    void test(CharSequence seq) { };
}
