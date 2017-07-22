package net.rebworks.playlist.watcher.teams;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import net.rebworks.playlist.watcher.teams.exceptions.SerializationException;

public class Serializer {

    public byte[] serialize(final Card card){
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new Jdk8Module());
            return objectMapper.writeValueAsBytes(card);
        } catch (final JsonProcessingException exception) {
            throw new SerializationException(exception);
        }
    }

}
