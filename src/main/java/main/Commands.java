package main;

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
        ch.addCommand("join", event -> {
            //handles exception if user isn't in a voice channel
            try  {
                int theoremAmount = TheoremHandler.getTheoremAmount(event.getMessage().getContentRaw());
                List<String> theoremList = TheoremHandler.generateTheoreme(theoremAmount);
                theoremList.add(0,"./src/data/greeting.mp3");
                String[] theoremArr = theoremList.stream().toArray(String[]::new);

                VoiceChannel channel = event.getMember().getVoiceState().getChannel();
                VoiceChannelHandler.joinChannel(channel);

                AudioHandlerWrapper.addVoiceChannel(channel);
                AudioHandlerWrapper.playTrack(channel,theoremArr);

            }catch(NoVoiceChannelError e){
                event.getChannel().sendMessage("You are not in a voice channel").queue();
            }catch(IOException e){
                System.out.println("could not load file");
            }catch(IllegalArgumentException e){
                event.getChannel().sendMessage("The was something wrong with you arguments").queue();
            }
        });

        //command to add specified amount of theorems to playlist
        ch.addCommand("play",event -> {
            int theoremAmount;
            try {
                //gets amount of theorems and checks if command attributes are valid
                theoremAmount = TheoremHandler.getTheoremAmount(event.getMessage().getContentRaw());

                //Checks if bot is in same voiceChannel as calling Member
                VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel();
                VoiceChannelHandler.checkVoiceChannel(voiceChannel);

                //Gets Paths to theorems and plays them
                List<String> theoremList = TheoremHandler.generateTheoreme(theoremAmount);
                String[] theoremArr = theoremList.stream().toArray(String[]::new);
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
                event.getChannel().sendMessage("The was something wrong with you arguments").queue();
                return;
            }

            event.getChannel().sendMessage("Added " + theoremAmount + " theoreme to the playlist").queue();
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
