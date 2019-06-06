package com.github.tnerevival.core.configuration;

import com.github.tnerevival.TNELib;
import net.tnemc.config.CommentedConfiguration;

import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel.
 *
 * TNELib Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0
 * International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
public class ConfigurationMapper {

  private Map<String, CommentedConfiguration> configurations = new HashMap<>();
  private Map<String, String> configurationsMap = new HashMap<>();

  public void initializeFile(String file, String... identifiers) throws UnsupportedEncodingException {
    CommentedConfiguration configuration = new CommentedConfiguration(new File(TNELib.instance().getDataFolder(), file), new InputStreamReader(TNELib.instance().getResource(file), "UTF8"));

    if(configuration != null) {
      configuration.load();
      addConfiguration(file, configuration, identifiers);
    }
  }

  public void addConfiguration(String file, CommentedConfiguration configuration) {
    configurations.put(file, configuration);
  }

  public void addConfiguration(String file, CommentedConfiguration configuration, String... identifiers) {
    configurations.put(file, configuration);
    addIdentifiers(file, identifiers);
  }

  public void addIdentifiers(String file, String... identifiers) {
    for(String str : identifiers) {
      configurationsMap.put(str, file);
    }
  }

  public CommentedConfiguration getConfigurationByID(String identifier) {
    if(configurationsMap.containsKey(identifier)) {
      return configurations.getOrDefault(configurationsMap.get(identifier), null);
    }
    return null;
  }

  public String parseIdentifier(String node) {
    return node.split("\\.")[0];
  }

  public CommentedConfiguration getConfiguration(String node) {
    return getConfigurationByID(parseIdentifier(node));
  }

  public int getInt(String node) {
    return getConfiguration(node).getInt(node);
  }

  public boolean getBool(String node) {
    return getConfiguration(node).getBool(node);
  }

  public double getDouble(String node) {
    return getConfiguration(node).getDouble(node);
  }

  public short getShort(String node) {
    return getConfiguration(node).getShort(node);
  }

  public float getFloat(String node) {
    return getConfiguration(node).getFloat(node);
  }

  public BigDecimal getBigDecimal(String node) {
    return getConfiguration(node).getBigDecimal(node);
  }

  public String getString(String node) {
    TNELib.debug("Node: " + node);
    TNELib.debug("ID: " + parseIdentifier(node));
    TNELib.debug("Config Null?: " + (getConfiguration(node) == null));
    return getConfiguration(node).getString(node);
  }
}