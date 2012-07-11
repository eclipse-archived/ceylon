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
package com.redhat.ceylon.tools.importjar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.redhat.ceylon.cmr.api.Logger;

/*
 * More or less copied from the compiler's ShaSigner :(
 */
public class ShaSigner {
    
    public static String sha1(String jarFile, Logger log) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            // can't happen, specs say SHA-1 must be implemented
            log.warning("Failed to get a SHA-1 message digest, your JRE does not follow the specs. "
                    +"No SHA-1 signature will be made");
            return null;
        }
        FileInputStream is;
        File jar = new File(jarFile);
        try {
            is = new FileInputStream(jar);
        } catch (FileNotFoundException e) {
            // can't happen since we just created the file
            log.warning("Failed to open archive file "+jar.getPath()
                    +", no SHA-1 signature will be made");
            return null;
        }
        byte[] buffer = new byte[1024];
     
        int read = 0; 
        try {
            while ((read = is.read(buffer)) != -1) {
              digest.update(buffer, 0, read);
            }
        } catch (IOException e) {
            log.warning("Failed to read archive file "+jar.getPath()
                    +", no SHA-1 signature will be made");
            return null;
        }finally{
            try {
                is.close();
            } catch (IOException e) {
                // don't care
            }
        }
        return toHexString(digest.digest());
    }

    final static char[] Hexadecimal = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

    private static String toHexString(byte[] bytes){
        char[] chars = new char[bytes.length*2];
        for(int b=0,c=0;b<bytes.length;b++){
            int v = (int)bytes[b] & 0xFF;
            chars[c++] = Hexadecimal[v/16];
            chars[c++] = Hexadecimal[v%16];
        }
        return new String(chars);
    }

    
    public static File writeSha1(String sha1, Logger log) {
        File shaFile;
        try{
            shaFile = File.createTempFile("jar", ".sha1");
        }catch (IOException x){
            log.warning("Failed to create temporary file for the SHA-1 signature"
                    +", no SHA-1 signature will be made");
            return null;
        }
        OutputStream os;
        try {
            os = new FileOutputStream(shaFile);
        } catch (FileNotFoundException e) {
            log.warning("Failed to open archive file "+shaFile
                    +" for writing, no SHA-1 signature will be made");
            shaFile.delete();
            return null;
        }
        try {
            os.write(sha1.getBytes("ASCII"));
            return shaFile;
        } catch (UnsupportedEncodingException e) {
            log.warning("Failed to get an ASCII charset, your JRE does not follow the specs. "
                    +"No SHA-1 signature will be made");
            shaFile.delete();
            return null;
        } catch (IOException e) {
            log.warning("Failed to write to "+shaFile.getPath()
                    +", no SHA-1 signature will be made");
            shaFile.delete();
            return null;
        }finally{
            try {
                os.flush();
                os.close();
            } catch (IOException e) {
                // don't care
            }
        }
    }

}
