package catering.businesslogic.summarySheet;

import catering.businesslogic.CatERing;
import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.event.*;
import catering.businesslogic.user.*;
import catering.businesslogic.menu.*;
import catering.businesslogic.recipe.*;
import catering.businesslogic.calendar.Workshift;

import java.util.ArrayList;
import java.util.Iterator;

public class SheetManager {
    private SummarySheet currentSheet;
    private ArrayList<SheetEventReceiver> eventReceivers;
    public ArrayList<SummarySheet> getSummarySheets() {
        return SummarySheet.loadAllSheets();
    }

    public SheetManager() {
        eventReceivers = new ArrayList<>();
    }

    public SummarySheet generateSummarySheet(ServiceInfo service, EventInfo event) throws UseCaseLogicException {
        catering.businesslogic.user.User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if (!user.isChef()) {
            throw new UseCaseLogicException();
        }

        if (event != null && event.isAssignedToChef(user) && event.getMenuInUse() != null && event.hasService(service)) {
            SummarySheet ss = new SummarySheet(service);
            ArrayList<MenuItem> listItems = event.getMenuInUse().getFreeItems();
            for (MenuItem fi : listItems) {
                Task t = new Task(fi.getItemRecipe());
                ss.getListTasks().add(t);

            }
            ArrayList<Section> listSections = event.getMenuInUse().getSections();
            for (Section sec : listSections) {
                for (MenuItem i : sec.getItems()) {
                    Task t = new Task(i.getItemRecipe());
                    ss.getListTasks().add(t);
                }
            }
            this.setCurrentSheet(ss);
            this.notifySheetCreated(ss);
            return ss;
        }
        else {
            throw new UseCaseLogicException();
        }
    }

    public SummarySheet openSummarySheet(SummarySheet ss)  throws UseCaseLogicException {
        catering.businesslogic.user.User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if (!user.isChef()) {
            throw new UseCaseLogicException();
        }
        if (ss != null) {
            return ss;
        }
        else {
            throw new UseCaseLogicException();
        }
    }

    public SummarySheet resetSummarySheet(SummarySheet ss, ServiceInfo service) throws UseCaseLogicException {
        catering.businesslogic.user.User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if (!user.isChef()) {
            throw new UseCaseLogicException();
        }

        if (ss.getService() == service) {
            Iterator<Task> iterator = ss.getListTasks().iterator();
            while (iterator.hasNext()) {
                Task t = iterator.next();
                iterator.remove();
            }
            ss.setService(null);
            this.setCurrentSheet(ss);
            this.notifySheetResetted(ss);
            return ss;
        } else {
            throw new UseCaseLogicException();
        }
    }


    public Task defineTask(Recipe r) throws UseCaseLogicException {
        if (currentSheet == null) {
            throw new UseCaseLogicException();
        }
        Task t = currentSheet.addTask(r);
        notifyTaskAdded(t);
        return t;
    }

    public void deleteTask(Task t) throws UseCaseLogicException {
        if (currentSheet == null) {
            throw new UseCaseLogicException();
        }
        currentSheet.removeTask(t);
        notifyTaskDeleted(t);
    }

    public Task assignTask(Task t, Workshift ws, User c, int time, int doses) throws UseCaseLogicException {
        if (currentSheet != null && currentSheet.hasTask(t)) {
            currentSheet.addAssignmentTask(t, c, ws, time, doses);
            notifyAssignmentAdded(t);
            return t;
        }
        else {
            throw new UseCaseLogicException();
        }
    }

    public Task deleteAssignmentTask(Task t) throws UseCaseLogicException {
        if (currentSheet != null && currentSheet.hasTask(t)) {
            currentSheet.removeAssignmentTask(t);
            notifyAssignmentRemoved(t);
            return t;
        }
        else {
            throw new UseCaseLogicException();
        }
    }

    public Task setDosesToReady(Task t) throws UseCaseLogicException {
        if (currentSheet != null && currentSheet.hasTask(t)) {
            currentSheet.setDosesToReady(t);
            notifyAssignmentModified(t);
            return t;
        }
        else {
            throw new UseCaseLogicException();
        }
    }

    private void notifyTaskAdded(Task t) {
        for (SheetEventReceiver er : this.eventReceivers) {
            er.updateTaskAdded(currentSheet, t);
        }
    }

    private void notifyTaskDeleted(Task t) {
        for (SheetEventReceiver er : this.eventReceivers) {
            er.updateTaskRemoved(currentSheet, t);
        }
    }


    private void notifyAssignmentRemoved(Task t) {
        for (SheetEventReceiver er : this.eventReceivers) {
            er.updateAssignementRemoved(currentSheet, t);
        }
    }

    private void notifyAssignmentAdded(Task t) {
        for (SheetEventReceiver er : this.eventReceivers) {
            er.updateAssignementAdded(currentSheet, t);
        }
    }

    private void notifyAssignmentModified(Task t) {
        for (SheetEventReceiver er : this.eventReceivers) {
            er.updateAssignementModified(currentSheet, t);
        }
    }

    private void notifySheetResetted(SummarySheet ss) {
        for (SheetEventReceiver er : this.eventReceivers) {
            er.updateSheetResetted(ss);
        }
    }

    private void notifySheetCreated(SummarySheet ss) {
        for (SheetEventReceiver er : this.eventReceivers) {
            er.updateSheetCreated(ss);
        }
    }

    public void setCurrentSheet(SummarySheet ss) {
        this.currentSheet = ss;
    }

    public SummarySheet getCurrentSheet() {
        return this.currentSheet;
    }

    public ArrayList<SummarySheet> getAllSheets() {
        return SummarySheet.loadAllSheets();
    }

    public void addEventReceiver(SheetEventReceiver rec) {
        this.eventReceivers.add(rec);
    }

    public void removeEventReceiver(SheetEventReceiver rec) {
        this.eventReceivers.remove(rec);
    }

}
