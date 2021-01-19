# Theorem-Bot
A collaboration with @thisryan for a Discord.jar bot contest hosted by our university.  
## Function
This bot will join your voice channel, read some predefined random text and leave again.  
The original idea was to make the bot read some theorems from our math-skript. Thats why things are called e.g. `TheoremeHandler.  
But the Bot is capable of reading all kind of texts.  

### How it works
Admins and Bot-Hosts can tell the bot which texts to say by putting textfiles into `data/theoremes/`  
When the bot is called with `join` he will load a text from the selection.  
If the text is not availabe as `mp3` yet a new file will be generated using the Google Cloud text-to-speech API.  
There is also the option to enable random joins of the bot when someone joins a voice channel.  

### Setup
* Create a folder `data/` in `src/`  
* Create a file `config.proerties` in that folder
* Paste and fill in the following config
```properties
DISCORD_API_KEY=
PREFIX=
VERSION=V1.0.0
JOIN_CHANCE=000
CHRIS_ID =
ADRIAN_ID =
```
- `JOINCHANCE`: the chance of a random join in percent
- `ADRIAN_ID` / `CHRIS_ID` are fields to fill in the owners discord-ids to enable registration of texts via discord.  
* Set your Google Cloud API key as environment variable
* Run the following command to start the bot
```bash
mvn clean package && java -jar target/DiscordBot-1-jar-with-dependencies.jar
```

#### Please note
This project / bot isn't meant to run productive although it should be capable of doing so.
We've got multi-server support and things like that covered.  
It's rather our 'little' funny side-project, whose development might not be continued.  
We decided to publish it anyways to show of our code for fellow students and other interested people.  