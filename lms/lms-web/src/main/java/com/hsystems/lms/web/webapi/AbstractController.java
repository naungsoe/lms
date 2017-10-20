package com.hsystems.lms.web.webapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.hsystems.lms.common.util.JsonUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.web.util.ServletUtils;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractController {

  protected JsonNode findLocaleNode(HttpServletRequest request)
      throws IOException {

    String defaultLocale = "en_US";
    String locale = ServletUtils.getCookie(request, "locale");
    locale = StringUtils.isEmpty(locale) ? defaultLocale : locale;
    String commonFilePath = String.format(
        "locales/common/%s.json", locale);
    InputStream commonInputStream = getClass().getClassLoader()
        .getResourceAsStream(commonFilePath);
    JsonNode commonNode = JsonUtils.parseJson(commonInputStream);
    return commonNode.get(locale);
  }
}
