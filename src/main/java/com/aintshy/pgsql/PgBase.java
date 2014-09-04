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

import com.aintshy.api.Base;
import com.aintshy.api.Human;
import com.jcabi.aspects.Cacheable;
import com.jcabi.aspects.Immutable;
import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.Outcome;
import com.jcabi.jdbc.SingleOutcome;
import com.jcabi.log.Logger;
import com.jcabi.manifests.Manifests;
import com.jcabi.urn.URN;
import com.jolbox.bonecp.BoneCPDataSource;
import java.io.IOException;
import java.sql.SQLException;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Base in PostgreSQL.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@Immutable
@ToString
@EqualsAndHashCode(of = "src")
public final class PgBase implements Base {

    /**
     * Data source.
     */
    private final transient PgSource src;

    /**
     * Ctor.
     */
    public PgBase() {
        this(PgBase.bonecp());
    }

    /**
     * Ctor.
     * @param data Data source
     */
    public PgBase(@NotNull final DataSource data) {
        this.src = new PgSource() {
            @Override
            public DataSource get() {
                return data;
            }
        };
    }

    @Override
    public Human register(final String email, final String password)
        throws IOException {
        if (!email.matches(".+@.+")) {
            throw new Base.InvalidEmailFormatException(
                "email format is not correct"
            );
        }
        if (!password.matches(".{3,}")) {
            throw new Base.InvalidPasswordException(
                "password is not good enough, should be 3+ characters"
            );
        }
        Long number;
        try {
            number = new JdbcSession(this.src.get())
                .sql("SELECT id FROM human WHERE email=?")
                .set(email)
                .select(new SingleOutcome<Long>(Long.class, true));
            if (number == null) {
                number = new JdbcSession(this.src.get())
                    .sql("INSERT INTO human (email, password) VALUES (?, ?)")
                    .set(email)
                    .set(password)
                    .insert(new SingleOutcome<Long>(Long.class));
                Logger.info(this, "user #%d registered as %s", number, email);
            } else {
                final String pwd = new JdbcSession(this.src.get())
                    .sql("SELECT password FROM human WHERE id=?")
                    .set(number)
                    .select(new SingleOutcome<String>(String.class));
                if (!pwd.equals(password)) {
                    throw new Base.InvalidPasswordException("invalid password");
                }
            }
        } catch (final SQLException ex) {
            throw new IOException(ex);
        }
        return new PgHuman(this.src, number);
    }

    @Override
    public Human human(final URN urn) throws IOException {
        final long number = Long.parseLong(urn.nss());
        final boolean exists;
        try {
            exists = new JdbcSession(this.src.get())
                .sql("SELECT id FROM human WHERE id=?")
                .set(number)
                .select(Outcome.NOT_EMPTY);
        } catch (final SQLException ex) {
            throw new IOException(ex);
        }
        if (!exists) {
            throw new Base.HumanNotFoundException(
                String.format("user #%d not found", number)
            );
        }
        return new PgHuman(this.src, number);
    }

    /**
     * Get source.
     * @return Source
     */
    public PgSource source() {
        return this.src;
    }

    /**
     * Data source.
     * @return Source
     */
    @Cacheable(forever = true)
    private static DataSource bonecp() {
        final BoneCPDataSource src = new BoneCPDataSource();
        src.setDriverClass("org.postgresql.Driver");
        src.setJdbcUrl(Manifests.read("Aintshy-PgsqlJdbc"));
        src.setUser(Manifests.read("Aintshy-PgsqlUser"));
        src.setPassword(Manifests.read("Aintshy-PgsqlPassword"));
        return src;
    }

}
