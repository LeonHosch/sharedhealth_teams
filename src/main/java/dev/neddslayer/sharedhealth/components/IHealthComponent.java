package dev.neddslayer.sharedhealth.components;

import org.ladysnake.cca.api.v3.component.ComponentV3;

public interface IHealthComponent extends ComponentV3 {
    float getHealth(String team);
    void setHealth(String team, float health);
}