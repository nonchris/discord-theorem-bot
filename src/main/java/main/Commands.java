package main;

import net.dv8tion.jda.api.entities.VoiceChannel;


public class Commands {

    //Initializes the CommandHandler with every Command
    public static CommandHandler init(){
        CommandHandler ch = new CommandHandler();

        //command to make the bot join a channel
        ch.addCommand("join", event -> {
            //handles exception if user isn't in a voice channel
            try  {
                VoiceChannelHandler.joinChannel(event.getMember().getVoiceState().getChannel());
            }catch(NoVoiceChannelError e){
                event.getChannel().sendMessage("You are not in a voice channel").queue();
            }
        })
        ;

        //command for leaving a voice channel
        ch.addCommand("leave", event -> {
            VoiceChannel selfChannel = event.getGuild().getSelfMember().getVoiceState().getChannel();
            if(selfChannel == null) return;

            //check if user is in same channel as bot
            if(selfChannel.equals(event.getMember().getVoiceState().getChannel()))
                event.getGuild().getAudioManager().closeAudioConnection();
        });

        return ch;
    }
}
