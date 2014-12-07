package org.csn.subscriber;

import org.csn.subscriber.impl.TopicSubscriberImpl;

public class SubscriberFactory {

    public TopicSubscriber getTopicSubscriber() {
        return new TopicSubscriberImpl();
    }
}
