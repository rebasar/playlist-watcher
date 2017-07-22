package net.rebworks.playlist.watcher.signal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Signal;

/**
 * Look away...
 */
public class SignalHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignalHandler.class);

    public void install() {
        Signal.handle(new Signal("INT"), this::handleSignal);
        Signal.handle(new Signal("TERM"), this::handleSignal);
    }

    private void handleSignal(final Signal signal){
        LOGGER.info("Received signal {}({}). Terminating...\n", signal.getName(), signal.getNumber());
        System.exit(0);
    }

}
