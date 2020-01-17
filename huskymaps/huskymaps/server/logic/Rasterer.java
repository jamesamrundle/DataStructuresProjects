package huskymaps.server.logic;

import huskymaps.params.RasterRequest;
import huskymaps.params.RasterResult;
import huskymaps.utils.Constants;

import java.util.Objects;

import static huskymaps.utils.Constants.*;

/** Application logic for the RasterAPIHandler. */
public class Rasterer {

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param request RasterRequest
     * @return RasterResult
     */
    public static RasterResult rasterizeMap(RasterRequest request) {

        int depth = request.depth;


        double startX = Math.abs( (request.ullon-new Tile(depth,0,0).lon() ));
        double startY = Math.abs( (new Tile(depth,0,0).lat() - request.ullat));

        int XStartTile= (int)Math.floor( startX/ LON_PER_TILE[depth]);
        int YStartTile = (int)Math.floor( startY/Constants.LAT_PER_TILE[depth]);


        double boxLen = Math.abs(request.ullon - request.lrlon);
        double boxHeight = Math.abs(request.ullat - request.lrlat);

        // number of x and y according to coordinates given
        int endX = (int) Math.floor(((boxLen + startX) / LON_PER_TILE[depth]));
        int endY = (int) Math.floor(((boxHeight + startY) / Constants.LAT_PER_TILE[depth]));

        if (endX >= Constants.NUM_X_TILES_AT_DEPTH[depth]) endX = Constants.NUM_X_TILES_AT_DEPTH[depth]-1;
        if (endY >= Constants.NUM_Y_TILES_AT_DEPTH[depth]) endY = Constants.NUM_Y_TILES_AT_DEPTH[depth]-1;

        Tile[][] grid = new Tile[endY-YStartTile+1][endX-XStartTile+1];
        for(int y = YStartTile;y<=endY;y++){
            for(int x = XStartTile;x<=endX;x++){
                grid[y-YStartTile][x-XStartTile] = new Tile(depth,x,y);
            }

        }
        otherCalcs(request);
        return new RasterResult(grid);
    }

    private static void otherCalcs(RasterRequest req){
        double ULat = req.ullat;
        double ULON = req.ullon;
        double LLat = req.lrlat;
        double LLon = req.lrlon;

        int UX , UY ,LX,LY;
        ULON = (ULON - ROOT_ULLON) / (LON_PER_TILE[req.depth]);
        UX = (int)ULON;
        ULat = (ULat - ROOT_ULLAT)/ (-LAT_PER_TILE[req.depth]);
        UY = (int)ULat;

        LLon = (LLon - ROOT_ULLON) / (LON_PER_TILE[req.depth]);
        LX = (int)LLon;
        LLat = (LLat - ROOT_ULLAT)/ (-(LAT_PER_TILE[req.depth]));
        LY = (int)LLat;

        double lat = ROOT_ULLAT - (LAT_PER_TILE[req.depth] *LLat);
        double lon = ROOT_ULLON + (LON_PER_TILE[req.depth] * LLon);
        boolean outOflon = Double.compare(lon,ROOT_LRLON)>0;
        boolean outOflat = Double.compare(lat,ROOT_LRLAT)>0;
        //System.out.print(ROOT_LAT_DIFF);

    }

    private static int getTileLen(RasterRequest request){
        return (int) (Math.pow(2,request.depth+1)-1);

    }
    private static int getTileHeight(RasterRequest request){
        return (int) (Math.pow(2,request.depth)-1);

    }

    public static class Tile {
        public final int depth;
        public final int x;
        public final int y;

        public Tile(int depth, int x, int y) {
            this.depth = depth;
            this.x = x;
            this.y = y;
        }

        public Tile offset() {
            return new Tile(depth, x + 1, y + 1);
        }

        /**
         * Return the latitude of the upper-left corner of the given slippy map tile.
         * @return latitude of the upper-left corner
         * @source https://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
         */
        public double lat() {
            double n = Math.pow(2.0, MIN_ZOOM_LEVEL + depth);
            int slippyY = MIN_Y_TILE_AT_DEPTH[depth] + y;
            double latRad = Math.atan(Math.sinh(Math.PI * (1 - 2 * slippyY / n)));
            return Math.toDegrees(latRad);
        }

        /**
         * Return the longitude of the upper-left corner of the given slippy map tile.
         * @return longitude of the upper-left corner
         * @source https://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
         */
        public double lon() {
            double n = Math.pow(2.0, MIN_ZOOM_LEVEL + depth);
            int slippyX = MIN_X_TILE_AT_DEPTH[depth] + x;
            return slippyX / n * 360.0 - 180.0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tile tile = (Tile) o;
            return depth == tile.depth &&
                    x == tile.x &&
                    y == tile.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(depth, x, y);
        }

        @Override
        public String toString() {
            return "d" + depth + "_x" + x + "_y" + y + ".jpg";
        }
    }
}
