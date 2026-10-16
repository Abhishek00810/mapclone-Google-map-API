# Map_TODO

An Android app for creating and saving maps with your own markers, built with the Google Maps SDK.

## Setup

Add your Google Maps API key to `local.properties` in the project root (this file is git-ignored):

```
MAPS_API_KEY=your_api_key_here
```

## Project structure

```
app/src/main/java/com/example/map_todo/
├── Constants.kt                    # Intent extra keys
├── data/
│   ├── MapRepository.kt            # Saves/loads maps from app storage
│   └── model/                      # UserMap, Place
└── ui/
    ├── list/                       # MainActivity + MapsAdapter: list of saved maps
    ├── create/                     # CreateMapActivity: long-press to add markers
    └── display/                    # DisplayMapActivity: view a saved map
```
