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

import com.aintshy.api.Profile;
import com.aintshy.api.Sex;
import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Tv;
import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.Outcome;
import com.jcabi.jdbc.Preparation;
import com.jcabi.jdbc.SingleOutcome;
import com.jcabi.log.Logger;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Profile in PostgreSQL.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@Immutable
@ToString(of = "number")
@EqualsAndHashCode(of = { "src", "number" })
@SuppressWarnings("PMD.TooManyMethods")
final class PgProfile implements Profile {

    /**
     * Regex for user name.
     */
    private static final Pattern RE_NAME = Pattern.compile(
        "[a-zA-Z0-9\\- ]{4,36}"
    );

    /**
     * Regex for confirmation code.
     * @since 0.6
     */
    private static final Pattern RE_CODE = Pattern.compile("[0-9]{4}");

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
    PgProfile(final PgSource source, final long num) {
        this.src = source;
        this.number = num;
    }

    @Override
    public boolean confirmed() throws IOException {
        try {
            return new JdbcSession(this.src.get())
                .sql("SELECT id FROM human WHERE id=? AND code='0000'")
                .set(this.number)
                .select(Outcome.NOT_EMPTY);
        } catch (final SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void confirm(final String code) throws IOException {
        if (!PgProfile.RE_CODE.matcher(code).matches()) {
            throw new Profile.ConfirmCodeException(
                "the code always contains four digits"
            );
        }
        try {
            final int done = new JdbcSession(this.src.get())
                .sql("UPDATE human SET code='0000' WHERE id=? AND code=?")
                .set(this.number)
                .set(code)
                .update(Outcome.UPDATE_COUNT);
            if (done == 0) {
                throw new Profile.ConfirmCodeException(
                    "the code doesn't match our records"
                );
            }
            Logger.info(this, "email confirmed by human #%d", this.number);
        } catch (final SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public String name() throws IOException {
        try {
            return new JdbcSession(this.src.get())
                .sql("SELECT name FROM human WHERE id=?")
                .set(this.number)
                .select(new SingleOutcome<String>(String.class));
        } catch (final SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public String email() throws IOException {
        try {
            return new JdbcSession(this.src.get())
                .sql("SELECT email FROM human WHERE id=?")
                .set(this.number)
                .select(new SingleOutcome<String>(String.class));
        } catch (final SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public int year() throws IOException {
        try {
            return new JdbcSession(this.src.get())
                .sql("SELECT year FROM human WHERE id=?")
                .set(this.number)
                .select(new SingleOutcome<Long>(Long.class))
                .intValue();
        } catch (final SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public Sex sex() throws IOException {
        try {
            return Sex.valueOf(
                new JdbcSession(this.src.get())
                    .sql("SELECT sex FROM human WHERE id=?")
                    .set(this.number)
                    .select(new SingleOutcome<String>(String.class))
            );
        } catch (final SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public Locale locale() throws IOException {
        try {
            return new Locale(
                new JdbcSession(this.src.get())
                    .sql("SELECT locale FROM human WHERE id=?")
                    .set(this.number)
                    .select(new SingleOutcome<String>(String.class))
            );
        } catch (final SQLException ex) {
            throw new IOException(ex);
        }
    }

    // @checkstyle ParameterNumberCheck (5 lines)
    @Override
    public void update(final String name, final int year,
        final Sex sex, final Locale locale) throws IOException {
        if (!PgProfile.RE_NAME.matcher(name).matches()) {
            throw new Profile.UpdateException(
                "name should be 4-36 letters, spaces or numbers"
            );
        }
        final int age = Calendar.getInstance().get(Calendar.YEAR) - year;
        if (age <= Tv.TEN) {
            throw new Profile.UpdateException(
                "you're too young for our system"
            );
        }
        if (age > Tv.HUNDRED) {
            throw new Profile.UpdateException(
                "you're too old for us, sorry"
            );
        }
        try {
            new JdbcSession(this.src.get())
                // @checkstyle LineLength (1 line)
                .sql("UPDATE human SET name=?, year=?, sex=sex(?), locale=? WHERE id=?")
                .set(name)
                .set(year)
                .set(sex)
                .set(locale.getLanguage())
                .set(this.number)
                .update(Outcome.VOID);
        } catch (final SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public byte[] photo() throws IOException {
        try {
            return new JdbcSession(this.src.get())
                .sql("SELECT photo FROM human WHERE id=?")
                .set(this.number)
                .select(
                    new Outcome<byte[]>() {
                        @Override
                        public byte[] handle(final ResultSet rset,
                            final Statement stmt) throws SQLException {
                            if (!rset.next()) {
                                throw new IllegalStateException("no human");
                            }
                            return rset.getBytes(1);
                        }
                    }
                );
        } catch (final SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void photo(final byte[] bytes) throws IOException {
        try {
            new JdbcSession(this.src.get())
                .sql("UPDATE human SET photo=? WHERE id=?")
                .prepare(
                    new Preparation() {
                        @Override
                        public void prepare(final PreparedStatement stmt)
                            throws SQLException {
                            stmt.setBytes(1, bytes);
                            stmt.setLong(2, PgProfile.this.number);
                        }
                    }
                )
                .update(Outcome.VOID);
        } catch (final SQLException ex) {
            throw new IOException(ex);
        }
    }
}
