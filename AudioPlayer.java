// AudioPlayer.java
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AudioPlayer {
    private static List<Clip> activeClips = new ArrayList<>();

    public static void playSound(String filePath, boolean loop) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            
            activeClips.add(clip);
            
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY); // background music
            } else {
                clip.start(); // one-time sound effect
            }
            
            // Remove clip from list when it finishes (for non-looping sounds)
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP && !loop) {
                    activeClips.remove(clip);
                    clip.close();
                }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    public static void stopAll() {
        for (Clip clip : new ArrayList<>(activeClips)) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.close();
        }
        activeClips.clear();
    }
}
