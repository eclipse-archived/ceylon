import java.lang{JBoolean=Boolean, JInteger=Integer, Runtime, System, ObjectArray, Thread, Class}
import java.io{File, PrintStream }
import java.util{Locale }
import java.lang.management{...}

@noanno
void staticRefs() {
    // Taking refs
    
    // fields/getters
    Runtime runtime = Runtime.runtime;
    PrintStream stdout = System.\iout;
    PrintStream stderr = System.err;
    Thread.State runnableState = Thread.State.\iRUNNABLE;
    
    // functions
    Anything(Integer) exitRef = System.exit;
    Integer(Boolean,Boolean) compareRef = JBoolean.compare;
    ObjectArray<File>() fsRootsRef = File.listRoots;
    
    // static inner classes
    Locale.Builder() localeBuilderRef = Locale.Builder;
    
    // Immediate invocation
    Integer procs = Runtime.runtime.availableProcessors();
    System.\iout.flush();
    System.err.flush();
     
    System.exit(0);
    Integer cmp = JBoolean.compare(true, false);
    ObjectArray<File> roots = File.listRoots();
    Locale.Builder localeBuilder = Locale.Builder();
    
    value memoryBean = ManagementFactory.memoryMXBean;
    MemoryMXBean? memoryBean2 = ManagementFactory.memoryMXBean;
    ManagementFactory.memoryMXBean.gc();
    value on = ManagementFactory.getPlatformMXBean((nothing of Class<MemoryMXBean>)).objectName;
    
    value a = JavaWithStaticMembers.attribute;
    value a2 = JavaWithStaticMembersSubClass.attribute;
}