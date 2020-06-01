package de.edu.game.config.loader;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import de.edu.game.model.Coordinate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor // provide a Constructor with all args, witch is required by Gson
@Getter
public class ConfigLoader {
    static {
        try {
            final Gson gson = new Gson();
            //String path = new File("src/main/resources/conf.json").getAbsolutePath();
            final String path = new File("conf.json").getAbsolutePath();
            final ConfigLoader conf = gson.fromJson(new FileReader(path), ConfigLoader.class);
            // shuffle start positions
            Collections.shuffle(conf.getSpaceStations());
            shared = conf;
        } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // shared Object, so we don't need to read the config file again
    public static ConfigLoader shared;

    // Parameters witch should be in the config files
    // Here are the default values, if they are not configured in other ways
    // The default values will be override by the Config file
    private long timeoutInRounds = 0; //  TODO: maybe I will remove this
    private int maxPlayer = 4;
    private long timeAfterRound = 0;
    private int rows = 40;
    private int columns = 20;
    private MeepleConfig spaceStation = new MeepleConfig("SpaceStation", 400, "2w20+30", "2w20+30", 2,10);
    private MeepleConfig transporter = new MeepleConfig("Transporter", 100, "0d0","1d20+5",1,20);
    private AsteroidConfig asteroid = new AsteroidConfig();
    private List<Coordinate> spaceStations = new LinkedList<>(Arrays.asList(new Coordinate(0, 1, 1), new Coordinate(0, 28, 1), new Coordinate(0, 1, 18), new Coordinate(0, 38, 18)));
    private List<String> colors = new LinkedList<>(Arrays.asList("#ff0000", "#0033ff", "#06b500", "#aa00ff", "#f5930a"));


    public String getRandomColor() {
        Collections.shuffle(colors);
        String color = colors.get(0);
        this.colors.remove(color);
        return color;
    }

}
