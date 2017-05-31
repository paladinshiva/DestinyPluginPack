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

        Command                        Argument(s)      - Descriptio
    1.  debugguardian                  <PlayerName>     - Outputs Guardian debug informatio
    2.  info[-ps][-xb]                 [PlayerName]     - General information about playe
    3.  rumblelistgold                                  - Displays top 10 of rumble lis
    4.  rumblelistsilver                                - Displays rank 11-20 of rumble lis
    5.  rumblelistbronze                                - Displays rank 21-30 of rumble lis
    6.  rumbleaddtolist[-ps][-xb]      <PlayerName>     - MOD ONLY - Adds player to rumble list (Updated every 5 min
    7.  rumbleremovefromlist[-ps][-xb] <PlayerName>     - MOD ONLY - Removes player from rumble lis
    8.  rumblelist                                      - MOD ONLY - Displays in channel full rumble lis
    9.  rumblelist-csv                                  - MOD ONLY - PM's a CSV export of full rumble lis
    10. rumblelistrefresh                               - MOD ONLY - Refresh's all the stats in the current rumble lis
    11. rumblelist-importcsv           <CSV Attachment> - MOD ONLY - Import CSV with header of "playername, platform, show" into d
    12. trialsmap                                       - Displays current trials map with pictur
    13. fireteam[-ps][-xb]             <PlayerName>     - Displays Detailed information about current players ToO Firetea
    14. fireteam#[-ps][-xb]            <PlayerName>     - Displays Detailed information about current players ToO Fireteam ( In Text Form 
    15. trials[-ps][-xb]               [PlayerName]     - Displays Detailed information about ToO playe
    16. trials#[-ps][-xb]              [PlayerName]     - Displays Detailed information about ToO player ( In Text Form 
    17. trialslistgold                                  - Displays top 10 of Trials lis
    18. trialslistsilver                                - Displays rank 11-20 of Trials lis
    19. trialslistbronze                                - Displays rank 21-30 of Trials lis
    20. trialsaddtolist[-ps][-xb]      <PlayerName>     - MOD ONLY - Adds player to ToO ranking list (Updated every 5 min
    21. trialsremovefromlist[-ps][-xb] <PlayerName>     - MOD ONLY - Removes player from ToO ranking lis
    22. trialslist                                      - MOD ONLY - Displays in channel full ToO list
    23. trialslist-csv                                  - MOD ONLY - PM's a CSV export of full ToO ranking lis
    24. trialslistrefresh                               - MOD ONLY - Refresh all the stats in the current ToO ranking lis
    25. trialslist-importcsv           <CSV Attachment> - MOD ONLY - Import CSV with header of "playername, platform, show" into d
    26. runpromotions                                   - MOD ONLY - Forces promotions to run on SG
    27. primary[-ps][-xb]              [PlayerName]     - Displays Players Primary Weapon
    28. special[-ps][-xb]              [PlayerName]     - Displays Players Secondary Weapon
    29. heavy[-ps][-xb]                [PlayerName]     - Displays Players Heavy Weapon
    30. loadout[-ps][-xb]              [PlayerName]     - Displays Players Weapon Loadou
    31. rumblelistwood                                  - Displays rank 31-40 of rumble lis
    32. trialslistwood                                  - Displays rank 31-40 of trials lis
    33. build                          [PlayerName]     - Displays player buil
    34. activity[-ps][-xb]             [PlayerName]     - Displays Characters current Activit

    [] = optional
    <> = Require
    -ps is to force a PSN Gamertag search, -xb is to force an XBOX Gamertag search
   
