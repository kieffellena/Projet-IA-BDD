import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;

/**
 * Extends the EV3TouchSensor to provide it with isPressed() functionality.
 */

public class TouchSensor extends EV3TouchSensor
{
    public TouchSensor(Port port)
    {
        super(port);
    }

    public boolean isPressed()
    {
        float[] sample = new float[1];
        fetchSample(sample, 0);

        return sample[0] != 0;
    }
}
