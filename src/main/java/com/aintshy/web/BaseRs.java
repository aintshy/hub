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
import com.jcabi.manifests.Manifests;
import com.rexsl.page.BasePage;
import com.rexsl.page.BaseResource;
import com.rexsl.page.Inset;
import com.rexsl.page.Link;
import com.rexsl.page.Resource;
import com.rexsl.page.auth.AuthInset;
import com.rexsl.page.auth.Identity;
import com.rexsl.page.inset.FlashInset;
import com.rexsl.page.inset.LinksInset;
import com.rexsl.page.inset.VersionInset;
import java.io.IOException;
import java.util.logging.Level;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Base RESTful resource.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@Resource.Forwarded
@Inset.Default(LinksInset.class)
public class BaseRs extends BaseResource {

    /**
     * Supplementary inset.
     * @return The inset
     */
    @Inset.Runtime
    public final Inset insetSupplementary() {
        return new Inset() {
            @Override
            public void render(final BasePage<?, ?> page,
                final Response.ResponseBuilder builder) {
                builder.type(MediaType.TEXT_XML);
                builder.header(HttpHeaders.VARY, "Cookie");
            }
        };
    }

    /**
     * Inset with a version of the product.
     * @return The inset
     */
    @Inset.Runtime
    public final Inset insetVersion() {
        return new VersionInset(
            Manifests.read("Aintshy-Version"),
            Manifests.read("Aintshy-Revision"),
            Manifests.read("Aintshy-Date")
        );
    }

    /**
     * Human inset (if logged in).
     * @return The inset
     */
    @Inset.Runtime
    public final Inset humanLinks() {
        return new Inset() {
            @Override
            public void render(final BasePage<?, ?> page,
                final Response.ResponseBuilder builder) {
                if (!BaseRs.this.auth().identity().equals(Identity.ANONYMOUS)) {
                    try {
                        page.append(new JxHuman(BaseRs.this.human()));
                    } catch (final IOException ex) {
                        throw new IllegalStateException(ex);
                    }
                }
            }
        };
    }

    /**
     * Links inset.
     * @return The inset
     */
    @Inset.Runtime
    public final Inset insetLinks() {
        return new Inset() {
            @Override
            public void render(final BasePage<?, ?> page,
                final Response.ResponseBuilder builder) {
                page.link(
                    new Link(
                        "ask",
                        BaseRs.this.uriInfo().getBaseUriBuilder().clone()
                            .path(AskRs.class)
                            .path(AskRs.class, "post")
                    )
                );
                page.link(
                    new Link(
                        "upload-photo",
                        BaseRs.this.uriInfo().getBaseUriBuilder().clone()
                            .path(ProfileRs.class)
                            .path(ProfileRs.class, "upload")
                    )
                );
            }
        };
    }

    /**
     * Authentication inset.
     * @return The inset
     */
    @Inset.Runtime
    public final AuthInset auth() {
        return new AuthInset(this, Manifests.read("Aintshy-SecurityKey"));
    }

    /**
     * Flash.
     * @return The inset with flash
     */
    @Inset.Runtime
    public final FlashInset flash() {
        return new FlashInset(this);
    }

    /**
     * Get current human.
     * @return Human
     */
    protected final Human human() throws IOException {
        final Identity identity = this.auth().identity();
        if (identity.equals(Identity.ANONYMOUS)) {
            throw this.flash().redirect(
                this.uriInfo().getBaseUri(),
                "please login",
                Level.INFO
            );
        }
        try {
            return this.base().human(identity.urn());
        } catch (final Base.HumanNotFoundException ex) {
            throw new WebApplicationException(
                ex,
                Response.seeOther(this.uriInfo().getBaseUri())
                    .cookie(this.auth().logout())
                    .entity(ex.getLocalizedMessage())
                    .build()
            );
        }
    }

    /**
     * Get base.
     * @return Base
     */
    protected final Base base() {
        return Base.class.cast(
            this.servletContext().getAttribute(Base.class.getName())
        );
    }

}
