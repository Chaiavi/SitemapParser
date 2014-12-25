/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

/**
 * Created by Avi Hayun on 12/25/2014.
 *
 * Sitemap Tool for recursively fetching all URLs from a sitemap (and all of it's children [in the case it is a sitemap index])
 */
public class SitemapParser {
  private static SiteMapParser parser = new SiteMapParser();
  private static Logger LOG = LoggerFactory.getLogger(SitemapParser.class);

  public static void main(String[] args) {
    if (args.length < 1) {
      LOG.info("Usage: SitemapParser [URL_TO_PARSE]");
    } else {
      try {
        URL url = new URL(args[0]);
        parse(url);
      } catch (MalformedURLException mue) {
        LOG.error("Bad URL: " + args[0], mue);
      } catch (Exception e) {
        LOG.error("Error occurred while trying to parse: " + args[0], e);
      }
    }
  }

  /** Recursive sitemap parsing method */
  private static void parse(URL url) throws IOException, UnknownFormatException {
    AbstractSiteMap sm = parser.parseSiteMap(url);

    if (sm.isIndex()) {
      Collection<AbstractSiteMap> links = ((SiteMapIndex) sm).getSitemaps();
      for (AbstractSiteMap asm : links) {
        parse(asm.getUrl());
      }
    } else {
      Collection<SiteMapURL> links = ((SiteMap) sm).getSiteMapUrls();
      for (SiteMapURL smu : links) {
        LOG.info(smu.toString());
      }
    }
  }
}