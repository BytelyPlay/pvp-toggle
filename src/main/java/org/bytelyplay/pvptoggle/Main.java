package org.bytelyplay.pvptoggle;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.bytelyplay.pvptoggle.listeners.PVPManager;

@Mod(Main.MODID)
public class Main {
    public static final String MODID = "pvptoggle";
    public Main(IEventBus bus, ModContainer container) {
        bus.addListener(this::commonSetup);

        PVPManager.readAll();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {}
}
