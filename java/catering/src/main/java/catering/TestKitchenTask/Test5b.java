package catering.TestKitchenTask;

import catering.businesslogic.CatERing;
import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.summarySheet.SheetManager;
import catering.businesslogic.summarySheet.Task;

public class Test5b {
    public static void main(String[] args) {
        try {
            System.out.println("TEST FAKE LOGIN");
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());

            System.out.println("\nTEST SET DOSES TO READY");
            SheetManager sheetManager = CatERing.getInstance().getSheetManager();
            sheetManager.setCurrentSheet(sheetManager.getSummarySheets().get(0));
            Task task = sheetManager.getCurrentSheet().getListTasks().get(0);
            System.out.println(task);
            sheetManager.setDosesToReady(task);
            System.out.println("Doses set to ready for task: " + task);
        } catch (UseCaseLogicException e) {
            System.out.println("Errore di logica nello use case: " + e.getMessage());
        }
    }
}

