package net.rebworks.playlist.watcher.daemon;

import net.rebworks.playlist.watcher.daemon.sleep.SleepStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Runner {

    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);

    private final Task task;
    private final SleepStrategy sleepStrategy;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    public Runner(final Task task, final SleepStrategy sleepStrategy) {
        this.task = task;
        this.sleepStrategy = sleepStrategy;
    }

    public void run() {
        while (true) {
            try {
                final long sleepDuration = sleepStrategy.calculate();
                LOGGER.debug("Will sleep for {} milliseconds", sleepDuration);
                final ScheduledFuture<TaskResult> future = scheduledExecutorService.schedule(task::run,
                                                                                               sleepDuration,
                                                                                               TimeUnit.MILLISECONDS);
                final TaskResult taskResult = future.get();
                if (taskResult.isTerminate()){
                    return;
                }
            } catch (final InterruptedException | ExecutionException e) {
                LOGGER.info("Runner interrupted, exiting");
                return;
            }
        }
    }

}
