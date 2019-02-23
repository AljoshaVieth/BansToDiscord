package de.c0din.banstodiscord;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

/** Created by Aljosha on 22.02.2019 */
public class KickListener implements Listener {



  @EventHandler
  private void onKick(PlayerKickEvent event) {
    Player player = event.getPlayer();
    BansToDiscordManager manager = BansToDiscordManager.getInstance();
    if (player.isBanned()) {
      manager.sendMessageToDiscord(player, true, event.getReason());
    } else {
      manager.sendMessageToDiscord(player, false, event.getReason());
    }
  }


}
