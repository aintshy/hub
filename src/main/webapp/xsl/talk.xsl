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
        <title><xsl:text>talk</xsl:text></title>
    </xsl:template>
    <xsl:template match="page" mode="body">
        <p style="text-align:center">
            <a href="{links/link[@rel='next']/@href}">
                <xsl:text>next</xsl:text>
            </a>
        </p>
        <xsl:apply-templates select="role"/>
        <form action="{links/link[@rel='post']/@href}" method="post">
            <fieldset>
                <input name="text" style="width:100%" placeholder="Post your answer..." maxlength="140"/>
            </fieldset>
        </form>
        <xsl:apply-templates select="messages/message"/>
        <xsl:apply-templates select="talk/question"/>
    </xsl:template>
    <xsl:template match="role">
        <p style="text-align:center">
            <img src="{links/link[@rel='photo']/@href}" class="photo"/>
            <div>
                <xsl:value-of select="name"/>
                <xsl:text> </xsl:text>
                <xsl:value-of select="sex"/>
                <xsl:text>/</xsl:text>
                <xsl:value-of select="age"/>
            </div>
        </p>
    </xsl:template>
    <xsl:template match="talk/question">
        <p>
            <xsl:attribute name="class">
                <xsl:text>msg msg-</xsl:text>
                <xsl:choose>
                    <xsl:when test="/page/role/asking = 'true'">
                        <xsl:text>his</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>mine</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:value-of select="."/>
        </p>
    </xsl:template>
    <xsl:template match="message">
        <p>
            <xsl:attribute name="class">
                <xsl:text>msg msg-</xsl:text>
                <xsl:choose>
                    <xsl:when test="/page/role/asking = asking">
                        <xsl:text>his</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>mine</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:value-of select="text"/>
        </p>
    </xsl:template>
</xsl:stylesheet>
