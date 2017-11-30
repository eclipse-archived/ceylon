import ceylon.file {
    File,
    Path
}

shared class FileResourceLoader(
    Path path
) {
    String loadFile() {
        if(is File file= path.resource) {
            return "";
        } else {
            return "";
        }
    }
}