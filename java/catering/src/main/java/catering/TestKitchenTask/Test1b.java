package catering.TestKitchenTask;

import catering.businesslogic.CatERing;
import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.summarySheet.SheetManager;
import catering.businesslogic.summarySheet.SummarySheet;
import catering.businesslogic.user.User;

public class Test1b {
    public static void main(String[] args) {
        try {
            System.out.println("TEST FAKE LOGIN");
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            User currentUser = CatERing.getInstance().getUserManager().getCurrentUser();
            System.out.println(currentUser);

            System.out.println("\nTEST RESET SUMMARY SHEET");
            SheetManager sheetManager = CatERing.getInstance().getSheetManager();
            SummarySheet sheet = sheetManager.getSummarySheets().get(2);
            SummarySheet resetSheet = sheetManager.resetSummarySheet(sheet, sheet.getService());
            System.out.println(resetSheet);
        } catch (UseCaseLogicException e) {
            System.out.println("Errore di logica nello use case: " + e.getMessage());
        }
    }
}