# Playlist Watcher

A simple tool for posting additions to a Spotify playlist to the Microsoft
Teams group chat for discussion

## Usage

What you will need:

- A Spotify client ID and client secret
- Username and the playlist ID of the playlist you want to track
- A Microsoft Teams webhook URL (I assume you know how to add a Webhook
  Connector to a channel)

Fill that information, using the provided
[example](playlist-watcher-example.yaml) configuration file as a template.
Start the server as: `java -jar playlist-watcher.jar <config-file-name>`

### Scheduling

Since the Spotify API is throttled, generally it is a good idea to default to a
mild polling interval. If you know that you will be receiving more updates
during certain times, you can configure the system to poll more often. See the
`schedule` section of the config file.

## Future development plans

- Extract the MS Teams related functionality to a separate maven module
- A rewrite using a bot, instead of a connector, which will prevent polluting a
  channel with lots of messages.
- Rewrite the tests in Kotlin or some other, proper, typed language
