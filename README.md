# 🗺️ SitemapParser

**A simple Java tool to parse any online sitemap.**

---

## Overview

SitemapParser handles all types of sitemaps — including compressed (`.gz`) sitemaps and **Sitemap Index files** with full recursive parsing. Just provide a URL and get all the sitemap content back.

> Built on top of [Crawler Commons](https://github.com/crawler-commons/crawler-commons) sitemap functionality. They did the heavy lifting of sitemap parsing — this project wraps it into an easy-to-use command-line tool.

---

## 🚀 Usage

### Command Line

```bash
java -jar SitemapParser_v%VERSION_NUMBER%.jar [URL_OF_A_SITEMAP]
```

### Windows

Use the included batch file:

```bash
ParseSitemap.bat
```

---

## ⚙️ Logging Configuration

SitemapParser uses **SLF4J** as its logging API with **Logback** as the implementation.

To customize the log output, edit the `logback.xml` configuration file included in the release zip, then run with:

```bash
java -Dlogback.configurationFile=logback.xml -jar SitemapParser_v%VERSION_NUMBER%.jar [URL]
```

---

## ✨ Features

| Feature | Details |
|---|---|
| **Standard Sitemaps** | Parses XML sitemaps |
| **Compressed Sitemaps** | Handles `.gz` zipped sitemaps |
| **Sitemap Index** | Recursively parses sitemap index files |
| **Easy to Use** | Single command — just pass a URL |

---

## 🙏 Credits

Sitemap parsing powered by [Crawler Commons](https://github.com/crawler-commons/crawler-commons).
