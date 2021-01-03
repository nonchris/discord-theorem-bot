package main;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/*
CommandHandler uses a Map to link command messages to
command functions(Consumer) that are defined
in the Commands class
 */
public class CommandHandler extends ListenerAdapter{

    private Map<String, Consumer<MessageReceivedEvent>> commands;

    public CommandHandler() {commands = new HashMap<>();}

    //used register a command
    //commands aren't case sensitive
    public void addCommand(String command, Consumer<MessageReceivedEvent> consumer){
        command = command.toLowerCase();
        commands.put(command, consumer);
    }

    //checking and calling a command if registered
    public void checkCommand(MessageReceivedEvent event) {
        //bots cant enter commands
        if(event.getAuthor().isBot()) return;

        //getting only first word as command - in case there are arguments passed in
        String command = event.getMessage().getContentRaw().toLowerCase().split(" ")[0];

        if(!command.startsWith(DiscordBot.prefix)) return;
        command = command.replace(DiscordBot.prefix, ""); //getting rid of prefix

        //searching in map and executing
        if(commands.containsKey(command))
            commands.get(command).accept(event);
    }

    @Override
    //checking for each message if it contains a command
    public void onMessageReceived(MessageReceivedEvent event){
        checkCommand(event);
    }

}
