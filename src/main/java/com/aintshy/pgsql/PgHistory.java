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

import com.aintshy.api.History;
import com.aintshy.api.Talk;
import com.google.common.base.Joiner;
import com.jcabi.aspects.Immutable;
import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.Utc;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * History in PostgreSQL.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.4
 */
@Immutable
@ToString(of = "number")
@EqualsAndHashCode(of = { "src", "number" })
final class PgHistory implements History {

    /**
     * Data source.
     */
    private final transient PgSource src;

    /**
     * Human number.
     */
    private final transient long number;

    /**
     * Since this date.
     */
    private final transient long date;

    /**
     * Ctor.
     * @param source Data source
     * @param num Number
     */
    PgHistory(final PgSource source, final long num) {
        this(source, num, Long.MAX_VALUE);
    }

    /**
     * Ctor.
     * @param source Data source
     * @param num Number
     * @param since Since
     */
    PgHistory(final PgSource source, final long num, final long since) {
        this.src = source;
        this.number = num;
        this.date = since;
    }

    @Override
    public Iterable<Talk> iterate() throws IOException {
        final Date since;
        if (this.date == Long.MAX_VALUE) {
            since = new Date();
        } else {
            since = new Date(this.date);
        }
        try {
            return new JdbcSession(this.src.get())
                .set(this.number)
                .set(this.number)
                .set(new Utc(since))
                .sql(
                    Joiner.on(' ').join(
                        "SELECT talk, MAX(message.date) AS dt FROM message",
                        "JOIN talk ON talk.id=message.talk",
                        "JOIN question q ON q.id=talk.question",
                        "WHERE responder=? OR asker=?",
                        "AND message.date < ?",
                        "GROUP BY talk",
                        "ORDER BY dt DESC"
                    )
                )
                .select(
                    new ListOutcome<Talk>(
                        new ListOutcome.Mapping<Talk>() {
                            @Override
                            public Talk map(final ResultSet rset)
                                throws SQLException {
                                return new PgTalk(
                                    PgHistory.this.src,
                                    rset.getLong(1)
                                );
                            }
                        }
                    )
                );
        } catch (final SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public History since(final Date since) {
        return new PgHistory(this.src, this.number, since.getTime());
    }
}
