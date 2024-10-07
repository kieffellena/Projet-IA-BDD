import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;

public class DifferentialDrive
{
    private EV3LargeRegulatedMotor mLeftMotor;
    private EV3LargeRegulatedMotor mRightMotor;

    private final static int SPEED = 50;


    public DifferentialDrive(Port left_port, Port right_port)
    {
        mLeftMotor = new EV3LargeRegulatedMotor(left_port);
        mRightMotor = new EV3LargeRegulatedMotor(right_port);

        mLeftMotor.setSpeed(SPEED);
        mRightMotor.setSpeed(SPEED);
    }


    public void forward()
    {
        mLeftMotor.forward();
        mRightMotor.forward();
    }


    public void stop()
    {
        mLeftMotor.stop();
        mRightMotor.stop();
    }


    public void rotateClockwise()
    {
        mLeftMotor.forward();
        mRightMotor.backward();
    }


    public void rotateCounterClockwise()
    {
        mLeftMotor.backward();
        mRightMotor.forward();
    }
}