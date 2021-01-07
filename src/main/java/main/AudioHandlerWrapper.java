package main;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class AudioHandlerWrapper {

    //handles connection between audio player and voice channel
    //(der Rest is trivial können sie als Übung ja selbst einmal machen)
    private static class AudioPlayerSendHandler implements AudioSendHandler {
        private final AudioPlayer audioPlayer;
        private AudioFrame lastFrame;

        public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
            this.audioPlayer = audioPlayer;
        }

        @Override
        public boolean canProvide() {
            lastFrame = audioPlayer.provide();
            return lastFrame != null;
        }

        @Override
        public ByteBuffer provide20MsAudio() {
            return ByteBuffer.wrap(lastFrame.getData());
        }

        @Override
        public boolean isOpus() {
            return true;
        }
    }

    private static AudioPlayerManager playerManager;
    private static Map<VoiceChannel, TheoremAudioEventAdapter> players; //used for registering all players

    //creating new AudioPlayerManager
    //needs to be called once at the start of the bot
    public static void init() {
        playerManager = new DefaultAudioPlayerManager();

        //enables loading local files
        AudioSourceManagers.registerLocalSource(playerManager);

        players = new HashMap<>();
    }

    //Creates new AudioPlayer and EventAdapter and maps it to its voice channel
    public static void addVoiceChannel(VoiceChannel channel) {
        AudioPlayer player = playerManager.createPlayer();
        TheoremAudioEventAdapter eventAdapter = new TheoremAudioEventAdapter(channel,player);
        player.addListener(eventAdapter);
        channel.getGuild().getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
        players.put(channel, eventAdapter);
    }

    public static void playTrack(VoiceChannel channel, String... tracks){
        //Getting AudioEventAdapter from map
        TheoremAudioEventAdapter eventAdapter = players.get(channel);
        for(String track : tracks){

            playerManager.loadItem(track, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack audioTrack) {
                    eventAdapter.queue(audioTrack);
                }

                //TODO: Real logging
                @Override
                public void playlistLoaded(AudioPlaylist audioPlaylist) {
                    System.out.println("There was a playlist loaded: " + track);
                }

                @Override
                public void noMatches() {
                    System.out.println("There was no match for: " + track);
                }

                @Override
                public void loadFailed(FriendlyException e) {
                    System.out.println("Load failed for: " + track);
                }
            });

        }
    }

    public static void deletePlayer(VoiceChannel channel){
        players.remove(players.get(channel));
    }
}
