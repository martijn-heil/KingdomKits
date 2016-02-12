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
#### Configuration:
````
# Author: Ninjoh
#
# All item lists use bukkit material names. (Not item ID!, for example: DIAMOND_SWORD)
# For further information see: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html

# Don't change this!
configVersion: 3

# Interval in ticks for saving the data file.
# NOTE: 1 second = 20 ticks
# Default value is set to 6000 ticks, wich is 5 minutes exact.
dataFileSaveInterval: 6000

# Enable integration with factions?
# This is for the maxpercentage stuff with player classes.
# If set to false faction integration will be disabled, and the maxpercentage stuff
# will have no effect at all.
#
# If the factions plugin is not installed but this value is set to true, faction integration
# will not be enabled since it is impossible to do that when factions isn't installed.
enableFactionsIntegration: true



# Server region used for UUID API interaction
# This is used to get the UUID of a player with a given name.
#
# Valid values: EU or US
serverRegion: 'EU'


# Enchanting section
enchanting:
  blacklistedItems: # All items wich cannot be enchanted.
  - 'WOODEN_SWORD'
  - 'STONE_SWORD'
  - 'GOLD_SWORD'
  - 'IRON_SWORD'
  - 'DIAMOND_SWORD'
  - 'LEATHER_HELMET'
  - 'LEATHER_CHESTPLATE'
  - 'LEATHER_LEGGINGS'
  - 'LEATHER_BOOTS'
  - 'CHAINMAIL_HELMET'
  - 'CHAINMAIL_CHESTPLATE'
  - 'CHAINMAIL_LEGGINGS'
  - 'CHAINMAIL_BOOTS'
  - 'GOLD_HELMET'
  - 'GOLD_CHESTPLATE'
  - 'GOLD_LEGGINGS'
  - 'GOLD_BOOTS'
  - 'IRON_HELMET'
  - 'IRON_CHESTPLATE'
  - 'IRON_LEGGINGS'
  - 'IRON_BOOTS'
  - 'DIAMOND_HELMET'
  - 'DIAMOND_CHESTPLATE'
  - 'DIAMOND_LEGGINGS'
  - 'DIAMOND_BOOTS'
  - 'BOW'
  - 'GOLDEN_APPLE'


# Crafting section
crafting:
  blacklistedItems: # All items wich cannot be crafted.
  - 'WOODEN_SWORD'
  - 'STONE_SWORD'
  - 'GOLD_SWORD'
  - 'IRON_SWORD'
  - 'DIAMOND_SWORD'
  - 'LEATHER_HELMET'
  - 'LEATHER_CHESTPLATE'
  - 'LEATHER_LEGGINGS'
  - 'LEATHER_BOOTS'
  - 'CHAINMAIL_HELMET'
  - 'CHAINMAIL_CHESTPLATE'
  - 'CHAINMAIL_LEGGINGS'
  - 'CHAINMAIL_BOOTS'
  - 'GOLD_HELMET'
  - 'GOLD_CHESTPLATE'
  - 'GOLD_LEGGINGS'
  - 'GOLD_BOOTS'
  - 'IRON_HELMET'
  - 'IRON_CHESTPLATE'
  - 'IRON_LEGGINGS'
  - 'IRON_BOOTS'
  - 'DIAMOND_HELMET'
  - 'DIAMOND_CHESTPLATE'
  - 'DIAMOND_LEGGINGS'
  - 'DIAMOND_BOOTS'
  - 'BOW'
  - 'GOLDEN_APPLE'
  - 'BOAT'

# Soulbound section
soulbound:

  # Make soulbound items undamagable/unbreakable?
  soulboundItemsCannotBeDamaged: true

  # Prevent people from using weapons wich aren't soulbound..
  # Includes swords and bows (NOTE: Does not include axes!)
  preventNonSoulboundWeaponUsage: true

  # Prevent people from using axes wich aren't soublund against
  # entities (players, animals, monsters, etc).
  preventNonSoulboundAxeUsage: true


  # Default class for players
  defaultClass: 'footsoldier'

  # Cool down in minutes
  # Default value(1440) is 1 day exact.
  coolDownInMinutes: 1440

  # If a player changes faction; should his
  # player class be reset to the default player class?
  setPlayerClassToDefaultOnFactionChange: true


  # Classes are defined below
  # Essentials kits are used for the kits.
  # WARNING: Do not name a class "default"
  classes:
    footsoldier:
      kitName: 'footsoldier'
      useMaxPercentagePerFaction: true
      maxPercentagePerFaction: 100
    archer:
      kitName: 'archer'
      useMaxPercentagePerFaction: true
      maxPercentagePerFaction: 50
    knight:
      kitName: 'knight'
      useMaxPercentagePerFaction: false
      maxPercentagePerFaction: 100
    lord:
      kitName: 'lord'
      useMaxPercentagePerFaction: false
      maxPercentagePerFaction: 100

# Potion section
potions:

  # Disable potions?
  #
  # Disables drinking/consuming of potions
  # as well as throwing potions
  disablePotions: true


# Use the potions section above to disable
# consuming of potions as well as
# throwing potions & dispensers/droppers shooting potions
# So, don't add POTION to this list.
consume:
  blacklistedItems: # Items wich cannot be consumed (eat, drink, etc).
    - 'GOLDEN_APPLE'

# Use the potions section above to disable
# consuming of potions as well as
# throwing potions & dispensers/droppers shooting potions

# Section for preventing players using items.
usage:
  blacklistedItems: # Items wich cannot be used (right mouse button).
  - 'BOAT'
  - 'ENDER_PEARL'
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
Note: Adding ```` lore:&6&oSoulbound ```` makes the item soulbound.
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