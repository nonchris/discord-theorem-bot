package main;

import main.exceptions.NoVoiceChannelError;
import main.exceptions.NotInThisVoiceChannelException;


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
        });

        //command for leaving a voice channel
        ch.addCommand("leave", event -> { try {
                VoiceChannelHandler.disconnectChannel(event.getMember().getVoiceState().getChannel());
            } catch (NoVoiceChannelError noVoiceChannelError) {
                event.getChannel().sendMessage("You're not even in a voice channel").queue();
            }catch(NotInThisVoiceChannelException e){
                event.getChannel().sendMessage("I'm not in your voice channel").queue();
            }
        });

        return ch;
    }
}
