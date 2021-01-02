package main;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

public class VoiceChannelHandler extends ListenerAdapter {

    PlayerWrapper playerWrapper = new PlayerWrapper();

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        //ignores other bots and self
        if(event.getAuthor().isBot()) return;

        //gets message content and checks for command prefix
        Message message = event.getMessage();
        String content = message.getContentRaw();

        if(!content.startsWith(DiscordBot.prefix)) return;

        //checking for valid command
        if(content.contains("join")) {
            joinChannel(event.getGuild(), event.getChannel(), event.getMember().getVoiceState().getChannel());
        }
        else if(content.contains("leave")) {
            event.getGuild().getAudioManager().closeAudioConnection();
        }
    }

    private void joinChannel(Guild guild, MessageChannel channel, VoiceChannel connectedChannel) {
        //returns if bot is already in voice channel
        if(guild.getSelfMember().getVoiceState().inVoiceChannel()) return;
        System.out.println("Connected");

        //returns if user is not in voice channel
        if(connectedChannel == null) {
            channel.sendMessage("You are not connected to a voice channel!").queue();
            return;
        }

        //getting audio manager
        AudioManager audioManager = guild.getAudioManager();

        //connecting to channel
        audioManager.openAudioConnection(connectedChannel);
        System.out.println("Joined Channel: " + connectedChannel.getName());

        //starts audio
        playerWrapper.play(audioManager);
    }

}
