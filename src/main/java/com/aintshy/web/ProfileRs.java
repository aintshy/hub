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

import com.jcabi.aspects.Tv;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Profile.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@Path("/profile")
public final class ProfileRs extends BaseRs {

    /**
     * Upload a photo.
     * @throws IOException If fails
     */
    @POST
    @Path("/upload")
    public void upload(final InputStream photo) throws IOException {
        final BufferedImage image = ImageIO.read(photo);
        final Image thumb = image.getScaledInstance(
            Tv.FOUR * Tv.HUNDRED, -1, Image.SCALE_SMOOTH
        );
        final BufferedImage nice = new BufferedImage(
            thumb.getWidth(null), thumb.getHeight(null),
            BufferedImage.TYPE_INT_RGB
        );
        nice.getGraphics().drawImage(thumb, 0, 0, null);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(nice, "png", baos);
        new SafeHuman(this.human(), this).profile().photo(baos.toByteArray());
        throw this.flash().redirect(
            this.uriInfo().getBaseUri(),
            "photo updated, thanks",
            Level.INFO
        );
    }

}
