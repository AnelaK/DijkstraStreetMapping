/*Name: Anela Karamustafic
 */

import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

///////RECEIVED GUIDANCE FROM GITHUB.COM///////
public class Graph {
    HashMap<String, Node> Node;
    HashMap<String, String> trace;
    Set<String> roadIDs = new HashSet<>();
    static private double AVERAGE_NEW_YORK_SPEED = 72.42048; //average speed in km, used to find average time

    public void setAVERAGE_NEW_YORK_SPEED(double s) {
        AVERAGE_NEW_YORK_SPEED = s;
    }

    public double getAVERAGE_NEW_YORK_SPEED() {
        return AVERAGE_NEW_YORK_SPEED;
    }


    public void parseData(String fileName) throws FileNotFoundException {
        if (fileName.equals("ur.txt")) {
            setAVERAGE_NEW_YORK_SPEED(16.09344);
        }
        else if (fileName.equals("monroe.txt")) {
            setAVERAGE_NEW_YORK_SPEED(56.32704);
        }

        Node = new HashMap<>();
        Scanner scanner = new Scanner(new File(fileName));
        while(scanner.hasNextLine()) {
            String type = scanner.next();
            String id = scanner.next();
            String para1 = scanner.next();
            String para2 = scanner.next();

            if(type.equals("i")) {
                Node.put(id, new Node(id, Double.parseDouble(para1), Double.parseDouble(para2)));
            }

            else {
                Node i1 = Node.get(para1);
                Node i2 = Node.get(para2);

                Edge road = new Edge(id, para2, haversineFunction(i1, i2));
                i1.addRoad(road);

                road = new Edge(id, para1,  haversineFunction(i1, i2));
                i2.addRoad(road);
            }
        }
    }

    public ArrayList<String> parseDataString(String fileName) throws FileNotFoundException {
        ArrayList<String> list = new ArrayList<String>();
        Scanner scanner = new Scanner(new File(fileName));
        while(scanner.hasNextLine()) {
            String type = scanner.next();
            String id = scanner.next();
            String para1 = scanner.next();
            String para2 = scanner.next();
            list.add(id);
        }
        return list;
    }

    //Function to find the distance between 2 intersections based on the earths Latitude and
    //Longitude. Used guidence from https://en.wikipedia.org/wiki/Haversine_formula
   public double haversineFunction(Node intersection1, Node intersection2) {
        double radius = 6378.137; //earth radius in km
        double sinLattitude = Math.pow(Math.sin(Math.toRadians((intersection2.getLatitude() - intersection1.getLatitude())/2)),2);
        double sinLongitude = Math.pow(Math.sin(Math.toRadians((intersection2.getLongitude() - intersection1.getLongitude())/2)),2);
        double cosLattitude = Math.cos(Math.toRadians(intersection1.getLatitude()))*Math.cos(Math.toRadians(intersection2.getLatitude()))*sinLongitude;
        return 2*radius*Math.asin(Math.sqrt(sinLattitude + cosLattitude));
    }

    public static void createFile(ArrayList<String> list, String filename) {
        try {
            BufferedWriter outputWriter = new BufferedWriter(new FileWriter(filename));
            for (int i = 0; i < list.size(); i++) {
                outputWriter.write(list.get(i));
                outputWriter.newLine();
            }
            outputWriter.flush();  
            outputWriter.close();
        } catch(IOException e){
            System.out.println(e);
        }
    }

    public void shortestPath(String start, String end) {
        ArrayList<String> outputList = new ArrayList<String>();
        outputList.add("Directions from " + start + " to " + end + ":");
        outputList.add(" ");
        trace = new HashMap<>();
        roadIDs = new HashSet<>();
        Heap heap = new Heap();
        heap.addElement(start, 0);

        while(heap.size > 0) {
            String intersectionId = heap.removeTop();
            if(intersectionId.equals(end)) {
                break;
            }

            for(Edge road : Node.get(intersectionId).roadList) {
                if(!heap.contatinIntersection(road.destinationID)) {
                    heap.addElement(road.destinationID, heap.getValue(intersectionId) + road.weight);
                    trace.put(road.destinationID, road.id);
                } 

                else if(heap.getValue(road.destinationID) > heap.getValue(intersectionId) + road.weight) {
                    heap.updateValue(road.destinationID, heap.getValue(intersectionId) + road.weight);
                    trace.replace(road.destinationID, road.id);
                }
            }
        }

        if(!heap.contatinIntersection(end)) {   
            outputList.add("Can't find path, these intersections are not connected.");
            System.out.println("Can't find path, these intersections are not connected.");
            return;
        }

        ArrayList<String> path = new ArrayList<>();
        String intersectionId = end;
        while(!intersectionId.equals(start)) {
            path.add(intersectionId);
            roadIDs.add(trace.get(intersectionId));

            for(Edge road : Node.get(intersectionId).roadList) {
                if(road.id.equals(trace.get(intersectionId))) {
                    intersectionId = road.destinationID;
                    break;
                }
            }
        }
        path.add(start);
        for(int i = path.size() - 1; i >= 0; i--) {
            outputList.add(path.size() - i + ". " + path.get(i));
            System.out.println(path.get(i));
        }
        outputList.add(" ");
        outputList.add("Distance traveled (km): " + heap.getValue(end));
        outputList.add("Estimated Time (hours): " + heap.getValue(end)/AVERAGE_NEW_YORK_SPEED);
        outputList.add("*Estimated time is based on the average speed in this area with no unexpected traffic or stops.*");
        System.out.println("Distance traveled (km): " + heap.getValue(end));
        System.out.println("Estimated Time (hours): " + heap.getValue(end)/AVERAGE_NEW_YORK_SPEED);

        createFile(outputList, "directions.txt");
    }

    public static double toMilesPerHour(double kmhr){
        return 0.621371*kmhr;
    }
    
}
