# Destiny Plugin Pack - 1.2

**Plugin Configuration Requirements**

Upon using any command for the first time the following will be added to your BOT Config file

          Config Key                             Config Value
          BUNGIE_API_KEY                         The API Key for making queries 

It is recommended that you add this to your BotConfig file before doing anything else

#Important Information
**To Use any of the List commands, you will need to have a MySQL Server set up in Botconfig**
**To Use any of the Image Badges, you will need to have a WebServer set up in Botconfig**

#Commands
**Current Plugin Commands as of Version 1.2** 

All commands begin with an '!'

        Command                        Argument(s)      - Description
    1.  debugguardian                  <PlayerName>     - Outputs Guardian debug information
    2.  info[-ps][-xb]                 [PlayerName]     - General information about player
    3.  rumblelistgold                                  - Displays top 10 of rumble list
    4.  rumblelistsilver                                - Displays rank 11-20 of rumble list
    5.  rumblelistbronze                                - Displays rank 21-30 of rumble list
    6.  rumbleaddtolist[-ps][-xb]      <PlayerName>     - MOD ONLY - Adds player to rumble list (Updated every 5 min)
    7.  rumbleremovefromlist[-ps][-xb] <PlayerName>     - MOD ONLY - Removes player from rumble list
    8.  rumblelist                                      - MOD ONLY - Displays in channel full rumble list
    9.  rumblelist-csv                                  - MOD ONLY - PM's a CSV export of full rumble list
    10. rumblelistrefresh                               - MOD ONLY - Refresh's all the stats in the current rumble list
    11. rumblelist-importcsv           <CSV Attachment> - MOD ONLY - Import CSV with header of "playername, platform, show" into db
    12. trialsmap                                       - Displays current trials map with picture
    13. fireteam[-ps][-xb]             <PlayerName>     - Displays Detailed information about current players ToO Fireteam
    14. fireteam#[-ps][-xb]            <PlayerName>     - Displays Detailed information about current players ToO Fireteam ( In Text Form )
    15. trials[-ps][-xb]               [PlayerName]     - Displays Detailed information about ToO player
    16. trials#[-ps][-xb]              [PlayerName]     - Displays Detailed information about ToO player ( In Text Form )
    17. trialslistgold                                  - Displays top 10 of Trials list
    18. trialslistsilver                                - Displays rank 11-20 of Trials list
    19. trialslistbronze                                - Displays rank 21-30 of Trials list
    20. trialsaddtolist[-ps][-xb]      <PlayerName>     - MOD ONLY - Adds player to ToO ranking list (Updated every 5 min)
    21. trialsremovefromlist[-ps][-xb] <PlayerName>     - MOD ONLY - Removes player from ToO ranking list
    22. trialslist                                      - MOD ONLY - Displays in channel full ToO list
    23. trialslist-csv                                  - MOD ONLY - PM's a CSV export of full ToO ranking list
    24. trialslistrefresh                               - MOD ONLY - Refresh all the stats in the current ToO ranking list
    25. trialslist-importcsv           <CSV Attachment> - MOD ONLY - Import CSV with header of "playername, platform, show" into db
    26. primary[-ps][-xb]              [PlayerName]     - Displays Players Primary Weapon
    27. special[-ps][-xb]              [PlayerName]     - Displays Players Secondary Weapon
    28. heavy[-ps][-xb]                [PlayerName]     - Displays Players Heavy Weapon
    29. loadout[-ps][-xb]              [PlayerName]     - Displays Players Weapon Loadout
    30. rumblelistwood                                  - Displays rank 31-40 of rumble list
    31. trialslistwood                                  - Displays rank 31-40 of trials list
    32. build                          [PlayerName]     - Displays player build
    33. activity[-ps][-xb]             [PlayerName]     - Displays Characters current Activity

    [] = optional
    <> = Require
    -ps is to force a PSN Gamertag search, -xb is to force an XBOX Gamertag search
   
