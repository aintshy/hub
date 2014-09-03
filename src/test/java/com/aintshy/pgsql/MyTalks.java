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
package com.aintshy.pgsql;

import com.aintshy.api.Human;
import com.aintshy.api.Talk;
import com.google.common.collect.Lists;
import com.jcabi.aspects.Immutable;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * All next talks I should look at.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
final class MyTalks {

    /**
     * Human.
     */
    private final transient Human human;

    /**
     * Ctor.
     * @param hmn Human
     */
    MyTalks(final Human hmn) {
        this.human = hmn;
    }

    /**
     * Get them all.
     * @return List of them
     * @throws IOException If fails
     */
    public Collection<Talk> fetch() throws IOException {
        final Collection<Talk> mine = new LinkedList<Talk>();
        while (true) {
            final Iterable<Talk> next = this.human.next();
            if (!next.iterator().hasNext()) {
                break;
            }
            mine.addAll(Lists.newArrayList(next));
            for (final Talk talk : next) {
                talk.messages().iterate();
            }
        }
        return mine;
    }
}
