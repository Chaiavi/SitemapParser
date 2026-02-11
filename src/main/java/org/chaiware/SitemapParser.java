/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.chaiware;

import crawlercommons.sitemaps.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

/**
 * Sitemap Tool for recursively fetching all URLs from a sitemap (and all of its children [in the case it is a sitemap index])
 */
public class SitemapParser {
  private static final SiteMapParser PARSER = new SiteMapParser();
  private static final Logger LOG = LoggerFactory.getLogger(SitemapParser.class);
  private static final DateTimeFormatter OUTPUT_TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

  public static void main(String[] args) {
    if (args.length < 1) {
      LOG.info("Usage: SitemapParser [URL_TO_PARSE]");
      System.exit(1);
    } else {
      String inputArg = args.length == 1 ? args[0] : String.join(" ", args);
      try {
        InputSpec input = resolveInput(inputArg);
        URL url = input.url();
        Path outputPath = buildOutputPath(input.outputBaseName());
        int totalUrls;
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
          totalUrls = parseSitemap(url, writer);
        }
        LOG.info("Wrote {} URLs to {}", totalUrls, outputPath.toAbsolutePath());
      } catch (IllegalArgumentException iae) {
        LOG.error("Bad URI: {}", inputArg, iae);
        System.exit(2);
      } catch (MalformedURLException mue) {
        LOG.error("Bad URL: {}", inputArg, mue);
        System.exit(2);
      } catch (Exception e) {
        LOG.error("Error occurred while trying to parse: {}", inputArg, e);
        System.exit(3);
      }
    }
  }

  /** Recursive sitemap parsing method */
  private static int parseSitemap(URL url, BufferedWriter writer)
    throws IOException, UnknownFormatException {
    AbstractSiteMap sm = PARSER.parseSiteMap(url);
    int count = 0;

    if (sm.isIndex()) {
      Collection<AbstractSiteMap> links = ((SiteMapIndex) sm).getSitemaps();
      for (AbstractSiteMap asm : links) {
        count += parseSitemap(asm.getUrl(), writer);
      }
    } else {
      Collection<SiteMapURL> links = ((SiteMap) sm).getSiteMapUrls();
      for (SiteMapURL smu : links) {
        writer.write(smu.getUrl().toString());
        writer.newLine();
        count++;
      }
    }
    return count;
  }

  private static Path buildOutputPath(String outputBaseName) {
    String baseName = outputBaseName.replaceAll("[^A-Za-z0-9._-]+", "_");
    String fileName = baseName + "_" + OUTPUT_TIMESTAMP.format(LocalDateTime.now()) + ".txt";
    return Path.of(fileName);
  }

  private static InputSpec resolveInput(String arg) throws MalformedURLException {
    try {
      Path path = Paths.get(arg);
      if (Files.exists(path)) {
        return new InputSpec(path.toUri().toURL(), path.getFileName().toString());
      }
    } catch (InvalidPathException ignored) {
      // Fall back to treating input as a URI.
    }
    URI inputUri = URI.create(arg);
    if ("file".equalsIgnoreCase(inputUri.getScheme())) {
      Path path = Path.of(inputUri);
      return new InputSpec(inputUri.toURL(), path.getFileName().toString());
    }
    if (inputUri.getScheme() != null) {
      return new InputSpec(inputUri.toURL(), inputUri.toString());
    }
    return new InputSpec(inputUri.toURL(), inputUri.toString());
  }

  private record InputSpec(URL url, String outputBaseName) {
  }
}
