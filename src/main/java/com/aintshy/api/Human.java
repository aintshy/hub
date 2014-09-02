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
package com.aintshy.api;

import com.jcabi.aspects.Immutable;
import com.jcabi.urn.URN;

/**
 * Human.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
public interface Human {

    /**
     * His unique URN.
     * @return URN
     */
    URN urn();

    /**
     * His profile.
     * @return Profile
     */
    Profile profile();

    /**
     * Post a new question.
     * @param text Text to post
     */
    void ask(String text);

    /**
     * Get talk by ID.
     * @param number Talk number
     * @return Talk
     */
    Talk talk(long number);

    /**
     * Get next talk to watch.
     * @return Talk
     */
    Talk next();

}
