//package cir.lab.dao;
//
//import cir.lab.csn.component.SensorNetworkManager;
//import cir.lab.csn.component.impl.SensorNetworkManagerImpl;
//import cir.lab.csn.data.SensorNetwork;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import java.util.*;
//
//public class SemanticConceptDAOTest {
//    Logger logger = LoggerFactory.getLogger(SemanticConceptDAOTest.class);
//    private SensorNetworkManager controller;
//
//    final static String name1 = "N1";
//    final static String name2 = "N2";
//    final static String name3 = "N3";
//
//    final static String concept1 = "concept1";
//    final static String concept2 = "concept2";
//    final static String concept3 = "concept3";
//    final static String concept4 = "concept4";
//
//    @Before
//    public void setUp() {
//        controller = new SensorNetworkManagerImpl();
//    }
//
//    @Test
//    public void conceptsTest() {
//        String id1 = controller.registerNetwork(name1, null);
//        String id2 = controller.registerNetwork(name2, null);
//        String id3 = controller.registerNetwork(name3, null);
//
//        controller.addTag(concept1, id1);
//        controller.addTag(concept1, id1);
//        controller.addTag(concept2, id1);
//        Set<String> conceptSet = controller.getAllTags(id1);
//        System.out.println("\nPrint");
//        for(String concept: conceptSet)
//            System.out.println(concept);
//
//        conceptSet.add(concept1);
//        conceptSet.add(concept2);
//        conceptSet.add(concept3);
//        conceptSet.add(concept4);
//        controller.addTags(conceptSet, id2);
//        conceptSet = controller.getAllTags(id2);
//        System.out.println("\nPrint");
//        for(String concept: conceptSet)
//            System.out.println(concept);
//
//        controller.removeTag(concept2, id2);
//        controller.removeTag(concept4, id2);
//        conceptSet = controller.getAllTags(id2);
//        System.out.println("\nPrint");
//        for(String concept: conceptSet)
//            System.out.println(concept);
//
//        controller.addTag(concept2, id2);
//        controller.addTag(concept4, id2);
//        conceptSet = controller.getAllTags(id2);
//        System.out.println("\nPrint");
//        for(String concept: conceptSet)
//            System.out.println(concept);
//
//        controller.removeAllTags(id2);
//        conceptSet = controller.getAllTags(id2);
//        System.out.println("\nPrint");
//        for(String concept: conceptSet)
//            System.out.println(concept);
//
//        controller.removeAllTags(id1);
//        controller.removeAllTags(id2);
//        controller.removeAllTags(id3);
//    }
//
//    @After
//    public void tearDown() {
//        controller.removeAllNetworks();
//    }
//
//}
