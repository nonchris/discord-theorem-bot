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
import net.dv8tion.jda.api.managers.AudioManager;
import java.nio.ByteBuffer;


public class PlayerWrapper {

    //handles connection between audio player and voice channel
    //(der Rest is trivial können sie als Übung ja selbst einmal machen)
    private class AudioPlayerSendHandler implements AudioSendHandler {
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

    AudioPlayerManager playerManager;
    AudioPlayer player;

    TheoremAudioEventAdapter eventAdapter;

    public PlayerWrapper() {
        //init audio player manager
        playerManager = new DefaultAudioPlayerManager();
        //enables audio manager to read and play local files
        AudioSourceManagers.registerLocalSource(playerManager);

        //init audio player
        player = playerManager.createPlayer();
        eventAdapter = new TheoremAudioEventAdapter(playerManager);
        player.addListener(eventAdapter);
    }

    public void play(AudioManager audioManager) {

        audioManager.setSendingHandler(new AudioPlayerSendHandler(player));

        eventAdapter.setAudioManager(audioManager); //for closing the connection

        //loading track and passing it to audio player
        playerManager.loadItem("./src/data/greeting.mp3", new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                player.playTrack(audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                System.out.println("list loaded");
            }

            @Override
            public void noMatches() {
                System.out.println("no matches");
            }

            @Override
            public void loadFailed(FriendlyException e) {
                System.out.println("Load Failed");
            }
        });
    }
}

