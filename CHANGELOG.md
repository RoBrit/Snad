## [21.0.2]

## Changed

- Updated to latest Neoforge version 21.0.95+
- Migrated placable checking logic to tags:
  - `#snad/snad_placeable_crops` for snad crops (defaults to cactus, sugar cane, bamboo)
  - `#snad/snad_requires_water` for snad crops that require water to grow (defaults to sugar cane)
  - `#snad/suol_placeable_crops` for suol crops (defaults to nether wart)

### Fixed

- Crashing due to config loading issues
