package dev.neddslayer.sharedhealth.mixin;

import com.mojang.authlib.GameProfile;
import dev.neddslayer.sharedhealth.components.SharedHealthComponent;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static dev.neddslayer.sharedhealth.components.SharedComponentsInitializer.*;

import java.util.HashSet;
import java.util.Set;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    @Shadow public abstract ServerWorld getEntityWorld();
    private static final Set<String> killingTeams = new HashSet<>();

    public ServerPlayerEntityMixin(World world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "damage", at = @At("RETURN"))
    public void damageListener(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		// ensure that damage is only taken if the damage listener is handled; you shouldn't be able to punch invulnerable players, etc.
		if (cir.getReturnValue() && this.isAlive()) {
			// float currentHealth = this.getHealth();
			SharedHealthComponent component = SHARED_HEALTH.get(this.getEntityWorld().getScoreboard());
            String team = this.getScoreboardTeam() != null ? this.getScoreboardTeam().getName() : "default";
            float knownHealth = component.getHealth(team);
            component.setHealth(team, knownHealth - amount);
			// if (currentHealth != knownHealth) {
			// 	component.setHealth(currentHealth);
			// }
		}
    }

    @Inject(method = "onDeath", at = @At("TAIL"))
    public void killEveryoneOnDeath(DamageSource damageSource, CallbackInfo ci) {
        String teamstr = this.getScoreboardTeam() != null ? this.getScoreboardTeam().getName() : "default";

        if (killingTeams.contains(teamstr)) {
            return;
        }

        killingTeams.add(teamstr);
        var team = this.getScoreboardTeam();
        
        this.getEntityWorld().getServer().getPlayerManager().getPlayerList().stream().filter(p -> p.getScoreboardTeam() == team).forEach(p -> p.kill(this.getEntityWorld()));

        SHARED_HEALTH.get(this.getEntityWorld().getScoreboard()).setHealth(teamstr, 20.0f);
        SHARED_HUNGER.get(this.getEntityWorld().getScoreboard()).setHunger(20);
		SHARED_SATURATION.get(this.getEntityWorld().getScoreboard()).setSaturation(20.0f);
        killingTeams.remove(teamstr);
    }
}
