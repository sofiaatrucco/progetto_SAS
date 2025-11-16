package catering.businesslogic;

import catering.businesslogic.calendar.CalendarManager;
import catering.businesslogic.event.EventManager;
import catering.businesslogic.menu.MenuManager;
import catering.businesslogic.recipe.RecipeManager;
import catering.businesslogic.summarySheet.SheetManager;
import catering.businesslogic.user.UserManager;
import catering.persistence.MenuPersistence;
import catering.persistence.SheetPersistence;

public class CatERing {
    private static CatERing singleInstance;

    public static CatERing getInstance() {
        if (singleInstance == null) {
            singleInstance = new CatERing();
        }
        return singleInstance;
    }

    private MenuManager menuMgr;
    private RecipeManager recipeMgr;
    private UserManager userMgr;
    private EventManager eventMgr;
    private SheetManager sheetMgr;
    private CalendarManager calendarMgr;

    private MenuPersistence menuPersistence;
    private SheetPersistence sheetPersistence;

    private CatERing() {
        menuMgr = new MenuManager();
        recipeMgr = new RecipeManager();
        userMgr = new UserManager();
        eventMgr = new EventManager();
        sheetMgr = new SheetManager();
        calendarMgr = new CalendarManager();
        menuPersistence = new MenuPersistence();
        menuMgr.addEventReceiver(menuPersistence);
        sheetPersistence = new SheetPersistence();
        sheetMgr.addEventReceiver(sheetPersistence);
    }

    public MenuManager getMenuManager() {
        return menuMgr;
    }

    public RecipeManager getRecipeManager() {
        return recipeMgr;
    }

    public UserManager getUserManager() {
        return userMgr;
    }

    public EventManager getEventManager() { return eventMgr; }

    public SheetManager getSheetManager() { return sheetMgr; }

    public CalendarManager getCalendarManager() {
        return calendarMgr;
    }
}
