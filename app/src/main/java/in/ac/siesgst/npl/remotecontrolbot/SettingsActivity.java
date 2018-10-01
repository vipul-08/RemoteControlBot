package in.ac.siesgst.npl.remotecontrolbot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button submitBtn;
    EditText rr, fr, ff, fl, ll, bl, bb, br, resetMotor;
    SharedPreferenceManager sharedPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferenceManager = new SharedPreferenceManager(SettingsActivity.this);

        submitBtn = findViewById(R.id.submitChanges);

        rr = findViewById(R.id.hardRight);
        rr.setText(String.valueOf(sharedPreferenceManager.getRight()));
        ll = findViewById(R.id.hardLeft);
        ll.setText(String.valueOf(sharedPreferenceManager.getLeft()));
        ff = findViewById(R.id.forward);
        ff.setText(String.valueOf(sharedPreferenceManager.getForward()));
        bb = findViewById(R.id.backward);
        bb.setText(String.valueOf(sharedPreferenceManager.getBackward()));

        fr = findViewById(R.id.softRightF);
        fr.setText(String.valueOf(sharedPreferenceManager.getSoftRightF()));
        fl = findViewById(R.id.softLeftF);
        fl.setText(String.valueOf(sharedPreferenceManager.getSoftLeftF()));
        br = findViewById(R.id.softRightB);
        br.setText(String.valueOf(sharedPreferenceManager.getSoftRightB()));
        bl = findViewById(R.id.softLeftB);
        bl.setText(String.valueOf(sharedPreferenceManager.getSoftLeftB()));

        resetMotor = findViewById(R.id.resetMotor);
        resetMotor.setText(String.valueOf(sharedPreferenceManager.getMotorReset()));


        if (getIntent().getBooleanExtra("ip_set", false)) {
            Log.d("IP", getIntent().getStringExtra("ip"));
            sharedPreferenceManager.createIpSession(getIntent().getStringExtra("ip"));
        }


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferenceManager.changeAllControls(Integer.parseInt(rr.getText().toString()), Integer.parseInt(fr.getText().toString()), Integer.parseInt(ff.getText().toString()), Integer.parseInt(fl.getText().toString()), Integer.parseInt(ll.getText().toString()), Integer.parseInt(bl.getText().toString()), Integer.parseInt(bb.getText().toString()), Integer.parseInt(br.getText().toString()), Integer.parseInt(resetMotor.getText().toString()));
                Intent i = new Intent(SettingsActivity.this, JoystickActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
            }
        });

        toolbar = findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.homeIcon) {
            Intent i = new Intent(SettingsActivity.this, JoystickActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

}
