package main.utils;

import main.audio.AudioHandlerWrapper;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;


public class Cleanup extends ListenerAdapter {

    @Override
    //function to clean up remaining connection in case the ot gets kicked from a voice channel
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        VoiceChannel channel = event.getChannelLeft();
        if(event.getMember() == event.getGuild().getSelfMember() && AudioHandlerWrapper.isStillRegistered(channel)){
            AudioHandlerWrapper.deletePlayer(channel);
        }
    }
}
