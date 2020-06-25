package de.marvin2k0.zombiearchergame;

import de.marvin2k0.zombiearchergame.listeners.GameListener;
import de.marvin2k0.zombiearchergame.listeners.SignListener;
import de.marvin2k0.zombiearchergame.utils.CustomConfig;
import de.marvin2k0.zombiearchergame.utils.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import de.marvinleiers.minigameapi.MinigameAPI;

public class ZAMain extends JavaPlugin
{
    public static MinigameAPI api;
    public static ZAMain plugin;
    public static CustomConfig config;

    @Override
    public void onEnable()
    {
        Text.setUp(this);
        CustomConfig.setUp(this);
        plugin = this;
        api = MinigameAPI.getAPI(this);
        config = new CustomConfig(this.getDataFolder().getPath() + "/config.yml");

        this.getServer().getPluginManager().registerEvents(new SignListener(), this);
        this.getServer().getPluginManager().registerEvents(new GameListener(), this);

        this.getCommand("zombies").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(Text.get("noplayer"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0)
        {
            player.sendMessage(Text.get("prefix") + " /zb wand");
            player.sendMessage(Text.get("prefix") + " /zb setspawn zombies <game>");
            player.sendMessage(Text.get("prefix") + " /zb setspawn archers <game>");
            player.sendMessage(Text.get("prefix") + " /zb setlobby <game>");
            return true;
        }

        if (args[0].equalsIgnoreCase("leave"))
        {
            if (MinigameAPI.inGame(player))
            {
                MinigameAPI.gameplayers.get(player).getGame().leave(this, player);
                return true;
            }
        }

        if (args.length == 2)
        {
            if (args[0].equalsIgnoreCase("setlobby"))
                config.setLocation("games." + args[1] + ".lobby", player.getLocation());
            else if (args[0].equalsIgnoreCase("wand"))
                //TODO:
            return true;
        }

        if (args.length < 3)
        {
            player.sendMessage(Text.get("prefix") + " /zb setspawn zombies <game>");
            player.sendMessage(Text.get("prefix") + " /zb setspawn archers <game>");
            return true;
        }

        if (args[0].equalsIgnoreCase("setspawn"))
        {

            if (!api.exists(this, args[2]))
                api.createGame(this, args[2]);

            if (args[1].equalsIgnoreCase("zombies"))
            {
                config.setLocation("games." + args[2] + ".zombies", player.getLocation());
                return true;
            }
            else if (args[1].equalsIgnoreCase("archers"))
            {
                config.setLocation("games." + args[2] + ".archers", player.getLocation());
                return true;
            }
            else
            {
                player.sendMessage("§cUsage: /zb setspawn <archers|zombies>");
                return true;
            }
        }

        return false;
    }

    public static MinigameAPI getApi()
    {
        return api;
    }
}
