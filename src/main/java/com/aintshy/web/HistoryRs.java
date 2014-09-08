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

import com.aintshy.api.Human;
import com.aintshy.api.Talk;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.jcabi.aspects.Tv;
import com.rexsl.page.JaxbGroup;
import com.rexsl.page.Link;
import com.rexsl.page.PageBuilder;
import java.io.IOException;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * History page.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.4
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@Path("/history")
public final class HistoryRs extends BaseRs {

    /**
     * Since.
     */
    private transient long since = Long.MAX_VALUE;

    /**
     * Set since.
     * @param date Date
     */
    @QueryParam("since")
    public void setSince(final Long date) {
        if (date != null) {
            this.since = date;
        }
    }

    /**
     * Get index page.
     * @return The JAX-RS response
     * @throws IOException If fails
     */
    @GET
    @Path("/")
    public Response index() throws IOException {
        final List<Talk> talks = Lists.newArrayList(
            Iterables.limit(this.human().history().iterate(), Tv.TWENTY)
        );
        EmptyPage page = new PageBuilder()
            .stylesheet("/xsl/history.xsl")
            .build(EmptyPage.class)
            .init(this);
        if (!talks.isEmpty()) {
            page = page.link(
                new Link(
                    "more",
                    this.uriInfo()
                        .getBaseUriBuilder().clone()
                        .path(HistoryRs.class)
                        .path(HistoryRs.class, "index")
                        .queryParam("since", "{d}")
                        .build(
                            talks.get(talks.size() - 1)
                                .messages().iterate()
                                .iterator().next()
                                .date().getTime()
                        )
                )
            );
        }
        final Human human = this.human();
        return page.append(
            JaxbGroup.build(
                Collections2.transform(
                    Lists.newArrayList(this.human().history().iterate()),
                    new Function<Talk, JxStory>() {
                        @Override
                        public JxStory apply(final Talk talk) {
                            return new JxStory(talk, human, HistoryRs.this);
                        }
                    }
                ),
                "history"
            )
        ).render().build();
    }

}
