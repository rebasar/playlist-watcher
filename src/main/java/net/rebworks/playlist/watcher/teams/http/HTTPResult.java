package net.rebworks.playlist.watcher.teams.http;

import java.util.Optional;

public interface HTTPResult {
    boolean isSuccess();

    default String getMessage() {
        return "";
    }

    default Optional<Exception> getException(){
        return Optional.empty();
    }

    default boolean hasException(){
        return getException().isPresent();
    }

    static HTTPResult exception(final Exception e) {
        return new HTTPResult() {
            @Override
            public boolean isSuccess() {
                return false;
            }

            @Override
            public String getMessage() {
                return e.getMessage();
            }

            @Override
            public Optional<Exception> getException(){
                return Optional.of(e);
            }
        };
    }

    static HTTPResult fail(int responseCode, String read) {
        return new HTTPResult() {
            @Override
            public boolean isSuccess() {
                return false;
            }

            @Override
            public String getMessage() {
                return String.format("Request failed. Response code: %s, Response body: %s", responseCode, read);
            }
        };
    }

    static HTTPResult success() {
        return () -> true;
    }
}
