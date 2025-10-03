package org.bytelyplay.pvptoggle.listeners;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import org.bytelyplay.pvptoggle.Main;
import org.bytelyplay.pvptoggle.utils.Messages;

@EventBusSubscriber(modid = Main.MODID)
public class AttackListeners {
    @SubscribeEvent
    public static void onAttackEvent(LivingIncomingDamageEvent event) {
        DamageSource source = event.getSource();

        Entity victimEntity = event.getEntity();
        Entity attackerEntity = source.getEntity();
        if (attackerEntity == null) return;

        if (victimEntity instanceof Player victim &&
                attackerEntity instanceof Player attacker) {
            if (!(PVPManager.getPVP(victim.getUUID()) && PVPManager.getPVP(attacker.getUUID()))) {
                victim.displayClientMessage(Messages.ATTACK_DENIED, false);
                attacker.displayClientMessage(Messages.ATTACK_DENIED, false);
                event.setCanceled(true);
            }
        }
    }
}
