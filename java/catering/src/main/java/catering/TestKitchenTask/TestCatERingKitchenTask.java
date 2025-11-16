package catering.TestKitchenTask;

import catering.businesslogic.CatERing;
import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.calendar.CalendarManager;
import catering.businesslogic.calendar.Workshift;
import catering.businesslogic.event.EventInfo;
import catering.businesslogic.event.ServiceInfo;
import catering.businesslogic.recipe.Recipe;
import catering.businesslogic.summarySheet.SheetManager;
import catering.businesslogic.summarySheet.SummarySheet;
import catering.businesslogic.summarySheet.Task;
import catering.businesslogic.user.User;

public class TestCatERingKitchenTask {
    public static void main(String[] args) {
        try {
            /* System.out.println("TEST DATABASE CONNECTION");
            PersistenceManager.testSQLConnection();*/
            CatERing.getInstance().getMenuManager().getAllMenus();
            System.out.println("TEST FAKE LOGIN");
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            User currentUser = CatERing.getInstance().getUserManager().getCurrentUser();
            System.out.println(currentUser);

            System.out.println("\nTEST GENERATE SUMMARY SHEET");
            SheetManager sheetManager = CatERing.getInstance().getSheetManager();
            EventInfo event = CatERing.getInstance().getEventManager().getEventInfo().get(0);
            ServiceInfo service = event.getServices().get(0);
            SummarySheet summarySheet = sheetManager.generateSummarySheet(service, event);
            System.out.println(summarySheet);

            System.out.println("");

            System.out.println("\nTEST ADD TASK TO SUMMARY SHEET");
            Recipe recipe = CatERing.getInstance().getRecipeManager().getRecipes().get(0);
            Task newTask = sheetManager.defineTask(recipe);
            System.out.println(newTask);

            System.out.println("");

            System.out.println("TEST FAKE LOGIN");
            CatERing.getInstance().getUserManager().fakeLogin("Marinella");
            currentUser = CatERing.getInstance().getUserManager().getCurrentUser();
            System.out.println(currentUser);

            System.out.println("\nTEST ASSIGN TASK TO WORKSHIFT");
            CalendarManager calendarManager = CatERing.getInstance().getCalendarManager();
            Workshift workshift = calendarManager.getWorkshiftInfo().get(0);
            sheetManager.setCurrentSheet(sheetManager.getSummarySheets().get(2));
            Task task = sheetManager.getCurrentSheet().getListTasks().get(6);
            sheetManager.assignTask(task, workshift, currentUser, 60, 10);
            System.out.println("Task assigned: " + task);

            System.out.println("");

        } catch (UseCaseLogicException e) {
            System.out.println("Errore di logica nello use case");
        }
    }
}
