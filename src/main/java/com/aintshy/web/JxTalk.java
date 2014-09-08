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

import com.aintshy.api.Talk;
import java.io.IOException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Jaxb Talk.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@XmlRootElement(name = "talk")
@XmlAccessorType(XmlAccessType.NONE)
final class JxTalk {

    /**
     * Talk.
     */
    private final transient Talk talk;

    /**
     * Ctor.
     */
    JxTalk() {
        throw new UnsupportedOperationException("#JxTalk()");
    }

    /**
     * Ctor.
     * @param tlk Talk
     */
    JxTalk(final Talk tlk) {
        this.talk = tlk;
    }

    /**
     * Its number.
     * @return Number
     */
    @XmlElement(name = "number")
    public long getNumber() {
        return this.talk.number();
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
     * Its locale.
     * @return Locale
     * @throws IOException If fails
     */
    @XmlElement(name = "locale")
    public String getLocale() throws IOException {
        return this.talk.locale().getDisplayLanguage();
    }

}
