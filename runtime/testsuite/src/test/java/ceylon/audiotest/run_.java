/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.audiotest;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.AudioFileFormat.Type;

/**
 * @author Tako Schotanus
 */
public class run_ {
    public static void main(String[] args) throws Exception {
        Mixer mixer = AudioSystem.getMixer(null);
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        Type[] fileTypes = AudioSystem.getAudioFileTypes();
        boolean moduleHasMixer = mixer != null;
        int moduleMixerCount = mixers.length;
        int moduleFileTypeCount = fileTypes.length;
        System.out.println("Number of mixers/filetypes using Ceylon runtime = " + moduleMixerCount + "/" + moduleFileTypeCount);
        
        boolean plainHasMixer = Boolean.valueOf(System.getProperty("ceylon.runtime.test.services.audiotest.hasmixer"));
        if (plainHasMixer != moduleHasMixer) {
            throw new AssertionError("Getting default mixer gives different result when obtained from plain Java versus the Ceylon runtime");
        }
        int plainMixerCount = Integer.valueOf(System.getProperty("ceylon.runtime.test.services.audiotest.mixers"));
        if (plainMixerCount != moduleMixerCount) {
            throw new AssertionError("Number of mixers not equal when obtained from plain Java versus the Ceylon runtime");
        }
        int plainFileTypesCount = Integer.valueOf(System.getProperty("ceylon.runtime.test.services.audiotest.filetypes"));
        if (plainFileTypesCount != moduleFileTypeCount) {
            throw new AssertionError("Number of filetypes not equal when obtained from plain Java versus the Ceylon runtime");
        }
        System.out.println("Everything OK");
    }
}
