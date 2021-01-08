package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Class for handling every Theorem related functionality
 */
public class TheoremHandler {

    //path to theorem directory
    private static final String theoremPath = "./src/data/theoreme";

    //Function for randomly picking specified amount of theorems
    public static List<String> generateTheoreme(int amount) throws IOException {
        //Gets a List of all available Theorems
        List<Path> paths = Files.list(Paths.get(theoremPath)).collect(Collectors.toList());
        List<Path> takenPaths = new ArrayList<>();
        List<String> theoreme = new ArrayList<>();

        Random rand = new Random();

        int i = 0;
        while(i < amount){
            //Picks a random path
            int rand_index = rand.nextInt(paths.size());
            Path path = paths.get(rand_index);

            //if theorem is not already taken put
            //put it in the theorems list and save it as taken
            if(!takenPaths.contains(path)){
                theoreme.add(path.toString());
                takenPaths.add(path);
                paths.remove(rand_index);
                i++;
            }

            //if every path is taken once: reset paths list to pick doubles
            if(paths.isEmpty()){
                paths.addAll(takenPaths);
                takenPaths.clear();
            }
        }

        return theoreme;
    }

    //Function for getting the amount of theorems that should be played
    public static int getTheoremAmount(String message){
        String[] words = message.split(" ");
        int number;

        try{
            number = Integer.parseInt(words[1]);
        }catch(NumberFormatException e){
            throw new IllegalArgumentException();
        }catch(IndexOutOfBoundsException e){
            return 1;
        }

        return number;
    }


}
