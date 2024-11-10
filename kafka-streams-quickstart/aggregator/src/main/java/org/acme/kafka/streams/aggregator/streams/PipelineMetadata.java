package org.acme.kafka.streams.aggregator.streams;

import java.util.Set;

public class PipelineMetadata {

    public String host;
    public Set<String> partitions;

    public PipelineMetadata(String host, Set<String> partitions) {
        this.host = host;
        this.partitions = partitions;
    }
}
