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
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//
//public class SensorNetworkDAOTest {
//    Logger logger = LoggerFactory.getLogger(SensorNetworkDAOTest.class);
//    private SensorNetworkManager controller;
//
//    final static String name1 = "N1";
//    final static String name2 = "N2";
//    final static String name3 = "N3";
//    final static String name4 = "N4";
//    final static String name5 = "N5";
//    final static String name6 = "N6";
//
//    @Before
//    public void setUp() {
//        controller = new SensorNetworkManagerImpl();
//    }
//
//    @Test
//    public void NetworkTest() {
//        System.out.println("registerNetwork Test");
//        String id1 = controller.registerNetwork(name1, null);
//        String id2 = controller.registerNetwork(name2, null);
//        String id3 = controller.registerNetwork(name3, null);
//        Set<String> member = new HashSet<String>();
//        member.add(id1);
//        member.add(id2);
//        member.add(id3);
//        String id4 = controller.registerNetwork(name4, member);
//        member.add(id4);
//        String id5 = controller.registerNetwork(name5, member);
//        member.add(id5);
//        String id6 = controller.registerNetwork(name6, member);
//
//
//        System.out.println("\ngetAllNetworks Test");
//        Set<SensorNetwork> networkSet = controller.getAllNetworks();
//        for(SensorNetwork network : networkSet)
//            System.out.println(network);
//
//        System.out.println("\ngetAllNetworkIDs Test");
//        Set<String> set = controller.getAllNetworkIDs();
//        for(String id : set)
//            System.out.println(id);
//
//        System.out.println("\ngetNetworkIDs Test");
//        set = controller.getNetworkIDs(2, 3);
//        for(String id : set)
//            System.out.println(id);
//
//        System.out.println("\ngetAllNetworkTopicPaths Test");
//        set = controller.getAllNetworkTopicPaths();
//        for(String topicPath : set)
//            System.out.println(topicPath);
//
//        System.out.println("\ngetNetworkTopicPaths Test");
//        set = controller.getNetworkTopicPaths(3, 3);
//        for(String topicPath : set)
//            System.out.println(topicPath);
//
//        System.out.println("\ngetAllNetworkAndMemberIDs Test");
//        Map<String, Set<String>> map = controller.getAllNetworkAndMemberIDs();
//        for(String key : map.keySet())
//            System.out.println("Network: " + key + " Member ID: " + map.get(key));
//
//        System.out.println("\ngetAllTopicPathAndMemberIDs Test");
//        map = controller.getAllTopicPathAndMemberIDs();
//        for(String key : map.keySet())
//            System.out.println("TopicPath: " + key + " Member ID: " + map.get(key));
//
//
//        System.out.println("\ngetNetwork Test");
//        SensorNetwork network = controller.getNetwork(id1);
//        System.out.println(network);
//        network = controller.getNetwork(id5);
//        System.out.println(network);
//
//        System.out.println("\nremoveNetwork Test");
//        controller.removeNetwork(id1);
//        controller.removeNetwork(id4);
//        System.out.println("All: " + controller.getAllNetworkCount());
//        System.out.println("Operating: " + controller.getOperatingNetworkCount());
//        System.out.println("Stopped: " + controller.getStoppedNetworkCount());
//        System.out.println("Faulted: " + controller.getFaultedNetworkCount());
//
//        assertFalse(controller.isItNetwork(id1));
//        assertTrue(controller.isItNetwork(id2));
//        assertTrue(controller.isItNetwork(id3));
//        assertFalse(controller.isItNetwork(id4));
//        assertTrue(controller.isItNetwork(id5));
//        assertTrue(controller.isItNetwork(id6));
//        assertFalse(controller.isItNetwork("3400"));
//        assertFalse(controller.isItNetwork("340000"));
//
//        System.out.println("\ngetParentNetworks Test");
//        networkSet = controller.getParentNetworks(id1);
//        for(SensorNetwork net : networkSet)
//            System.out.println(net);
//
//        System.out.println("\ngetParentNetworkIDs Test");
//        set = controller.getParentNetworkIDs(id2);
//        for(String id : set)
//            System.out.println(id);
//
//
//        System.out.println("\ngetParentNetworkTopicPaths Test");
//        set = controller.getParentNetworkTopicPaths(id3);
//        for(String id : set)
//            System.out.println(id);
//
//
//
//    }
//
//    @After
//    public void tearDown() {
//        controller.removeAllNetworks();
//    }
//
//}
