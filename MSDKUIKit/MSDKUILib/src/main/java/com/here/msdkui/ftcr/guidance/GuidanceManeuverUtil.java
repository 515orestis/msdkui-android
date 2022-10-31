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

package com.here.msdkui.ftcr.guidance;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.here.android.mpa.ftcr.FTCRManeuver;
import com.here.android.mpa.ftcr.FTCRRoute;
import com.here.android.mpa.routing.Maneuver;
import com.here.msdkui.R;
import com.here.msdkui.ftcr.FTCRUtil;
import com.here.msdkui.ftcr.guidance.base.BaseGuidancePresenter;

import java.util.List;

/**
 * A convenience class to get {@link Maneuver} related information for guidance.
 */
public final class GuidanceManeuverUtil {

    private static final int NEXT_NEXT_MANEUVER_THRESHOLD = 750;

    private GuidanceManeuverUtil() {
    }

    /**
     * Gets the index of a maneuver with a list of maneuvers. Maneuvers are compared based on location and the action.
     * @param maneuver the maneuver to find.
     * @param maneuvers the list of maneuvers.
     * @return the index of the given maneuver in the given maneuver list or -1 if the maneuver is not part of the list.
     */
    public static int getIndexOfManeuver(FTCRManeuver maneuver, List<FTCRManeuver> maneuvers) {
        for (int x = 0; x < maneuvers.size(); x++) {
            final FTCRManeuver tmpManeuver = maneuvers.get(x);
            if (maneuversEqual(maneuver, tmpManeuver)) {
                return x;
            }
        }
        return -1;
    }

    /**
     * Checks if two {@link Maneuver}s are equals.
     * @param maneuverA - a maneuver.
     * @param maneuverB - a maneuver.
     * @return true if maneuvers are equal, else false.
     */
    public static boolean maneuversEqual(FTCRManeuver maneuverA, FTCRManeuver maneuverB) {
        return maneuverA == null ?
                maneuverB == null :
                maneuverB != null &&
                        maneuverA.getPosition().equals(maneuverB.getPosition()) &&
                        maneuverA.getAction() == maneuverB.getAction();
    }

    /**
     * Gets street name of the current {@link Maneuver}.
     * @param context - a context.
     * @param maneuver - the maneuver to use.
     * @return a string containing the street of current maneuver.
     */
    public static String getCurrentManeuverStreet(@NonNull Context context,
                                                  @NonNull FTCRManeuver maneuver) {
        return combineRoadNumberAndName(context, maneuver, "",
                !maneuver.getRoadNames().isEmpty() ? maneuver.getNextRoadNames().get(0) : "");
    }

    /**
     * Gets next {@link Maneuver} street as string representation.
     * @param context - a context.
     * @param maneuver - the maneuver to use.
     * @param baseGuidancePresenter - the presenter to use.
     * @return a string containing the street of next maneuver or null if maneuver is null.
     */
    public static String determineNextManeuverStreet(Context context, FTCRManeuver maneuver,
            BaseGuidancePresenter baseGuidancePresenter) {
        String nextManeuverStreetValue = null;
        if (maneuver != null) {
            nextManeuverStreetValue = getNextStreet(context, maneuver);
            if (TextUtils.isEmpty(nextManeuverStreetValue)) {
                nextManeuverStreetValue = getNextToNext(context, maneuver, baseGuidancePresenter);
            }
            if (TextUtils.isEmpty(nextManeuverStreetValue)) {
                final String roadName = !maneuver.getRoadNames().isEmpty() ?
                maneuver.getRoadNames().get(0) : "";
                nextManeuverStreetValue = combineRoadNumberAndName(context, maneuver, "",
                        roadName);
            }
        }

        return nextManeuverStreetValue;
    }

    private static String getNextToNext(Context context, FTCRManeuver maneuver,
            BaseGuidancePresenter baseGuidancePresenter) {
        String nextToNextStreetValue = null;
        int distance = 0;
        FTCRManeuver afterNextManeuver = getNextManeuver(baseGuidancePresenter, maneuver);
        while (distance < NEXT_NEXT_MANEUVER_THRESHOLD &&
                afterNextManeuver != null && nextToNextStreetValue == null) {
            distance += getDistanceFromPreviousManeuver(baseGuidancePresenter.getRoute(),
                    afterNextManeuver);
            nextToNextStreetValue = combineRoadNumberAndName(context, afterNextManeuver,
                    "",
                    !afterNextManeuver.getNextRoadNames().isEmpty() ?
                            afterNextManeuver.getNextRoadNames().get(0) : "");
            afterNextManeuver = getNextManeuver(baseGuidancePresenter, afterNextManeuver);
        }
        return nextToNextStreetValue;
    }

    private static String getNextStreet(Context context, FTCRManeuver maneuver) {
        final String nextRoadNumber = "";
        final String nextRoadName = !maneuver.getNextRoadNames().isEmpty() ?
                maneuver.getNextRoadNames().get(0) : "";
        return combineRoadNumberAndName(context, maneuver, nextRoadNumber, nextRoadName);
    }


    /**
     * Gets the next {@link Maneuver}, if next maneuver is {@code null} it try to get the next one.
     */
    private static FTCRManeuver getNextManeuver(BaseGuidancePresenter baseGuidancePresenter,
                                            FTCRManeuver lastManeuver) {
        return  getFollowingManeuver(baseGuidancePresenter.getRoute(), lastManeuver);
    }

    /**
     * Combines Road name and Road Number with a "/" if needed.
     *
     * @return null if empty string is returned.
     */
    private static String combineRoadNumberAndName(Context context, FTCRManeuver maneuver,
                                                   String roadNumber, String roadName) {

        String tempRoadName = roadName;

        // When leaving highway, prefer to show exit signpost text instead of road name.
        if (FTCRUtil.getAction(maneuver) == Maneuver.Action.LEAVE_HIGHWAY) {
            // TODO no info in FTCR API
            tempRoadName = null;//getStringFromSignpost(null, tempRoadName);
        }

        // In order to prevent empty road string use roadNumber if roadName isn't available.
        final String roadText = TextUtils.isEmpty(tempRoadName) ? roadNumber : tempRoadName;

        // Skip formatting if road name contains road number.
        if (!TextUtils.isEmpty(roadNumber) && tempRoadName.contains(roadNumber)) {
            return roadText;
        }

        // Format string if needed.
        if (!TextUtils.isEmpty(tempRoadName) && !TextUtils.isEmpty(roadNumber)) {
            return context.getString(R.string.msdkui_maneuver_road_name_divider, roadNumber, tempRoadName);
        }

        return roadText;
    }

    public static FTCRManeuver getPreviousManeuver(FTCRRoute route, FTCRManeuver maneuver) {
        FTCRManeuver prevManeuver = null;
        for (FTCRManeuver ftcrManeuver : route.getManeuvers()) {
            if (GuidanceManeuverUtil.maneuversEqual(ftcrManeuver, maneuver)) {
                return prevManeuver;
            } else {
                prevManeuver = ftcrManeuver;
            }
        }
        return null;
    }

    /**
     * Gets the following {@link Maneuver} of given lastManeuver.
     *
     * @param route
     *         {@link Route} to get all maneuvers.
     * @param lastManeuver
     *         input {@link Maneuver} to get its following maneuver.
     */
    private static FTCRManeuver getFollowingManeuver(FTCRRoute route, FTCRManeuver lastManeuver) {
        List<FTCRManeuver> maneuvers = route.getManeuvers();
        for (int i = 0; i < maneuvers.size(); i++) {
            if (i + 1 < maneuvers.size()
                    && GuidanceManeuverUtil.maneuversEqual(maneuvers.get(i), lastManeuver)) {
                return maneuvers.get(i + 1);
            }
        }
        return null;
    }

    public static int getDistanceFromPreviousManeuver(FTCRRoute route, FTCRManeuver currManeuver) {
        return getDistanceFromPreviousManeuver(route.getManeuvers(), currManeuver);
    }

    public static int getDistanceFromPreviousManeuver(List<FTCRManeuver> maneuvers,
                                                      FTCRManeuver currManeuver) {
        FTCRManeuver prevManeuver = null;
        for (FTCRManeuver maneuver : maneuvers) {
            if (maneuversEqual(maneuver, currManeuver)) {
                return prevManeuver != null ? (int) prevManeuver.getLength() : 0;
            }
            prevManeuver = maneuver;
        }
        return 0;
    }
}
