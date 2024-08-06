package net.uhb217.blaststats.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.explosion.Explosion;
import net.uhb217.blaststats.CrystalExplodeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Explosion.class)
public class CrystalExplodeMixin {

    @Inject(method = "collectBlocksAndDamageEntities", at = @At("TAIL"))
    private void onExplode(CallbackInfo info) {
        Explosion explosion = (Explosion) (Object) this;
        Entity entity = explosion.getEntity();
        assert entity != null;
        if (entity.getType() == EntityType.END_CRYSTAL) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.world != null) {
                CrystalExplodeEvent.EVENT.invoker().interact(client.world, client.player);
            }
        }
    }
}
