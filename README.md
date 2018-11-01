SitemapParser
=============

**Simple Java Tool to Parse any Online Sitemap**

The tool parses all sorts of Sitemaps, including zipped ones and even sitemap Indexes (recursive parsing).
Just send a URL of an online sitemap and you will get all of its content

This tool basically uses [CrawlerCommons](https://code.google.com/p/crawler-commons/) Sitemap functionality - they did the heavy lifting of the sitemap parsing, I just created a wrapper tool which can easily run and do the work.


#### Usage
Java -jar SitemapParser v0.6.jar [URL OF A SITEMAP]
Or just use the windows batch command line file (ParseSitemap.bat).


#### Please Note
SitemapParser uses SLF4J as its logging API, backed by logback as implementation, so in order to configure the log trace, tweak the logback.xml configuration file included in the released zip file (and run the jar with the -Dlogback.configurationFile=logback.xml flag).
