import java.io { File }

void run() {
    File dir = File(".");
    print("Contents of folder: ");
    print(dir);
    Array<File?> files = dir.listFiles().array;
    for (File? f in files) {
        print(f);
    }
    for (Integer i in 0..files.size) {
        print(i);
    }
}
