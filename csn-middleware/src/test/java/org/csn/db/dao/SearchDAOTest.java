//package cir.lab.dao;
//
//import cir.lab.csn.component.SensorNetworkManager;
//import cir.lab.csn.component.impl.SensorNetworkManagerImpl;
//import cir.lab.csn.data.SensorNetwork;
//import cir.lab.csn.db.CSNDAOFactory;
//import cir.lab.csn.db.dao.SearchDAO;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import java.util.*;
//
//public class SearchDAOTest {
//    Logger logger = LoggerFactory.getLogger(SearchDAOTest.class);
//
//    final static String name1 = "N1";
//    final static String name2 = "N2";
//    final static String name3 = "N3";
//    final static String name4 = "N4";
//    final static String name5 = "N5";
//    final static String name6 = "N6";
//
//    final static String concept1 = "concept1";
//    final static String concept2 = "concept2";
//    final static String concept3 = "concept3";
//    final static String concept4 = "concept4";
//    final static String concept5 = "concept5";
//    final static String concept6 = "concept6";
//
//    private SensorNetworkManager controller;
//    private SearchDAO searchDAO;
//
//    @Before
//    public void setUp() {
//        controller = new SensorNetworkManagerImpl();
//        searchDAO = new CSNDAOFactory().searchDAO();
//    }
//
//    @Test
//    public void conceptSearchTest() {
//        String id1 = controller.registerNetwork(name1, null);
//        String id2 = controller.registerNetwork(name2, null);
//        String id3 = controller.registerNetwork(name3, null);
//        String id4 = controller.registerNetwork(name4, null);
//        String id5 = controller.registerNetwork(name5, null);
//        String id6 = controller.registerNetwork(name6, null);
//
//        controller.addTag(concept1, id1);
//        controller.addTag(concept2, id1);
//        controller.addTag(concept3, id1);
//
//        controller.addTag(concept2, id2);
//        controller.addTag(concept3, id2);
//        controller.addTag(concept4, id2);
//
//        controller.addTag(concept3, id3);
//        controller.addTag(concept4, id3);
//        controller.addTag(concept5, id3);
//
//        controller.addTag(concept4, id4);
//        controller.addTag(concept5, id4);
//        controller.addTag(concept6, id4);
//
//        controller.addTag(concept2, id5);
//        controller.addTag(concept3, id5);
//        controller.addTag(concept4, id5);
//        controller.addTag(concept5, id5);
//        controller.addTag(concept6, id5);
//
//        controller.addTag(concept1, id6);
//        controller.addTag(concept2, id6);
//        controller.addTag(concept3, id6);
//        controller.addTag(concept4, id6);
//        controller.addTag(concept5, id6);
//        controller.addTag(concept6, id6);
//
//        System.out.println("\n\nConcept1 Search: id1, id6");
//        Set<String> idSet = searchDAO.searchNetworkWithTag(concept1);
//        for(String id: idSet)
//            System.out.print(id + "\t");
//
//        System.out.println("\n\nConcept2 Search: id1, id2, id5, id6");
//        idSet = searchDAO.searchNetworkWithTag(concept2);
//        for(String id: idSet)
//            System.out.print(id + "\t");
//
//        System.out.println("\n\nConcept4 Search: id2, id3, id4, id5, id6");
//
//        idSet = searchDAO.searchNetworkWithTag(concept4);
//        for(String id: idSet)
//            System.out.print(id + "\t");
//
//
//        Set<String> concepts = new HashSet<>();
//        concepts.add(concept2);
//        concepts.add(concept3);
//        concepts.add(concept4);
//        System.out.println("\n\nMulti Concepts Search: id2, id5, id6");
//        idSet = searchDAO.searchNetworkWithConcepts(concepts);
//        for(String id: idSet)
//            System.out.print(id + "\t");
//
//
//        concepts.add(concept5);
//        System.out.println("\n\nMulti Concepts Search2: id5, id6");
//        idSet = searchDAO.searchNetworkWithConcepts(concepts);
//        for(String id: idSet)
//            System.out.print(id + "\t");
//
//        controller.removeAllTags(id1);
//        controller.removeAllTags(id2);
//        controller.removeAllTags(id3);
//        controller.removeAllTags(id4);
//        controller.removeAllTags(id5);
//        controller.removeAllTags(id6);
//    }
//
//    @After
//    public void tearDown() {
//        controller.removeAllNetworks();
//    }
//}
