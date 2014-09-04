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
    <xsl:template match="page">
        <xsl:text disable-output-escaping="yes">&lt;!DOCTYPE html&gt;</xsl:text>
        <html lang="en">
            <head>
                <meta charset="UTF-8"/>
                <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                <meta name="author" content="www.aintshy.com"/>
                <link rel="stylesheet" type="text/css" media="all" href="/css/style.css?{version/revision}"/>
                <link rel="icon" type="image/gif" href="//img.aintshy.com/logo.png?{version/revision}"/>
                <xsl:apply-templates select="." mode="head"/>
                <script type="text/javascript">//<![CDATA[
                    var _gaq = _gaq || [];
                    _gaq.push(['_setAccount', 'UA-1963507-36']);
                    _gaq.push(['_trackPageview']);
                    (function() {
                        var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
                        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                        var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
                    })();
                //]]></script>
            </head>
            <body>
                <div class="wrapper">
                    <header class="head">
                        <div>
                            <a href="{links/link[@rel='home']/@href}">
                                <img src="//img.aintshy.com/logo.svg" class="logo" alt="aintshy logo"/>
                            </a>
                        </div>
                        <xsl:apply-templates select="human"/>
                    </header>
                    <xsl:apply-templates select="flash"/>
                    <div class="main">
                        <xsl:apply-templates select="." mode="body"/>
                    </div>
                    <footer class="foot">
                        <ul>
                            <xsl:apply-templates select="version"/>
                            <xsl:apply-templates select="millis"/>
                            <li title="server load average">
                                <xsl:attribute name="class">
                                    <xsl:choose>
                                        <xsl:when test="@sla &gt; 6">
                                            <xsl:text>error</xsl:text>
                                        </xsl:when>
                                        <xsl:when test="@sla &gt; 3">
                                            <xsl:text>warning</xsl:text>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:text>inherit</xsl:text>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:attribute>
                                <xsl:value-of select="@sla"/>
                            </li>
                        </ul>
                        <!--
                        <ul>
                            <li>
                                <a href="http://www.teamed.io">
                                    <img src="//img.teamed.io/btn.svg" alt="teamed.io logo"/>
                                </a>
                            </li>
                        </ul>
                        -->
                    </footer>
                </div>
            </body>
        </html>
    </xsl:template>
    <xsl:template match="human">
        <ul>
            <li>
                <xsl:value-of select="name"/>
            </li>
            <li>
                <xsl:value-of select="age"/>
            </li>
            <li>
                <xsl:value-of select="sex"/>
            </li>
            <li>
                <xsl:value-of select="locale"/>
            </li>
            <li>
                <a title="log out" href="{/page/links/link[@rel='rexsl:logout']/@href}">
                    <xsl:text>exit</xsl:text>
                </a>
            </li>
        </ul>
        <form action="{/page/links/link[@rel='ask']/@href}" method="post">
            <fieldset class="inline">
                <input style="width:90%" name="text" placeholder="Ask a question..." maxlength="140"/>
            </fieldset>
        </form>
        <form action="{/page/links/link[@rel='upload-photo']/@href}" method="post" enctype="multipart/form-data">
            <fieldset class="inline">
                <input type="file" name="photo"/>
                <input type="submit" value="Upload photo"/>
            </fieldset>
        </form>
    </xsl:template>
    <xsl:template match="page/millis">
        <xsl:variable name="msec" select="number(.)"/>
        <li title="page load time">
            <xsl:attribute name="class">
                <xsl:choose>
                    <xsl:when test="$msec &gt; 5000">
                        <xsl:text>error</xsl:text>
                    </xsl:when>
                    <xsl:when test="$msec &gt; 1000">
                        <xsl:text>warning</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>inherit</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:choose>
                <xsl:when test="$msec &gt; 1000">
                    <xsl:value-of select="format-number($msec div 1000, '0.0')"/>
                    <xsl:text>s</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="format-number($msec, '#')"/>
                    <xsl:text>ms</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </li>
    </xsl:template>
    <xsl:template match="version">
        <li title="deployed version">
            <xsl:value-of select="name"/>
        </li>
    </xsl:template>
    <xsl:template match="flash">
        <div>
            <xsl:attribute name="class">
                <xsl:text>flash </xsl:text>
                <xsl:choose>
                    <xsl:when test="level = 'INFO'">
                        <xsl:text>success</xsl:text>
                    </xsl:when>
                    <xsl:when test="level = 'WARNING'">
                        <xsl:text>warning</xsl:text>
                    </xsl:when>
                    <xsl:when test="level = 'SEVERE'">
                        <xsl:text>error</xsl:text>
                    </xsl:when>
                </xsl:choose>
            </xsl:attribute>
            <xsl:value-of select="message"/>
        </div>
    </xsl:template>
</xsl:stylesheet>
