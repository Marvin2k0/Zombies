package de.marvin2k0.zombiearchergame.listeners;

import de.marvin2k0.zombiearchergame.ZAMain;
import de.marvinleiers.minigameapi.game.Game;
import de.marvin2k0.zombiearchergame.utils.Text;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener
{
    @EventHandler
    public void onSign(SignChangeEvent event)
    {
        final Player player = event.getPlayer();

        if (!player.hasPermission("zombies.sign"))
            return;

        if (event.getLine(0).equalsIgnoreCase("[Zombies]") && !event.getLine(1).isEmpty() && ZAMain.getApi().exists(ZAMain.plugin, event.getLine(1)))
        {
            Game game = ZAMain.getApi().getGameFromName(ZAMain.plugin, event.getLine(1));

            event.setLine(0, "§6Zombies");
            event.setLine(1, event.getLine(1));
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if (!event.hasBlock())
            return;

        if (event.getClickedBlock().getType().toString().contains("SIGN"))
        {
            final Player player = event.getPlayer();
            final Sign sign = (Sign) event.getClickedBlock().getState();

            if (event.hasItem() && event.getItem().getType() == Material.DIAMOND_AXE)
                return;

            if (sign.getLine(0).equals("§6Zombies") && ZAMain.getApi().exists(ZAMain.plugin, sign.getLine(1)))
            {
                if (!ZAMain.config.getConfig().isSet("games." + sign.getLine(1) + ".lobby") ||
                        !ZAMain.config.getConfig().isSet("games." + sign.getLine(1) + ".zombies") ||
                        !ZAMain.config.getConfig().isSet("games." + sign.getLine(1) + ".archers"))
                {
                    player.sendMessage(Text.get("not-all-spawns-set"));
                    return;
                }

                Game game = ZAMain.getApi().getGameFromName(ZAMain.plugin, sign.getLine(1));
                game.join(ZAMain.plugin, player);

                event.setCancelled(true);
            }
        }
    }
}
