import android.app.job.JobParameters;
import android.app.job.JobService;
import android.widget.Toast;

public class alerter_back_service extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Toast.makeText(this, "HOla", Toast.LENGTH_SHORT).show();

        return false;
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
