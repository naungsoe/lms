package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.Scalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("/graphql")
public class GraphQLController extends AbstractController {

  private static final String FILE_NAME = "schema.graphqls";

  private final Provider<Properties> propertiesProvider;

  @Inject
  GraphQLController(
      Provider<Properties> propertiesProvider) {

    this.propertiesProvider = propertiesProvider;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response query(
      @Context UriInfo uriInfo)
      throws IOException {

    GraphQLObjectType queryType = GraphQLObjectType.newObject()
        .name("helloWorldQuery")
        .field(GraphQLFieldDefinition.newFieldDefinition()
            .type(Scalars.GraphQLString)
            .name("result")
            .staticValue("Hello World"))
        .build();

    GraphQLSchema schema = GraphQLSchema.newSchema()
        .query(queryType)
        .build();

    GraphQL graphQL = GraphQL.newGraphQL(schema).build();
    ExecutionResult result = graphQL.execute("{result}");
    return Response.ok(result.getData()).build();
  }

  private GraphQLSchema getSchema() {
    InputStream stream = getClass().getClassLoader()
        .getResourceAsStream(FILE_NAME);
    InputStreamReader reader = new InputStreamReader(stream);

    SchemaParser parser = new SchemaParser();
    SchemaGenerator generator = new SchemaGenerator();
    TypeDefinitionRegistry registry = parser.parse(reader);
    RuntimeWiring wiring = buildWiring();
    return generator.makeExecutableSchema(registry, wiring);
  }

  private RuntimeWiring buildWiring() {
    return RuntimeWiring.newRuntimeWiring()
        .type("Query", typeWiring -> typeWiring
            .dataFetcher("user", new StaticDataFetcher(StarWarsData.getArtoo()))
        )
        .build();
  }
}
