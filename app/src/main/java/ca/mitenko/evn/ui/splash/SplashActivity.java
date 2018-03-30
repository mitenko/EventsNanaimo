package ca.mitenko.evn.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.mitenko.evn.R;
import ca.mitenko.evn.ui.hub.HubActivity;

public class SplashActivity extends AppCompatActivity
    implements View.OnClickListener {
    /**
     * Hub Fragment container
     */
    @BindView(R.id.splash_btn)
    TextView enterButton;

    /**
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        enterButton.setOnClickListener(this);
    }

    /**
     * {@inheritDoc}
     * @param view
     */
    @Override
    public void onClick(View view) {
        enterButton.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(this, HubActivity.class);
        startActivity(intent);
    }
}
