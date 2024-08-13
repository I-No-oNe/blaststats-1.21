package net.uhb217.pvp_counter.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public interface CrystalExplodeEvent {
    Event<CrystalExplodeEvent> EVENT = EventFactory.createArrayBacked(CrystalExplodeEvent.class
    ,listeners-> (clientWorld, player)->{
        for (CrystalExplodeEvent listener : listeners) {
            ActionResult result = listener.interact(clientWorld, player);
            if(result != ActionResult.PASS) {
                return result;
            }
        }
        return ActionResult.PASS;
    });
    ActionResult interact(ClientWorld world, PlayerEntity player);
}