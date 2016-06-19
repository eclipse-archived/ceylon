import ceylon.buffer.charset {
    utf8
}
import de.dlkw.ccrypto.svc {
    sha1
}

// TODO FsModule, Stats, PathModule should of course all be from TypeScript, and fs, path loaded via module system

dynamic FsModule {
    shared formal Integer openSync(String path, String mode);
    shared formal void closeSync(Integer fd);
    shared formal void writeSync(Integer fd, String data, Integer? position, String encoding);
    shared formal void mkdirSync(String path);
    shared formal Stats statSync(String path);
}

dynamic Stats {
    shared formal Boolean isDirectory();
}

dynamic PathModule {
    shared formal String dirname(String path);
}

FsModule fs { dynamic { return require("fs"); } }
PathModule path { dynamic { return require("path"); } }

"Recursively creates all parent directories of this directory (and this directory itself)."
void mkdirWithParents(String dir) {
    try {
        if (fs.statSync(dir).isDirectory()) {
            // okay
            return;
        } else {
            throw Exception("Path ``dir`` exists and is not a directory`");
        }
    } catch (Throwable t) {
        if (t.message == "Path ``dir`` exists and is not a directory`") {
            throw t;
        } else {
            // statSync threw an exception – path does not exist
            mkdirWithParents(path.dirname(dir));
            fs.mkdirSync(dir);
        }
    }
}

"A file writer for Node.js that also writes an accompanying `.sha1` file."
class Sha1Writer(String path) satisfies Destroyable {
    mkdirWithParents(package.path.dirname(path));
    Integer fd = fs.openSync(path, "w");
    
    value digester = sha1();
    
    shared void write(String text) {
        fs.writeSync(fd, text, null, "utf8");
        value buffer = utf8.encodeBuffer(text);
        digester.update(buffer);
    }
    
    shared void writeLine(String line) {
        write(line + operatingSystem.newline);
    }
    
    shared actual void destroy(Throwable? error) {
        fs.closeSync(fd);
        if (!error exists) {
            Integer fd2 = fs.openSync(path + ".sha1", "w");
            try {
                for (byte in digester.digest()) {
                    fs.writeSync(fd2, formatInteger(byte.unsigned, 16).padLeading(2, '0'), null, "utf8");
                }
            } finally {
                fs.closeSync(fd2);
            }
        }
    }
}
