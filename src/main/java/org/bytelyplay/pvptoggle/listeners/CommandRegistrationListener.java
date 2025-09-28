package org.bytelyplay.pvptoggle.listeners;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.bytelyplay.pvptoggle.Main;
import org.bytelyplay.pvptoggle.utils.Messages;

import java.util.UUID;

@EventBusSubscriber(modid = Main.MODID)
public class CommandRegistrationListener {
    @SubscribeEvent
    public static void onCommandRegistrationEvent(RegisterCommandsEvent event) {
        // Normally i'd make this modular but it is not worth it for one command...
        event.getDispatcher().register(
                LiteralArgumentBuilder.<CommandSourceStack>literal("pvp")
                .executes((ctx) -> {
                    CommandSourceStack source = ctx.getSource();
                    if (source.isPlayer()) {
                        Player sender = source.getPlayerOrException();
                        UUID senderUUID = sender.getUUID();

                        PVPManager.setPVP(senderUUID,
                                !PVPManager.getPVP(senderUUID));
                        boolean pvpState = PVPManager.getPVP(senderUUID);
                        if (pvpState) {
                            sender.sendSystemMessage(Messages.ENABLED_PVP);
                        } else {
                            sender.sendSystemMessage(Messages.DISABLED_PVP);
                        }
                    } else {
                        source.sendSystemMessage(Messages.ONLY_PLAYERS_CAN_EXECUTE);
                    }
                    return 1;
                })
        );
    }
}
