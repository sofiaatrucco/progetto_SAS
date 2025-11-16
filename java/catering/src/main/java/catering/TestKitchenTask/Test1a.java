package catering.TestKitchenTask;

import catering.businesslogic.CatERing;
import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.summarySheet.SheetManager;
import catering.businesslogic.summarySheet.SummarySheet;
import catering.businesslogic.user.User;

public class Test1a {
    public static void main(String[] args) {
        try {
            System.out.println("TEST FAKE LOGIN");
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            User currentUser = CatERing.getInstance().getUserManager().getCurrentUser();
            System.out.println(currentUser);

            System.out.println("\nTEST OPEN SUMMARY SHEET");
            SheetManager sheetManager = CatERing.getInstance().getSheetManager();
            SummarySheet summarySheet = sheetManager.openSummarySheet(sheetManager.getSummarySheets().get(2));
            System.out.println(summarySheet);
        } catch (UseCaseLogicException e) {
            System.out.println("Errore di logica nello use case: " + e.getMessage());
        }
    }
}
