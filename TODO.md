# TODO LIST:

---

## High Priority

- [X] fix market recipe
- [X] check block light level
- [X] fix market item texture
- [X] make sure market block has loot table
- [X] check where villager menu start / ends on screen
- [ ] brain description isnt capitalised, rest are
- [ ] heart has weird gap
- [ ] some too long, max 4 lines - preferably 3. OR make font smaller (must be consistent across organs)
- [X] make shift click work
- [X] when selling organs the slot is empyt but items can still be taken from it
- [X] handle credsits client side, but update on leave on login to store on server.
- [X] credits not changing server side
- [X] buying organs always at full durability
 
---

## Low Priority

- [X] Add market Saved data to overworld
- - [ ] check data is synced over server - i dont see why it 
wouldnt but better check
- [X] add credits nbt to player
- [X] Add a method which updates market data
- [X] Add market block
- [X] add market block entity
- - [X] give inventory
- [X] Add market screen
- - [X] make screen asset
- - [X] make menu function
- - [X] buy and sell buttons - edits credit data
- [X] Add network packets
- - [X] Item bought/sold
- - [X] get item cost
- [ ] ~~if you switch to a different organ than in the slot then it shows the wrong estimated cost~~~

---

## Optional

- [ ] Commands to help debug market
- - [ ] show prices / demand
- - [ ] change prices / demand
- - [ ] force market update
- [ ] market crash event
- [X] add a volatility param for the organs - this would affect how much 
they fluctuate
- [ ] organ functions
- - [ ] buffs
- - [ ] failiure
- [ ] graph history of organ on screen
- [ ] make particles nicer


---

## Final

- [ ] remeber to remove organ_base.json
- [ ] go through and remove all redundant logger

---

## Evans WIP

working on menu, needs buy and sell functionality,

## Mikolajs WIP

organ descriptions

---

## Explanation:

### World saved data

To save the market data and synchronise it, the data has to be saved
to the world by the server. This data will include prices of organs and
organs bought and sold since last update.

### NBT data

NBT data can be saved to anything such as players, this doesn't have to 
synced so can be saved client side. We need NBT data to store the amount of 
money/credits the player currently has.

### Market update method

Prices will change randomly but influenced by demand. More organs sold then
prices goes down and vice versa. In general prices should increase if left
alone, as players are more likely to sell organs then hoard them.

Btw i think i would be funny to have a really low chance of the market just
completely crashing for no reason.

### Market block

This will have to be a block entity but to begin with just use a regular 
block. The idea is that it will almost act like villager trades. The player
will have credits which will let them buy and sell organs which can then be 
traded amongst eachother.

### Market screen

For the block entity, should have buttons to select an organ. Needs to
display cost and player credits.

### Network packets

For communication between server and player. Mainly for requesting organ 
prices and sending buy and sell data.
