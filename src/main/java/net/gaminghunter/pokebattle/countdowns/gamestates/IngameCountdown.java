package net.gaminghunter.pokebattle.countdowns.gamestates;

import lombok.Getter;
import lombok.Setter;
import net.gaminghunter.pokebattle.Pokebattle;
import net.gaminghunter.pokebattle.countdowns.PokeSpawnCountdown;
import net.gaminghunter.pokebattle.enums.GameState;
import net.gaminghunter.pokebattle.utils.LocationUtils;
import net.gaminghunter.pokebattle.utils.MessageUtils;
import net.gaminghunter.pokebattle.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitTask;

/**
 * Created by Luca on 15.06.2017.
 */
public class IngameCountdown {

    //TODO Change time
    private int remoteTime = 60;

    @Getter @Setter
    private int time;
    @Getter
    private boolean started;
    private BukkitTask task;

    private long delay = 0;
    private long period = 20;
    private int counter = 1;

    public void start() {
        time = remoteTime;
        if (!started) {
            started = true;
            task = Bukkit.getScheduler().runTaskTimer(Pokebattle.getInstance(), () -> {
                switch (time) {
                    case 5:
                        MessageUtils.sendActionBar("§eDie Sammelphase endet in §3" + time + " §eSekunden");
                        playPling();
                        break;
                    case 4:
                        MessageUtils.sendActionBar("§eDie Sammelphase endet in §3" + time + " §eSekunden");
                        playPling();
                        break;
                    case 3:
                        MessageUtils.sendActionBar("§eDie Sammelphase endet in §3" + time + " §eSekunden");
                        playPling();
                        break;
                    case 2:
                        MessageUtils.sendActionBar("§eDie Sammelphase endet in §3" + time + " §eSekunden");
                        playPling();
                        break;
                    case 1:
                        MessageUtils.sendActionBar("§eDie Sammelphase endet in §3einer §eSekunde");
                        playPling();
                        break;
                    case 0:
                        MessageUtils.sendActionBar("§eDie Sammelphase endet §3jetzt");
                        playPling();
                        stopGame();
                        stop();
                        break;
                }
                Pokebattle.getInstance().getIngameBoard().updateTitle();
                time--;

            }, delay, period);
        } else {
            task.cancel();
            started = false;
            start();
        }
    }

    public void stop() {
        started = false;
        time = remoteTime;
        if(task != null) {
            task.cancel();
        }
    }

    private void playPling() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.playSound(p.getLocation(), Sound.NOTE_PLING, (float)0.3, 2);
        });
    }

    public void stopGame() {
        Pokebattle.getInstance().getSpawnCountdowns().values().forEach(pokeSpawnCountdown -> {
            pokeSpawnCountdown.stop();
        });
        Entity shopVillager = Pokebattle.getInstance().getShopVillagerLocation().getWorld().spawnEntity(
                Pokebattle.getInstance().getShopVillagerLocation(), EntityType.VILLAGER
        );
        shopVillager.setCustomName("§ePoke§3Shop");
        shopVillager.setCustomNameVisible(true);
        Utils.setAiEnabled(shopVillager, false);
        Pokebattle.getInstance().setGameState(GameState.SHOP);
        Bukkit.getOnlinePlayers().forEach(o -> {
            o.getInventory().clear();
            o.teleport(Pokebattle.getInstance().getShopLocation());
        });
        Pokebattle.getInstance().getShopCountdown().start();
    }

}
