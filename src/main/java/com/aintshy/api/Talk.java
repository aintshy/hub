/**
 * Copyright (c) 2014, Aintshy.com
 * All rights reserved.
 *
 * Redistribution and use in source or binary forms, with or without
 * modification, are NOT permitted.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.aintshy.api;

import com.jcabi.aspects.Immutable;
import java.io.IOException;

/**
 * Talk.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
public interface Talk {

    /**
     * Nothing to talk about.
     */
    Talk EMPTY = new Talk() {
        @Override
        public long number() {
            throw new UnsupportedOperationException("#number()");
        }
        @Override
        public Human asker() throws IOException {
            throw new UnsupportedOperationException("#asker()");
        }
        @Override
        public Human responder() throws IOException {
            throw new UnsupportedOperationException("#responder()");
        }
        @Override
        public Messages messages() {
            throw new UnsupportedOperationException("#messages()");
        }
    };

    /**
     * Its number.
     * @return The number
     */
    long number();

    /**
     * Who asked the question.
     * @return Human who asked
     */
    Human asker() throws IOException;

    /**
     * Who is answering.
     * @return Human who is answering
     */
    Human responder() throws IOException;

    /**
     * All messages of it.
     * @return All messages
     */
    Messages messages();

}
