package com.github.tnerevival.core.db;

import com.github.tnerevival.core.DataManager;
import com.github.tnerevival.core.db.sql.SQLResult;

import java.sql.*;
import java.util.*;

public abstract class SQLDatabase implements DatabaseConnector {

  private TreeMap<Integer, SQLResult> results = new TreeMap<>();
  protected Connection connection;
  protected DataManager manager;

  public SQLDatabase(DataManager manager) {
    this.manager = manager;
  }

  public abstract void connect(DataManager manager) throws SQLException;

  public Boolean connected(DataManager manager) {
    return connection != null;
  }

  public Connection connection(DataManager manager)  {
    if(!connected(manager)) {
      try {
        connect(manager);
      } catch(SQLException e) {
        e.printStackTrace();
      }
    }
    return connection;
  }

  public int executeQuery(String query) {
    if(!connected(manager)) {
      try {
        connect(manager);
      } catch(SQLException e) {
        e.printStackTrace();
      }
    }
    try {
      Statement statement = connection(manager).createStatement();
      return addResult(statement, statement.executeQuery(query));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return -1;
  }

  public int executePreparedQuery(String query, Object[] variables) {
    if(!connected(manager)) {
      try {
        connect(manager);
      } catch(SQLException e) {
        e.printStackTrace();
      }
    }
    try {
      PreparedStatement statement = connection(manager).prepareStatement(query);
      for(int i = 0; i < variables.length; i++) {
        statement.setObject((i + 1), variables[i]);
      }
      return addResult(statement, statement.executeQuery());
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return -1;
  }

  public void executeUpdate(String query) {
    if(!connected(manager)) {
      try {
        connect(manager);
      } catch(SQLException e) {
        e.printStackTrace();
      }
    }
    try {
      Statement statement = connection(manager).createStatement();
      statement.executeUpdate(query);
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      close(manager);
    }
  }

  public void executePreparedUpdate(String query, Object[] variables) {
    if(!connected(manager)) {
      try {
        connect(manager);
      } catch(SQLException e) {
        e.printStackTrace();
      }
    }
    try {
      PreparedStatement prepared = connection(manager).prepareStatement(query);

      for(int i = 0; i < variables.length; i++) {
        prepared.setObject((i + 1), variables[i]);
      }
      prepared.executeUpdate();
      prepared.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      close(manager);
    }
  }

  public int addResult(Statement statement, ResultSet resultSet) {
    int key = (results.isEmpty())? 1 : results.lastKey() + 1;
    SQLResult result = new SQLResult(key, statement, resultSet);
    results.put(result.getId(), result);
    return result.getId();
  }

  public ResultSet results(int id) {
    return results.get(id).getResult();
  }

  public void closeResult(int id) {
    results.get(id).close();
    results.remove(id);
    if(results.size() <= 0) close(manager);
  }

  public void close(DataManager manager) {
    close(manager, false);
  }

  public void close(DataManager manager, boolean force) {
    if(connected(manager)) {
      if(!force && results.size() <= 0) {
        results.clear();
        try {
          connection.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
        connection = null;
      } else if(force) {
        try {
          for (SQLResult result : results.values()) {
            result.close();
          }
          results.clear();
          connection.close();
          connection = null;
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static Integer boolToDB(boolean value) {
    return (value)? 1 : 0;
  }

  public static Boolean boolFromDB(int value) {
    return (value == 1);
  }
}