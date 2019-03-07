package in.ac.siesgst.npl.remotecontrolbot;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    public static final String TIDE_RIPPERS = "Tide Ripperz";
//    public static final String SHELL_SHOCK = "Shell Shock";
    public static final String BATTLE_BOTS = "Battle Bots";
    private SharedPreferenceManager sharedPreferenceManager;
    public static final Event[] events = new Event[]{
            // TODO Change Action Icon
            new Event(TIDE_RIPPERS, R.drawable.tiderippers, R.drawable.kick, R.drawable.robosoccer_joy),
//            new Event(SHELL_SHOCK, R.drawable.shellshock, R.drawable.bullet, R.drawable.shellshock_joy),
            new Event(BATTLE_BOTS, R.drawable.battlebots, R.drawable.bullet, R.drawable.robosoccer_joy)
    };
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.robo_events_list);
        recyclerView.setAdapter(new EventAdapter());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sharedPreferenceManager = new SharedPreferenceManager(this);
        getWindow().setStatusBarColor(Color.BLACK);
    }


    class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.event_card, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.eventName.setText(events[position].getName());
            Glide.with(MainActivity.this)
                    .load(events[position].getBackgroundDrawable())
                    .into(holder.eventBg);
            holder.bindViewHolder();
        }

        @Override
        public int getItemCount() {
            return events.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView eventName;
            ImageView eventBg;

            public ViewHolder(View itemView) {
                super(itemView);
                eventName = itemView.findViewById(R.id.event_title);
                eventBg = itemView.findViewById(R.id.event_bg);
            }

            void bindViewHolder() {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent joyIntent = new Intent(MainActivity.this, JoystickActivity.class);
                        joyIntent.putExtra("event", new Gson().toJson(events[getAdapterPosition()]));
                        startActivity(joyIntent);
                    }
                });
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("ip_set", sharedPreferenceManager.isIpThere());
            i.putExtra("ip", sharedPreferenceManager.getIp());
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

}
