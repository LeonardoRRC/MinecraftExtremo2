package me.meowquantum.com.mineextermo2.Listeners;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;

public class CustomDropListener implements Listener {

    private final Random random = new Random();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Material blockType = event.getBlock().getType();
        ItemStack drop = null;

        if (blockType == Material.GRASS_BLOCK || blockType == Material.DIRT) {
            if (random.nextDouble() < 0.05) {
                drop = new ItemStack(Material.BONE);
            }
        }

        else if (blockType == Material.GRASS) {
            if (random.nextDouble() < 0.1) {
                drop = new ItemStack(Material.PUMPKIN_SEEDS);
            }
        }

        if (drop != null) {
            event.setDropItems(false);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Zombie) {
            if (random.nextDouble() < 0.5) {
                event.getDrops().add(new ItemStack(Material.BONE));
            }
        } else if (event.getEntity() instanceof Creeper) {
            if (random.nextDouble() < 0.5) {
                Creeper creeper = (Creeper) event.getEntity();
                creeper.getWorld().createExplosion(creeper.getLocation(), 3F, false, false);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Skeleton && event.getEntity() instanceof Player) {
            if (random.nextDouble() < 0.2) {
                shootTripleArrows((Skeleton) event.getDamager());
            }
        }
    }

    private void shootTripleArrows(Skeleton skeleton) {
        Player target = (Player) skeleton.getTarget(); // Assuming the target is a player
        if (target == null) return;

        Vector direction = target.getLocation().toVector().subtract(skeleton.getLocation().toVector()).normalize();

        for (int i = 0; i < 3; i++) {
            Arrow arrow = skeleton.getWorld().spawn(skeleton.getEyeLocation(), Arrow.class);
            arrow.setShooter(skeleton);
            arrow.setVelocity(direction.multiply(1.5).add(new Vector(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5)));
        }
    }
}
