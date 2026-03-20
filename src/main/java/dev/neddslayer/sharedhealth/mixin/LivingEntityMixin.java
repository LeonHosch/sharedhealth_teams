package dev.neddslayer.sharedhealth.mixin;

import dev.neddslayer.sharedhealth.components.SharedHealthComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.neddslayer.sharedhealth.components.SharedComponentsInitializer.SHARED_HEALTH;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow public abstract boolean isAlive();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "heal", at=@At("HEAD"))
    public void healListener(float amount, CallbackInfo ci) {
        if ((LivingEntity) (Object) this instanceof ServerPlayerEntity player && this.isAlive()) {
            SharedHealthComponent component = SHARED_HEALTH.get(player.getEntityWorld().getScoreboard());
            float knownHealth = component.getHealth();
            // the previous check wont work anymore with the changed damage system
            // players can heal multiple times now with one splash potion (depending on how many players are affected), which is kinda up for debate if wanted or not
            component.setHealth(knownHealth + amount);
        }
    }
}
