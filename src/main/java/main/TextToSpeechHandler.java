package main;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;

import java.io.*;

public class TextToSpeechHandler {

    private final SynthesisInput.Builder inputBuilder;
    private final VoiceSelectionParams.Builder paramsBuilder;

    public TextToSpeechHandler() {
        //Creating two builder for google TTS
        inputBuilder = SynthesisInput.newBuilder(); //Used to create the text input for TTS
        paramsBuilder = VoiceSelectionParams.newBuilder(); //Used to set sound of TTS voice
    }

    /**
     * function that handles the communication with google cloud
     * it writes response to the given file
     * @param outputFile file the audio is written to
     */
    public void generateMP3(String outputFile) throws IOException {
        //trying to create connection with google servers
        try(TextToSpeechClient client = TextToSpeechClient.create()){
            //building text input and settings for TTS
            SynthesisInput input = inputBuilder.build();
            VoiceSelectionParams voice = paramsBuilder.build();

            //setting audio format to mp3
            AudioConfig audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();

            //sending input and settings to google cloud - receiving audio in return
            SynthesizeSpeechResponse response = client.synthesizeSpeech(input, voice, audioConfig);

            //extracting audio as bytes from response
            ByteString audioContents = response.getAudioContent();

            //Write bytes to audio file
            //TODO: logging
            try(OutputStream out = new FileOutputStream((outputFile))){
                out.write(audioContents.toByteArray());
            }catch(FileNotFoundException e){
                System.out.println("File not found in output");
            }catch(IOException e){
                System.out.println("IO exception in output");
            }
        }
    }

    //function to set text that will be sent to google
    public void setMessage(String message){
        inputBuilder.setText(message);
    }

    //function to set language
    public void setLanguageCode(String code){
        paramsBuilder.setLanguageCode(code);
    }

    //function to set gender of TTS voice
    public void setSsmlGender(SsmlVoiceGender gender){
        paramsBuilder.setSsmlGender(gender);
    }

    public SynthesisInput.Builder getInputBuilder(){
        return inputBuilder;
    }

    public VoiceSelectionParams.Builder getVoiceParamsBuilder(){
        return paramsBuilder;
    }



}
