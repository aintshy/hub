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

import com.aintshy.api.Role;
import com.rexsl.page.Link;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Role in JAXB.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.2
 */
@XmlRootElement(name = "role")
@XmlAccessorType(XmlAccessType.NONE)
final class JxRole {

    /**
     * Role.
     */
    private final transient Role role;

    /**
     * Base.
     */
    private final transient BaseRs base;

    /**
     * Ctor.
     */
    JxRole() {
        throw new UnsupportedOperationException("#JxTalker()");
    }

    /**
     * Ctor.
     * @param rle Role
     * @param res Base resource
     */
    JxRole(final Role rle, final BaseRs res) {
        this.role = rle;
        this.base = res;
    }

    /**
     * He is asking?
     * @return TRUE if he is asking
     * @throws IOException If fails
     */
    @XmlElement(name = "asking")
    public boolean isAsking() throws IOException {
        return this.role.isAsking();
    }

    /**
     * His name.
     * @return Name
     * @throws IOException If fails
     */
    @XmlElement(name = "name")
    public String getName() throws IOException {
        return this.role.talker().profile().name();
    }

    /**
     * His age.
     * @return Age
     * @throws IOException If fails
     */
    @XmlElement(name = "age")
    public int getAge() throws IOException {
        return Calendar.getInstance().get(Calendar.YEAR)
            - this.role.talker().profile().year();
    }

    /**
     * His sex.
     * @return Sex
     * @throws IOException If fails
     */
    @XmlElement(name = "sex")
    public String getSex() throws IOException {
        return this.role.talker().profile().sex().toString();
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
                "photo",
                this.base.uriInfo().getBaseUriBuilder().clone()
                    .path(PhotoRs.class)
                    .path(PhotoRs.class, "index")
                    .build(this.role.talker().urn().nss())
            )
        );
        return links;
    }

}
