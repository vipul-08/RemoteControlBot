package in.ac.siesgst.npl.remotecontrolbot;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.TextInputEditText;
import android.support.transition.TransitionManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class JoystickActivity extends AppCompatActivity {

    private String curr = "";

    // UI Elements in Joystick Activity
    private JoystickView joystickView;
    private ImageView secondaryAction;
    private TextView eventTitle;
    private ConstraintLayout rootView;
    private TextView editConnection;

    private SharedPreferenceManager sharedPreferenceManager;

    // Swap Controls
    private boolean isJoystickToRight = true;
    private ImageView swapControlButton;

    private int buttonMargin;

    // AlertDialog
    private View dialogView;
    private TextInputEditText ipAddressText;
    private Button submitButton;
    private AlertDialog connectivityDialog;
    private Event event;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);

        event = new Gson().fromJson(getIntent().getStringExtra("event"), new TypeToken<Event>(){}.getType());

        eventTitle = findViewById(R.id.joystick_event_title);
        eventTitle.setText(event.getName());

        rootView = findViewById(R.id.joystick_root_view);
        rootView.setBackgroundResource(event.getBackgroundDrawable());

        joystickView = findViewById(R.id.joystick_view);
        joystickView.setBackgroundResource(event.getJoyDrawable());

        secondaryAction = findViewById(R.id.button_view);

        if (event.getName().equalsIgnoreCase(MainActivity.TIDE_RIPPERS))
            secondaryAction.setVisibility(View.GONE);

        secondaryAction.setImageResource(event.getButtonDrawable());

        secondaryAction.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    new Thread(new UDPSender(12)).start();
                    secondaryAction.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#80000000")));
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    new Thread(new UDPSender(11)).start();
                    secondaryAction.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#80FFFFFF")));
                }
                return true;
            }
        });

        buttonMargin = ((ViewGroup.MarginLayoutParams) secondaryAction.getLayoutParams()).bottomMargin;

        editConnection = findViewById(R.id.edit_connection);

        swapControlButton = findViewById(R.id.controls_swap_button);

        swapControlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapControls();
            }
        });

        sharedPreferenceManager = new SharedPreferenceManager(JoystickActivity.this);

        dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_connectivity, null, false);

        // IP Addr InputView
        ipAddressText = dialogView.findViewById(R.id.ip_address_textview);

        // Submit button
        submitButton = dialogView.findViewById(R.id.submit_button);

        connectivityDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        if (sharedPreferenceManager.isIpThere()) {
            ipAddressText.setText(sharedPreferenceManager.getIp());
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ipAddress = ipAddressText.getText().toString();

                if (ipAddress.isEmpty()) {
                    ipAddressText.setError("Please Enter a Valid IP");
                    return;
                }

                sharedPreferenceManager.createIpSession(ipAddress);

                connectivityDialog.dismiss();
            }
        });

        connectivityDialog.show();

        editConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!connectivityDialog.isShowing()) {
                    connectivityDialog.show();
                }
            }
        });

        if (sharedPreferenceManager.isFirstRun()) {
            sharedPreferenceManager.firstRun();
            sharedPreferenceManager.defaultControls();
            Log.d("First", "Run");
        }

        joystickView.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {

                if (((angle <= 30 && angle >= 0) || (angle >= 331 && angle <= 359)) && strength >= 85 && !curr.equals("rr")) {
                    new Thread(new UDPSender(sharedPreferenceManager.getRight())).start();
                    curr = "rr";
                } else if (angle >= 31 && angle <= 60 && strength >= 85 && !curr.equals("rf")) {
                    // Do nothing
                    curr = "rf";
                } else if (angle >= 61 && angle <= 120 && strength >= 85 && !curr.equals("ff")) {
                    new Thread(new UDPSender(sharedPreferenceManager.getForward())).start();
                    curr = "ff";
                } else if (angle >= 121 && angle <= 150 && strength >= 85 && !curr.equals("lf")) {
                    // Do nothing
                    curr = "lf";
                } else if (angle >= 151 && angle <= 210 && strength >= 85 && !curr.equals("ll")) {
                    new Thread(new UDPSender(sharedPreferenceManager.getLeft())).start();
                    curr = "ll";
                } else if (angle >= 211 && angle <= 240 && strength >= 85 && !curr.equals("lb")) {
                    // Do nothing
                    curr = "lb";
                } else if (angle >= 241 && angle <= 300 && strength >= 85 && !curr.equals("bb")) {
                    new Thread(new UDPSender(sharedPreferenceManager.getBackward())).start();
                    curr = "bb";
                } else if (angle >= 301 && angle <= 330 && strength >= 85 && !curr.equals("rb")) {
                    // Do nothing
                    curr = "rb";
                } else if (strength <= 85 && !curr.equals("reset")) {
                    new Thread(new UDPSender(sharedPreferenceManager.getMotorReset())).start();
                    curr = "reset";
                }
            }
        });
    }

    // Swaps the JoystickView and Secondary Action Button
    private void swapControls() {
        ConstraintSet rootConstraintSet = new ConstraintSet();
        rootConstraintSet.clone(rootView);

        rootConstraintSet.clear(R.id.joystick_view, ConstraintSet.END);
        rootConstraintSet.clear(R.id.joystick_view, ConstraintSet.START);

        rootConstraintSet.clear(R.id.button_view, ConstraintSet.START);
        rootConstraintSet.clear(R.id.button_view, ConstraintSet.END);

        rootConstraintSet.setMargin(R.id.button_view, ConstraintSet.START, buttonMargin);
        rootConstraintSet.setMargin(R.id.button_view, ConstraintSet.END, buttonMargin);

        if (isJoystickToRight) {
            rootConstraintSet.connect(R.id.joystick_view, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            rootConstraintSet.connect(R.id.button_view, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        } else {
            rootConstraintSet.connect(R.id.joystick_view, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            rootConstraintSet.connect(R.id.button_view, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        }

        TransitionManager.beginDelayedTransition(rootView);

        rootConstraintSet.applyTo(rootView);

        isJoystickToRight = !isJoystickToRight;
    }

    @Override
    protected void onStop() {
        sharedPreferenceManager.closeIpSession();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    class UDPSender implements Runnable {

        int message;

        public UDPSender(int message) {
            this.message = message;
        }

        @Override
        public void run() {
            try {
                DatagramSocket udpsocket = new DatagramSocket(1111);
                udpsocket.setReuseAddress(true);
                InetAddress serverAddress = InetAddress.getByName(sharedPreferenceManager.getIp());
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                PrintStream pout = new PrintStream(byteArrayOutputStream);
                pout.print(message);
                byte[] buf = byteArrayOutputStream.toByteArray();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddress, 1111);
                udpsocket.send(packet);
                udpsocket.close();
            } catch (SocketException e) {
                Log.e("UDP", "Socket Error" + e);
            } catch (IOException e) {
                Log.e("UDP Send", "IO Error" + e);
            }
        }
    }

}
