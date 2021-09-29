/*Name: Anela Karamustafic
 *NETID: akaramus
 *URID: 31404167
 *Project 3
 *Partner: Henry Gardner
 *Lab Section: 6:15-7:30 pm
 */

import java.util.ArrayList;
import java.util.List;

///////RECEIVED GUIDANCE FROM GITHUB.COM///////
public class Node {
    String id;
    double latitude;
    double longitude;

    List<Edge> roadList;

    public Node(String id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;

        roadList = new ArrayList<>();
    }

    public void addRoad(Edge road) {
        roadList.add(road);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}