import glob
import os
import subprocess

java_home = os.path.join(
    os.environ["HOME"], "work/olmec/build/linux-amd64/j2sdk-image")

classpath = ":".join((
    "lib/antlrworks-1.3.1.jar",
    os.path.join(java_home, "lib/tools.jar"),
    "build/classes"))

paths = glob.glob("corpus/tests/*.ceylon")
paths.sort()
for path in paths:
    out, err = subprocess.Popen(
        (os.path.join(java_home, "bin/java"),
         "-ea",
         "-cp", classpath,
         "com.redhat.ceylon.compiler.launcher.CeylonCompiler",
         "-t",
         path),
        stdout = subprocess.PIPE,
        stderr = subprocess.PIPE).communicate()
    success = err == path + "\n"
    colour = success and 32 or 31
    print "\x1B[%dm%s\x1B[0m" % (colour, path)
