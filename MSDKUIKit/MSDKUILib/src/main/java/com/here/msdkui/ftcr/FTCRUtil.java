package com.here.msdkui.ftcr;

import com.here.android.mpa.ftcr.FTCRManeuver;
import com.here.android.mpa.ftcr.FTCRRoute;
import com.here.android.mpa.ftcr.FTCRRouteWarning;
import com.here.android.mpa.routing.Maneuver;
import com.here.msdkui.ftcr.guidance.GuidanceManeuverUtil;

import java.util.List;

public class FTCRUtil {

    // TODO This matching is not ideal, theoretically it can be slightly improved by trying different use cases in practise.
    // TODO Because FTCR route uses complatelly different data source, ACTION, TURN, ICON can not be 100% matched
    // TODO with default route maneuver

    public static Maneuver.Action getAction(FTCRManeuver ftcrManeuver) {
        switch (ftcrManeuver.getAction()) {
            default:
            case UNDEFINED:
                return Maneuver.Action.UNDEFINED;
            case DEPART:
            case DEPART_AIRPORT:
                return Maneuver.Action.UNDEFINED;
            case ARRIVE:
            case ARRIVE_AIRPORT:
            case ARRIVE_LEFT:
            case ARRIVE_RIGHT:
                return Maneuver.Action.END;
            case LEFT_U_TURN:
            case RIGHT_U_TURN:
                return Maneuver.Action.UTURN;

            case LEFT_LOOP:
            case SHARP_LEFT_TURN:
            case LEFT_TURN:
            case SLIGHT_LEFT_TURN:
            case CONTINUE_ON:
            case SLIGHT_RIGHT_TURN:
            case RIGHT_TURN:
            case SHARP_RIGHT_TURN:
            case RIGHT_LOOP:
            case LEFT_EXIT:
            case RIGHT_EXIT:
            case LEFT_RAMP:
            case RIGHT_RAMP:
            case LEFT_FORK:
            case MIDDLE_FORK:
            case RIGHT_FORK:
            case LEFT_MERGE:
            case RIGHT_MERGE:
            case NAME_CHANGE:
            case TRAFFIC_CIRCLE:
            case FERRY:
                switch (ftcrManeuver.getDirection()) {
                    case FORWARD:
                        return Maneuver.Action.HEAD_TO;
                    case BEAR_RIGHT:
                    case LIGHT_RIGHT:
                    case RIGHT:
                    case HARD_RIGHT:
                    case BEAR_LEFT:
                    case LIGHT_LEFT:
                    case LEFT:
                    case HARD_LEFT:
                        return Maneuver.Action.JUNCTION;
                    case U_TURN_RIGHT:
                    case U_TURN_LEFT:
                        return Maneuver.Action.UTURN;
                    default:
                        return Maneuver.Action.UNDEFINED;
                }
            case LEFT_ROUNDABOUT_EXIT_1:
            case LEFT_ROUNDABOUT_EXIT_2:
            case LEFT_ROUNDABOUT_EXIT_3:
            case LEFT_ROUNDABOUT_EXIT_4:
            case LEFT_ROUNDABOUT_EXIT_5:
            case LEFT_ROUNDABOUT_EXIT_6:
            case LEFT_ROUNDABOUT_EXIT_7:
            case LEFT_ROUNDABOUT_EXIT_8:
            case LEFT_ROUNDABOUT_EXIT_9:
            case LEFT_ROUNDABOUT_EXIT_10:
            case LEFT_ROUNDABOUT_EXIT_11:
            case LEFT_ROUNDABOUT_EXIT_12:
            case RIGHT_ROUNDABOUT_EXIT_1:
            case RIGHT_ROUNDABOUT_EXIT_2:
            case RIGHT_ROUNDABOUT_EXIT_3:
            case RIGHT_ROUNDABOUT_EXIT_4:
            case RIGHT_ROUNDABOUT_EXIT_5:
            case RIGHT_ROUNDABOUT_EXIT_6:
            case RIGHT_ROUNDABOUT_EXIT_7:
            case RIGHT_ROUNDABOUT_EXIT_8:
            case RIGHT_ROUNDABOUT_EXIT_9:
            case RIGHT_ROUNDABOUT_EXIT_10:
            case RIGHT_ROUNDABOUT_EXIT_11:
            case RIGHT_ROUNDABOUT_EXIT_12:
                return Maneuver.Action.ROUNDABOUT;
        }
    }

    public static Maneuver.Turn getTurn(FTCRManeuver ftcrManeuver) {
        switch (ftcrManeuver.getAction()) {
            case LEFT_ROUNDABOUT_EXIT_1:
                return Maneuver.Turn.ROUNDABOUT_1;
            case LEFT_ROUNDABOUT_EXIT_2:
                return Maneuver.Turn.ROUNDABOUT_2;
            case LEFT_ROUNDABOUT_EXIT_3:
                return Maneuver.Turn.ROUNDABOUT_3;
            case LEFT_ROUNDABOUT_EXIT_4:
                return Maneuver.Turn.ROUNDABOUT_4;
            case LEFT_ROUNDABOUT_EXIT_5:
                return Maneuver.Turn.ROUNDABOUT_5;
            case LEFT_ROUNDABOUT_EXIT_6:
                return Maneuver.Turn.ROUNDABOUT_6;
            case LEFT_ROUNDABOUT_EXIT_7:
                return Maneuver.Turn.ROUNDABOUT_7;
            case LEFT_ROUNDABOUT_EXIT_8:
                return Maneuver.Turn.ROUNDABOUT_8;
            case LEFT_ROUNDABOUT_EXIT_9:
                return Maneuver.Turn.ROUNDABOUT_9;
            case LEFT_ROUNDABOUT_EXIT_10:
                return Maneuver.Turn.ROUNDABOUT_10;
            case LEFT_ROUNDABOUT_EXIT_11:
                return Maneuver.Turn.ROUNDABOUT_11;
            case LEFT_ROUNDABOUT_EXIT_12:
                return Maneuver.Turn.ROUNDABOUT_12;
            case RIGHT_ROUNDABOUT_EXIT_1:
                return Maneuver.Turn.ROUNDABOUT_1;
            case RIGHT_ROUNDABOUT_EXIT_2:
                return Maneuver.Turn.ROUNDABOUT_2;
            case RIGHT_ROUNDABOUT_EXIT_3:
                return Maneuver.Turn.ROUNDABOUT_3;
            case RIGHT_ROUNDABOUT_EXIT_4:
                return Maneuver.Turn.ROUNDABOUT_4;
            case RIGHT_ROUNDABOUT_EXIT_5:
                return Maneuver.Turn.ROUNDABOUT_5;
            case RIGHT_ROUNDABOUT_EXIT_6:
                return Maneuver.Turn.ROUNDABOUT_6;
            case RIGHT_ROUNDABOUT_EXIT_7:
                return Maneuver.Turn.ROUNDABOUT_7;
            case RIGHT_ROUNDABOUT_EXIT_8:
                return Maneuver.Turn.ROUNDABOUT_8;
            case RIGHT_ROUNDABOUT_EXIT_9:
                return Maneuver.Turn.ROUNDABOUT_9;
            case RIGHT_ROUNDABOUT_EXIT_10:
                return Maneuver.Turn.ROUNDABOUT_10;
            case RIGHT_ROUNDABOUT_EXIT_11:
                return Maneuver.Turn.ROUNDABOUT_11;
            case RIGHT_ROUNDABOUT_EXIT_12:
                return Maneuver.Turn.ROUNDABOUT_12;
            case LEFT_U_TURN:
            case RIGHT_U_TURN:
                return Maneuver.Turn.RETURN;
            case DEPART:
            case DEPART_AIRPORT:
            case ARRIVE:
            case ARRIVE_AIRPORT:
            case ARRIVE_LEFT:
            case ARRIVE_RIGHT:
            case LEFT_LOOP:
            case SHARP_LEFT_TURN:
            case LEFT_TURN:
            case SLIGHT_LEFT_TURN:
            case CONTINUE_ON:
            case SLIGHT_RIGHT_TURN:
            case RIGHT_TURN:
            case SHARP_RIGHT_TURN:
            case RIGHT_LOOP:
            case LEFT_EXIT:
            case RIGHT_EXIT:
            case LEFT_RAMP:
            case RIGHT_RAMP:
            case LEFT_FORK:
            case MIDDLE_FORK:
            case RIGHT_FORK:
            case LEFT_MERGE:
            case RIGHT_MERGE:
            case NAME_CHANGE:
            case TRAFFIC_CIRCLE:
            case FERRY:
                switch (ftcrManeuver.getDirection()) {
                    case BEAR_RIGHT:
                        return Maneuver.Turn.QUITE_RIGHT;
                    case LIGHT_RIGHT:
                        return Maneuver.Turn.LIGHT_RIGHT;
                    case RIGHT:
                        return Maneuver.Turn.QUITE_RIGHT;
                    case HARD_RIGHT:
                        return Maneuver.Turn.HEAVY_RIGHT;
                    case BEAR_LEFT:
                        return Maneuver.Turn.QUITE_LEFT;
                    case LIGHT_LEFT:
                        return Maneuver.Turn.LIGHT_LEFT;
                    case LEFT:
                        return Maneuver.Turn.QUITE_LEFT;
                    case HARD_LEFT:
                        return Maneuver.Turn.HEAVY_LEFT;
                    case U_TURN_RIGHT:
                    case U_TURN_LEFT:
                        return Maneuver.Turn.RETURN;
                    default:
                    case FORWARD:
                        return Maneuver.Turn.UNDEFINED;
                }
            default:
            case UNDEFINED:
                return Maneuver.Turn.UNDEFINED;
        }

    }

    public static Maneuver.Icon getIcon(FTCRManeuver ftcrManeuver) {
        switch (ftcrManeuver.getAction()) {
            case LEFT_ROUNDABOUT_EXIT_1:
                return Maneuver.Icon.ROUNDABOUT_1_LH;
            case LEFT_ROUNDABOUT_EXIT_2:
                return Maneuver.Icon.ROUNDABOUT_2_LH;
            case LEFT_ROUNDABOUT_EXIT_3:
                return Maneuver.Icon.ROUNDABOUT_3_LH;
            case LEFT_ROUNDABOUT_EXIT_4:
                return Maneuver.Icon.ROUNDABOUT_4_LH;
            case LEFT_ROUNDABOUT_EXIT_5:
                return Maneuver.Icon.ROUNDABOUT_5_LH;
            case LEFT_ROUNDABOUT_EXIT_6:
                return Maneuver.Icon.ROUNDABOUT_6_LH;
            case LEFT_ROUNDABOUT_EXIT_7:
                return Maneuver.Icon.ROUNDABOUT_7_LH;
            case LEFT_ROUNDABOUT_EXIT_8:
                return Maneuver.Icon.ROUNDABOUT_8_LH;
            case LEFT_ROUNDABOUT_EXIT_9:
                return Maneuver.Icon.ROUNDABOUT_9_LH;
            case LEFT_ROUNDABOUT_EXIT_10:
                return Maneuver.Icon.ROUNDABOUT_10_LH;
            case LEFT_ROUNDABOUT_EXIT_11:
                return Maneuver.Icon.ROUNDABOUT_11_LH;
            case LEFT_ROUNDABOUT_EXIT_12:
                return Maneuver.Icon.ROUNDABOUT_12_LH;
            case RIGHT_ROUNDABOUT_EXIT_1:
                return Maneuver.Icon.ROUNDABOUT_1;
            case RIGHT_ROUNDABOUT_EXIT_2:
                return Maneuver.Icon.ROUNDABOUT_2;
            case RIGHT_ROUNDABOUT_EXIT_3:
                return Maneuver.Icon.ROUNDABOUT_3;
            case RIGHT_ROUNDABOUT_EXIT_4:
                return Maneuver.Icon.ROUNDABOUT_4;
            case RIGHT_ROUNDABOUT_EXIT_5:
                return Maneuver.Icon.ROUNDABOUT_5;
            case RIGHT_ROUNDABOUT_EXIT_6:
                return Maneuver.Icon.ROUNDABOUT_6;
            case RIGHT_ROUNDABOUT_EXIT_7:
                return Maneuver.Icon.ROUNDABOUT_7;
            case RIGHT_ROUNDABOUT_EXIT_8:
                return Maneuver.Icon.ROUNDABOUT_8;
            case RIGHT_ROUNDABOUT_EXIT_9:
                return Maneuver.Icon.ROUNDABOUT_9;
            case RIGHT_ROUNDABOUT_EXIT_10:
                return Maneuver.Icon.ROUNDABOUT_10;
            case RIGHT_ROUNDABOUT_EXIT_11:
                return Maneuver.Icon.ROUNDABOUT_11;
            case RIGHT_ROUNDABOUT_EXIT_12:
                return Maneuver.Icon.ROUNDABOUT_12;
            case LEFT_U_TURN:
                return Maneuver.Icon.UTURN_LEFT;
            case RIGHT_U_TURN:
                return Maneuver.Icon.UTURN_RIGHT;
            case DEPART:
            case DEPART_AIRPORT:
                return Maneuver.Icon.START;
            case ARRIVE:
            case ARRIVE_AIRPORT:
            case ARRIVE_LEFT:
            case ARRIVE_RIGHT:
                return Maneuver.Icon.END;
            case SHARP_LEFT_TURN:
                return Maneuver.Icon.HEAVY_LEFT;
            case LEFT_TURN:
                return Maneuver.Icon.QUITE_LEFT;
            case SLIGHT_LEFT_TURN:
                return Maneuver.Icon.LIGHT_LEFT;
            case SHARP_RIGHT_TURN:
                return Maneuver.Icon.HEAVY_RIGHT;
            case RIGHT_TURN:
                return Maneuver.Icon.QUITE_RIGHT;
            case SLIGHT_RIGHT_TURN:
                return Maneuver.Icon.LIGHT_RIGHT;
            case FERRY:
                return Maneuver.Icon.FERRY;
            case LEFT_EXIT:
                return Maneuver.Icon.LEAVE_HIGHWAY_LEFT_LANE;
            case RIGHT_EXIT:
                return Maneuver.Icon.LEAVE_HIGHWAY_RIGHT_LANE;
            // TODO
            case CONTINUE_ON:
            case LEFT_RAMP:
            case RIGHT_RAMP:
            case LEFT_FORK:
            case MIDDLE_FORK:
            case RIGHT_FORK:
            case LEFT_MERGE:
            case RIGHT_MERGE:
            case NAME_CHANGE:
            case TRAFFIC_CIRCLE:
            case LEFT_LOOP:
            case RIGHT_LOOP:
            default:
            case UNDEFINED:
                return Maneuver.Icon.UNDEFINED;
        }
    }
}
