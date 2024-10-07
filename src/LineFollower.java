import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;


public class LineFollower
{
    public static void main(String[] args)
    {
        Controller controller = new Controller(SensorPort.S1, MotorPort.B, MotorPort.D);
        
        controller.run();
        
    }
}