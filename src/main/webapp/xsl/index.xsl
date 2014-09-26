<?xml version="1.0"?>
<!--
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
 -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml" version="1.0">
    <xsl:output method="xml" omit-xml-declaration="yes"/>
    <xsl:include href="/xsl/layout.xsl"/>
    <xsl:template match="page" mode="head">
        <title><xsl:text>login</xsl:text></title>
    </xsl:template>
    <xsl:template match="page" mode="body">
        <p>
            <xsl:text>
                Please, enter your email and password and we let you in.
                If you've never been here before, give us your
                email and any password, we'll register you right away.
            </xsl:text>
        </p>
        <form action="{links/link[@rel='enter']/@href}" method="post">
            <fieldset>
                <input name="email" size="30" placeholder="Email..."/>
                <input name="password" type="password" size="23" placeholder="Password..."/>
                <input type="submit" value="Login"/>
            </fieldset>
        </form>
        <p>
            <xsl:text>
                We never share your email with anyone and never send
                any promotional messages.
            </xsl:text>
        </p>
        <p>
            <xsl:text>
                Already registered here but forgot your password? No
                problem. Give us your email and we'll send you the
                password right away.
            </xsl:text>
        </p>
        <form action="{links/link[@rel='remind']/@href}" method="post">
            <fieldset>
                <input name="email" size="30" placeholder="Email..."/>
                <input type="submit" value="Remind"/>
            </fieldset>
        </form>
    </xsl:template>
</xsl:stylesheet>
