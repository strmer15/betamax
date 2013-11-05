/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.freeside.betamax.io;

import java.text.Normalizer;

public final class FilenameNormalizer {

    public String toFilename(String tapeName) {
        return Normalizer.normalize(tapeName, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("[^\\w\\d]+", "_")
                .replaceFirst("^_", "")
                .replaceFirst("_$", "");
    }

    private static final FilenameNormalizer INSTANCE = new FilenameNormalizer();

    public static FilenameNormalizer getInstance() {
        return INSTANCE;
    }
}