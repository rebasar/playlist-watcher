package net.rebworks.playlist.watcher.daemon;

@FunctionalInterface
interface Task {

    TaskResult run();

}
