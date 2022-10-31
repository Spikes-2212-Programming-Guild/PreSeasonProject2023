package frc.robot.subsystems;

import com.spikes2212.command.DashboardedSubsystem;
import com.spikes2212.util.Limelight;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;

public class Vision extends DashboardedSubsystem {

    private static Vision instance;

    private final PhotonCamera photonCamera;
    private final Limelight limelight;

    public static Vision getInstance() {
        if (instance == null) {
            instance = new Vision(new PhotonCamera("photonvision"), new Limelight());
        }
        return instance;
    }

    private Vision(PhotonCamera photonCamera, Limelight limelight) {
        super("vision");
        this.photonCamera = photonCamera;
        this.limelight = limelight;
        this.configureDashboard();
    }

    public double getPhotonVisionYaw() {
        PhotonPipelineResult result = photonCamera.getLatestResult();
        if (result.hasTargets()) {
            return result.getBestTarget().getYaw();
        }
        return 0;// no target
    }

    public void setDriverMode(boolean mode) {
        photonCamera.setDriverMode(mode);
    }

    public double getLimelightYaw() {
        return limelight.getHorizontalOffsetFromTargetInDegrees();
    }

    public boolean limelightHasTarget() {
        return limelight.isOnTarget();
    }

    @Override
    public void configureDashboard() {
        namespace.putBoolean("limelight has target", this::limelightHasTarget);
        namespace.putNumber("limelight yaw", this::getLimelightYaw);
        namespace.putNumber("photon vision yaw", this::getPhotonVisionYaw);
    }
}
