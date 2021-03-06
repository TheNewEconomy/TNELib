/**
 * The New Economy Library Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
package com.github.tnerevival.user;

import com.github.tnerevival.TNELib;
import com.github.tnerevival.core.api.MojangAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by creatorfromhell on 11/6/2016.
 **/
public class IDFinder {

  public static UUID ecoID(String username) {
    return ecoID(username, false);
  }

  public static UUID ecoID(String username, boolean skip) {
    if(TNELib.instance().getUuidManager().hasID(username)) {
      return TNELib.instance().getUuidManager().getID(username);
    }
    UUID eco = (skip)? genUUID() : genUUID(username);
    //TNELib.instance().getUuidManager().addUUID(username, eco);
    return eco;
  }

  public static String getUsername(String identifier) {
    if(isUUID(identifier)) {
      UUID id = UUID.fromString(identifier);
      OfflinePlayer player = Bukkit.getOfflinePlayer(id);
      if(player == null) {
        return MojangAPI.getPlayerUsername(id);
      }
      return player.getName();
    }
    return identifier;
  }

  public static UUID genUUID(String name) {
    UUID id = Bukkit.getOfflinePlayer(name).getUniqueId();
    if(id != null) return id;

    id = MojangAPI.getPlayerUUID(name);
    if(id != null) return id;

    return genUUID();
  }

  public static UUID genUUID() {
    UUID id = UUID.randomUUID();
    while(TNELib.instance().getUuidManager().containsUUID(id)) {
      //This should never happen, but we'll play it safe
      id = UUID.randomUUID();
    }
    return id;
  }

  public static String ecoToUsername(UUID id) {
    return (TNELib.instance().getUuidManager().containsUUID(id))? TNELib.instance().getUuidManager().getUsername(id) : getUsername(id.toString());
  }

  public static UUID getID(CommandSender sender) {
    if(!(sender instanceof Player)) {
      return getID(TNELib.instance().consoleName);
    }
    return getID((Player)sender);
  }

  public static UUID getID(Player player) {
    return getID(player.getName());
  }

  public static UUID getID(OfflinePlayer player) {
    if(!TNELib.instance().useUUID) {
      return ecoID(player.getName());
    }
    return player.getUniqueId();
  }

  public static Player getPlayer(String identifier) {
    UUID id = getID(identifier);
    if(!TNELib.instance().useUUID) {
      return Bukkit.getPlayer(IDFinder.ecoToUsername(id));
    }
    if(!Bukkit.getServer().getOnlineMode()) {
      return Bukkit.getPlayer(IDFinder.ecoToUsername(id));
    }
    return Bukkit.getPlayer(id);
  }

  private static OfflinePlayer getOffline(String identifier, boolean username) {
    if(username) return Bukkit.getOfflinePlayer(identifier);
    UUID id = getID(identifier);

    return Bukkit.getOfflinePlayer(id);
  }

  private static OfflinePlayer getOffline(UUID id) {
    return Bukkit.getOfflinePlayer(id);
  }

  public static UUID getID(String identifier) {
    identifier = ChatColor.stripColor(identifier.replaceAll("\\[.*?\\] ?", "")).trim();
    TNELib.debug("GETID: " + identifier);
    if(isUUID(identifier)) {
      return UUID.fromString(identifier);
    }

    if(identifier.contains(TNELib.instance().factionPrefix)) {
      TNELib.debug("Faction");
      UUID id = ecoID(identifier);
      checkSpecial(id);
      return id;
    }

    if(identifier.contains("towny-war-chest")) {
      TNELib.debug("Towny War Chest");
      UUID id = ecoID(identifier);
      checkSpecial(id);
      return id;
    }

    if(identifier.contains(TNELib.instance().townPrefix)) {
      TNELib.debug("Towny Town");
      UUID id = ecoID(identifier);
      checkSpecial(id);
      return id;
    }

    if(identifier.contains(TNELib.instance().nationPrefix)) {
      TNELib.debug("Towny Nation");
      UUID id = ecoID(identifier);
      checkSpecial(id);
      return id;
    }

    if(!TNELib.instance().useUUID) {
      TNELib.debug("ECO ID RETURNED");
      return ecoID(identifier);
    }

    UUID mojangID = (identifier.equalsIgnoreCase(TNELib.instance().consoleName))? null : Bukkit.getOfflinePlayer(identifier).getUniqueId();
    if(mojangID == null) {
      TNELib.debug("MOJANG API RETURNED NULL VALUE");
      return ecoID(identifier);
    }
    //TNELib.instance().getUuidManager().addUUID(identifier, mojangID);
    return mojangID;
  }

  private static void checkSpecial(UUID id) {
    if(!TNELib.instance().special.contains(id)) {
      TNELib.instance().special.add(id);
    }
  }

  public static boolean isUUID(String lookup) {
    try {
      UUID.fromString(lookup);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }
}