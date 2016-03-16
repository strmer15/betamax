/*
 * Copyright 2011 the original author or authors.
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

package software.betamax.tape.yaml;

import org.yaml.snakeyaml.nodes.Tag;
import software.betamax.io.FileResolver;
import software.betamax.message.Request;
import software.betamax.message.Response;
import software.betamax.tape.MemoryTape;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

class YamlTape extends MemoryTape {

    private static final Logger LOG = Logger.getLogger(YamlTape.class.getName());

    public static final Tag TAPE_TAG = new Tag("!tape");
    public static final Tag FILE_TAG = new Tag("!file");

    private transient volatile boolean dirty;
    private transient ReadWriteLock dirtyLock = new ReentrantReadWriteLock();

    YamlTape(FileResolver fileResolver) {
        super(fileResolver);
    }

    @Override
    public boolean isDirty() {
        dirtyLock.readLock().lock();
        try {
            return dirty;
        } finally {
            dirtyLock.readLock().unlock();
        }
    }

    @Override
    public void record(Request request, Response response) {
        dirtyLock.writeLock().lock();
        try {
            LOG.info("Recording the YamlTape");
            super.record(request, response);
            LOG.info("Setting the YamlTape to dirty");
            dirty = true;
        } finally {
            dirtyLock.writeLock().unlock();
        }
    }
}