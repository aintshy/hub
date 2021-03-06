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
import java.io.IOException;
import java.util.Locale;

/**
 * Human profile.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
@SuppressWarnings("PMD.TooManyMethods")
public interface Profile {

    /**
     * Email confirmed?
     * @return TRUE if confirmed already
     * @throws IOException If fails
     */
    boolean confirmed() throws IOException;

    /**
     * Confirm email.
     * @param code Code to use
     * @throws IOException If fails
     */
    void confirm(String code) throws IOException;

    /**
     * Resend confirmation code.
     * @param pocket Where to put it
     * @throws IOException If fails
     */
    void resend(Pocket pocket) throws IOException;

    /**
     * His name.
     * @return Name
     * @throws IOException If fails
     */
    String name() throws IOException;

    /**
     * His email.
     * @return Email
     * @throws IOException If fails
     */
    String email() throws IOException;

    /**
     * His year of birth.
     * @return Year of birth
     * @throws IOException If fails
     */
    int year() throws IOException;

    /**
     * His sex.
     * @return Sex ("male", "female", or "third")
     * @throws IOException If fails
     */
    Sex sex() throws IOException;

    /**
     * His language.
     * @return Language
     * @throws IOException If fails
     */
    Locale locale() throws IOException;

    /**
     * Update profile.
     * @param name Name
     * @param age Age
     * @param sex Sex
     * @param locale Locale
     * @throws IOException If fails
     * @checkstyle ParameterNumberCheck (5 lines)
     */
    void update(String name, int age, Sex sex, Locale locale)
        throws IOException;

    /**
     * Get photo.
     * @return PNG image
     * @throws IOException If fails
     */
    byte[] photo() throws IOException;

    /**
     * Upload photo.
     * @param bytes PNG image
     * @throws IOException If fails
     */
    void photo(byte[] bytes) throws IOException;

    /**
     * Profile can't be updated.
     */
    final class UpdateException extends RuntimeException {
        /**
         * Serialization marker.
         */
        private static final long serialVersionUID = 305929936831895556L;
        /**
         * Ctor.
         * @param cause Cause
         */
        public UpdateException(final String cause) {
            super(cause);
        }
    }

    /**
     * Confirmation code is wrong.
     * @since 0.6
     */
    final class ConfirmCodeException extends RuntimeException {
        /**
         * Serialization marker.
         */
        private static final long serialVersionUID = 305929936831895556L;
        /**
         * Ctor.
         * @param cause Cause
         */
        public ConfirmCodeException(final String cause) {
            super(cause);
        }
    }

}
