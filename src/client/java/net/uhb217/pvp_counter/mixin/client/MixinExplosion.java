package net.uhb217.pvp_counter.mixin.client;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.explosion.Explosion;
import net.uhb217.pvp_counter.client.Global;
import net.uhb217.pvp_counter.config.ConfigFile;
import net.uhb217.pvp_counter.events.CrystalExplodeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Explosion.class)
public class MixinExplosion implements Global {

    @Inject(method = "collectBlocksAndDamageEntities", at = @At("TAIL"))
    private void onExplode(CallbackInfo info) {
        Explosion explosion = (Explosion) (Object) this;
        Entity entity = explosion.getEntity();
        assert entity != null;
        if (entity.getType() == EntityType.END_CRYSTAL && ConfigFile.getConfig().crystalCount) {
            if (mc.player.getWorld() != null) {
                CrystalExplodeEvent.EVENT.invoker().interact(mc.world,mc.player);
            }
        }
    }
}
