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

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Tv;
import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.Outcome;
import com.jcabi.log.Logger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Dump of the entire DB.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
@ToString
@EqualsAndHashCode(of = "src")
final class PgDump {

    /**
     * Data source.
     */
    private final transient PgSource src;

    /**
     * Ctor.
     * @param data Data source
     */
    PgDump(final PgSource data) {
        this.src = data;
    }

    /**
     * Dump the entire DB to log (for testing mostly).
     * @throws SQLException If fails
     */
    void print() throws SQLException {
        final String[] tables = {"human", "question", "talk", "message"};
        final JdbcSession jdbc = new JdbcSession(this.src.get());
        final StringBuilder txt = new StringBuilder(Tv.THOUSAND);
        final Outcome<Void> outcome = new Outcome<Void>() {
            @Override
            public Void handle(final ResultSet rset,
                final Statement stmt) throws SQLException {
                final ResultSetMetaData meta = rset.getMetaData();
                final int cols = meta.getColumnCount();
                txt.append('\n').append(meta.getTableName(0)).append('\n');
                for (int col = 1; col <= cols; ++col) {
                    txt.append(meta.getColumnName(col)).append('|');
                }
                txt.append('\n');
                while (rset.next()) {
                    for (int col = 1; col <= cols; ++col) {
                        txt.append(rset.getString(col)).append('|');
                    }
                    txt.append('\n');
                }
                return null;
            }
        };
        for (final String table : tables) {
            jdbc.sql(String.format("SELECT * FROM %s", table)).select(outcome);
        }
        Logger.info(this, txt.toString());
    }

}
