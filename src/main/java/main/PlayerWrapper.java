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

}
