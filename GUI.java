/*Name: Anela Karamustafic
 */

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Desktop;
import java.io.FileNotFoundException;

///////RECEIVED GUIDANCE FROM GITHUB.COM///////
public class GUI extends JPanel implements ActionListener, MouseListener {
    HashMap<String, Node> intersections;
    double minLat, maxLat, minLong, maxLong;
    int padding = 200;
    Set<String> roadIDs;
    JButton directionsButton;
    JButton enter;
    JComboBox startChoices;
    JComboBox endChoices;
    JFrame frame;

    public GUI(HashMap<String, Node> list, Set<String> roadIDs) throws HeadlessException {
        this.intersections = list;
        this.roadIDs = roadIDs;
        minLat = 200;
        maxLat = -200;
        minLong = 200;
        maxLong = -200;
        for(String key : intersections.keySet()) {
            Node intersection = intersections.get(key);
            minLat = Math.min(minLat, intersection.getLatitude());
            maxLat = Math.max(maxLat, intersection.getLatitude());
            minLong = Math.min(minLong, intersection.getLongitude());
            maxLong = Math.max(maxLong, intersection.getLongitude());
        }

        StreetMap streetMap = new StreetMap();
        String inputFile = streetMap.getInputFile();
        Graph graph = new Graph();
        ArrayList<String> lines = new ArrayList<String>();
        try {
            lines = graph.parseDataString(inputFile);
        } catch(FileNotFoundException e){
            System.out.println(e);
        }

        frame = new JFrame();
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel();

        directionsButton = new JButton("Open Directions");

        startChoices = new JComboBox();
        startChoices.addItem("Enter a Starting Position: ");
        for (int i = 0; i < lines.size(); i++) {
            startChoices.addItem(lines.get(i));
        }
        startChoices.setSelectedIndex(0);

        endChoices = new JComboBox();
        endChoices.addItem("Enter an Ending Position: ");
        for (int i = 0; i < lines.size(); i++) {
            endChoices.addItem(lines.get(i));
        }
        endChoices.setSelectedIndex(0);

        enter = new JButton("Enter");

        directionsButton.addActionListener(this);
        startChoices.addMouseListener(this);
        endChoices.addMouseListener(this);
        enter.addActionListener(this);

        panel.add(directionsButton);
        panel.add(startChoices);
        panel.add(endChoices);
        panel.add(enter);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(this);
    }

    public void actionPerformed(ActionEvent event) {   
        Graphics graphics = this.getGraphics();
        if (event.getSource() == directionsButton) {
            System.out.println("...Opening Directions...");
            try {
                openFile("directions.txt");
            } catch(IOException e){
                System.out.println(e);
            }
        }
        if (event.getSource() == enter) {
            StreetMap streetMap = new StreetMap();
            String start = startChoices.getSelectedItem().toString();
            String end = endChoices.getSelectedItem().toString();
            
            frame.dispose();
            System.out.println();
            System.out.println("...Calculating directions from " + start + " to " + end + "...");
            String[] args = {streetMap.getInputFile(), "--show", "--directions", start, end};
            
          

            try {
                streetMap.main(args);
            } catch(FileNotFoundException e){
                System.out.println(e);
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        //System.out.println(e);
    }

    public void mousePressed(MouseEvent e) {
        //ystem.out.println(e);

    }

    public void mouseReleased(MouseEvent e) {
        //System.out.println(e);

    }

    public void mouseEntered(MouseEvent e) {
        //System.out.println(e);
    }

    public void mouseExited(MouseEvent e) {
        //System.out.println(e);
    }

    public static void openFile(String fileName) throws IOException {
        try {
            Desktop.getDesktop().open(new java.io.File("directions.txt"));
        } catch(FileNotFoundException e){
            System.out.println("...File not found... " + "\n" + "e");
        }

    }

    public int adjustedLatitude(double latitude) {
        return (int) ((maxLat - latitude) / (maxLat - minLat) * this.getHeight());
    }

    public int adjustedLongitude(double longitude) {
        return (int) ((longitude - minLong) / (maxLong - minLong) * this.getWidth());
    }

    @Override
    public void paintComponent(Graphics g) { 
        super.paintComponents(g);
        g.setColor(Color.BLUE);
        g.drawString("The red path is the shortest distance between the 2 intersections:",getWidth()/4,10);
        g.setColor(Color.BLACK);
        Graphics2D g2D = (Graphics2D) g;
        for(String key: intersections.keySet()) {
            Node intersection = intersections.get(key);
            for(Edge road: intersection.roadList) {
                g2D.drawLine(adjustedLongitude(intersection.getLongitude()),adjustedLatitude(intersection.getLatitude()),adjustedLongitude(intersections.get(road.destinationID).getLongitude()),adjustedLatitude(intersections.get(road.destinationID).getLatitude()));
                if(roadIDs.contains(road.id)) {
                    g2D.setColor(Color.RED);
                    g2D.setStroke(new BasicStroke(3));
                    g2D.drawLine(adjustedLongitude(intersection.getLongitude()),adjustedLatitude(intersection.getLatitude()),adjustedLongitude(intersections.get(road.destinationID).getLongitude()),adjustedLatitude(intersections.get(road.destinationID).getLatitude()));
                    g2D.setColor(Color.BLACK);
                    g2D.setStroke(new BasicStroke(1));
                }
            }
        }
    }
}
