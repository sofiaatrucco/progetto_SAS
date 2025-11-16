package catering.businesslogic.summarySheet;

import java.util.*;

import catering.businesslogic.recipe.*;
import catering.businesslogic.calendar.*;
import catering.businesslogic.user.*;
import catering.businesslogic.event.*;
import catering.businesslogic.menu.*;
import catering.persistence.BatchUpdateHandler;
import catering.persistence.PersistenceManager;
import catering.persistence.ResultHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Task {
    private int id;
    private int time;
    private int doses;
    private User user;
    private Workshift workshift;
    private Recipe recipe;

    public Task(Recipe r) {
        this.id = 0;
        this.recipe = r;
    }

    public static void AssignmentModified(int sheetid, Task t) {
        String tUpdate = "UPDATE catering.SheetTasks SET " +
                "doses = " + t.getDoses() +
                " WHERE sheet_id = " + sheetid + " AND id = " + t.getId() + ";";

        PersistenceManager.executeUpdate(tUpdate);
        t.id = PersistenceManager.getLastId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public int getDoses() {
        return doses;
    }

    public User getCook() {
        return user;
    }

    public Workshift getWorkshift() {
        return workshift;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setDoses(int doses) {
        this.doses = doses;
    }

    public void setCook(User user) {
        this.user = user;
    }

    public void setWorkshift(Workshift workshift) {
        this.workshift = workshift;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", time=" + time +
                ", doses=" + doses +
                ", user=" + user +
                ", workshift=" + workshift +
                ", recipe=" + recipe +
                '}';
    }

    public static void saveNewTask(int sheetid, Task t) {
        String tInsert = "INSERT INTO catering.sheettasks (sheet_id, recipe_id) VALUES (?,?);";
        PersistenceManager.executeBatchUpdate(tInsert, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, sheetid);
                ps.setInt(2, t.getRecipe().getId());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // should be only one
                if(count == 0) {
                    t.id=rs.getInt(1);
                }
            }
        });
    }

    public static void saveAllNewTasks(int sheetid, List<Task> tasks) {
        String tInsert = "INSERT INTO catering.SheetTasks (sheet_id, recipe_id) VALUES (?, ?);";
        PersistenceManager.executeBatchUpdate(tInsert, tasks.size(), new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, sheetid);
                ps.setInt(2, tasks.get(batchCount).getRecipe().getId());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // Non è necessario gestire gli ID generati manualmente
            }
        });
    }


    public static void deleteTask(int sheetid, Task t) {
        String tdel = "DELETE FROM SheetTasks WHERE id = " + t.id;
        PersistenceManager.executeUpdate(tdel);
    }

    public static ArrayList<Task> loadTasksFor(int sheet_id) {
        ArrayList<Task> result = new ArrayList<>();
        String query = "SELECT * FROM sheettasks WHERE sheet_id = " + sheet_id;
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                Task t = new Task(Recipe.loadRecipeById(rs.getInt("recipe_id")));
                t.id = rs.getInt("id");
                result.add(t);
            }
        });
        return result;
    }

    public static void AssignmentAdded(int sheetid, Task t) {
        String tUpdate = "UPDATE catering.SheetTasks SET " +
                "time = " + t.getTime() + ", " +
                "doses = " + t.getDoses() + ", " +
                "workshift_id = " + t.getWorkshift().getId() + ", " +
                "cook_id = " + t.getCook().getId() +
                " WHERE sheet_id = " + sheetid + " AND id = " + t.getId() + ";";

        PersistenceManager.executeUpdate(tUpdate);
        t.id = PersistenceManager.getLastId();
    }


    public static void AssignmentRemoved(int sheetid, Task t) {
        String tUpdate = "UPDATE catering.SheetTasks SET " +
                "time = " + 0 + ", " +
                "doses = " + 0 + ", " +
                "workshift_id = NULL, " +
                "cook_id = NULL " +
                "WHERE sheet_id = " + sheetid + " AND id = " + t.getId() + ";";

        PersistenceManager.executeUpdate(tUpdate);
        t.id = PersistenceManager.getLastId();
    }

}
