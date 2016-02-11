package edx.sfc.objects;

public class CrimeLocation {
    private boolean needsRecoding;
    private double longitude;
    private double latitude;
    private String humanAddress;

    public boolean needsRecoding() {
        return needsRecoding;
    }

    public void setNeedsRecoding(boolean needsRecoding) {
        this.needsRecoding = needsRecoding;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getHumanAddress() {
        return humanAddress;
    }

    public void setHumanAddress(String humanAddress) {
        this.humanAddress = humanAddress;
    }
}
