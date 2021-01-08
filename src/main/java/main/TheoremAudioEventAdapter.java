package main;


import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import main.exceptions.NoVoiceChannelError;
import main.exceptions.NotInThisVoiceChannelException;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Handles events on audio players
 * It queues tracks to play next
 * Starts and stops playback on AudioPlayer
 */
public class TheoremAudioEventAdapter extends AudioEventAdapter {

    private Queue<AudioTrack> queue;//to safe queued tracks
    private final VoiceChannel voiceChannel;//the voice channel the audio player is playing in
    private final AudioPlayer player;//the audio player this event adapter is linked with

    public TheoremAudioEventAdapter(VoiceChannel vc, AudioPlayer player) {
        queue = new LinkedList<>();
        voiceChannel = vc;
        this.player = player;
    }

    //Function called from outside to queue up tracks
    public void queue(AudioTrack track) {
        //if no track is played play the new song else queue it up
        if(player.getPlayingTrack() == null) player.playTrack(track);
        else queue.add(track);
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        super.onPlayerPause(player);
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        super.onPlayerResume(player);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        super.onTrackStart(player, track);
    }

    @Override
    //trying to play next track from queue when track ended
    //deletes audio player that is linked to the voice channel the bot should disconnect
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        //if there is no queue anymore the ending was played and we should disconnect the voice channel
        if(queue == null){
            //Exception can be ignored, because we only call that function when we are connected to a voice channel
            try{ VoiceChannelHandler.disconnectChannel(voiceChannel); }catch (Exception ignored){}
            AudioHandlerWrapper.deletePlayer(voiceChannel);
        }
        //if the queue ist Empty there are no tracks left and the ending should be played
        //and queue ist set to null this way we can always add more tracks without having the ending in the middle
        if(queue.isEmpty()){
            AudioHandlerWrapper.playTrack(voiceChannel,"./src/data/ending.mp3");
            queue = null;
        }
        player.playTrack(queue.poll());
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        super.onTrackException(player, track, exception);
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        super.onTrackStuck(player, track, thresholdMs);
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs, StackTraceElement[] stackTrace) {
        super.onTrackStuck(player, track, thresholdMs, stackTrace);
    }

    @Override
    public void onEvent(AudioEvent event) {
        super.onEvent(event);
    }
}
