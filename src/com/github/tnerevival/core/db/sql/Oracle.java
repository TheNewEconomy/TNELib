package com.github.tnerevival.core.db.sql;

import com.github.tnerevival.core.DataManager;
import com.github.tnerevival.core.db.SQLDatabase;

/**
 * Created by creatorfromhell.
 *
 * The New Chat Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0
 * International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
public class Oracle extends SQLDatabase {
  public Oracle(DataManager manager) {
    super(manager);
  }

  @Override
  public String getDriver() {
    return "oracle.jdbc.driver.OracleDriver";
  }

  @Override
  public Boolean dataSource() {
    return true;
  }

  @Override
  public String dataSourceURL() {
    return "oracle.jdbc.pool.OracleDataSource";
  }

  @Override
  public String getURL(String file, String host, int port, String database) {
    return "jdbc:oracle:thin:@" + host + ":" + port + ":" + database;
  }
}