package net.rebworks.playlist.watcher.teams;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.rebworks.playlist.watcher.teams.ImmutableCard.Builder;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface Card {

    @JsonProperty("@type")
    default String getType(){
        return "MessageCard";
    }

    @JsonProperty("@context")
    default String getContext(){
        return "http://schema.org/extensions";
    }

    default String getThemeColor(){
        return "1ed65f";
    }

    Optional<String> getTitle();

    String getText();

    static Builder builder(){
        return ImmutableCard.builder();
    }

}
