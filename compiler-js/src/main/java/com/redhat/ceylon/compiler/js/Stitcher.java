package com.redhat.ceylon.compiler.js;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/** A simple program that takes the main JS module file and replaces #include markers with the contents of other files.
 * 
 * @author Enrique Zamudio
 */
public class Stitcher {

    private static void stitch(File infile, PrintWriter writer) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile), "UTF-8"));
        try {
            String line = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    if (line.startsWith("//#include ")) {
                        File auxfile = new File(infile.getParentFile(), line.substring(11).trim());
                        if (auxfile.exists() && auxfile.isFile() && auxfile.canRead()) {
                            stitch(auxfile, writer);
                        } else {
                            throw new IllegalArgumentException("Invalid included file " + auxfile);
                        }
                    } else if (!line.endsWith("//IGNORE")) {
                        writer.println(line);
                    }
                }
            }
        } finally {
            if (reader != null) reader.close();
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("This program requires 2 arguments to run:");
            System.err.println("1. The path to the main JS file");
            System.err.println("2. The destination directory for the processed file");
            System.exit(1);
            return;
        }
        File infile = new File(args[0]);
        if (infile.exists() && infile.isFile() && infile.canRead()) {
            File outdir = new File(args[1]);
            if (outdir.exists() && outdir.isDirectory() && outdir.canWrite()) {
                File outfile = new File(outdir, infile.getName());
                PrintWriter writer = new PrintWriter(outfile, "UTF-8");
                stitch(infile, writer);
                writer.close();
            } else {
                System.err.println("Output directory is invalid: " + outdir);
                System.exit(3);
            }
        } else {
            System.err.println("Input file is invalid: " + infile);
            System.exit(2);
        }
    }
}
