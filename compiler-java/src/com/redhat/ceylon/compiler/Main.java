package com.redhat.ceylon.compiler;

import java.lang.reflect.Method;

import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JavacFileManager;


public class Main extends com.sun.tools.javac.Main {

	/** Programmatic interface to the Java Programming Language
	 * compiler, javac.
	 *
	 * @param args The command line arguments that would normally be
	 * passed to the javac program as described in the man page.
	 * @return an integer equivalent to the exit value from invoking
	 * javac, see the man page for details.
	 */
	public static int compile(String[] args) {
		com.sun.tools.javac.main.Main compiler =
			new com.redhat.ceylon.compiler.launcher.Main("javac");
		return compiler.compile(args);
	}

	/** Unsupported command line interface.
     * @param args   The command line parameters.
     */
    public static void main(String[] args) throws Exception {
      if (args.length > 0 && args[0].equals("-Xjdb")) {
        String[] newargs = new String[args.length + 2];
        Class<?> c = Class.forName("com.sun.tools.example.debug.tty.TTY");
        Method method = c.getDeclaredMethod ("main", new Class[] {args.getClass()});
        method.setAccessible(true);
        System.arraycopy(args, 1, newargs, 3, args.length - 1);
        newargs[0] = "-connect";
        newargs[1] = "com.sun.jdi.CommandLineLaunch:options=-esa -ea:com.sun.tools...";
        newargs[2] = "com.sun.tools.javac.Main";
        method.invoke(null, new Object[] { newargs });
      } else {
        System.exit(compile(args));
      }
    }

}
