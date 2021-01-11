package main.commands;

import main.DiscordBot;
import main.utils.TheoremHandler;
import main.audio.AudioHandlerWrapper;
import main.audio.VoiceChannelHandler;
import main.exceptions.NoVoiceChannelError;
import main.exceptions.NotInThisVoiceChannelException;
import main.utils.EmbedFactory;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ConstantConditions")
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

        //command for adding theorems through the bot
        ch.addCommand("add-entry", Commands::addEntry);

        //hydro command
        ch.addCommand("hydro", Commands::hydro);

        return ch;
    }

    //functionality for join command
    public static void join(MessageReceivedEvent event){
        //handles exception if user isn't in a voice channel
        try  {
            int theoremAmount = TheoremHandler.getTheoremAmount(event.getMessage().getContentRaw());
            List<String> theoremList = TheoremHandler.generateTheorems(theoremAmount);
            String[] theoremArr = theoremList.toArray(new String[0]);

            VoiceChannel channel = event.getMember().getVoiceState().getChannel();
            VoiceChannelHandler.joinChannel(channel);

            AudioHandlerWrapper.addVoiceChannel(channel);
            AudioHandlerWrapper.playTrack(channel,theoremArr);

            AudioHandlerWrapper.printMap();

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
            List<String> theoremList = TheoremHandler.generateTheorems(theoremAmount);
            String[] theoremArr = theoremList.toArray(new String[0]);
            AudioHandlerWrapper.playTrack(voiceChannel, theoremArr);

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

        event.getChannel().sendMessage("Added " + theoremAmount + "theorems to the playlist").queue();
    }

    //functionality for leave command
    public static void leave(MessageReceivedEvent event) {
        try {
            VoiceChannelHandler.disconnectChannel(event.getMember().getVoiceState().getChannel());
        }catch (NoVoiceChannelError noVoiceChannelError) {
            event.getChannel().sendMessage("You're not even in a voice channel").queue();
        }catch(NotInThisVoiceChannelException e){
            event.getChannel().sendMessage("I'm not in your voice channel").queue();
        }
    }

    //functionality for help command
    public static void help(MessageReceivedEvent event){
        EmbedFactory.MESSAGE().setTitle("Command list")
                .setMessage("The following commands are available using `"+ DiscordBot.prefix +"` as prefix: \n\n " +
                "`join` [optional amount of theorems standard = 1] \n\n" +
                "`play` [amount of theorems] adds specified amount of theorems to you playlist\n\n " +
                "This bot is open source: https://github.com/nonchris/discord-theorem-bot")
                .setFooter("Please report any issues on GitHub - This bot runs: " + DiscordBot.config.get("VERSION"))
                .dispatch(event.getTextChannel());
    }

    //functionality for add-entry command
    public static void addEntry(MessageReceivedEvent event){
        //getting id and checks if member is authorized to do that
        long AdrianID = Long.parseLong((String)DiscordBot.config.get("ADRIAN_ID"));
        long ChrisID = Long.parseLong((String)DiscordBot.config.get("CHRIS_ID"));
        long memberID = event.getMember().getIdLong();

        //if an other member except bot owners tries to add a theorem - sending error
        if(memberID != AdrianID && memberID != ChrisID){
            EmbedFactory.PERMISSION_USER().dispatch(event.getTextChannel());
            return;
        }

        //splitting the message into its tokens
        String[] tokens = event.getMessage().getContentRaw().split(" ");

        try {
            //get the filename for theorem
            String fileName = tokens[1];

            //if the given message does not have enough parameters end the function
            if(tokens.length <= 2){
                EmbedFactory.WRONG_PARAMETER().setMessage("You did not give the right amount of parameters").dispatch(event.getTextChannel());
                return;
            }

            //get the text of the theorem
            String theoremText = Arrays.stream(tokens).skip(2).collect(Collectors.joining(" "));

            //add the theorem
            TheoremHandler.addTheorem(fileName, theoremText);

        } catch (FileAlreadyExistsException e) {
            EmbedFactory.WRONG_PARAMETER().setMessage("This Theorem File already exists").dispatch(event.getTextChannel());
            return;
        }

        //inform the user of success
        EmbedFactory.SUCCESS().setMessage("The Theorem was successfully added").dispatch(event.getTextChannel());
    }

    //functionality for hydro command
    public static void hydro(MessageReceivedEvent event) {
        EmbedFactory.MESSAGE().setTitle("HYDROO")
                .setMessage("Stay Hydrated!")
                .setColor(new Color(27, 0, 255, 255))
                .dispatch(event.getTextChannel());
    }
}
