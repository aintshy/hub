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

import com.aintshy.api.Profile;
import com.aintshy.api.Sex;
import com.aintshy.email.Postman;
import com.aintshy.email.SmtpPocket;
import com.rexsl.page.Link;
import com.rexsl.page.PageBuilder;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Setup pages.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
@Path("/setup")
public final class SetupRs extends BaseRs {

    /**
     * Email is not confirmed.
     * @return The JAX-RS response
     * @throws IOException If fails
     */
    @GET
    @Path("/not-confirmed")
    public Response notConfirmed() throws IOException {
        final Profile profile = this.human().profile();
        if (profile.confirmed()) {
            throw this.flash().redirect(
                this.uriInfo().getBaseUri(),
                String.format("email %s is already confirmed", profile.email()),
                Level.INFO
            );
        }
        return new PageBuilder()
            .stylesheet("/xsl/not-confirmed.xsl")
            .build(EmptyPage.class)
            .init(this)
            .link(
                new Link(
                    "confirm",
                    this.uriInfo().getBaseUriBuilder().clone()
                        .path(SetupRs.class)
                        .path(SetupRs.class, "confirm")
                        .build()
                )
            )
            .link(
                new Link(
                    "resend",
                    this.uriInfo().getBaseUriBuilder().clone()
                        .path(SetupRs.class)
                        .path(SetupRs.class, "resend")
                        .build()
                )
            )
            .render()
            .build();
    }

    /**
     * Confirm email.
     * @param code Code
     * @throws IOException If fails
     */
    @POST
    @Path("/confirm")
    public void confirm(@FormParam("code") final String code)
        throws IOException {
        final Profile profile = this.human().profile();
        try {
            profile.confirm(code);
        } catch (final Profile.ConfirmCodeException ex) {
            throw this.flash().redirect(this.uriInfo().getBaseUri(), ex);
        }
        throw this.flash().redirect(
            this.uriInfo().getBaseUri(),
            String.format("your email %s is confirmed", profile.email()),
            Level.INFO
        );
    }

    /**
     * Resend code by email.
     * @throws IOException If fails
     */
    @POST
    @Path("/resend")
    public void resend() throws IOException {
        final Profile profile = this.human().profile();
        profile.resend(
            new SmtpPocket(
                profile.email(),
                "confirmation code (resend)",
                "Hey, here is your code again: %s",
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
     * No details yet.
     * @return The JAX-RS response
     * @throws IOException If fails
     */
    @GET
    @Path("/no-details")
    public Response noDetails() throws IOException {
        final Profile profile = this.human().profile();
        if (profile.year() != 0) {
            throw this.flash().redirect(
                this.uriInfo().getBaseUri(),
                "your details are all set",
                Level.INFO
            );
        }
        return new PageBuilder()
            .stylesheet("/xsl/no-details.xsl")
            .build(EmptyPage.class)
            .init(this)
            .link(
                new Link(
                    "details",
                    this.uriInfo().getBaseUriBuilder().clone()
                        .path(SetupRs.class)
                        .path(SetupRs.class, "details")
                        .build()
                )
            )
            .render()
            .build();
    }

    /**
     * Set profile details.
     * @param year Year
     * @param sex Sex
     * @param name Name
     * @param lang Lang
     * @throws IOException If fails
     * @checkstyle ParameterNumberCheck (10 lines)
     */
    @POST
    @Path("/details")
    @SuppressWarnings("PMD.UseObjectForClearerAPI")
    public void details(
        @FormParam("year") final String year,
        @FormParam("sex") final String sex,
        @FormParam("name") final String name,
        @FormParam("lang") final String lang) throws IOException {
        if (!year.matches("[0-9]{4}")) {
            throw this.flash().redirect(
                this.uriInfo().getBaseUri(),
                "year of birth is not valid",
                Level.WARNING
            );
        }
        if (!sex.matches("M|F|T")) {
            throw this.flash().redirect(
                this.uriInfo().getBaseUri(),
                "sex is not valid",
                Level.WARNING
            );
        }
        if (!lang.matches("[a-z]{2}")) {
            throw this.flash().redirect(
                this.uriInfo().getBaseUri(),
                "language (locale) is in wrong format",
                Level.WARNING
            );
        }
        final Profile profile = this.human().profile();
        try {
            profile.update(
                name, Integer.parseInt(year),
                Sex.valueOf(sex), new Locale(lang)
            );
        } catch (final Profile.UpdateException ex) {
            throw this.flash().redirect(this.uriInfo().getBaseUri(), ex);
        }
        throw this.flash().redirect(
            this.uriInfo().getBaseUri(),
            "profile updated successfully",
            Level.INFO
        );
    }
}
