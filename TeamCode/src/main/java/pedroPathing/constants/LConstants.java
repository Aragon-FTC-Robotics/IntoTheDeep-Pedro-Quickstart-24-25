package pedroPathing.constants;

import com.pedropathing.localization.*;
import com.pedropathing.localization.constants.*;

public class LConstants {
    static {
        ThreeWheelConstants.forwardTicksToInches = 0.00296993062;
        ThreeWheelConstants.strafeTicksToInches = 0.00302896577;
        ThreeWheelConstants.turnTicksToInches = 0.003;
        ThreeWheelConstants.leftY = 5.1;
        ThreeWheelConstants.rightY = -5.1;
        ThreeWheelConstants.strafeX = -5.25;
        ThreeWheelConstants.leftEncoder_HardwareMapName = "rightBack";
        ThreeWheelConstants.rightEncoder_HardwareMapName = "leftBack";
        ThreeWheelConstants.strafeEncoder_HardwareMapName = "rightFront";
        ThreeWheelConstants.leftEncoderDirection = Encoder.FORWARD;
        ThreeWheelConstants.rightEncoderDirection = Encoder.REVERSE;
        ThreeWheelConstants.strafeEncoderDirection = Encoder.REVERSE;
    }
}




