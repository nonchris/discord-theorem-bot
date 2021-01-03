package main;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;

public class VoiceChannelHandler {

    private static PlayerWrapper playerWrapper = new PlayerWrapper();

    public static void joinChannel(VoiceChannel voiceChannel) throws NoVoiceChannelError {
        //returns if user is not in voice channel
        if(voiceChannel == null) {
            throw new NoVoiceChannelError();
        }

        Guild guild = voiceChannel.getGuild();

        //returns if bot is already in voice channel
        if(guild.getSelfMember().getVoiceState().inVoiceChannel()) return;

        //getting audio manager
        AudioManager audioManager = guild.getAudioManager();

        //connecting to channel
        audioManager.openAudioConnection(voiceChannel);
        System.out.println("Joined Channel: " + voiceChannel.getName());

        //starts audio
        playerWrapper.play(audioManager);
    }

}
