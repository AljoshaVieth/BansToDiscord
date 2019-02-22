package de.c0din.banstodiscord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import javax.security.auth.login.LoginException;
import java.util.logging.Logger;

/** Created by Aljosha on 22.02.2019 */
public class BansToDiscord extends JavaPlugin {
  private JDA api;
  static BansToDiscord instance;
  static final Logger log = Logger.getLogger("Minecraft");

  @Override
  public void onEnable() {
    instance = this;
    saveDefaultConfig();
    PluginManager pluginManager = Bukkit.getPluginManager();
    String token = getConfig().getString("discord.token");
    try {
      api = new JDABuilder(token).build();
    } catch (LoginException e) {
      log.severe(String.format("[%s] - Disabled, since no connection to Discord could be established! Please check the config.yml", getDescription().getName()));
      pluginManager.disablePlugin(this);
    }
    if(pluginManager.isPluginEnabled(this)){
      pluginManager.registerEvents(new KickListener(), this);
    }
  }

  JDA getApi() {
    return api;
  }
}
