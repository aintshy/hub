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
        <xsl:apply-templates select="talk"/>
        <xsl:apply-templates select="messages/message"/>
    </xsl:template>
    <xsl:template match="talk">
        <p>
            <xsl:text>talk #</xsl:text>
            <xsl:value-of select="number"/>
        </p>
    </xsl:template>
    <xsl:template match="message">
        <p>
            <xsl:attribute name="class">
                <xsl:text>message message-</xsl:text>
                <xsl:if test="asking = 'true'">
                    <xsl:text>asking</xsl:text>
                </xsl:if>
                <xsl:if test="asking = 'false'">
                    <xsl:text>answering</xsl:text>
                </xsl:if>
            </xsl:attribute>
            <xsl:value-of select="text"/>
        </p>
    </xsl:template>
</xsl:stylesheet>
