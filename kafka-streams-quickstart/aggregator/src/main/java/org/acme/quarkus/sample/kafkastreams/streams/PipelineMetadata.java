package org.acme.quarkus.sample.kafkastreams.streams;

import java.util.Set;

public class PipelineMetadata {

    public String host;
    public Set<String> partitions;

    public PipelineMetadata(String host, Set<String> partitions) {
        this.host = host;
        this.partitions = partitions;
    }
}
