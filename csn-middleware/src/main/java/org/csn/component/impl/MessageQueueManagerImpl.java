package org.csn.component.impl;

import org.csn.component.MessageQueueManager;
import org.csn.data.ReturnType;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.HealthViewMBean;
import org.apache.activemq.broker.jmx.TopicViewMBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MessageQueueManagerImpl implements MessageQueueManager {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private BrokerService service;
    private JMXServiceURL jmxURL;
    private JMXConnector jmxConnector;

    private static final String JMX_SERVICE_URL = "service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi";
    private MBeanServerConnection mBeanConn;
    private ObjectName objectName;
    private static final String BROKER_OBJECT_NAME = "org.apache.activemq:brokerName=localhost,type=Broker";
    private static final String HEALTH_OBJECT_NAME = "org.apache.activemq:type=Broker,brokerName=localhost,service=Health";


    private BrokerViewMBean brokerMBean;
    private HealthViewMBean healthMBean;

    private void connectDefaultJMX(String objName) {
        try {
            jmxURL = new JMXServiceURL(JMX_SERVICE_URL);
            jmxConnector = JMXConnectorFactory.connect(jmxURL);
            mBeanConn = jmxConnector.getMBeanServerConnection();
            objectName = new ObjectName(objName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
    }

    private void getBrokerMBean() {
        connectDefaultJMX(BROKER_OBJECT_NAME);
        brokerMBean = (BrokerViewMBean) MBeanServerInvocationHandler.newProxyInstance(mBeanConn, objectName, BrokerViewMBean.class, true);
    }

    private void getHealthMBean() {
        connectDefaultJMX(HEALTH_OBJECT_NAME);
        healthMBean = (HealthViewMBean) MBeanServerInvocationHandler.newProxyInstance(mBeanConn, objectName, HealthViewMBean.class, true);
    }


    @Override
    public void createTopic(String topicPath) {
        getBrokerMBean();
        try {
            brokerMBean.addTopic(topicPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeTopic(String topicPath) {
        getBrokerMBean();
        try {
            brokerMBean.removeTopic(topicPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getMSGEnqueuedCNT(String topicPath) {
        long retCNT = 0;
        ObjectName[] names = brokerMBean.getTopics();
        Map<String, Long> map = new HashMap<String, Long>();
        for(ObjectName name : names) {
            TopicViewMBean topicMBean = (TopicViewMBean) MBeanServerInvocationHandler.newProxyInstance(mBeanConn, name, TopicViewMBean.class, true);
            if(topicMBean.getName().substring(0, 8).compareTo("ActiveMQ") != 0 &&
                    topicMBean.getName().compareTo(topicPath) == 0) {
                retCNT = topicMBean.getEnqueueCount();
            }
        }
        return retCNT;
    }

    @Override
    public long getMSGDequeuedCNT(String topicPath) {
        long retCNT = 0;
        ObjectName[] names = brokerMBean.getTopics();
        Map<String, Long> map = new HashMap<String, Long>();
        for(ObjectName name : names) {
            TopicViewMBean topicMBean = (TopicViewMBean) MBeanServerInvocationHandler.newProxyInstance(mBeanConn, name, TopicViewMBean.class, true);
            if(topicMBean.getName().substring(0, 8).compareTo("ActiveMQ") != 0 &&
                    topicMBean.getName().compareTo(topicPath) == 0) {
                retCNT = topicMBean.getDequeueCount();
            }
        }
        return retCNT;
    }

    @Override
    public long getPublisherCount(String topicPath) {
        long retCNT = 0;
        ObjectName[] names = brokerMBean.getTopics();
        Map<String, Long> map = new HashMap<String, Long>();
        for(ObjectName name : names) {
            TopicViewMBean topicMBean = (TopicViewMBean) MBeanServerInvocationHandler.newProxyInstance(mBeanConn, name, TopicViewMBean.class, true);
            if(topicMBean.getName().substring(0, 8).compareTo("ActiveMQ") != 0 &&
                    topicMBean.getName().compareTo(topicPath) == 0) {
                retCNT = topicMBean.getProducerCount();
            }
        }
        return retCNT;
    }

    @Override
    public long getSubscriberCount(String topicPath) {
        long retCNT = 0;
        ObjectName[] names = brokerMBean.getTopics();
        Map<String, Long> map = new HashMap<String, Long>();
        for(ObjectName name : names) {
            TopicViewMBean topicMBean = (TopicViewMBean) MBeanServerInvocationHandler.newProxyInstance(mBeanConn, name, TopicViewMBean.class, true);
            if(topicMBean.getName().substring(0, 8).compareTo("ActiveMQ") != 0 &&
                    topicMBean.getName().compareTo(topicPath) == 0) {
                retCNT = topicMBean.getEnqueueCount();
            }
        }
        return retCNT;
    }

    @Override
    public ReturnType setDataDelivererConfiguration(String settings) {
        try {
            service = BrokerFactory.createBroker(settings);
            return ReturnType.Done;
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnType.Error;
        }
    }

    @Override
    public ReturnType startDataDeliverer() {
        try {
            service.start();
            logger.info("Finish to start Data Deliverer");
            return ReturnType.Done;
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnType.Error;
        }
    }

    @Override
    public ReturnType stopDataDeliverer() {
        try {
            service.stop();
            logger.info("finish to stop Data Deliverer");
            return ReturnType.Done;
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnType.Error;
        }
    }

    @Override
    public long getTotalMSGEnqueueCount() {
        getBrokerMBean();
        return brokerMBean.getTotalEnqueueCount();
    }

    @Override
    public long getTotalMSGDequeueCount() {
        getBrokerMBean();
        return brokerMBean.getTotalDequeueCount();
    }

    @Override
    public long getTotalConsumerCount() {
        getBrokerMBean();
        return brokerMBean.getTotalConsumerCount();
    }

    @Override
    public long getTotalProducerCount() {
        getBrokerMBean();
        return brokerMBean.getTotalProducerCount();
    }

    @Override
    public Set<String> getTopicSubscribers() {
        getBrokerMBean();
        ObjectName[] names = brokerMBean.getTopicSubscribers();
        Set<String> subscribers = new HashSet<String>();
        for(ObjectName name : names) {
            subscribers.add(name.toString());
        }
        return subscribers;
    }

    @Override
    public int getStoreUsagePercentage() {
        getBrokerMBean();
        return brokerMBean.getStorePercentUsage();
    }

    @Override
    public int getMemoryUsagePercentage() {
        getBrokerMBean();
        return brokerMBean.getMemoryPercentUsage();
    }

    @Override
    public ReturnType doGarbageCollect() {
        getBrokerMBean();
        try {
            brokerMBean.gc();
            return ReturnType.Done;
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnType.Error;
        }
    }


    @Override
    public ReturnType createSnapshotDeliverer() {
        return null;
    }

    @Override
    public Set<Map<String, Object>> getAllTopicStatus() {
        getBrokerMBean();
        ObjectName[] names = brokerMBean.getTopics();
        Set<Map<String, Object>>  topicSet = new HashSet<Map<String, Object>>();
        for(ObjectName name : names) {
            TopicViewMBean topicMBean = (TopicViewMBean) MBeanServerInvocationHandler.newProxyInstance(mBeanConn, name, TopicViewMBean.class, true);
            if(topicMBean.getName().substring(0, 8).compareTo("ActiveMQ") != 0) {
                Map<String, Object> tempMap = new HashMap<String, Object>();
                tempMap.put("topicName", topicMBean.getName());
                tempMap.put("inNum", topicMBean.getDequeueCount());
                tempMap.put("outNum", topicMBean.getEnqueueCount());
                topicSet.add(tempMap);
            }
        }
        return topicSet;
    }

    @Override
    public Map<String, Long> getAllTopicConsumerCount() {
        getBrokerMBean();
        ObjectName[] names = brokerMBean.getTopics();
        Map<String, Long> map = new HashMap<String, Long>();
        for(ObjectName name : names) {
            TopicViewMBean topicMBean = (TopicViewMBean) MBeanServerInvocationHandler.newProxyInstance(mBeanConn, name, TopicViewMBean.class, true);
            if(topicMBean.getName().substring(0, 8).compareTo("ActiveMQ") != 0)
                map.put(topicMBean.getName(), topicMBean.getConsumerCount());
        }
        return map;
    }

    @Override
    public Map<String, Set<String>> getAllTopicSubscribers() {
        getBrokerMBean();
        ObjectName[] names = brokerMBean.getTopics();
        Map<String, Set<String>> map = new HashMap<String, Set<String>>();
        for(ObjectName name : names) {
            TopicViewMBean topicMBean = (TopicViewMBean) MBeanServerInvocationHandler.newProxyInstance(mBeanConn, name, TopicViewMBean.class, true);
            Set<String> subscribers = new HashSet<String>();
            try {
                ObjectName[] subNames = topicMBean.getSubscriptions();
                for(ObjectName subName : subNames) {
                    subscribers.add(subName.getCanonicalName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (MalformedObjectNameException e) {
                e.printStackTrace();
            }
            map.put(topicMBean.getName(), subscribers);
        }
        return map;
    }


    @Override
    public String getCurrentHealthStatus() {
        getHealthMBean();
        return healthMBean.getCurrentStatus();
    }
}
