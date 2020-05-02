package org.acme.tokenmanager.config;

import org.acme.tokenmanager.controllers.dtos.Entry;
import org.infinispan.protostream.SerializationContextInitializer;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;

@AutoProtoSchemaBuilder(
        includeClasses = {
            Entry.class
        },
        schemaFileName = "entry.proto",
        schemaFilePath = "proto/",
        schemaPackageName = "entries")
public interface EntriesSerializationContextInitializer extends  SerializationContextInitializer {
}
