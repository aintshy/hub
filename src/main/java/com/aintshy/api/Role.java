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
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Role in a talk.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.2
 */
@Immutable
@ToString
@EqualsAndHashCode(of = { "talk", "human" })
public final class Role {

    /**
     * Talk.
     */
    private final transient Talk talk;

    /**
     * Who is looking into it.
     */
    private final transient Human human;

    /**
     * Ctor.
     * @param tlk Talk
     * @param hmn Human
     */
    public Role(final Talk tlk, final Human hmn) {
        this.talk = tlk;
        this.human = hmn;
    }

    /**
     * Is he asking.
     * @return TRUE if that talker is asking me
     * @throws IOException If fails
     */
    public boolean isAsking() throws IOException {
        return this.talk.responder().equals(this.human);
    }

    /**
     * Get talker.
     * @return Human that talks to me
     * @throws IOException If fails
     */
    public Human talker() throws IOException {
        Human talker = this.talk.asker();
        if (talker.equals(this.human)) {
            talker = this.talk.responder();
        }
        return talker;
    }

}
