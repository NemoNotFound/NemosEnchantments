# Changelog v1.8

## Changes
- Added the Felling enchantment for axes
  - Cuts down an entire tree at once
  - Uses durability normally for every broken block
  - A group of directly or diagonally connected logs from the same wood family with enough matching, non-persistent leaves nearby is considered a tree for felling
  - Regular and stripped logs are considered the same wood family
  - Player-placed persistent leaves do not qualify a structure as a tree
  - Processes at most 256 logs per use
