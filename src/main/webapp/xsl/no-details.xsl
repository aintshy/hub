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
        <title><xsl:text>not enough details</xsl:text></title>
    </xsl:template>
    <xsl:template match="page" mode="body">
        <p>
            <xsl:text>
                This is the last step. We need to know
                a little bit about you, in order to match
                you with others. Please, try to be accurate :)
            </xsl:text>
        </p>
        <form action="{links/link[@rel='details']/@href}" method="post">
            <fieldset>
                <input name="name" size="20" placeholder="Name..."/>
                <input name="age" size="4" placeholder="Age..."/>
                <select name="sex">
                    <option value="M">male</option>
                    <option selected="selected" value="F">female</option>
                    <option value="T">third</option>
                </select>
                <select name="lang">
                    <option selected="selected" value="EN">English</option>
                    <option value="SP">Spanish</option>
                    <option value="ZH">Chinese</option>
                    <option value="AR">Arabic</option>
                    <option value="HI">Hindi</option>
                    <option value="RU">Russian</option>
                </select>
                <input type="submit" value="Save"/>
            </fieldset>
        </form>
    </xsl:template>
</xsl:stylesheet>
