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
            // float currentHealth = player.getHealth();
            SharedHealthComponent component = SHARED_HEALTH.get(player.getEntityWorld().getScoreboard());
            String team = player.getScoreboardTeam() != null ? player.getScoreboardTeam().getName() : "default";
            float knownHealth = component.getHealth(team);

            float newHealth = knownHealth + amount;
            if (newHealth > 20.0f) newHealth = 20.0f;
            component.setHealth(team, newHealth);
            // if (currentHealth == knownHealth) {
            //     component.setHealth(knownHealth + amount);
            // }
        }
    }
}
