package catering.businesslogic.summarySheet;

import catering.businesslogic.recipe.*;
import catering.businesslogic.calendar.*;
import catering.businesslogic.menu.*;
import catering.businesslogic.user.*;
import catering.businesslogic.event.*;
import catering.persistence.BatchUpdateHandler;
import catering.persistence.PersistenceManager;
import catering.persistence.ResultHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SummarySheet {
    private static Map<Integer, SummarySheet> loadedSheets = new HashMap<Integer, SummarySheet>();
    private int id;
    private ArrayList<Task> listTasks;
    private ServiceInfo service;

    public SummarySheet() {
    }

    public SummarySheet(ServiceInfo service) {
        this.id = 0;
        this.service = service;
        this.listTasks = new ArrayList<Task>();
    }

    public static void saveNewSheet(SummarySheet ss) {
        String sheetInsert = "INSERT INTO catering.Sheets (service_id) VALUES (?);";
        int[] result = PersistenceManager.executeBatchUpdate(sheetInsert, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, ss.getService().getId());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // should be only one
                if (count == 0) {
                    ss.id = rs.getInt(1);
                }
            }
        });

        if (result[0] > 0) { // sheet effettivamente inserito
            // salva le tasks
            if (ss.listTasks.size() > 0) {
                Task.saveAllNewTasks(ss.id, ss.listTasks);
            }

            loadedSheets.put(ss.id, ss);
        }
    }

    public static void deleteSheet(SummarySheet ss) {
        // delete tasks
        String delTask = "DELETE FROM SheetTasks WHERE sheet_id = " + ss.id;
        PersistenceManager.executeUpdate(delTask);

        String del = "DELETE FROM Sheets WHERE id = " + ss.getId();
        PersistenceManager.executeUpdate(del);
        loadedSheets.remove(ss.getId());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Task> getListTasks() {
        return listTasks;
    }

    public void setListTasks(ArrayList<Task> listTasks) {
        this.listTasks = listTasks;
    }

    public ServiceInfo getService() {
        return service;
    }

    public void setService(ServiceInfo service) {
        this.service = service;
    }

    @Override
    public String toString() {
        return "SummarySheet{" +
                "id=" + id +
                ", listTasks=" + listTasks +
                ", service=" + service +
                '}';
    }

    public Task addTask(Recipe r) {
        Task t = new Task(r);
        listTasks.add(t);
        return t;
    }

    public void removeTask(Task t) {
        listTasks.remove(t);
    }

    public boolean hasTask(Task t) {
        return listTasks.contains(t);
    }

    public void addAssignmentTask(Task t, User c, Workshift ws, int time, int doses) {
        if (hasTask(t)) {
            if (c != null && c.isAvailable(ws) && !ws.isFull()) {
                t.setCook(c);
                t.setWorkshift(ws);
            }
            else if (c == null) {
                t.setWorkshift(ws);
            }
            else if ((c == null && t.getCook() == null) || (c == null && t.getCook() != null && t.getCook().isAvailable(ws) && !ws.isFull())) {
                t.setWorkshift(ws);
            }
            if (time != 0) t.setTime(time);
            if (doses != 0) t.setDoses(doses);
        }
        else {
            System.out.println("Il foglio di riferimento non contiene questo task.");
        }
    }

    public void removeAssignmentTask(Task t) {
        if (hasTask(t)) {
            if (t.getCook() != null) t.setCook(null);
            if (t.getWorkshift() != null) t.setWorkshift(null);
            if (t.getTime() != 0) t.setTime(0);
            if (t.getDoses() != 0) t.setDoses(0);
        }
        else {
            System.out.println("Il foglio di riferimento non contiene questo task.");
        }
    }

    public void setDosesToReady(Task t) {
        if (hasTask(t)) {
            t.setDoses(0);
        }
        else {
            System.out.println("Il foglio di riferimento non contiene questo task.");
        }
    }

    public static ArrayList<SummarySheet> loadAllSheets() {
        String query = "SELECT * FROM sheets WHERE " + true;
        ArrayList<SummarySheet> newSheets = new ArrayList<>();
        ArrayList<Integer> newSids = new ArrayList<>();
        ArrayList<SummarySheet> oldSheets = new ArrayList<>();
        ArrayList<Integer> oldSids = new ArrayList<>();
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int id = rs.getInt("id");
                if (loadedSheets.containsKey(id)) {
                    SummarySheet ss = loadedSheets.get(id);
                    ss.getService().setId(rs.getInt("service_id"));
                    oldSids.add(rs.getInt("id"));
                    oldSheets.add(ss);
                } else {
                    SummarySheet ss = new SummarySheet();
                    ss.id = id;
                    String name_service = ServiceInfo.loadServiceName(rs.getInt("service_id"));
                    ServiceInfo ser = new ServiceInfo(name_service);
                    ss.setService(ser);
                    ss.getService().setId(rs.getInt("service_id"));
                    newSids.add(rs.getInt("id"));
                    newSheets.add(ss);
                }
            }
        });

        for (int i = 0; i < newSheets.size(); i++) {
            SummarySheet ss = newSheets.get(i);
            // load tasks
            ss.listTasks = Task.loadTasksFor(ss.id);
        }

        for (int i = 0; i < oldSheets.size(); i++) {
            SummarySheet ss = oldSheets.get(i);
            // load tasks
            ss.updateTasks(Task.loadTasksFor(ss.id));
        }
        for (SummarySheet ss: newSheets) {
            loadedSheets.put(ss.id, ss);
        }
        return new ArrayList<SummarySheet>(loadedSheets.values());
    }

    private void updateTasks(ArrayList<Task> newTasks) {
        ArrayList<Task> updatedList = new ArrayList<>();
        for (int i = 0; i < newTasks.size(); i++) {
            Task t = newTasks.get(i);
            Task prev = this.findTaskById(t.getId());
            if (prev == null) {
                updatedList.add(t);
            } else {
                prev.setRecipe(t.getRecipe());
                updatedList.add(prev);
            }
        }
        this.listTasks.clear();
        this.listTasks.addAll(updatedList);
    }

    private Task findTaskById(int id) {
        for (Task t : listTasks) {
            if (t.getId() == id) return t;
        }
        return null;
    }
}
