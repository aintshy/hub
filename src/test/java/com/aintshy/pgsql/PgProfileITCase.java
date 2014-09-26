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
import com.aintshy.api.Profile;
import com.aintshy.api.Sex;
import com.jcabi.aspects.Tv;
import java.util.Calendar;
import java.util.Locale;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Integration case for {@link PgProfile}.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
public final class PgProfileITCase {

    /**
     * PgProfile can expose data.
     * @throws Exception If fails
     */
    @Test
    public void exposesData() throws Exception {
        final Base base = new PgBase();
        final Human human = base.register(
            "h3es@aintshy.com", "-9w8skkha", Pocket.CONSOLE
        );
        final Profile profile = human.profile();
        final int year = Calendar.getInstance().get(Calendar.YEAR) - Tv.THIRTY;
        profile.update("Jeffrey", year, Sex.M, Locale.GERMAN);
        MatcherAssert.assertThat(profile.email(), Matchers.startsWith("h3es"));
        MatcherAssert.assertThat(profile.year(), Matchers.equalTo(year));
        MatcherAssert.assertThat(profile.sex(), Matchers.equalTo(Sex.M));
        MatcherAssert.assertThat(profile.locale(), Matchers.is(Locale.GERMAN));
        MatcherAssert.assertThat(profile.name(), Matchers.startsWith("Jeff"));
    }

    /**
     * PgProfile can update photo.
     * @throws Exception If fails
     */
    @Test
    public void updatesPhoto() throws Exception {
        final Base base = new PgBase();
        final Human human = base.register(
            "f8ke@aintshy.com", "-9w9lKha", Pocket.CONSOLE
        );
        final Profile profile = human.profile();
        final byte[] bytes = {0x00, 0x7f, 0x1f, 0x09};
        profile.photo(bytes);
        MatcherAssert.assertThat(
            profile.photo(), Matchers.equalTo(bytes)
        );
    }

}
