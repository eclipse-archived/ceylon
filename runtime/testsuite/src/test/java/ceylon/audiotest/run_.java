/*
 * Copyright 2014 Red Hat inc. and third party contributors as noted
 * by the author tags.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ceylon.audiotest;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.AudioFileFormat.Type;

/**
 * @author Tako Schotanus
 */
public class run_ {
    public static void main(String[] args) throws Exception {
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        Type[] fileTypes = AudioSystem.getAudioFileTypes();
        int moduleMixerCount = mixers.length;
        int moduleFileTypeCount = fileTypes.length;
        System.out.println("Number of mixers/filetypes using Ceylon runtime = " + moduleMixerCount + "/" + moduleFileTypeCount);
        
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
