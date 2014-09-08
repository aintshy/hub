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
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Message.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
public interface Message {

    /**
     * Is it an asking message?
     * @return TRUE if this message is coming from the asker
     * @throws IOException If fails
     */
    boolean asking() throws IOException;

    /**
     * Text.
     * @return Text of it
     * @throws IOException If fails
     */
    String text() throws IOException;

    /**
     * Date.
     * @return Date of it
     * @throws IOException If fails
     */
    Date date() throws IOException;

    /**
     * Simple implementation.
     */
    @Immutable
    @ToString
    @EqualsAndHashCode(of = { "askng", "txt" })
    final class Simple implements Message {
        /**
         * Asking.
         */
        private final transient boolean askng;
        /**
         * Text.
         */
        private final transient String txt;
        /**
         * When.
         */
        private final transient long when;
        /**
         * Ctor.
         * @param asking Asking
         * @param text Text
         */
        public Simple(final boolean asking, final String text) {
            this(asking, text, new Date());
        }
        /**
         * Ctor.
         * @param asking Asking
         * @param text Text
         * @param date Date
         */
        public Simple(final boolean asking, final String text,
            final Date date) {
            this.askng = asking;
            this.txt = text;
            this.when = date.getTime();
        }
        @Override
        public boolean asking() {
            return this.askng;
        }
        @Override
        public String text() {
            return this.txt;
        }
        @Override
        public Date date() {
            return new Date(this.when);
        }
    }

}
