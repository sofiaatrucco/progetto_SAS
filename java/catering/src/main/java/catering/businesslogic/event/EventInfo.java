package catering.businesslogic.event;

import java.util.ArrayList;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import catering.businesslogic.menu.Menu;
import catering.businesslogic.user.User;
import catering.persistence.PersistenceManager;
import catering.persistence.ResultHandler;

public class EventInfo implements EventItemInfo {
    private int id;
    private String name;
    private Date dateStart;
    private Date dateEnd;
    private int participants;
    private User organizer;

    private ArrayList<Integer> menusInUse = new ArrayList<>();

    private ArrayList<ServiceInfo> services;

    public EventInfo(String name) {
        this.name = name;
        id = 0;
    }

    public int getId() {
        return this.id;
    }

    public ArrayList<Integer> getMenusInUse() {
        return this.menusInUse;
    }

    public Menu getMenuInUse() {
        int menu_id = menusInUse.get(0);
        return Menu.loadedMenus.get(menu_id);
    }

    public boolean isAssignedToChef(User user) {
        return organizer == user;
    }

    public boolean hasService(ServiceInfo service) { return services.contains(service);}

    public ArrayList<ServiceInfo> getServices() {
        return this.services;
    }

    public String toString() {
        return name + ": " + dateStart + "-" + dateEnd + ", " + participants + " pp. (" + organizer.getUserName() + ")";
    }

    // STATIC METHODS FOR PERSISTENCE

    public void menusInUse() {
        String query = "SELECT approved_menu_id FROM services WHERE event_id = " + this.getId();
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int approved_menu_id = rs.getInt("approved_menu_id");
                if(approved_menu_id != 0) getMenusInUse().add(approved_menu_id);
            }
        });
    }

    public static ArrayList<EventInfo> loadAllEventInfo() {
        ArrayList<EventInfo> all = new ArrayList<>();
        String query = "SELECT * FROM Events WHERE true";
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                String n = rs.getString("name");
                EventInfo e = new EventInfo(n);
                e.id = rs.getInt("id");
                e.dateStart = rs.getDate("date_start");
                e.dateEnd = rs.getDate("date_end");
                e.participants = rs.getInt("expected_participants");
                int org = rs.getInt("organizer_id");
                e.organizer = User.loadUserById(org);
                all.add(e);
            }
        });

        for (EventInfo e : all) {
            e.services = ServiceInfo.loadServiceInfoForEvent(e.id);
            e.menusInUse();
        }
        return all;
    }
}
