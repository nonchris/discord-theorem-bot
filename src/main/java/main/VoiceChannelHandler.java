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
