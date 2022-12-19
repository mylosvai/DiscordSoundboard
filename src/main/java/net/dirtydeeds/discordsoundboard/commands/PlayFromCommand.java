package net.dirtydeeds.discordsoundboard.commands;

import net.dirtydeeds.discordsoundboard.SoundPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dave Furrer
 * <p>
 * Command to play the requested sound file
 */
public class PlayFromCommand extends Command {
    private static final Logger LOG = LoggerFactory.getLogger(PlayFromCommand.class);

    private final SoundPlayer soundPlayer;

    public PlayFromCommand(SoundPlayer soundPlayer) {
        this.soundPlayer = soundPlayer;
        this.name = "playf";
        this.help = "Plays the specified sound from the list. add ~number to start the player at the specified milliseconds";
    }

    @Override
    protected void execute(CommandEvent event) {
        try {
            long positionNumber = 1;
            String fileNameRequested = event.getCommandString();
            if (event.getCommandString().equals(this.name)) {
                fileNameRequested = event.getArguments().getFirst();
            }



            // If there is the repeat character (~) then cut up the message string.
            /*int repeatIndex = fileNameRequested.indexOf('~');
            if (repeatIndex > -1) {
                fileNameRequested = fileNameRequested.substring(1, repeatIndex).trim();
                if (repeatIndex + 1 != fileNameRequested.length()) { // If there is something after the ~ then repeat for that value
                    positionNumber = Long.parseLong(fileNameRequested.substring(repeatIndex + 1).trim()); // +1 to ignore the ~ character
                }
            }*/
            
            soundPlayer.playForUser(fileNameRequested, event.getAuthor().getName(), 1, null);

            if(event.getArguments().size() > 1)
            {
                positionNumber = Long.parseLong(event.getArguments().get(1));
                soundPlayer.setPlayerPosition(event.getRequestingUser(), null, positionNumber);                
            }
            LOG.info("Attempting to play file: {} at position {} ms. Requested by {}.", fileNameRequested, positionNumber, event.getRequestingUser());

            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}