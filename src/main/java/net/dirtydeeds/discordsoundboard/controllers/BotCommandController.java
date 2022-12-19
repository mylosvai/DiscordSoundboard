package net.dirtydeeds.discordsoundboard.controllers;

import net.dirtydeeds.discordsoundboard.SoundPlaybackException;
import net.dirtydeeds.discordsoundboard.SoundPlayer;
import net.dirtydeeds.discordsoundboard.controllers.response.ChannelResponse;
import net.dirtydeeds.discordsoundboard.controllers.response.AudioPositionInfoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.sedmelluq.discord.lavaplayer.track.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("/bot")
@SuppressWarnings("unused")
public class BotCommandController {

    private final SoundPlayer soundPlayer;

    @Inject
    public BotCommandController(SoundPlayer soundPlayer) {
        this.soundPlayer = soundPlayer;
    }

    @PostMapping(value = "/playFile")
    public HttpStatus playSoundFile(@RequestParam String soundFileId, @RequestParam String username,
                                    @RequestParam(defaultValue = "1") Integer repeatTimes,
                                    @RequestParam(defaultValue = "") String voiceChannelId,
                                    @RequestParam(defaultValue = "1") long playerPosition
                                    ) {
        soundPlayer.playForUser(soundFileId, username, repeatTimes, voiceChannelId);
        soundPlayer.setPlayerPosition(username, voiceChannelId, playerPosition);
        return HttpStatus.OK;
    }

    @PostMapping(value = "/playUrl")
    public HttpStatus playSoundUrl(@RequestParam String url, @RequestParam String username,
                                   @RequestParam(defaultValue = "") String voiceChannelId) {
        soundPlayer.playForUser(url, username, 1, voiceChannelId);
        return HttpStatus.OK;
    }

    @PostMapping(value = "/random")
    public HttpStatus soundCommand(@RequestParam String username,
                                   @RequestParam(defaultValue = "") String voiceChannelId) {
        try {
            soundPlayer.playRandomSoundFile(username, null);
        } catch (SoundPlaybackException e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.OK;
    }

    @PostMapping(value = "/stop")
    public HttpStatus stopPlayback(@RequestParam String username,
                                   @RequestParam(defaultValue = "") String voiceChannelId) {
        soundPlayer.stop(username, voiceChannelId);
        return HttpStatus.OK;
    }

    @PostMapping(value = "/pause")
    public HttpStatus pausePlayback(@RequestParam String username,
                                   @RequestParam(defaultValue = "") String voiceChannelId) {
        soundPlayer.pause(username, voiceChannelId);
        return HttpStatus.OK;
    }

    @PostMapping(value = "/volume")
    public HttpStatus setVolume(@RequestParam Integer volume, @RequestParam String username,
                                @RequestParam(defaultValue = "") String voiceChannelId) {
        soundPlayer.setSoundPlayerVolume(volume, username, null);
        return HttpStatus.OK;
    }

    @GetMapping(value = "/volume")
    public float getVolume(@RequestParam String username, @RequestParam(defaultValue = "") String voiceChannelId) {
        return soundPlayer.getSoundPlayerVolume(username, voiceChannelId);
    }

    @GetMapping(value = "/channels")
    public List<ChannelResponse> getVoiceChannels() {
        return soundPlayer.getVoiceChannels();
    }

    @GetMapping(value = "/position")
    public AudioPositionInfoResponse getPosition(@RequestParam String username, @RequestParam(defaultValue = "") String voiceChannelId) {

        //long currentPosition, long totalLength, String Title, boolean Paused
        AudioPositionInfoResponse info = soundPlayer.getCurrentAudioTrackInformation(username, voiceChannelId);
        if(info != null)
        {
            return info;
        }
        return new AudioPositionInfoResponse(-1, -1, "", true);
    }

    @PostMapping(value = "/setPosition")
    public HttpStatus setPosition(@RequestParam String username,
                                    @RequestParam(defaultValue = "") String voiceChannelId,
                                    @RequestParam(defaultValue = "1") long playerPosition
                                    ) {        
        soundPlayer.setPlayerPosition(username, voiceChannelId, playerPosition);
        return HttpStatus.OK;
    }


}
