package net.dirtydeeds.discordsoundboard.controllers.response;

import lombok.Getter;

@Getter
public class AudioPositionInfoResponse {
    long currentPosition;
    long totalLength;
    String Title;
    boolean Paused;

    public AudioPositionInfoResponse(long currentPosition, long totalLength, String Title, boolean Paused) {
        this.currentPosition = currentPosition;
        this.totalLength = totalLength;
        this.Title = Title;
        this.Paused = Paused;        
    }
}

