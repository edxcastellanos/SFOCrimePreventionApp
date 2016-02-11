package edx.sfc.objects;

import java.util.Date;

public class Crime {
    private Date datetime;
    private String category;
    private String pdDistrict;
    private CrimeLocation location;
    private String address;
    private String descript;
    private String dayOfWeek;
    private String resolution;
    private String incidntNumber; //I'm defining this variable as String because in the future the incidntNumbers could reach the integer limit

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPdDistrict() {
        return pdDistrict;
    }

    public void setPdDistrict(String pdDistrict) {
        this.pdDistrict = pdDistrict;
    }

    public CrimeLocation getLocation() {
        return location;
    }

    public void setLocation(CrimeLocation location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getIncidntNumber() {
        return incidntNumber;
    }

    public void setIncidntNumber(String incidntNumber) {
        this.incidntNumber = incidntNumber;
    }
}
