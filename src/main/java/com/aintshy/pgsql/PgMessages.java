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

import com.aintshy.api.Message;
import com.aintshy.api.Messages;
import com.google.common.base.Joiner;
import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Tv;
import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.Outcome;
import com.jcabi.jdbc.SingleOutcome;
import com.jcabi.jdbc.Utc;
import com.jcabi.log.Logger;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Messages in a talk in PostgreSQL.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
@ToString(of = "number")
@EqualsAndHashCode(of = { "src", "number" })
final class PgMessages implements Messages {

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
    PgMessages(final PgSource source, final long num) {
        this.src = source;
        this.number = num;
    }

    @Override
    public Message post(final boolean asking,
        final String text) throws IOException {
        if (text.isEmpty()) {
            throw new IllegalArgumentException("text can't be empty");
        }
        try {
            new JdbcSession(this.src.get())
                // @checkstyle LineLength (1 line)
                .sql("INSERT INTO message (talk, asking, text) VALUES (?, ?, ?)")
                .set(this.number)
                .set(asking)
                .set(text)
                .insert(Outcome.VOID);
            Logger.info(this, "posted in #%d, asking=%B", this.number, asking);
            return new Message.Simple(asking, text);
        } catch (final SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public Iterable<Message> iterate() throws IOException {
        try {
            final Collection<Message> msgs = new JdbcSession(this.src.get())
                .set(this.number)
                .sql(
                    Joiner.on(' ').join(
                        "SELECT asking, text, date FROM message",
                        "WHERE talk=? ORDER BY date DESC"
                    )
                )
                .select(
                    new ListOutcome<Message>(
                        new ListOutcome.Mapping<Message>() {
                            @Override
                            public Message map(final ResultSet rset)
                                throws SQLException {
                                return PgMessages.message(rset);
                            }
                        }
                    )
                );
            if (!msgs.isEmpty()) {
                new JdbcSession(this.src.get())
                    // @checkstyle LineLength (1 line)
                    .sql("UPDATE message SET seen=now() WHERE talk=? AND seen IS NULL")
                    .set(this.number)
                    .update(Outcome.VOID);
                Logger.info(this, "%d seen in #%d", msgs.size(), this.number);
            }
            return msgs;
        } catch (final SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public int size() throws IOException {
        try {
            return new JdbcSession(this.src.get())
                .sql("SELECT COUNT(id) FROM message WHERE talk=?")
                .set(this.number)
                .select(new SingleOutcome<Long>(Long.class))
                .intValue();
        } catch (final SQLException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * Make a message from result set.
     * @param rset Result set
     * @return Message
     * @throws SQLException If fails
     */
    private static Message message(final ResultSet rset) throws SQLException {
        return new Message.Simple(
            rset.getBoolean(1),
            rset.getString(2),
            Utc.getTimestamp(rset, Tv.THREE)
        );
    }

}
