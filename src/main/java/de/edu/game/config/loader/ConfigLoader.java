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
    //TODO: add default Values if the filed is not configured!
    private long timeoutInRounds;
    private int maxPlayer;
    private long timeAfterRound;
    private int rows;
    private int columns;
    private int spaceStationHq;
    private String spaceStationDamage;
    private int spaceStationAttackRange;
    private List<Coordinate> spaceStations;
    private List<String> colors = new LinkedList<>(Arrays.asList("#ff0000", "#0033ff", "#06b500", "#aa00ff", "#f5930a"));


    public String getRandomColor() {
        Collections.shuffle(colors);
        String color = colors.get(0);
        this.colors.remove(color);
        return color;
    }

}
