package catering.businesslogic.calendar;

import java.util.ArrayList;

public class CalendarManager {
    public ArrayList<Workshift> getWorkshiftInfo() {
        return Workshift.loadAllWorkshiftInfo();
    }
}