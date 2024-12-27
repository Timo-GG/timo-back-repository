package com.tools.seoultech.timoproject.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.List;

@Deprecated
public class MatchInfoDeserializer extends StdDeserializer<List<Integer>> {
    public MatchInfoDeserializer() {
        this(null);
    }
    public MatchInfoDeserializer(Class<?> vc) {
        super(vc);
    }
    @Override
    public List<Integer> deserialize(
            JsonParser jsonParser, DeserializationContext ctxt
    ) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        JsonNode node1 =  node.get("id");

        return null;
    }

}
