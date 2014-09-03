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
import java.io.IOException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Jaxb Human.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@XmlRootElement(name = "human")
@XmlAccessorType(XmlAccessType.NONE)
final class JxHuman {

    /**
     * Human.
     */
    private final transient Human human;

    /**
     * Ctor.
     */
    JxHuman() {
        throw new UnsupportedOperationException("#JxHuman()");
    }

    /**
     * Ctor.
     * @param hmn Human
     */
    JxHuman(final Human hmn) {
        this.human = hmn;
    }

    /**
     * His URN.
     * @return URN
     * @throws IOException If fails
     */
    @XmlElement(name = "urn")
    public String getUrn() throws IOException {
        return this.human.urn().toString();
    }

    /**
     * His name.
     * @return Name
     * @throws IOException If fails
     */
    @XmlElement(name = "name")
    public String getName() throws IOException {
        return this.human.profile().name();
    }

    /**
     * His age.
     * @return Age
     * @throws IOException If fails
     */
    @XmlElement(name = "age")
    public int getAge() throws IOException {
        return this.human.profile().age();
    }

    /**
     * His sex.
     * @return Sex
     * @throws IOException If fails
     */
    @XmlElement(name = "sex")
    public String getSex() throws IOException {
        return this.human.profile().sex().toString();
    }

    /**
     * His locale.
     * @return Locale
     * @throws IOException If fails
     */
    @XmlElement(name = "locale")
    public String getLocale() throws IOException {
        return this.human.profile().locale().toString();
    }

}
