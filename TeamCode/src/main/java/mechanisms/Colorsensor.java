package mechanisms;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import java.util.Objects;

public class Colorsensor {
    NormalizedColorSensor colorSensor;
    NormalizedRGBA sensedcolors;
    public float[] color = {0,0,0}; //defaut
    float gain = 30.0f;
    public float[] redHigher = {222,132,90}; //CLOSER TO WHITE
    public float[] redLower = {107,71,46}; //CLOSER TO BLACK
    public float[] yellowHigher = {255,255,140}; //etc
    public float[] yellowLower = {130,180,75};
    public float[] blueHigher = {100,130,255};
    public float[] blueLower = {48,80,112};
    public void init(HardwareMap hm) {
        colorSensor = hm.get(NormalizedColorSensor.class, "sensor_color");
        colorSensor.setGain(gain);
        if (colorSensor instanceof SwitchableLight) {
            ((SwitchableLight) colorSensor).enableLight(true);
        }

    }
    public void Loop(){
        sensedcolors = colorSensor.getNormalizedColors();
        color = Objects.nonNull(sensedcolors) ? new float[] {255*sensedcolors.red,255*sensedcolors.green,255*sensedcolors.blue} : new float[] {0,0,0};
    }
    public float[] getColor() {
        return new float[] {color[0],color[1],color[2]};
    }
    public boolean sensorIsRed() {
        return colorInRange(color,redLower,redHigher);
    }
    public boolean sensorIsYellow() {
        return colorInRange(color,yellowLower,yellowHigher);
    }
    public boolean sensorIsBlue() {
        return colorInRange(color,blueLower,blueHigher);
    }
    public boolean colorInRange(float[] color, float[] min, float[] max) {
        return
                min[0] <= color[0] && color[0] <= max[0] && //Red is within min and max range
                min[1] <= color[1] && color[1] <= max[1] && //Green is within min and max range
                min[2] <= color[2] && color[2] <= max[2];   //brue is sithin the range,
    }
}
