package pl.edu.amu.wmi.wmitimetable.http;

import android.util.Log;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import pl.edu.amu.wmi.wmitimetable.task.SchedulesRestTask;

public class NullHostNameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String hostname, SSLSession session) {
        if(!hostname.equals(SchedulesRestTask.REST_URL)) {
            Log.w("REST Client", "Not allowed certificate for " + hostname);
            return false;
        }else {
            Log.i("REST Client", "Approving certificate for " + hostname);
            return true;
        }
    }

}