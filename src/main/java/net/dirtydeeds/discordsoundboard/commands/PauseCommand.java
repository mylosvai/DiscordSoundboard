package net.dirtydeeds.discordsoundboard.commands;

import net.dirtydeeds.discordsoundboard.SoundPlayer;

import javax.lang.model.util.ElementScanner14;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dave Furrer
 * <p>
 * Command to stop any playback
 */
public class PauseCommand extends Command {

    private static final Logger LOG = LoggerFactory.getLogger(PauseCommand.class);

    private final SoundPlayer soundPlayer;

    public PauseCommand(SoundPlayer soundPlayer) {
        this.soundPlayer = soundPlayer;
        this.name = "pause";
        this.help = "Pause the sound that is currently playing";
    }

    @Override
    protected void execute(CommandEvent event) {
        int fadeoutTimeout = 0;
        if (event.getArguments().size() > 0) {
            fadeoutTimeout = Integer.parseInt(event.getArguments().getFirst());
        }

        LOG.info("Pause requested by {}", event.getRequestingUser());
        long res = soundPlayer.pause(event.getRequestingUser(), null);
        if (res >= 0) {
            event.replyByPrivateMessage("Playback paused, position " + res + " ms");
        } else if(res == -1) {
            event.replyByPrivateMessage("Playback resumed.");
        }
        else
        {
            event.replyByPrivateMessage("Nothing was playing.");
        }
    }
}
