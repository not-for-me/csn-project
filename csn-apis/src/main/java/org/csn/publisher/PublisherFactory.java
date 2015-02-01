package org.csn.publisher;

import org.csn.publisher.impl.TopicPublisherImpl;

public class PublisherFactory {
    public TopicPublisher getTopicPublisher() {
        return new TopicPublisherImpl();
    }
}
