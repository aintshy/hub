<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns="http://www.w3.org/1999/xhtml" version="2.0">
  <xsl:template match="/page">
    <xsl:text disable-output-escaping="yes">&lt;!DOCTYPE html&gt;</xsl:text>
    <html lang="en">
      <head>
        <meta charset="UTF-8"/>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="description" content="aintshy"/>
        <meta name="keywords" content="aintshy"/>
        <meta name="author" content="aintshy.com"/>
        <link rel="icon" type="image/gif" href="http://img.aintshy.com/favicon.ico"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <xsl:apply-template select="." mode="head"/>
      </head>
      <body>
        <div>
          <xsl:apply-templates select="." mode="body"/>
        </div>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>
