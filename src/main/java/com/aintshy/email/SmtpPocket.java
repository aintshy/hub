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

import com.aintshy.api.Pocket;
import com.jcabi.aspects.Immutable;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * SMTP pocket.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.6
 */
@Immutable
@ToString
@EqualsAndHashCode(of = { "address", "postman" })
public final class SmtpPocket implements Pocket {

    /**
     * Email to send to.
     */
    private final transient String address;

    /**
     * SMTP postman.
     */
    private final transient Postman postman;

    /**
     * Ctor.
     * @param addr Email address
     * @param pst Postman
     */
    public SmtpPocket(final String addr, final Postman pst) {
        this.address = addr;
        this.postman = pst;
    }

    @Override
    public void put(final String code) throws IOException {
        final Email email = new SimpleEmail();
        email.setSubject("confirmation code");
        try {
            email.setFrom("aintshy.com <no-reply@aintshy.com>");
            email.setMsg(
                String.format(
                    "Hi, your confirmation code is %s\n\n--\naintshy.com",
                    code
                )
            );
            email.addTo(this.address);
            this.postman.deliver(email);
        } catch (final EmailException ex) {
            throw new IOException(ex);
        }
    }
}
