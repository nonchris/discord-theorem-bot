package main;

import com.google.protobuf.Message;
import main.exceptions.NoVoiceChannelError;
import main.exceptions.NotInThisVoiceChannelException;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.List;


public class Commands {

    //Initializes the CommandHandler with every Command
    public static CommandHandler init(){
        CommandHandler ch = new CommandHandler();

        //command to make the bot join a channel
        ch.addCommand("join", Commands::join);

        //command to add specified amount of theorems to playlist
        ch.addCommand("play", Commands::play);

        //command for leaving a voice channel
        ch.addCommand("leave", Commands::leave);

        //TODO: automate this in CommandHandler?
        //command for showing all available commands
        ch.addCommand("help", Commands::help);

        return ch;
    }

    //functionality for join command
    public static void join(MessageReceivedEvent event){
        //handles exception if user isn't in a voice channel
        try  {
            int theoremAmount = TheoremHandler.getTheoremAmount(event.getMessage().getContentRaw());
            List<String> theoremList = TheoremHandler.generateTheoreme(theoremAmount);
            theoremList.add(0,"./src/data/greeting.mp3");
            String[] theoremArr = theoremList.toArray(new String[0]);

            VoiceChannel channel = event.getMember().getVoiceState().getChannel();
            VoiceChannelHandler.joinChannel(channel);

            AudioHandlerWrapper.addVoiceChannel(channel);
            AudioHandlerWrapper.playTrack(channel,theoremArr);

        }catch(NoVoiceChannelError e){
            EmbedFactory.PERMISSION_USER().setMessage("You are not in a voice channel").dispatch(event.getTextChannel());
        }catch(IOException e){
            System.out.println("could not load file");
        }catch(IllegalArgumentException e){
            EmbedFactory ef = EmbedFactory.WRONG_PARAMETER();
            ef.setMessage("The was something wrong with you arguments").dispatch(event.getTextChannel());
        }
    }

    //functionality for play command
    public static void play(MessageReceivedEvent event){
        int theoremAmount;
        try {
            //gets amount of theorems and checks if command attributes are valid
            theoremAmount = TheoremHandler.getTheoremAmount(event.getMessage().getContentRaw());

            //Checks if bot is in same voiceChannel as calling Member
            VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel();
            VoiceChannelHandler.checkVoiceChannel(voiceChannel);

            //Gets Paths to theorems and plays them
            List<String> theoremList = TheoremHandler.generateTheoreme(theoremAmount);
            String[] theoremArr = theoremList.toArray(new String[0]);
            AudioHandlerWrapper.playTrack(voiceChannel,theoremArr);

        } catch (IOException e) {
            return;
        }catch(NoVoiceChannelError e) {
            event.getChannel().sendMessage("You are not in a voice channel").queue();
            return;
        }catch(NotInThisVoiceChannelException e){
            event.getChannel().sendMessage("Im not in your voice channel").queue();
            return;
        }catch(IllegalArgumentException e){
            EmbedFactory.WRONG_PARAMETER().dispatch(event.getTextChannel());
            return;
        }

        event.getChannel().sendMessage("Added " + theoremAmount + "theoreme to the playlist").queue();
    }

    //functionality for leave command
    public static void leave(MessageReceivedEvent event) {
        try {
            VoiceChannelHandler.disconnectChannel(event.getMember().getVoiceState().getChannel());
        } catch (NoVoiceChannelError noVoiceChannelError) {
            event.getChannel().sendMessage("You're not even in a voice channel").queue();
        }catch(NotInThisVoiceChannelException e){
            event.getChannel().sendMessage("I'm not in your voice channel").queue();
        }
    }

    //functionality for help command
    public static void help(MessageReceivedEvent event){
        EmbedFactory.MESSAGE().setTitle("Command list")
                .setMessage("The following commands are available using `"+ DiscordBot.prefix +"` as prefix: \n\n " +
                "`join` [optional amount of theorems standart = 1] \n\n" +
                "`play` [amount of theorems] adds specified amount of theorems to you playlist\n\n " +
                "This bot is open source: https://github.com/nonchris/discord-theorem-bot")
                .setFooter("Please report any issues on GitHub - This bot runs: " + DiscordBot.config.get("VERSION"))
                .dispatch(event.getTextChannel());
    }
}
