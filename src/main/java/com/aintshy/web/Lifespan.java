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
package com.aintshy.web;

import com.aintshy.api.Base;
import com.aintshy.email.Postman;
import com.aintshy.pgsql.PgBase;
import com.jcabi.aspects.Loggable;
import com.jcabi.manifests.Manifests;
import java.io.IOException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Lifespan.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@ToString
@EqualsAndHashCode
@Loggable(Loggable.INFO)
public final class Lifespan implements ServletContextListener {

    @Override
    public void contextInitialized(final ServletContextEvent event) {
        final Base base;
        try {
            Manifests.append(event.getServletContext());
            base = new PgBase();
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
        event.getServletContext().setAttribute(Base.class.getName(), base);
        event.getServletContext().setAttribute(
            Postman.class.getName(), Lifespan.postman()
        );
    }

    @Override
    public void contextDestroyed(final ServletContextEvent event) {
        // nothing
    }

    /**
     * Make a postman.
     * @return Postman
     */
    private static Postman postman() {
        final Postman postman;
        final String host = Manifests.read("Aintshy-SmtpHost");
        if ("test".equals(host)) {
            postman = Postman.CONSOLE;
        } else {
            postman = new Postman.Default(
                host,
                Integer.parseInt(Manifests.read("Aintshy-SmtpPort")),
                Manifests.read("Aintshy-SmtpUser"),
                Manifests.read("Aintshy-SmtpPassword")
            );
        }
        return postman;
    }

}
