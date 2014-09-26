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
package com.aintshy.email;

import com.jcabi.aspects.Immutable;
import com.jcabi.log.Logger;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

/**
 * Postman.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.6
 */
@Immutable
public interface Postman {

    /**
     * Console only.
     */
    Postman CONSOLE = new Postman() {
        @Override
        public void deliver(final Email email) {
            Logger.info(this, "email to %s", email.getToAddresses());
        }
    };

    /**
     * Create an email to send.
     * @param email Email to deliver
     * @throws IOException If fails
     */
    void deliver(Email email) throws IOException;

    /**
     * Default.
     */
    @Immutable
    @ToString
    @EqualsAndHashCode(of = { "host", "port", "user", "password" })
    final class Default implements Postman {
        /**
         * SMTP host.
         */
        private final transient String host;
        /**
         * SMTP port.
         */
        private final transient int port;
        /**
         * SMTP user name.
         */
        private final transient String user;
        /**
         * SMTP password.
         */
        private final transient String password;
        /**
         * Ctor.
         * @param hst Host
         * @param prt Port
         * @param login Login
         * @param pwd Password
         * @checkstyle ParameterNumberCheck (5 lines)
         */
        public Default(final String hst, final int prt,
            final String login, final String pwd) {
            this.host = hst;
            this.port = prt;
            this.user = login;
            this.password = pwd;
        }
        @Override
        public void deliver(final Email email) throws IOException {
            email.setHostName(this.host);
            email.setSmtpPort(this.port);
            email.setAuthenticator(
                new DefaultAuthenticator(this.user, this.password)
            );
            email.setSSLOnConnect(true);
            try {
                email.send();
            } catch (final EmailException ex) {
                throw new IOException(ex);
            }
        }
    }

}
