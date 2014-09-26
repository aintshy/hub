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
package com.aintshy.api.mock;

import com.aintshy.api.Profile;
import com.aintshy.api.Sex;
import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Tv;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

/**
 * Mock Profile.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
public final class MkProfile implements Profile {

    @Override
    public boolean confirmed() throws IOException {
        return true;
    }

    @Override
    public void confirm(final String code) throws IOException {
        throw new UnsupportedOperationException("#confirm()");
    }

    @Override
    public String name() throws IOException {
        return "Jeff Lebowski";
    }

    @Override
    public String email() throws IOException {
        return "test@aintshy.com";
    }

    @Override
    public int year() throws IOException {
        return Calendar.getInstance().get(Calendar.YEAR) - Tv.THIRTY;
    }

    @Override
    public Sex sex() throws IOException {
        return Sex.M;
    }

    @Override
    public Locale locale() throws IOException {
        return Locale.ENGLISH;
    }

    // @checkstyle ParameterNumberCheck (5 lines)
    @Override
    public void update(final String name, final int age,
        final Sex sex, final Locale locale) {
        throw new UnsupportedOperationException("#update()");
    }

    @Override
    public byte[] photo() throws IOException {
        return new byte[0];
    }

    @Override
    public void photo(final byte[] bytes) throws IOException {
        throw new UnsupportedOperationException("#photo()");
    }
}
