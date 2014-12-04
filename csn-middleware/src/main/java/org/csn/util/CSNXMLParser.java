package org.csn.util;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;

public class CSNXMLParser {

    public static void parseXML(String filePath) {
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(filePath);
        Document document = null;

        try {
            document = (Document) builder.build(xmlFile);
            Element rootNode = document.getRootElement();


        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
