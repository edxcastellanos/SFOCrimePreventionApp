package edx.sfc.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import edx.sfc.objects.Crime;
import edx.sfc.objects.CrimeLocation;

public class JSONReaderSFC {
    public static Crime[] readSFCCrimes(String jsonStr) {
        Crime[] crimes = null;
        int index = 0;
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            crimes = new Crime[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                index = i;
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String dateStr = jsonObject.getString("date").split("T")[0];
                String timeStr = jsonObject.getString("time");

                CrimeLocation cl = new CrimeLocation();
                cl.setLatitude(jsonObject.getDouble("y"));
                cl.setLongitude(jsonObject.getDouble("x"));

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, Integer.parseInt(dateStr.split("-")[0]));
                cal.set(Calendar.MONTH, Integer.parseInt(dateStr.split("-")[1]) - 1);
                cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateStr.split("-")[2]));
                cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeStr.split(":")[0]));
                cal.set(Calendar.MINUTE, Integer.parseInt(timeStr.split(":")[1]));
                Date dateFromCalendar= cal.getTime();

                crimes[i] = new Crime();
                crimes[i].setLocation(cl);
                crimes[i].setDatetime(dateFromCalendar);
                crimes[i].setCategory(jsonObject.getString("category"));
                crimes[i].setPdDistrict((jsonObject.isNull("pddistrict")) ? "UNKNOWN" : jsonObject.getString("pddistrict"));
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