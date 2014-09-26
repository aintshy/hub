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
import com.aintshy.api.Human;
import com.aintshy.api.Talk;
import com.aintshy.email.Postman;
import com.aintshy.email.SmtpPocket;
import com.google.common.collect.Iterables;
import com.rexsl.page.Link;
import com.rexsl.page.PageBuilder;
import com.rexsl.page.auth.Identity;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

/**
 * Anonymous usage page.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
@Path("/")
public final class AnonymousRs extends BaseRs {

    /**
     * Front page with login.
     * @return The JAX-RS response
     * @throws IOException If fails
     */
    @GET
    @Path("/")
    public Response index() throws IOException {
        if (!this.auth().identity().equals(Identity.ANONYMOUS)) {
            final Iterable<Talk> talks =
                new SafeHuman(this.human(), this).next();
            if (Iterables.isEmpty(talks)) {
                throw new WebApplicationException(
                    Response.seeOther(
                        this.uriInfo().getBaseUriBuilder().clone()
                            .path(AnonymousRs.class)
                            .path(AnonymousRs.class, "empty")
                            .build()
                    ).build()
                );
            }
            throw new WebApplicationException(
                Response.seeOther(
                    this.uriInfo().getBaseUriBuilder()
                        .clone()
                        .path(TalkRs.class)
                        .path(TalkRs.class, "index")
                        .build(talks.iterator().next().number())
                ).build()
            );
        }
        return new PageBuilder()
            .stylesheet("/xsl/index.xsl")
            .build(EmptyPage.class)
            .init(this)
            .link(new Link("enter", "./enter"))
            .link(new Link("remind", "./remind"))
            .render()
            .build();
    }

    /**
     * Nothing to show, no talks.
     * @return JAX-RS response
     * @throws IOException If fails
     */
    @GET
    @Path("/empty")
    public Response empty() throws IOException {
        this.human();
        return new PageBuilder()
            .stylesheet("/xsl/empty.xsl")
            .build(EmptyPage.class)
            .init(this)
            .render()
            .build();
    }

    /**
     * Remind by email.
     * @param email Email
     * @throws IOException If fails
     */
    @POST
    @Path("/remind")
    public void resend(@FormParam("email") final String email)
        throws IOException {
        this.base().remind(
            email,
            new SmtpPocket(
                email,
                "password remind",
                "Hey, here is your password: %s",
                Postman.class.cast(
                    this.servletContext().getAttribute(
                        Postman.class.getName()
                    )
                )
            )
        );
        throw this.flash().redirect(
            this.uriInfo().getBaseUri(),
            "check your inbox",
            Level.INFO
        );
    }

    /**
     * Enter the system.
     * @param email Email
     * @param password Password
     * @throws IOException If fails
     */
    @POST
    @Path("/enter")
    public void enter(@FormParam("email") final String email,
        @FormParam("password") final String password) throws IOException {
        final Human human;
        try {
            human = this.base().register(
                email, password,
                new SmtpPocket(
                    email,
                    "confirmation code",
                    "Hi, your confirmation code is: %s",
                    Postman.class.cast(
                        this.servletContext().getAttribute(
                            Postman.class.getName()
                        )
                    )
                )
            );
        } catch (final Base.InvalidPasswordException ex) {
            throw this.flash().redirect(this.uriInfo().getBaseUri(), ex);
        } catch (final Base.InvalidEmailFormatException ex) {
            throw this.flash().redirect(this.uriInfo().getBaseUri(), ex);
        } catch (final Base.InvalidPasswordFormatException ex) {
            throw this.flash().redirect(this.uriInfo().getBaseUri(), ex);
        }
        final Identity identity = new Identity.Simple(
            human.urn(),
            human.urn().nss(),
            URI.create("http://img.aintshy.com/no-photo.png")
        );
        final NewCookie cookie = this.auth().cookie(identity);
        throw new WebApplicationException(
            Response.seeOther(this.uriInfo().getBaseUri())
                .cookie(cookie)
                .header("X-Aintshy-Token", cookie.getValue())
                .build()
        );
    }

}
