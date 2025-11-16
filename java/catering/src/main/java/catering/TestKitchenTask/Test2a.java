package catering.TestKitchenTask;

import catering.businesslogic.CatERing;
import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.summarySheet.SheetManager;
import catering.businesslogic.summarySheet.Task;

public class Test2a {
    public static void main(String[] args) {
        try {
            System.out.println("TEST FAKE LOGIN");
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());

            System.out.println("\nTEST DELETE TASK FROM SUMMARY SHEET");
            SheetManager sheetManager = CatERing.getInstance().getSheetManager();
            sheetManager.setCurrentSheet(sheetManager.getSummarySheets().get(1));
            Task task = sheetManager.getCurrentSheet().getListTasks().get(1);
            sheetManager.deleteTask(task);
            System.out.println("Task deleted: " + task);
        } catch (UseCaseLogicException e) {
            System.out.println("Errore di logica nello use case: " + e.getMessage());
        }
    }
}
