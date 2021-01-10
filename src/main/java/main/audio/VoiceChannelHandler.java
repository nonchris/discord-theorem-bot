package main.audio;

import main.exceptions.NoVoiceChannelError;
import main.exceptions.NotInThisVoiceChannelException;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;

@SuppressWarnings("ConstantConditions")
public class VoiceChannelHandler {

    public static void joinChannel(VoiceChannel voiceChannel) throws NoVoiceChannelError {
        //returns if user is not in voice channel
        if(voiceChannel == null) throw new NoVoiceChannelError();

        Guild guild = voiceChannel.getGuild();

        //returns if bot is already in voice channel
        if(guild.getSelfMember().getVoiceState().inVoiceChannel()) return;

        //getting audio manager
        AudioManager audioManager = guild.getAudioManager();

        //connecting to channel
        audioManager.openAudioConnection(voiceChannel);
        System.out.println("Joined Channel: " + voiceChannel.getName());
    }

    public static void disconnectChannel(VoiceChannel voiceChannel) throws NoVoiceChannelError, NotInThisVoiceChannelException {
        checkVoiceChannel(voiceChannel);

        voiceChannel.getGuild().getAudioManager().closeAudioConnection();
    }

    public static void checkVoiceChannel(VoiceChannel voiceChannel) throws NotInThisVoiceChannelException,NoVoiceChannelError{
        if(voiceChannel == null) throw new NoVoiceChannelError();

        VoiceChannel selfChannel = voiceChannel.getGuild().getSelfMember().getVoiceState().getChannel();
        if(!voiceChannel.equals(selfChannel)) throw new NotInThisVoiceChannelException();
    }

}
