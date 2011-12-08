/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.ceylondoc;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String destDir = null;
        String srcDir = null;
        boolean showPrivate = false;
        boolean omitSource = false;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("-d".equals(arg)) {
                System.err.println("-d: option not yet supported (though perhaps you meant -out?)");
                System.exit(1);
            } else if ("-out".equals(arg)) {
                destDir = args[++i];
            } else if ("-src".equals(arg)) {
                srcDir = args[++i];
            } else if ("-rep".equals(arg)) {
                System.err.println("-rep: option not yet supported");
                System.exit(1);
            } else if ("-private".equals(arg)) {
                showPrivate = true;
            } else if ("-omit-source".equals(arg)) {
                omitSource = true;
            } else {
                System.err.println("Processing modules by name is not supported yet");                
            }
            
        }
        if (destDir == null) {
            System.err.println("-out <dest-dir>: option required");
            System.exit(1);
        }
        if (srcDir == null) {
            System.err.println("-src <src-dir>: option required");
            System.exit(1);
        }

        File file = new File(srcDir);
        if (file.exists() == false) {
            System.err.println(srcDir + " is not a file or directory");
            System.exit(1);
        }

        CeylonDocTool ceylonDocTool = new CeylonDocTool(file);
        ceylonDocTool.setShowPrivate(showPrivate);
        ceylonDocTool.setDestDir(destDir);
        ceylonDocTool.setSrcDir(srcDir);
        ceylonDocTool.setOmitSource(omitSource);
        ceylonDocTool.makeDoc();
    }
}
