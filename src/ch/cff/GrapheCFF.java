/**
 * @fichier GrapheCFF.java
 * @titre Réseau CFF
 * @description Class uses the package Graphe with all methods dedicated to CFF
 * @auteurs Kevin Estalella & Federico Lerda
 * @date 21 Mars 2017
 * @version 1.0
 */


package ch.cff;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.xml.sax.*;
import org.w3c.dom.*;

import ch.cff.Graphe;

public class GrapheCFF extends Graphe {

    // Get the ID of a city when you have the name
    public HashMap<String, Integer> arraySearchId;

    // Get the city when you have the ID
    public HashMap<Integer, Ville> arrayCitiesId;

    /**
     * CONSTRUCTOR.
     */
    public GrapheCFF() {
        super();
        // for graphical purposes, it contains the x,y position of each city
        this.arrayCitiesId = new HashMap<Integer, Ville>();

        // associates an ID with a City
        this.arraySearchId = new HashMap<String, Integer>();
    }

    /**
     * Get the list of the cities in the good way.
     *
     */
    public void getListOfCities() {
        String retour = new String();

        Map<String, Integer> result = new LinkedHashMap<>();

        //sort by value and put it into the "result" map
        arraySearchId.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue())
                .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));

        for (Entry<String, Integer> city : result.entrySet()) {
            retour += "[" + city.getValue() + ":" + city.getKey() + "] ";
        }

        System.out.println(retour);
    }

    /**
     * Load the XML file.
     *
     * @param xml  Path and name of the file.
     * @return If all went well or not.
     */
    public boolean readXML(String xml) {
        Document dom;
        // Make an instance of the DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            // use the factory to take an instance of the document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // parse using the builder to get the DOM mapping of the
            // XML file
            dom = db.parse(xml);
            Element doc = dom.getDocumentElement();
            doc.normalize();

            Ville tmpVille;

            // Villes
            NodeList velements = doc.getElementsByTagName("ville");
            for (int i = 0; i < velements.getLength(); i++) {
                Node vNode = velements.item(i);
                if (vNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element vElement = (Element) vNode;

                    String name = vElement.getElementsByTagName("nom").item(0)
                            .getTextContent().trim();
                    String longitude = vElement
                            .getElementsByTagName("longitude").item(0)
                            .getTextContent().trim();
                    String latitude = vElement.getElementsByTagName("latitude")
                            .item(0).getTextContent().trim();

                    // x : longitude, y : latitude
                    tmpVille = new Ville(name, Integer.parseInt(longitude),
                            Integer.parseInt(latitude));
                    arrayCitiesId.put(i, tmpVille);
                    arraySearchId.put(name, i);
                }
            }

            // Liaisons
            NodeList lelements = doc.getElementsByTagName("liaison");
            for (int i = 0; i < lelements.getLength(); i++) {
                Node lNode = lelements.item(i);
                if (lNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element lElement = (Element) lNode;

                    String ville1 = lElement.getElementsByTagName("vil_1")
                            .item(0).getTextContent().trim();

                    String ville2 = lElement.getElementsByTagName("vil_2")
                            .item(0).getTextContent().trim();

                    String temps = lElement.getElementsByTagName("temps")
                            .item(0).getTextContent().trim();

                    if (!this.existSommet(arraySearchId.get(ville1))) {
                        this.addSommet(arraySearchId.get(ville1));
                    }

                    this.addLiaison(arraySearchId.get(ville1),
                            arraySearchId.get(ville2), Integer.parseInt(temps));

                    if (!this.existSommet(arraySearchId.get(ville2))) {
                        this.addSommet(arraySearchId.get(ville2));
                    }

                    this.addLiaison(arraySearchId.get(ville2),
                            arraySearchId.get(ville1), Integer.parseInt(temps));
                }
            }

        } catch (IOException e) {
            System.out.println("IOException");
        } catch (SAXException e) {
            System.out.println("SAXException");
        } catch (ParserConfigurationException e) {
            System.out.println("ParserConf");
        }



        return true;

    }

    /**
     * Write the graph into a XML file.
     *
     * @param filename  Path and name of the file.
     * @return If all went well or not.
     */
    public boolean writeXML(String filename) {

        try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            // First element
            Document doc = db.newDocument();
            Element firstElement = doc.createElement("reseau");
            doc.appendChild(firstElement);

            // Title
            Element titleElement = doc.createElement("titre");
            firstElement.appendChild(titleElement);
            titleElement.appendChild(doc.createTextNode("CFF"));

            Ville tmpCity;
            // Cites
            for (Integer idCity : arrayCitiesId.keySet()) {
                tmpCity = arrayCitiesId.get(idCity);

                Element cityElement = doc.createElement("ville");
                firstElement.appendChild(cityElement);

                Element nameElement = doc.createElement("nom");
                cityElement.appendChild(nameElement);
                nameElement.appendChild(doc.createTextNode(tmpCity.getName()));

                Element xElement = doc.createElement("latitude");
                cityElement.appendChild(xElement);
                xElement.appendChild(doc.createTextNode(Integer
                        .toString(tmpCity.getCoordX())));
                // Longitude
                Element yElement = doc.createElement("longitude");
                cityElement.appendChild(yElement);
                yElement.appendChild(doc.createTextNode(Integer
                        .toString(tmpCity.getCoordY())));
            }

            // Liaisons
            for (Integer key : super.edges.keySet()) {
                HashMap<Integer, Integer> edge = super.edges.get(key);

                for (Integer key2 : edge.keySet()) {
                    Integer value = edge.get(key2);

                    Element liaisonElement = doc.createElement("liaison");
                    firstElement.appendChild(liaisonElement);

                    Element cityAElement = doc.createElement("vil_1");
                    liaisonElement.appendChild(cityAElement);
                    cityAElement.appendChild(doc
                            .createTextNode(this.arrayCitiesId.get(key)
                                    .getName()));

                    Element cityBElement = doc.createElement("vil_2");
                    liaisonElement.appendChild(cityBElement);
                    cityBElement.appendChild(doc
                            .createTextNode(this.arrayCitiesId.get(key2)
                                    .getName()));

                    Element timeElement = doc.createElement("temps");
                    liaisonElement.appendChild(timeElement);
                    timeElement.appendChild(doc.createTextNode(Integer
                            .toString(value)));
                }
                System.out.println();
            }

            // Write into XML file.
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));

            transformer.transform(source, result);

            System.out.println("File saved !");

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Get the best path of the graph and get the cities associated.
     *
     * @param sourcePath  First city
     * @param destPath  Second city
     * @return The path.
     */
    public String getBestPathFloydCity(Integer sourcePath, Integer destPath) {
        ArrayList<Integer> bestPath = super.getBestPathFloyd(sourcePath,
                destPath);

        String path = new String("[");

        for (Integer idCity : bestPath) {
            path += this.arrayCitiesId.get(idCity).getName();
            if (bestPath.lastIndexOf(idCity) != bestPath.size() - 1)
                path += ":";
        }

        path += "]";

        return path;
    }

    public String printCostDijkstra() {

        for (Integer key2 : getListCostDji().keySet()) {
            Integer value = getListCostDji().get(key2);

            System.out.print("[" + this.arrayCitiesId.get(key2).getName() + ":" + value + "] ");
        }
        return "";
    }

    public String printPrecDijkstra() {

        for (Integer key2 : getListPrecDji().keySet()) {
            Integer value = getListPrecDji().get(key2);
            if (value != key2) {
                System.out.print("[" + this.arrayCitiesId.get(value).getName() + "<-" + this.arrayCitiesId.get(key2).getName() + "] ");
            }
        }
        return "";
    }

    public String printCostDestDijkstra(int idDest) {

        for (Integer key2 : getListCostDji().keySet()) {
            Integer value = getListCostDji().get(key2);
            if(key2 == idDest) {
                System.out.print(value);
            }
        }
        return "";
    }


    public String printPathDijkstra(int idSrc, int idDest) {

        String myStr = "";
        while (idSrc != idDest) {
            for (Integer key2 : getListPrecDji().keySet()) {
                Integer value = getListPrecDji().get(key2);
                if (key2 == idDest) {
                    myStr = ":" + this.arrayCitiesId.get(key2).getName() + myStr;
                    idDest = value;
                }
            }
        }
        myStr = "[" + this.arrayCitiesId.get(idSrc).getName() + myStr + "]";

        System.out.print(myStr);
        return "";
    }

    /**
     * Print a matrix in a good form.
     *
     * @param matrix 	The matrix to print.
     */
    public void printMatrix(int matrix[][]) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] == Integer.MAX_VALUE)
                    System.out.print("inf ");
                else
                    System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }


    /**
     * Add a city in the graph.
     *
     * @param nameCity 	The city to add.
     */
    public void addCity(String nameCity) {
        int i = 0;

        // If we find a free space in the list, we place the new city here
        while (arrayCitiesId.containsKey(i)) {
            i++;
        }

        addSommet(i);

        Ville newCity = new Ville(nameCity, 0, 0);
        arrayCitiesId.put(i, newCity);
        arraySearchId.put(nameCity, i);
    }

    /**
     * Add a link between two cites in the graph.
     *
     * @param str1 	First city.
     * @param str2 	Second city.
     * @param str2	Weight of the link.
     */
    public void addCitiesLiaison(String str1, String str2, String str3) {
        addLiaison(arraySearchId.get(str1), arraySearchId.get(str2),
                Integer.parseInt(str3));
    }

    /**
     * Delete the city of the graph and delete all the links associated.
     *
     * @param str1 	City to delete
     */
    public void deleteCity(String str1) {
        int idCity = arraySearchId.get(str1);
        deleteSommet(idCity);
        arrayCitiesId.remove(idCity);
        arraySearchId.remove(str1);

        // Control if the deleted city is not associated with a link
        for (Integer key : super.edges.keySet()) {
            HashMap<Integer, Integer> edge = super.edges.get(key);

            if (edge.containsKey(idCity))
                deleteLiaison(key, idCity);
        }
    }

    /**
     * Delete the link between two cities of the graph
     *
     * @param str1 	First city
     * @param str2 	Second city
     */
    public void deleteCitesLiaison(String str1, String str2) {
        deleteLiaison(arraySearchId.get(str1), arraySearchId.get(str2));
    }

    @Override
    public void addSommet(Integer name) {
        // TODO Auto-generated method stub
        super.addSommet(name);
    }

    @Override
    public void deleteSommet(Integer name) {
        // TODO Auto-generated method stub
        super.deleteSommet(name);
    }

    @Override
    public void addLiaison(Integer name1, Integer name2, Integer value) {
        // TODO Auto-generated method stub
        super.addLiaison(name1, name2, value);
    }

    @Override
    public void deleteLiaison(Integer name1, Integer name2) {
        // TODO Auto-generated method stub
        super.deleteLiaison(name1, name2);
    }

    @Override
    public String toString() {
        for (Integer key : super.edges.keySet()) {
            HashMap<Integer, Integer> edge = super.edges.get(key);

            System.out.print(this.arrayCitiesId.get(key).getName() + " ");

            for (Integer key2 : edge.keySet()) {
                Integer value = edge.get(key2);

                System.out.print("[" + this.arrayCitiesId.get(key2).getName()
                        + ":" + value + "] ");
            }
            System.out.println();
        }
        return "";
    }
}