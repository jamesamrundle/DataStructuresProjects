package huskymaps.server.handler.impl;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import huskymaps.params.RouteRequest;
import huskymaps.params.RouteResult;
import huskymaps.server.handler.APIRouteHandler;
import huskymaps.server.logic.NavigationDirection;
import huskymaps.server.logic.Router;
import spark.Request;
import spark.Response;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

import static huskymaps.utils.Constants.*;
import static huskymaps.utils.Constants.BASE_DIR_PATH;

/**
 * Handles requests from the web browser for routes between locations. The
 * route will be returned as image data, as well as (optionally) driving directions.
 */
public class RoutingAPIHandler extends APIRouteHandler<RouteRequest, RouteResult> {

    @Override
    protected RouteRequest parseRequest(Request request) {
        return RouteRequest.from(request);
    }

    /**
     * Takes a user query in the form of a pair of (lat/lon) values, and finds
     * street directions between the given points.
     * @param request RouteRequest
     * @param response Ignored.
     * @return RouteResult
     */
    @Override
    protected RouteResult processRequest(RouteRequest request, Response response) {
        ROUTE_LIST = Router.shortestPath(SEMANTIC_STREET_GRAPH, request);
        return new RouteResult(!ROUTE_LIST.isEmpty(), getDirectionsText());
    }


    /** Takes the current route and converts it into an HTML-friendly String. */
    private String getDirectionsText() {
        List<NavigationDirection> directions = Router.routeDirections(SEMANTIC_STREET_GRAPH, ROUTE_LIST);
        if (directions == null || directions.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int step = 1;
        for (NavigationDirection d: directions) {

            sb.append(String.format("%d. %s <br>", step, d));
            step += 1;
        }
        return sb.toString();
    }
}
