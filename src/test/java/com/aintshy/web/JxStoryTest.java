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

import com.aintshy.api.mock.MkHuman;
import com.aintshy.api.mock.MkTalk;
import com.jcabi.matchers.JaxbConverter;
import com.jcabi.matchers.XhtmlMatchers;
import com.rexsl.page.mock.ResourceMocker;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

/**
 * Test case for {@link JxStory}.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.5.2
 */
public final class JxStoryTest {

    /**
     * JxStory can be converted to XML.
     * @throws Exception If fails
     */
    @Test
    public void convertsToXml() throws Exception {
        final BaseRs base = new ResourceMocker().mock(BaseRs.class);
        final JxStory story = new JxStory(
            new MkTalk(), new MkHuman(), base
        );
        MatcherAssert.assertThat(
            JaxbConverter.the(story),
            XhtmlMatchers.hasXPaths(
                "/story/role[name='Jeff Lebowski']",
                "/story/role[age=30]",
                "/story/role[sex='M']",
                "/story/role/links/link[@rel='photo']",
                "/story/messages",
                "/story/question",
                "/story/number"
            )
        );
    }

}
