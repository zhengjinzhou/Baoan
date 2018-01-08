package com.zhou.baoan.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.zhou.baoan.widget.LoadDialog;
import butterknife.ButterKnife;

/**
 * Created by zhou on 2017/12/11.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public abstract int getLayout();
    public abstract void init();
    protected LoadDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
        dialog = new LoadDialog(this,false,"加载中...");
        init();
    }

    public void startToActivity(Class<?> zall){
        Intent intent = new Intent(this, zall);
        startActivity(intent);
    }
}
