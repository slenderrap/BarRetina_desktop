package com.project.Utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;

public class UtilsConfig {

    public static boolean configExists() {
        Path configPath = Paths.get(System.getProperty("user.dir"), "barretina_desktop", "config.xml");
        if (configPath == null) {
            return false;
        }
        return Files.exists(configPath);
    }

    public static void saveConfig(URI serverUrl, String place) {
        Document doc = getDocument();
        doc = construirDocument(doc, serverUrl, place);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            Path resourcePath = Paths.get(System.getProperty("user.dir"), "barretina_desktop", "config.xml");
            File configFile = new File(resourcePath.toString());
            if (!configFile.exists()) {
                Files.createFile(resourcePath);
            }
            StreamResult result = new StreamResult(configFile);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Document construirDocument(Document doc, URI serverUrl, String place) {
        Element root = doc.createElement("config");
        root.setAttribute("version", "1.0");
        Element serverUrlElement = doc.createElement("serverUrl");
        serverUrlElement.appendChild(doc.createTextNode(serverUrl.toString()));
        root.appendChild(serverUrlElement);
        Element placeElement = doc.createElement("place");
        placeElement.appendChild(doc.createTextNode(place));
        root.appendChild(placeElement);
        doc.appendChild(root);
        return doc;
    }

    private static Document getDocument() {
       DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
       try {
           DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
           Document doc = dBuilder.newDocument();
           return doc; 
       } catch (Exception e) {
           throw new RuntimeException("Error al crear el document XML", e);
       }
    }

}
