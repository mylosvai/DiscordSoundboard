package net.dirtydeeds.discordsoundboard.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.List;

public class AudioHandler extends AudioEventAdapter implements AudioSendHandler {

    private final PlayerManager manager;
    private final AudioPlayer audioPlayer;
    private final long guildId;
    private AudioFrame lastFrame;
    private long seekPosition;

    protected AudioHandler(PlayerManager manager, Guild guild, AudioPlayer player)
    {
        seekPosition = -1;
        this.manager = manager;
        this.audioPlayer = player;
        this.guildId = guild.getIdLong();
    }

    public int addTrack(AudioTrack track)
    {
        audioPlayer.playTrack(track);
        return -1;
    }

    public int addTracks(List<AudioTrack> tracks) {
        return -1;
    }

    public AudioPlayer getPlayer()
    {
        return audioPlayer;
    }

    public void setSeekPosition(long ms){
        seekPosition = ms;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        int repeatTimes = (int)track.getUserData();
        if (repeatTimes > 1) {
            track.setUserData(--repeatTimes);
            audioPlayer.playTrack(track.makeClone());
        }
    }
    
    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {        
            if(seekPosition != -1)
            {   
                track.setPosition(seekPosition);
            }
            seekPosition = -1;
      }

    @Override
    public boolean canProvide() {
        lastFrame = audioPlayer.provide();
        return lastFrame != null;
    }

    @Nullable
    @Override
    public ByteBuffer provide20MsAudio() {
        return ByteBuffer.wrap(lastFrame.getData());
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
