package catering.persistence;

import catering.businesslogic.menu.Section;
import catering.businesslogic.summarySheet.*;
public class SheetPersistence implements SheetEventReceiver{

    @Override
    public void updateSheetCreated(SummarySheet ss) {
        SummarySheet.saveNewSheet(ss);
    }

    @Override
    public void updateSheetResetted(SummarySheet ss) {
        SummarySheet.deleteSheet(ss);
    }

    @Override
    public void updateTaskAdded(SummarySheet ss, Task t) {
        Task.saveNewTask(ss.getId(), t);
    }

    @Override
    public void updateTaskRemoved(SummarySheet ss, Task t) {
        Task.deleteTask(ss.getId(), t);
    }

    @Override
    public void updateAssignementAdded(SummarySheet ss, Task t) {
        Task.AssignmentAdded(ss.getId(), t);
    }

    @Override
    public void updateAssignementRemoved(SummarySheet ss, Task t) {
        Task.AssignmentRemoved(ss.getId(), t);
    }

    @Override
    public void updateAssignementModified(SummarySheet ss, Task t) {
        Task.AssignmentModified(ss.getId(), t);
    }
}
