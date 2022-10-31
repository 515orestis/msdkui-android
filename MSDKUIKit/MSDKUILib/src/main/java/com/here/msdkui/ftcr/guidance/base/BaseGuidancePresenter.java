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

package com.here.msdkui.ftcr.guidance.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.ftcr.FTCRLaneInformation;
import com.here.android.mpa.ftcr.FTCRManeuver;
import com.here.android.mpa.ftcr.FTCRNavigationManager;
import com.here.android.mpa.ftcr.FTCRRoute;
import com.here.android.mpa.ftcr.FTCRRouter;
import com.here.android.mpa.routing.Maneuver;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteResult;
import com.here.android.mpa.routing.RouteTta;
import com.here.android.mpa.routing.RoutingError;
import com.here.msdkui.ftcr.guidance.GuidanceManeuverUtil;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

/**
 * Base class for guidance presenters.
 * <p>The class interacts with {@link NavigationManager} and {@link PositioningManager} to support
 * easier handling of guidance events.</p>
 */
public class BaseGuidancePresenter {

    private final FTCRNavigationManager mNavigationManager;
    private final PositioningManager mPosManageer;

    private final FTCRNavigationManager.FTCRNavigationManagerListener mNavigationListener =
            new FTCRNavigationManager.FTCRNavigationManagerListener() {
                @Override
                public void onCurrentManeuverChanged(@Nullable FTCRManeuver ftcrManeuver,
                                                     @Nullable FTCRManeuver ftcrManeuver1) {
                    handleManeuverEvent();
                    handleNewInstructionEvent();
                }

                @Override
                public void onStopoverReached(int i) {

                }

                @Override
                public void onDestinationReached() {
                }

                @Override
                public void onRerouteBegin() {
                    handleRerouteBegin();
                }

                @Override
                public void onRerouteEnd(@Nullable FTCRRoute ftcrRoute,
                                         @NonNull FTCRRouter.ErrorResponse errorResponse) {
                    if (errorResponse.getErrorCode() == RoutingError.NONE) {
                        handleRerouteEnd(ftcrRoute);
                    } else {
                        handleRerouteFailed(errorResponse);
                    }
                }

                @Override
                public void onLaneInformation(@NonNull List<FTCRLaneInformation> list) {

                }
            };


    /**
     * Listener for {@link NavigationManager.PositionListener}.
     */
    private final PositioningManager.OnPositionChangedListener mPositionListener =
            new PositioningManager.OnPositionChangedListener() {
                @Override
                public void onPositionUpdated(PositioningManager.LocationMethod locationMethod,
                                              @Nullable GeoPosition geoPosition, boolean b) {
                    handlePositionUpdate();
                }

                @Override
                public void onPositionFixChanged(PositioningManager.LocationMethod locationMethod, PositioningManager.LocationStatus locationStatus) {

                }
            };

    private FTCRRoute mRoute;

    /**
     * Constructs a new instance using a {@link NavigationManager} instance and
     * a route to follow during guidance.
     *
     * @param navigationManager a {@link NavigationManager}.
     * @param route             a route to be used for guidance.
     */
    protected BaseGuidancePresenter(@NonNull FTCRNavigationManager navigationManager,
                                    @NonNull PositioningManager positioningManager,
                                    FTCRRoute route) {
        mNavigationManager = navigationManager;
        mPosManageer = positioningManager;
        mRoute = route;
    }

    /**
     * Resumes presenter to start listening to navigation events.
     */
    public void resume() {
        mNavigationManager.addNavigationListener(mNavigationListener);
        mPosManageer.addListener(new WeakReference<>(mPositionListener));
    }

    /**
     * Pauses presenter to stop listening to navigation events.
     */
    public void pause() {
        mNavigationManager.removeNavigationListener(mNavigationListener);
        mPosManageer.removeListener(mPositionListener);
    }

    /**
     * Called after resuming the presenter. Subclasses may override for custom needs.
     */
    protected void handleManeuverEvent() {
        // Let the sub class override it, if interested.
    }

    /**
     * Notifies on new instruction events generated by {@link NavigationManager}.
     * Subclasses may override the method if they are interested in the events forwarded by
     * {@link NavigationManager.NewInstructionEventListener}.
     */
    protected void handleNewInstructionEvent() {
        // Let the sub class override it, if interested.
    }

    /**
     * Notifies when GPS signal is lost.
     */
    protected void handleGpsLost() {
        // Let the sub class override it, if interested.
    }

    /**
     * Notifies when GPS signal is restored.
     */
    protected void handleGpsRestore() {
        // Let the sub class override it, if interested.
    }

    /**
     * Notifies when rerouting begins.
     */
    protected void handleRerouteBegin() {
        // Let the sub class override it, if interested.
    }

    /**
     * Notifies when rerouting ends with success.
     *
     * @param routeResult {@link RouteResult} object containing {@link Route} as result of route
     *                    calculation.
     */
    protected void handleRerouteEnd(FTCRRoute ftcrRoute) {
        // Let the sub class override it, if interested.
    }

    /**
     * Notifies when rerouting ends with failure.
     *
     * @param error rerouting error.
     */
    protected void handleRerouteFailed(FTCRRouter.ErrorResponse errorResponse) {
        // Let the sub class override it, if interested.
    }

    /*
     * Notifies when position updates.
     * The Position change notification is generated by {@link NavigationManager}.
     */
    protected void handlePositionUpdate() {
        // Let the sub class override it, if interested.
    }

    /**
     * Notifies when driving over speed limit.
     *
     * @param speedLimit current speed limit.
     */
    protected void handleSpeedExceeded(float speedLimit) {
        // Let the sub class override it, if interested.
    }

    /**
     * Notifies when no longer driving over speed limit.
     *
     * @param speedLimit current speed limit.
     */
    protected void handleSpeedExceededEnd(float speedLimit) {
        // Let the sub class override it, if interested.
    }

    /**
     * Gets the next {@link Maneuver Maneuver}.
     *
     * @return next {@link Maneuver Maneuver}.
     */
    public FTCRManeuver getNextManeuver() {
        return mNavigationManager.getCurrentManeuver();
    }

    /**
     * Gets the {@link Maneuver Maneuver} after next {@link Maneuver Maneuver}.
     *
     * @return after-next {@link Maneuver Maneuver}.
     */
    public FTCRManeuver getAfterNextManeuver() {
        return mNavigationManager.getNextManeuver();
    }

    /**
     * Gets the distance to the next {@link Maneuver Maneuver}.
     *
     * @return distance to next {@link Maneuver Maneuver}.
     */
    public long getNextManeuverDistance() {
        return mNavigationManager.getDistanceToCurrentManeuver();
    }

    /**
     * Gets the route that was set to be used for guidance.
     *
     * @return a route.
     */
    public FTCRRoute getRoute() {
        return mRoute;
    }

    /**
     * Sets a new route that should be used for guidance.
     *
     * @param route a route.
     */
    public void setRoute(FTCRRoute route) {
        this.mRoute = route;
    }

    /**
     * Gets estimated arrival date.
     *
     * @return a {@link Date} of arrival at the destination.
     */
    public @NonNull
    Date getEta() {
        return new Date(System.currentTimeMillis()
                + mNavigationManager.getRemainingTime(FTCRRoute.WHOLE_ROUTE) * 1000);
    }

    /**
     * Gets distance to the destination.
     *
     * @return distance in meters.
     */
    public long getDestinationDistance() {
        return mNavigationManager.getRemainingDistance(FTCRRoute.WHOLE_ROUTE);
    }

    /**
     * Gets time to arrive at the destination.
     *
     * @return time to arrive in seconds.
     */
    public Integer getTimeToArrival() {
        return Integer.valueOf((int) mNavigationManager.getRemainingTime(FTCRRoute.WHOLE_ROUTE));
    }




}
