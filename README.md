# Original task

Create 2 small command-line tools that load list of projects from our gallery and and extract some data for them.

1. Create a small tool that loads, parses list of projects from page of our gallery https://planner5d.com/gallery/floorplans and creates an output JSON with info for each project: project hash, thumbnail image uri. Example of output JSON: [{"hash": "0123ABC", "thumbnail": "https://..."}, ...]

2. Create another small tool that gets JSON file (generated in 1. step) does some processing and outputs info to CSV. Data processing that you should do for each project:
   2.1. Downloads project thumbnail, and write it to a file: [hash].jpg
   * Bonus: also write a [hash].webp file (convert to WebP format, you can use any library or utility)   
   2.2. Load project data JSON (you need to find uri of our API where to get project data JSON), parse it, count how many rooms there are in project and write this info to output XML (project hash + count of rooms).

3. Now read the data you got in JSON (from 1. step) and XML (from 2.2. step), zip it together and write to CSV. The CSV should have fields: hash, thumbnail, rooms.

So the final output after step 3. should be output.csv:
hash,thumbnail,rooms
0123ABC,"https://...",12

# Modules

- page-extractor - extracts page data into data/page.json
- image-processor - downloads floor plans' images and generates data/rooms.xml
- csv-merger - merge data/page.json and data/rooms.xml into data/output.csv

# How to run

Execute `run.sh`

# Future possible improvements

- parse all pages on https://planner5d.com/gallery/floorplans page with coroutines
- add API call to projects data via REST client
- make another module with common libs like logging, jackson, etc.
- dockerize module and package everything in docker-compose.yml with common volume in ./data directory