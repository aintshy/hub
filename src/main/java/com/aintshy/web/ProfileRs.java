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
import com.rexsl.page.Link;
import com.rexsl.page.PageBuilder;
import com.sun.jersey.multipart.FormDataParam;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Profile.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
@Path("/profile")
public final class ProfileRs extends BaseRs {

    /**
     * Upload photo.
     * @return The JAX-RS response
     * @throws IOException If fails
     */
    @GET
    @Path("/photo")
    public Response photo() throws IOException {
        new SafeHuman(this.human(), this);
        return new PageBuilder()
            .stylesheet("/xsl/photo.xsl")
            .build(EmptyPage.class)
            .init(this)
            .link(
                new Link(
                    "upload",
                    this.uriInfo().getBaseUriBuilder().clone()
                        .path(ProfileRs.class)
                        .path(ProfileRs.class, "upload")
                        .build()
                )
            )
            .render()
            .build();
    }

    /**
     * Upload a photo.
     * @param photo Photo to upload
     * @throws IOException If fails
     */
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void upload(@FormDataParam("photo") final InputStream photo)
        throws IOException {
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
