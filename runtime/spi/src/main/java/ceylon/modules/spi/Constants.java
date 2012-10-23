/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
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

package ceylon.modules.spi;


/**
 * Ceylon constants.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public enum Constants {
    IMPL_ARGUMENT_PREFIX("+"),
    CEYLON_ARGUMENT_PREFIX("-"),
    DEFAULT("default"),
    DEFAULT_VERSION("main"),
    ARGUMENTS("args"),
    SOURCES("sources"),
    CLASSES("classes"),
    MODULE_PATH("-mp"),
    CEYLON_RUNTIME_MODULE("ceylon.runtime"),
    MERGE_STRATEGY("com.redhat.ceylon.cmr.spi.MergeStrategy"),
    CONTENT_TRANSFORMER("com.redhat.ceylon.cmr.spi.ContentTransformer");

    private String value;

    Constants(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
