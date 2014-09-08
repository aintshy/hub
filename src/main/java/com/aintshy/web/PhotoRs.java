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

import com.jcabi.urn.URN;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;

/**
 * Photo.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@Path("/photo")
public final class PhotoRs extends BaseRs {

    /**
     * Show a photo.
     * @param num ID of the user
     * @return PNG
     * @throws IOException If fails
     */
    @GET
    @Path("/{id : \\d+}.png")
    @Produces("image/png")
    public Response index(@PathParam("id") final String num)
        throws IOException {
        final URN urn = URN.create(String.format("urn:aintshy:%s", num));
        final byte[] png = this.base().human(urn).profile().photo();
        if (png == null) {
            throw new WebApplicationException(
                Response.seeOther(
                    URI.create("http://img.aintshy.com/no-photo.png")
                ).build()
            );
        }
        final CacheControl cache = new CacheControl();
        cache.setMaxAge((int) TimeUnit.HOURS.toSeconds(1L));
        cache.setPrivate(false);
        return Response.ok(new ByteArrayInputStream(png))
            .cacheControl(cache)
            .type("image/png")
            .build();
    }

}
