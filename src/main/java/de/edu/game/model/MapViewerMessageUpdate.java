package de.edu.game.model;

import lombok.NoArgsConstructor;

/**
 * Small Class to manage Updates on the Map <br>
 * Used by the <a href="https://github.com/TetrisIQ/PSG-MapViewer">Map-Viewer</a> <br>
 * Can also handle update messages
 */
@NoArgsConstructor
public class MapViewerMessageUpdate {

    public static boolean hasChange = false;

    public static String message = "";

    public static void updateMap() {
        message = "Update";
        hasChange = true;
    }

    public static void send(String msg) {
        message = msg;
        hasChange = true;
    }

    public static void clear() {
        message = "";
        hasChange = false;
    }

    public boolean hasUpdates() {
        while (!Thread.currentThread().isInterrupted()) {
            if (hasChange) {
                return true;
            }
        }
        return false;
    }


}
