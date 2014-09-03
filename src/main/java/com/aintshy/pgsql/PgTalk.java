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
import com.aintshy.api.Messages;
import com.aintshy.api.Talk;
import com.jcabi.aspects.Immutable;
import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;
import java.io.IOException;
import java.sql.SQLException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Talk in PostgreSQL.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
@ToString(of = "number")
@EqualsAndHashCode(of = { "src", "number" })
final class PgTalk implements Talk {

    /**
     * Data source.
     */
    private final transient PgSource src;

    /**
     * Number of it.
     */
    private final transient long number;

    /**
     * Ctor.
     * @param source Data source
     * @param num Number
     */
    PgTalk(final PgSource source, final long num) {
        this.src = source;
        this.number = num;
    }

    @Override
    public long number() {
        return this.number;
    }

    @Override
    public Human asker() throws IOException {
        try {
            return new PgHuman(
                this.src,
                new JdbcSession(this.src.get())
                    .sql("SELECT asker FROM question JOIN talk ON question.id=talk.question AND talk.id=?")
                    .set(this.number)
                    .select(new SingleOutcome<Long>(Long.class))
            );
        } catch (final SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public Human responder() throws IOException {
        try {
            return new PgHuman(
                this.src,
                new JdbcSession(this.src.get())
                    .sql("SELECT responder FROM talk WHERE id=?")
                    .set(this.number)
                    .select(new SingleOutcome<Long>(Long.class))
            );
        } catch (final SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public String question() throws IOException {
        try {
            return new JdbcSession(this.src.get())
                .sql("SELECT text FROM question JOIN talk ON talk.question=question.id WHERE talk.id=?")
                .set(this.number)
                .select(new SingleOutcome<String>(String.class));
        } catch (final SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public Messages messages() {
        return new PgMessages(this.src, this.number);
    }
}
