# TODO LIST:

---

- [ ] Add market Saved data to overworld
- [ ] add credits nbt to player
- [ ] Add a method which updates market data 
- [ ] Add market block
- [ ] Add market screen
- [ ] Add network packets
- - [ ] Item bought
- - [ ] Item sold
- - [ ] get item cost

---

## Evans WIP



## Mikolajs WIP



---

## Explanation:

### World saved data

To save the market data and synchronise it, the data has to be saved
to the world by the server. This data will include prices of organs and
organs bought and sold since last update.

### NBT data

NBT data can be saved to anything such as players, this doesn't have to synced
so can be saved client side. We need NBT data to store the amount of 
money/credits the player currently has.
