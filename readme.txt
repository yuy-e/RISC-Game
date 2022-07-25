This a text to help tranfering for Eval1 to Eval2, which shows the main changes of the branch 2-6-yydev-addupgradeturn. 

[1] common
	change Unit, Add UnitType
	add Resource
	change IITerritory, Territory
	change Turn, AttackTurn, MoveTurn, add UpgradeTurn
	change Move Checking, OwnerChecking, PathChecking
	change V2Map, Map
	No change in MapTextView, TurnList
[2] server
	change AttackChecking
	change the HandleOrder.java
    change 'detectresult' func in MasterServer.java
[3] client
	No change
[4] Refactor Code 

note: 
[1] public void updateTerritoryInMap(String territoryName, HashMap<UnitType, Integer> unit_change, String newOwnerColor) in V2map.java
	reason: may need it in update combat result

[2] For the GUI part, just show the unit information of ALEVEL type now. 

[3] Now we have 2 kinds of resources: food & tech
    All territories can produce both of them, but different number.
    Move, Attack would cost the food resource. Upgrade would cost the tech. 
    At the start of the game, all players will have same food & tech resources.

[3] To Do note:
    Add resources Field to theTextPlayer: may need to show the info on the GUI as well.
    Add 'handleUpgrateOrder'
    Change 'handleSingleMoveOrder' - Add chose minimum cost path chosing & resourceChecker/Modify current Pathchecker
    Change 'handleSingleAttackOrder' - Add resourceChecker
    Add 'produceResource' to increase the resources to theTextPlayer at the end of each turn.
    
    Modify Test Files

[4] Question:
    1. Shall we have a maxmim tech level, and improve the tech level at the start?
    2. What's the order of all orders? upgrade -> move -> attack?
    3. Need we show the resource info of player in GUI?
    4. After player choosed the Action in GUI, shall we display differnt choice boxes or just disable useless ones? 
        ex. When choosing 'upgrade', shall we disable the toTerritory box and enable the new_unit_type box?
