#### Commands:
All sub commands below are sub command of the command */kingdomkits* (Alias: */kk*)

````
+-------------+---------------------------------------+----------------------+
| Sub-command |              Description              |      Permission      |
+-------------+---------------------------------------+----------------------+
| help        | Show plugin help.                     | None                 |
| info        | Show plugin info.                     | None                 |
| list        | Show a list of all classes.           | None                 |
| getClass    | Get a player's class.                 | None                 |
| setClass    | Set a player's class.                 | kingdomkits.setclass |
| reload      | Reload the plugin config.             | kingdomkits.reload   |
| bind        | Make the item in your hand soulbound. | kingdomkits.bind     |
+-------------+---------------------------------------+----------------------+
````


#### Permissions:
````
        kingdomkits.*:
            description: Gives access to all kingdomkits permissions & commands.
            children:
                kingdomkits.bind: true
                kingdomkits.setclass: true
                kingdomkits.setclass.class.*: true
                kingdomkits.setclass.others: true
                kingdomkits.getclass: true
                kingdomkits.getclass.others: true
                kingdomkits.list: true
                kingdomkits.bypass.factionmembershipchangeevent: true
        kingdomkits.bypass.*:
            description: Gives access to all kingdomkits bypass permissions.
            children:
                kingdomkits.bypass.factionmembershipchangeevent: true
                kingdomkits.bypass.changeclasscooldown: true
        kingdomkits.bind:
            default: op
        kingdomkits.list:
            default: true
        kingdomkits.setclass:
            default: op
        kingdomkits.setclass.class.*:
            default: op
        kingdomkits.setclass.others:
            default: op
        kingdomkits.getclass:
            default: true
        kingdomkits.getclass.others:
            default: true
        kingdomkits.bypass.factionmembershipchangeevent:
            default: op
        kingdomkits.bypass.changeclasscooldown:
            default: op
````

#### Dependencies:
- Dependencies:
    - [ArmorEquipEvent v1.5](https://www.spigotmc.org/resources/lib-armorequipevent.5478/update?update=56532)
    - [Essentials](https://hub.spigotmc.org/jenkins/job/Spigot-Essentials/)

- Optional Dependencies:
    - [Factions](https://www.spigotmc.org/resources/factions.1900/)


#### System requirements:
- Java >= 7


#### FAQ:
- Where to define the kits for the classes?
  - Answer: In the essentials config, the kits section.

- Where to define the player classes?
  - Answer: In the kingdomkits config.



#### Example soulbound *kit*:
These are defined in the essentials config, at the kits section;
````
# The lores are important; they make the item soulbound.
  footsoldier:
    delay: 0
    items:
      - 267 1 lore:&6&oSoulbound
      - 261 1 lore:&6&oSoulbound infinity:1
      - 306 1 lore:&6&oSoulbound
      - 307 1 lore:&6&oSoulbound
      - 300 1 lore:&6&oSoulbound
      - 305 1 lore:&6&oSoulbound
      - 262 1 lore:&6&oSoulbound
````