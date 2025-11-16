package catering.businesslogic.calendar;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import catering.businesslogic.user.User;
import catering.persistence.PersistenceManager;
import catering.persistence.ResultHandler;

import java.util.ArrayList;

public class Workshift {
    private int id;
    private Date date;
    private String place;
    private float starting_time;
    private int duration;
    private int bonus_time;
    private int capacity;
    private ArrayList<User> listUsersInWs = new ArrayList<>();

    public Workshift(Date date, String place, float starting_time, int duration, int bonus_time, int capacity) {
        this.id = 0;
        this.date = date;
        this.place = place;
        this.starting_time = starting_time;
        this.duration = duration;
        this.bonus_time = bonus_time;
        this.capacity = capacity;
    }

    public static ArrayList<Workshift> loadAllWorkshiftInfo() {
        ArrayList<Workshift> all = new ArrayList<>();
        String query = "SELECT * FROM workshifts WHERE " + true;
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                Date date = rs.getDate("date");
                String place = rs.getString("place");
                int starting_time = rs.getInt("starting_time");
                int duration = rs.getInt("duration");
                int bonus_time = rs.getInt("bonus_time");
                int capacity = rs.getInt("capacity");
                Workshift ws = new Workshift(date, place, starting_time, duration, bonus_time, capacity);
                ws.id = rs.getInt("id");

                all.add(ws);
            }
        });
        return all;
    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public float getStarting_time() {
        return starting_time;
    }

    public int getDuration() {
        return duration;
    }

    public int getBonus_time() {
        return bonus_time;
    }

    public int getCapacity() {
        return capacity;
    }

    public ArrayList<User> getListUsersInWs() {
        return listUsersInWs;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setStarting_time(float starting_time) {
        this.starting_time = starting_time;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setBonus_time(int bonus_time) {
        this.bonus_time = bonus_time;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setListUsersInWs(ArrayList<User> listUsersInWs) {
        this.listUsersInWs = listUsersInWs;
    }

    @Override
    public String toString() {
        return "Workshift{" +
                "id=" + id +
                ", date=" + date +
                ", place='" + place + '\'' +
                ", starting_time=" + starting_time +
                ", duration=" + duration +
                ", bonus_time=" + bonus_time +
                ", capacity=" + capacity +
                ", listUsersInWs=" + listUsersInWs +
                '}';
    }

    public boolean isFull() {
        //return capacity == listUsersInWs.size();
        return false;
    }


}
