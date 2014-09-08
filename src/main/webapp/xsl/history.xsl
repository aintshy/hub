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
        <title><xsl:text>history</xsl:text></title>
    </xsl:template>
    <xsl:template match="page" mode="body">
        <xsl:if test="not(history/story)">
            <p>
                <xsl:text>
                    No history for you yet...
                </xsl:text>
            </p>
        </xsl:if>
        <xsl:apply-templates select="history/story"/>
    </xsl:template>
    <xsl:template match="story">
        <p>
            <img src="{role/links/link[@rel='photo']/@href}" class="history-photo"/>
            <br/>
            <span>
                <xsl:value-of select="role/name"/>
                <xsl:text> </xsl:text>
                <xsl:value-of select="role/sex"/>
                <xsl:text>/</xsl:text>
                <xsl:value-of select="role/age"/>
            </span>
            <br/>
            <a href="{links/link[@rel='open']/@href}">
                <xsl:value-of select="question"/>
            </a>
        </p>
    </xsl:template>
</xsl:stylesheet>
