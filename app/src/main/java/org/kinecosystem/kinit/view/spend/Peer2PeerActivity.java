package org.kinecosystem.kinit.view.spend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.view.BaseActivity;

public class Peer2PeerActivity extends BaseActivity {

    public static Intent getIntent(Context context) {
        return new Intent(context, Peer2PeerActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_layout);
        getSupportFragmentManager().beginTransaction()
            .add(R.id.fragment_container, Peer2PeerSendFragment.newInstance()).commit();
    }

}
