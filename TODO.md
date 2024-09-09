# TODO LIST:

---

- [X] Add market Saved data to overworld
- - [ ] check data is synced over server - i dont see why it 
wouldnt but better check
- [X] add credits nbt to player
- [X] Add a method which updates market data
- [ ] Add market block
- [ ] Add market screen
- [ ] Add network packets
- - [ ] Item bought
- - [ ] Item sold
- - [ ] get item cost

---

## Optional

- [ ] Commands to help debug market
- - [ ] show prices / demand
- - [ ] change prices / demand
- - [ ] force market update
- [ ] market crash event
- [ ] add a volatility param for the organs - this would affect how much 
they fluctuate

---

## Evans WIP

added a packet for sending an organs price, i cant test it until the 
block is added so ill hold off on other packets.

## Mikolajs WIP



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
