package org.acme.rest.json.codec;

import com.mongodb.MongoClient;
import org.acme.rest.json.Fruit;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.UUID;

public class FruitCodec implements CollectibleCodec<Fruit> {

    private final Codec<Document> documentCodec;

    public FruitCodec() {
        this.documentCodec = MongoClient.getDefaultCodecRegistry().get(Document.class);
    }

    @Override
    public void encode(BsonWriter writer, Fruit fruit, EncoderContext encoderContext) {
        Document doc = new Document();
        doc.put("name", fruit.getName());
        doc.put("description", fruit.getDescription());
        documentCodec.encode(writer, doc, encoderContext);
    }

    @Override
    public Class<Fruit> getEncoderClass() {
        return Fruit.class;
    }

    @Override
    public Fruit generateIdIfAbsentFromDocument(Fruit document) {
        if (!documentHasId(document)) {
            document.setId(UUID.randomUUID().toString());
        }
        return document;
    }

    @Override
    public boolean documentHasId(Fruit document) {
        return document.getId() != null;
    }

    @Override
    public BsonValue getDocumentId(Fruit document) {
        return new BsonString(document.getId());
    }

    @Override
    public Fruit decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);
        Fruit fruit = new Fruit();
        if (document.getString("id") != null) {
            fruit.setId(document.getString("id"));
        }
        fruit.setName(document.getString("name"));
        fruit.setDescription(document.getString("description"));
        return fruit;
    }
}