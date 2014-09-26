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
package com.aintshy.pgsql;

import com.aintshy.api.Base;
import com.aintshy.api.Human;
import com.aintshy.api.Pocket;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * Integration case for {@link PgBase}.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
public final class PgBaseITCase {

    /**
     * PgBase can register a human and confirm.
     * @throws Exception If fails
     */
    @Test
    public void registersHumanAndConfirms() throws Exception {
        final Base base = new PgBase();
        final String email = "t@aintshy.com";
        final String password = "\u20ac=*'";
        final Pocket pocket = Mockito.mock(Pocket.class);
        final Human first = base.register(email, password, pocket);
        final ArgumentCaptor<String> captor =
            ArgumentCaptor.forClass(String.class);
        Mockito.verify(pocket).put(captor.capture());
        MatcherAssert.assertThat(
            first.profile().confirmed(),
            Matchers.is(false)
        );
        first.profile().confirm(captor.getValue());
        final Human second = base.register(email, password, Pocket.CONSOLE);
        MatcherAssert.assertThat(
            second.profile().confirmed(),
            Matchers.is(true)
        );
    }

}
