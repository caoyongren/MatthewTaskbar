/* Copyright 2016 Braden Farmer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.masterman.taskbar.activity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.UserManager;
import android.view.View;

import com.masterman.taskbar.R;
import com.masterman.taskbar.receiver.LockDeviceReceiver;
import com.masterman.taskbar.util.FreeformHackHelper;
import com.masterman.taskbar.util.U;

public class DummyActivity extends Activity {

    boolean shouldFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new View(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(shouldFinish)
            finish();
        else {
            shouldFinish = true;

            if(getIntent().hasExtra("uninstall")) {
                UserManager userManager = (UserManager) getSystemService(USER_SERVICE);

                Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + getIntent().getStringExtra("uninstall")));
                intent.putExtra(Intent.EXTRA_USER, userManager.getUserForSerialNumber(getIntent().getLongExtra("user_id", 0)));

                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) { /* Gracefully fail */ }
            } else if(getIntent().hasExtra("device_admin")) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, new ComponentName(this, LockDeviceReceiver.class));
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.device_admin_description));

                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    U.showToast(this, R.string.lock_device_not_supported);

                    finish();
                }
            } else if(getIntent().hasExtra("start_freeform_hack")) {
                SharedPreferences pref = U.getSharedPreferences(this);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                        && pref.getBoolean("freeform_hack", false)
                        && isInMultiWindowMode()
                        && !FreeformHackHelper.getInstance().isFreeformHackActive()) {
                    U.startFreeformHack(this, false, false);
                }

                finish();
            } else finish();
        }
    }
}