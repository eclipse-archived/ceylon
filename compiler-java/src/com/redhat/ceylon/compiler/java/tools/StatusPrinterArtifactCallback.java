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

package com.redhat.ceylon.compiler.java.tools;

import java.io.File;

import com.redhat.ceylon.cmr.api.ArtifactCallback;
import com.redhat.ceylon.common.StatusPrinter;

/**
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
final class StatusPrinterArtifactCallback implements ArtifactCallback {

    private StatusPrinter sp;
    private long size;
    private long read;
    private int previousPercentage;

    StatusPrinterArtifactCallback(StatusPrinter sp) {
        this.sp = sp;
    }


    @Override
    public void start(String nodeFullPath, long size, String contentStore) {
        this.size = size;
        read = 0;
        previousPercentage = 0;
        this.sp.logCapturedLine();
        // leave 6 for size
        int fitOn = Math.max(0, this.sp.getRemaining() - 6);
        this.sp.log(" from "+contentStore+" ("+(size/1024)+"kb)", fitOn);
        this.sp.captureLine();
    }

    @Override
    public void read(byte[] bytes, int length) {
        read += length;
        if(size != -1){
            int percentage = (int) Math.floor((((double)read)/size) * 100);
            if(previousPercentage != percentage){
                this.sp.logCapturedLine();
                this.sp.logRight(" "+percentage+"% ");
                previousPercentage = percentage;
            }
        }else{
            this.sp.logRight(" "+(read/1024)+"kb ");
        }
    }

    @Override
    public void done(File localFile) {
    }

    @Override
    public void error(File localFile, Throwable err) {
    }
}