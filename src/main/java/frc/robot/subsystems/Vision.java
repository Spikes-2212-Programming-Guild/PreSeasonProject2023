package frc.robot.subsystems;

import com.spikes2212.command.DashboardedSubsystem;
import com.spikes2212.util.Limelight;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;

public class Vision extends DashboardedSubsystem {

    private final PhotonCamera photonCamera;
    private final Limelight limelight;
    private static Vision instance;

    public static Vision getInstance() {
        if (instance == null) {
            instance = new Vision();
        }
        return instance;
    }
    public Vision() {
        super("photonvision");
        photonCamera = new PhotonCamera("photonvision");
        limelight = new Limelight();
    }

    public double photonvisionYaw() {
        PhotonPipelineResult result = photonCamera.getLatestResult();
        if (result.hasTargets()) {
            return result.getBestTarget().getYaw();
        }
        return 0;// no target
    }

    public void setDriverMode(boolean mode) {
        photonCamera.setDriverMode(mode);
    }

    public double limelightYaw(){
        return limelight.getHorizontalOffsetFromTargetInDegrees();
    }


    @Override
    public void configureDashboard() {
    }
}
