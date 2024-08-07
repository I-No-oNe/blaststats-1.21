package net.uhb217.blaststats.mixin.client;


import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.uhb217.blaststats.utils.ModVersionChecker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {


    @Inject(method = "getConnection", at = @At("RETURN"))
    private void onWorldLoadMixin(CallbackInfoReturnable<ClientConnection> cir) {
        try {
            ModVersionChecker.updateChecker();
        } catch (Exception e) {
            e.printStackTrace();
            ModVersionChecker.checked = false;
        }
    }
}
