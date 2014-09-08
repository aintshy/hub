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
                <label for="name">Everybody will see this name of yours</label>
                <input id="name" name="name" size="20" placeholder="Name..."/>
                <label for="year">We don't need an exact date, just a year</label>
                <input id="year" name="year" size="10" placeholder="Year of birth..."/>
                <label for="sex">Yes, we need to know your gender</label>
                <select id="sex" name="sex">
                    <option value="M">male</option>
                    <option selected="selected" value="F">female</option>
                    <option value="T">third</option>
                </select>
                <label for="lang">Which language you prefer to speak?</label>
                <select id="lang" name="lang">
                    <option selected="selected" value="EN">English</option>
                    <option value="ZH">Chinese</option>
                    <option value="AR">Arabic</option>
                    <option value="HI">Hindi</option>
                    <option value="SP">Spanish</option>
                    <option value="RU">Russian</option>
                </select>
                <input type="submit" value="Save"/>
            </fieldset>
        </form>
        <p>
            <xsl:text>
                You will NOT be able to change this information later,
                please be careful and accurate. The information you enter here will
                be publicly available to all other users.
            </xsl:text>
        </p>
    </xsl:template>
</xsl:stylesheet>
