package catering.businesslogic.summarySheet;

public interface SheetEventReceiver {
    public void updateSheetCreated(SummarySheet ss);

    public void updateSheetResetted(SummarySheet ss);

    public void updateTaskAdded(SummarySheet ss, Task t);

    public void updateTaskRemoved(SummarySheet ss, Task t);

    public void updateAssignementAdded(SummarySheet ss, Task t);

    public void updateAssignementRemoved(SummarySheet ss, Task t);

    public void updateAssignementModified(SummarySheet ss, Task t);
}
