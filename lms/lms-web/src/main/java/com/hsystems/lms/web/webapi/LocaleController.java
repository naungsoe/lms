package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.JsonUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.web.util.ServletUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by naungsoe on 15/10/16.
 */
@Path("locales")
public class LocaleController {

  private final Provider<Principal> principalProvider;

  @Inject
  LocaleController(Provider<Principal> principalProvider) {
    this.principalProvider = principalProvider;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{module}")
  public Response findBy(
      @PathParam("module") String module,
      @Context HttpServletRequest request)
      throws IOException {

    String defaultLocale = "en_US";
    String locale = ServletUtils.getCookie(request, "locale");
    locale = StringUtils.isEmpty(locale) ? defaultLocale : locale;
    String navigationFilePath = String.format(
        "locales/navigation/%s.json", locale);
    String moduleFilePath = String.format(
        "locales/%s/%s.json", module, locale);

    InputStream navInputStream = getClass().getClassLoader()
        .getResourceAsStream(navigationFilePath);
    JsonNode navNode = JsonUtils.parseJson(navInputStream);

    InputStream moduleInputStream = getClass().getClassLoader()
        .getResourceAsStream(moduleFilePath);
    JsonNode moduleNode = JsonUtils.parseJson(moduleInputStream);

    JsonNode moduleLocaleNode = moduleNode.get(locale);
    JsonNode navLocaleNode = navNode.get(locale);
    populateProperties(moduleLocaleNode, navLocaleNode);

    String json = moduleNode.toString();
    return Response.ok(json).build();
  }

  private void populateProperties(JsonNode primary, JsonNode source) {
    Iterator<String> properties = source.fieldNames();

    while (properties.hasNext()) {
      String property = properties.next();
      JsonNode propertyNode = source.get(property);
      ((ObjectNode) primary).set(property, propertyNode);
    }
  }
}
