<div align="center">

# SharedHealth

A Fabric Minecraft mod that syncs health (and optionally hunger) between players.

![SharedHealth Logo](https://github.com/Neddslayer/sharedhealth/blob/master/src/main/resources/assets/sharedhealth/textures/icon.png)

---

## Features

- **Team-based Shared Health**  
  Players share health **within their team** instead of globally.

- **Default Team**  
  Players without a team are automatically part of a global **default group**.

- **Optional Shared Hunger**  
  Hunger can be synced globally (not team-based).

---

## Usage

### Teams (Vanilla Commands)

SharedHealth uses Minecraft's built-in `/team` system:

```
/team add <team>
/team join <team> <player>
/team leave <player>
```

- Players in the same team share health.
- Players without a team share health with each other (default group).

---

### Gamerules

```
/gamerule shareHealth true|false
/gamerule shareHunger true|false
```

- **shareHealth** → Enables team-based shared health  
- **shareHunger** → Enables global shared hunger (not team-based)

---

</div>
