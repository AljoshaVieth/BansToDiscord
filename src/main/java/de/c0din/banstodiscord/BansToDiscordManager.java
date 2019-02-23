package de.c0din.banstodiscord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.entity.Player;

import java.awt.*;

/** Created by Aljosha on 23.02.2019 */
public class BansToDiscordManager {
  private static BansToDiscordManager instance;
  private final BansToDiscord bansToDiscord = BansToDiscord.instance;
  private final JDA jda = bansToDiscord.getJda();

  private BansToDiscordManager() {}

  public static BansToDiscordManager getInstance() {
    if (BansToDiscordManager.instance == null) {
      BansToDiscordManager.instance = new BansToDiscordManager();
    }
    return BansToDiscordManager.instance;
  }

  public void sendMessageToDiscord(Player player, boolean banned, String reason) {
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
    TextChannel textChannel = jda.getTextChannelById(channelId);
    if (textChannel != null) {
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
      BansToDiscord.log.severe(
          String.format(
              "[%s] - This discord-channel does not exist! Please check your config.yml",
              bansToDiscord.getDescription().getName()));
    }
  }
}
