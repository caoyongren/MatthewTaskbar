/* Copyright 2017 Braden Farmer
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

package com.masterman.taskbar.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.LinearLayout;

public class StartMenuLayout extends LinearLayout {
    private boolean viewHandlesBackButton = false;

    public StartMenuLayout(Context context) {
        super(context);
    }

    public StartMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StartMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void viewHandlesBackButton() {
        viewHandlesBackButton = true;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(viewHandlesBackButton && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            LocalBroadcastManager.getInstance(getContext()).
                    sendBroadcast(new Intent("com.farmerbb.taskbar.HIDE_START_MENU"));
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
