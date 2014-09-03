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
import com.aintshy.api.Profile;
import com.aintshy.api.Talk;
import com.google.common.base.Joiner;
import com.jcabi.aspects.Immutable;
import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;
import com.jcabi.log.Logger;
import com.jcabi.urn.URN;
import java.io.IOException;
import java.sql.SQLException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Human in PostgreSQL.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
@ToString
@EqualsAndHashCode(of = { "src", "number" })
final class PgHuman implements Human {

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
    PgHuman(final PgSource source, final long num) {
        this.src = source;
        this.number = num;
    }

    @Override
    public URN urn() {
        return URN.create(String.format("urn:aintshy:%d", this.number));
    }

    @Override
    public Profile profile() {
        return new PgProfile(this.src, this.number);
    }

    @Override
    public void ask(final String text) throws IOException {
        try {
            final long num = new JdbcSession(this.src.get())
                .sql("INSERT INTO question (asker, text) VALUES (?, ?)")
                .set(this.number)
                .set(text)
                .insert(new SingleOutcome<Long>(Long.class, true));
            Logger.info(this, "question #%d asked by #%d", num, this.number);
        } catch (final SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public Talk talk(final long num) {
        return new PgTalk(this.src, num);
    }

    @Override
    public Talk next() throws IOException {
        try {
            Talk talk = this.unread();
            if (Talk.EMPTY.equals(talk)) {
                talk = this.fresh();
                if (Talk.EMPTY.equals(talk)) {
                    talk = this.start();
                }
            }
            return talk;
        } catch (final SQLException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * Start new talk for the current human.
     * @return Talk
     */
    private Talk start() throws SQLException {
        final Long question = new JdbcSession(this.src.get())
            .sql(
                Joiner.on(' ').join(
                    "SELECT question.id FROM question",
                    "LEFT JOIN talk",
                    "ON talk.question=question.id AND responder=?",
                    "WHERE talk.id IS NULL AND asker != ?",
                    "LIMIT 1"
                )
            )
            .set(this.number)
            .set(this.number)
            .select(new SingleOutcome<Long>(Long.class, true));
        final Talk talk;
        if (question == null) {
            talk = Talk.EMPTY;
        } else {
            talk = new PgTalk(
                this.src,
                new JdbcSession(this.src.get())
                    .sql("INSERT INTO talk (question, responder) VALUES (?, ?)")
                    .set(question)
                    .set(this.number)
                    .insert(new SingleOutcome<Long>(Long.class))
            );
            Logger.info(
                this, "talk #%d started by #%d from question #%d",
                talk.number(), this.number, question
            );
        }
        return talk;
    }

    /**
     * Get unread talk.
     * @return Unread talk
     * @throws SQLException If fails
     */
    private Talk unread() throws SQLException {
        final Long num = new JdbcSession(this.src.get())
            .sql(
                Joiner.on(' ').join(
                    "SELECT talk FROM message",
                    "JOIN talk ON talk.id=message.talk",
                    "JOIN question ON question.id=talk.question",
                    "WHERE seen=false",
                    "AND ((asking=false AND responder=?)",
                    "OR (asking=true AND asker=?))",
                    "ORDER BY message.date DESC",
                    "LIMIT 1"
                )
            )
            .set(this.number)
            .set(this.number)
            .select(new SingleOutcome<Long>(Long.class, true));
        final Talk talk;
        if (num == null) {
            talk = Talk.EMPTY;
        } else {
            talk = new PgTalk(this.src, num);
        }
        return talk;
    }

    /**
     * Get fresh talk, with no messages, but waiting for response.
     * @return Fresh talk
     * @throws SQLException If fails
     */
    private Talk fresh() throws SQLException {
        final Long num = new JdbcSession(this.src.get())
            .sql(
                Joiner.on(' ').join(
                    "SELECT talk.id FROM talk",
                    "LEFT JOIN message ON message.talk=talk.id",
                    "WHERE message.id IS NULL",
                    "AND responder = ?",
                    "ORDER BY talk.date DESC",
                    "LIMIT 1"
                )
            )
            .set(this.number)
            .select(new SingleOutcome<Long>(Long.class, true));
        final Talk talk;
        if (num == null) {
            talk = Talk.EMPTY;
        } else {
            talk = new PgTalk(this.src, num);
        }
        return talk;
    }
}
