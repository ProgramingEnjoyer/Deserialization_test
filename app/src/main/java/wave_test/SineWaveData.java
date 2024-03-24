package wave_test;
import java.io.Serializable;

public class SineWaveData implements Serializable {
    // Serialization version UID for ensuring compatibility across serialization and deserialization
    private static final long serialVersionUID = 1L;

    // Timestamp
    private double time;
    // Sine wave value
    private double sineValue;

    // Constructor to create an object with a specific timestamp and sine wave value
    public SineWaveData(double time, double sineValue) {
        this.time = time;
        this.sineValue = sineValue;
    }

    // Getter method to retrieve the timestamp
    public double getTime() {
        return time;
    }

    // Getter method to retrieve the sine wave value
    public double getSineValue() {
        return sineValue;
    }

    // Setter method to set the timestamp
    public void setTime(double time) {
        this.time = time;
    }

}
