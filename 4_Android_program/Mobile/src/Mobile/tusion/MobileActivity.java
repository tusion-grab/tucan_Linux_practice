package Mobile.tusion;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class MobileActivity extends ListActivity {

    public static final String PREF_CAPTURE_STATE = "captureState";
    public static final String PREF_CAPTURE_FILE = "captureStatePrefs";
    static final int MENU_CAPTURE_ON = 1;
    static final int MENU_CAPTURE_OFF = 2;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SensorManager sensorManager = 
                (SensorManager)getSystemService( SENSOR_SERVICE  );
        ArrayList<SensorItem> items = new ArrayList<SensorItem>();
        List<Sensor> sensors = sensorManager.getSensorList( Sensor.TYPE_ALL );
        for( int i = 0 ; i < sensors.size() ; ++i )
            items.add( new SensorItem( sensors.get( i ) ) );
        sensorAdapter = new ArrayAdapter( this,
                                R.layout.sensor_row,
                                R.id.text1,
                                items );
        setListAdapter( sensorAdapter );
        SharedPreferences appPrefs = getSharedPreferences( 
                                        PREF_CAPTURE_FILE,
                                        MODE_PRIVATE );
        captureState = appPrefs.getBoolean( PREF_CAPTURE_STATE, false );
    }

    protected void onPause() {
        super.onPause();
        SharedPreferences appPrefs = getSharedPreferences( 
                                        PREF_CAPTURE_FILE,
                                        MODE_PRIVATE );
        SharedPreferences.Editor ed = appPrefs.edit();
        ed.putBoolean( PREF_CAPTURE_STATE, captureState );
        ed.commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_CAPTURE_ON, 1, R.string.capture_on );
		menu.add(0, MENU_CAPTURE_OFF, 2, R.string.capture_off );
        return result;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
        switch( id ) {
            case MENU_CAPTURE_ON:
                captureState = true;
                break;

            case MENU_CAPTURE_OFF:
                captureState = false;
                break;
        }
        return true;
    }

    protected void onListItemClick(
            ListView l,
            View v,
            int position,
            long id) {
        Sensor sensor = sensorAdapter.getItem( position ).getSensor();
        String sensorName = sensor.getName();
        Intent i = new Intent();
        i.setClassName( "Mobile.tusion","Mobile.tusion.SensorMonitor" );
        i.putExtra( "sensorname",sensorName );
        startActivity( i );
    }

    private ArrayAdapter<SensorItem> sensorAdapter;
    private boolean captureState = false;

    class SensorItem {
        SensorItem( Sensor sensor ) {
            this.sensor = sensor;
        }

        public String toString() {
            return sensor.getName();
        }

        Sensor getSensor() {
            return sensor;
        }

        private Sensor sensor;
    }
    
    
}