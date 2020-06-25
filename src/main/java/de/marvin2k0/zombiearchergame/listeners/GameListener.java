package de.marvin2k0.zombiearchergame.listeners;

import de.marvin2k0.zombiearchergame.ZAMain;
import de.marvinleiers.minigameapi.MinigameAPI;
import de.marvinleiers.minigameapi.events.GameStartEvent;
import de.marvinleiers.minigameapi.events.PlayerGameJoinEvent;
import de.marvinleiers.minigameapi.game.Game;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameListener implements Listener
{
    private static HashMap<Game, ArrayList<Player>> archers = new HashMap<>();
    private static HashMap<Game, ArrayList<Player>> zombies = new HashMap<>();

    private static Random random = new Random();

    @EventHandler
    public void onGameJoin(PlayerGameJoinEvent event)
    {
        Player player = event.getPlayer();
        Location lobby = ZAMain.config.getLocation("games." + event.getGame().getName() + ".lobby");

        player.teleport(lobby);
    }

    @EventHandler
    public void onStart(GameStartEvent event)
    {
        Game game = event.getGame();

        archers.put(game, new ArrayList<>());
        zombies.put(game, new ArrayList<>());

        if (game.getPlayers().size() < 5)
        {
            archers.get(game).addAll(game.getPlayers());

            for (int i = 0; i < 1; i++)
            {
                int randomArcher = random.nextInt(archers.get(game).size());
                zombies.get(game).add(archers.get(game).get(randomArcher));
                archers.get(game).remove(randomArcher);
            }

            System.out.println("zombies " + zombies.get(game).size());
            System.out.println("archers " + archers.get(game).size());
        }
        else if (game.getPlayers().size() < 12)
        {
            archers.get(game).addAll(game.getPlayers());

            for (int i = 0; i < 2; i++)
            {
                int randomArcher = random.nextInt(archers.get(game).size());
                zombies.get(game).add(archers.get(game).get(randomArcher));
                archers.get(game).remove(randomArcher);
            }

            System.out.println("zombies " + zombies.get(game).size());
            System.out.println("archers " + archers.get(game).size());
        }
        else
        {
            archers.get(game).addAll(game.getPlayers());

            for (int i = 0; i < 5; i++)
            {
                int randomArcher = random.nextInt(archers.get(game).size());
                zombies.get(game).add(archers.get(game).get(randomArcher));
                archers.get(game).remove(randomArcher);
            }

            System.out.println("zombies " + zombies.get(game).size());
            System.out.println("archers " + archers.get(game).size());
        }

        for (Player archer : archers.get(game))
            archer.teleport(ZAMain.config.getLocation("games." + game.getName() + ".archers"));

        for (Player zombie : zombies.get(game))
            zombie.teleport(ZAMain.config.getLocation("games." + game.getName() + ".zombies"));
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event)
    {
        if (MinigameAPI.inGame(event.getPlayer()))
            event.getPlayer().sendMessage("in game");
        else
            event.getPlayer().sendMessage("not in game");
    }
}
