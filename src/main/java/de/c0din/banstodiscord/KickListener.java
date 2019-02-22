package de.c0din.banstodiscord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

import java.awt.*;

/** Created by Aljosha on 22.02.2019 */
public class KickListener implements Listener {

  final BansToDiscord bansToDiscord = BansToDiscord.instance;
  final JDA api = bansToDiscord.getApi();

  @EventHandler
  public void onKick(PlayerKickEvent event) {
    Player player = event.getPlayer();
    if (player.isBanned()) {
      sendMessageToDiscord(player, true, event.getReason());
    } else {
      sendMessageToDiscord(player, false, event.getReason());
    }
  }

  private void sendMessageToDiscord(Player player, boolean banned, String reason) {
    String titleMessage;
    String channelId;
    if (banned) {
      titleMessage = bansToDiscord.getConfig().getString("messages.banned");
      channelId = bansToDiscord.getConfig().getString("discord.ban-channel");
    } else {
      titleMessage = bansToDiscord.getConfig().getString("messages.kicked");
      channelId = bansToDiscord.getConfig().getString("discord.kick-channel");
    }
    titleMessage = titleMessage.replaceAll("%player%", player.getName());
    TextChannel textChannel = api.getTextChannelById(channelId);
    if(textChannel != null){
      Message message =
              new MessageBuilder()
                      .setEmbed(
                              new EmbedBuilder()
                                      .setTitle(titleMessage)
                                      .setDescription("```" + reason + "```")
                                      .setColor(Color.decode(bansToDiscord.getConfig().getString("messages.color")))
                                      .setFooter("UUID: " + player.getUniqueId().toString(), null)
                                      .setThumbnail(
                                              "http://cravatar.eu/avatar/" + player.getUniqueId().toString() + "/512")
                                      .build())
                      .build();
      textChannel.sendMessage(message).queue();
    } else {
      BansToDiscord.log.severe(String.format("[%s] - This discord-channel does not exist! Please check your config.yml",
              bansToDiscord.getDescription().getName()));
    }
  }
}
