package main.commands;

import main.DiscordBot;
import main.audio.AudioHandlerWrapper;
import main.audio.VoiceChannelHandler;
import main.exceptions.NoVoiceChannelError;
import main.utils.TheoremHandler;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class RandomJoiner extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
        //Picks random number
        Random rand = new Random();
        int number = rand.nextInt(100);

        //checks if number is smaller than percent
        if(number <= Float.parseFloat((String) DiscordBot.config.get("JOIN_CHANCE"))){

            //gets voice channel and joins it
            VoiceChannel channel = event.getChannelJoined();
            try {
                VoiceChannelHandler.joinChannel(channel);
                AudioHandlerWrapper.addVoiceChannel(channel);

                //gets 1 theorem
                List<String> theorem = TheoremHandler.generateTheorems(1);

                //plays the theorem
                AudioHandlerWrapper.playTrack(channel,theorem.toArray(new String[0]));
            }catch(NoVoiceChannelError e){
                System.out.println("The bot could not follow join for some reason");
            }catch(IOException e){
                System.out.println("io Exception");
            }
        }
    }

}
