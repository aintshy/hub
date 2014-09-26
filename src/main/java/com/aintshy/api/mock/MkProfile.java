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

import com.aintshy.api.Pocket;
import com.aintshy.api.Profile;
import com.aintshy.api.Sex;
import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Tv;
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
    public boolean confirmed() {
        return true;
    }

    @Override
    public void confirm(final String code) {
        throw new UnsupportedOperationException("#confirm()");
    }

    @Override
    public void resend(final Pocket pocket) {
        throw new UnsupportedOperationException("#resend()");
    }

    @Override
    public String name() {
        return "Jeff Lebowski";
    }

    @Override
    public String email() {
        return "test@aintshy.com";
    }

    @Override
    public int year() {
        return Calendar.getInstance().get(Calendar.YEAR) - Tv.THIRTY;
    }

    @Override
    public Sex sex() {
        return Sex.M;
    }

    @Override
    public Locale locale() {
        return Locale.ENGLISH;
    }

    // @checkstyle ParameterNumberCheck (5 lines)
    @Override
    public void update(final String name, final int age,
        final Sex sex, final Locale locale) {
        throw new UnsupportedOperationException("#update()");
    }

    @Override
    public byte[] photo() {
        return new byte[0];
    }

    @Override
    public void photo(final byte[] bytes) {
        throw new UnsupportedOperationException("#photo()");
    }
}
