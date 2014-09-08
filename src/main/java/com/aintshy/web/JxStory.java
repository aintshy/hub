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
import com.aintshy.api.Role;
import com.aintshy.api.Talk;
import com.rexsl.page.Link;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * JAXB story.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.4
 */
@XmlRootElement(name = "story")
@XmlAccessorType(XmlAccessType.NONE)
final class JxStory {

    /**
     * Talk.
     */
    private final transient Talk talk;

    /**
     * Human.
     */
    private final transient Human human;

    /**
     * Base.
     */
    private final transient BaseRs base;

    /**
     * Ctor.
     */
    JxStory() {
        throw new UnsupportedOperationException("#JxStory()");
    }

    /**
     * Ctor.
     * @param tlk Role
     * @param self Self
     * @param res Base resource
     */
    JxStory(final Talk tlk, final Human self, final BaseRs res) {
        this.talk = tlk;
        this.base = res;
        this.human = self;
    }

    /**
     * Its question.
     * @return Question text
     * @throws IOException If fails
     */
    @XmlElement(name = "question")
    public String getQuestion() throws IOException {
        return this.talk.question();
    }

    /**
     * Total number of messages.
     * @return Total messages
     * @throws IOException If fails
     */
    @XmlElement(name = "messages")
    public int getMessages() throws IOException {
        return this.talk.messages().size();
    }

    /**
     * Role.
     * @return Role
     * @throws IOException If fails
     */
    @XmlElement(name = "role")
    public JxRole getRole() throws IOException {
        return new JxRole(new Role(this.talk, this.human), this.base);
    }

    /**
     * Its links.
     * @return Links
     * @throws IOException If fails
     */
    @XmlElementWrapper(name = "links")
    @XmlElement(name = "link")
    public Collection<Link> getLinks() throws IOException {
        final Collection<Link> links = new LinkedList<Link>();
        links.add(
            new Link(
                "open",
                this.base.uriInfo().getBaseUriBuilder().clone()
                    .path(TalkRs.class)
                    .path(TalkRs.class, "index")
                    .build(this.talk.number())
            )
        );
        return links;
    }

}
