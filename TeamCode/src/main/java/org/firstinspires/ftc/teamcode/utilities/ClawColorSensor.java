package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ClawColorSensor {

    public enum ColorOptions {
        RED, GREEN, BLUE, YELLOW, UNKNOWN
    }

    ColorSensor colorSensor;
    public ClawColorSensor(HardwareMap hmap) {
        this.colorSensor = hmap.get(ColorSensor.class, CONFIG.clawColorSensor);
    }

    public int red() {
        return colorSensor.red();
    }

    public int green() {
        return colorSensor.green();
    }

    public int blue() {
        return colorSensor.blue();
    }

    /**
     * @return std deviation between red, green, and blue values
     */
    public double stdDev() {
        int mean = (red() + green() + blue()) / 3;
        return Math.sqrt((Math.pow(red() - mean, 2) + Math.pow(green() - mean, 2) + Math.pow(blue() - mean, 2)) / 3);
    }

    public boolean mostly(ColorOptions color) {
        if (stdDev() < 10.0) return false;

        switch (color) {
            case RED:
                return red() > 2 * green() && red() > 2 * blue();
            case BLUE:
                return blue() > 2 * red() && blue() > 2 * green();
            default:
                return false;
        }
    }
}
