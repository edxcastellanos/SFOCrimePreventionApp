package edx.sfc.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import edx.sfc.objects.CrimeLocation;
import edx.sfc.objects.Crime;

public class JSONReaderSFC {
    public static Crime[] readSFCCrimes(String jsonStr){
        Crime[] crimes = null;
        int index= 0;
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
             crimes = new Crime[jsonArray.length()];

            for(int i=0; i<jsonArray.length(); i++){
                index=i;
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String dateStr = jsonObject.getString("date").split("T")[0];
                String timeStr = jsonObject.getString("time");

                CrimeLocation cl = new CrimeLocation();
                cl.setLatitude(jsonObject.getDouble("y"));
                cl.setLongitude(jsonObject.getDouble("x"));

                crimes[i] = new Crime();
                crimes[i].setLocation(cl);
                crimes[i].setDatetime(new Date(Integer.parseInt(dateStr.split("-")[0]) - 1900,
                        Integer.parseInt(dateStr.split("-")[1]) - 1,
                        Integer.parseInt(dateStr.split("-")[2]),
                        Integer.parseInt(timeStr.split(":")[0]),
                        Integer.parseInt(timeStr.split(":")[1])));
                crimes[i].setCategory(jsonObject.getString("category"));
                crimes[i].setPdDistrict((jsonObject.isNull("pddistrict"))? "UNKNOWN" :jsonObject.getString("pddistrict"));
                crimes[i].setAddress(jsonObject.getString("address"));
                crimes[i].setDescript(jsonObject.getString("descript"));
                crimes[i].setDayOfWeek(jsonObject.getString("dayofweek"));
                crimes[i].setResolution(jsonObject.getString("resolution"));
                crimes[i].setIncidntNumber(jsonObject.getString("incidntnum"));
            }
        } catch (JSONException e) {
            Log.i("Error at element" + index + 1, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return crimes;
    }
}