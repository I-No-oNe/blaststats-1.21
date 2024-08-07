package net.uhb217.blaststats.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.uhb217.blaststats.BlastStats.PREFIX;

@Mixin(PlayerEntity.class)
public class PlayerDieMixin {
    @Inject(method = "onDeath",at = @At("RETURN"))
    public void onDeath(DamageSource damageSource, CallbackInfo ci) {
        if(damageSource.getSource() instanceof PlayerEntity) {
            if (damageSource.getSource().getName() == MinecraftClient.getInstance().player.getName())
                MinecraftClient.getInstance().player.sendMessage(Text.literal(PREFIX + "§5You have died!"), false);
        }

    }
}
