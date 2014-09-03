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

import com.aintshy.api.Message;
import com.aintshy.api.Talk;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.rexsl.page.JaxbGroup;
import com.rexsl.page.Link;
import com.rexsl.page.PageBuilder;
import java.io.IOException;
import java.util.logging.Level;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Talk page.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@Path("/{number : \\d+}")
public final class TalkRs extends BaseRs {

    /**
     * Number of the talk.
     */
    private transient long number;

    /**
     * Set number.
     * @param num Talk number
     */
    @PathParam("number")
    public void setNumber(final Long num) {
        this.number = num;
    }

    /**
     * Get index page.
     * @return The JAX-RS response
     * @throws IOException If fails
     */
    @GET
    @Path("/")
    public Response index() throws IOException {
        final Talk talk = this.talk();
        return new PageBuilder()
            .stylesheet("/xsl/talk.xsl")
            .build(EmptyPage.class)
            .init(this)
            .link(new Link("post", "./post"))
            .link(new Link("next", "."))
            .append(new JxTalk(talk))
            .append(
                JaxbGroup.build(
                    Collections2.transform(
                        Lists.newArrayList(talk.messages().iterate()),
                        new Function<Message, Object>() {
                            @Override
                            public JxMessage apply(final Message msg) {
                                return new JxMessage(msg);
                            }
                        }
                    ),
                    "messages"
                )
            )
            .render()
            .build();
    }

    /**
     * Answer the talk.
     * @param text Text of the answer
     * @throws IOException If fails
     */
    @POST
    @Path("/post")
    public void answer(
        @FormParam("text") final String text) throws IOException {
        final Talk talk = this.talk();
        talk.messages().post(talk.asker().equals(this.human()), text);
        throw this.flash().redirect(
            this.uriInfo().getBaseUri(),
            "thanks for the message",
            Level.INFO
        );
    }

    /**
     * Get current talk.
     * @return Talk
     */
    private Talk talk() {
        return this.human().talk(this.number);
    }

}
