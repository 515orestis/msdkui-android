/*
 * Copyright (C) 2017-2020 HERE Europe B.V.
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

package com.here.msdkui.ftcr.routing;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.RequiresApi;

import com.here.android.mpa.ftcr.FTCRRouteOptions;
import com.here.msdkui.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * A view that shows an options panel to select an available mode of
 * {@link Route.TrafficPenaltyMode}.
 */
public class TrafficOptionsPanel extends OptionsPanel implements OptionItem.OnChangedListener {
    public enum Traffic {
        ON, OFF
    }
    private Map<Traffic, String> mResourceKey;
    private OptionItem mSubOptionItem;
    private Traffic mTraffic;
    private FTCRRouteOptions mRouteOptions;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public TrafficOptionsPanel(final Context context) {
        this(context, null, 0);
    }

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     *
     * @param attrs
     *         a set of attributes.
     */
    public TrafficOptionsPanel(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     *
     * @param attrs
     *         a set of attributes.
     *
     * @param defStyleAttr
     *         a default style attribute.
     */
    public TrafficOptionsPanel(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        populateResources();
        createPanel();
    }

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     *
     * @param attrs
     *         a set of attributes.
     *
     * @param defStyleAttr
     *         a default style attribute.
     *
     * @param defStyleRes
     *         a default style resource.
     *
     * Requires Lollipop (API level 21).
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TrafficOptionsPanel(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        populateResources();
        createPanel();
    }

    private void populateResources() {
        mResourceKey = new LinkedHashMap<>();
        mResourceKey.put(Traffic.OFF, getString(R.string.msdkui_disabled));
        mResourceKey.put(Traffic.ON, getString(R.string.msdkui_optimal));
    }

    private void createPanel() {
        mSubOptionItem = new OptionItemBuilders.SingleChoiceOptionItemBuilder(getContext())
                .setLabels(getString(R.string.msdkui_traffic), new ArrayList<String>(mResourceKey.values()))
                .build();
        mSubOptionItem.setListener(this);
        setOptionItems(Collections.singletonList(mSubOptionItem));
    }

    /**
     * Gets the underlying {@link DynamicPenalty}.
     * @return the {@link DynamicPenalty} or null if nothing was set.
     */
    public Traffic getTraffic() {
        populateDynamicPenalty();
        return mTraffic;
    }

    /**
     * Sets the {@link DynamicPenalty} to populate this panel.
     * @param penalty the {@link DynamicPenalty} to use for this panel.
     */
    public void setDynamicPenalty(final FTCRRouteOptions routeOptions) {
        mRouteOptions = routeOptions;
        select(mRouteOptions.isTrafficUsed() ? Traffic.ON : Traffic.OFF);
    }

    /**
     * Populates the panel using the set {@link DynamicPenalty}. Does nothing if no {@link DynamicPenalty}
     * was set.
     */
    public void populateDynamicPenalty() {
        final String label = ((SingleChoiceOptionItem) mSubOptionItem).getSelectedItemLabel();
        for (final Map.Entry<Traffic, String> entry : mResourceKey.entrySet()) {
            if (entry.getValue().equals(label)) {
                mRouteOptions.setUseTraffic(entry.getKey() == Traffic.ON);
                break;
            }
        }
    }

    private void select(final Traffic traffic) {
        for (final Map.Entry<Traffic, String> entry : mResourceKey.entrySet()) {
            if (traffic == entry.getKey()) {
                ((SingleChoiceOptionItem) mSubOptionItem).selectLabel(entry.getValue());
                break;
            }
        }
    }

    @Override
    public void onChanged(OptionItem item) {
        populateDynamicPenalty();
        notifyOnOptionChanged(item);
    }
}
