package net.uhb217.pvp_counter.mixin.client;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.uhb217.pvp_counter.client.Global;
import net.uhb217.pvp_counter.config.ConfigFile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements Global {
    @Inject(method = "onDeath",at = @At("RETURN"))
    public void onDeath(DamageSource damageSource, CallbackInfo ci) {
        if(damageSource.getSource() instanceof PlayerEntity && ConfigFile.getConfig().playerDeathsCount) {
            if (damageSource.getSource().getName() == mc.player.getName() && ConfigFile.getConfig().playerDeathsCount) {
                mc.player.sendMessage(Text.literal(PREFIX + "§5You have died!"), false);
            }
        }
    }
}
