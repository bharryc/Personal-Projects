import Base

--CSSB2 A1 

--Chunk1

--1
opposite :: Direction -> Direction 
opposite North = South 
opposite East = West 
opposite South = North 
opposite West = East

--2
noActions :: Item -> GameState -> Next GameState 
noActions Spoon _ = Same "There is no action that can be done with this item."
noActions Key _ = Same "There is no action that can be done with this item."

--3
winningRoom = Room { name = "Kitchen", description = "the Kitchen which has the escape tunnel", 
                     isWinRoom = True, requires = Just Key, items = [], monsters =[], 
                     doors = [], actions = noActions }

--4 
armory = Room { name = "Armory", description = "the Armory", isWinRoom = False, 
                requires = Nothing, items = [(Spoon, "on the workbench")], monsters = [], 
                doors = [(West, dungeon), (South, winningRoom)], actions = noActions}


--5
dungeon = Room { name = "Dungeon", description  = "the Dungeon", isWinRoom = False, 
                 requires = Nothing, items = [], monsters = [WoodTroll {health = 10, holding=Key}], 
                 doors = [], actions = useSpoon }

useSpoon :: Item -> GameState -> Next GameState 
useSpoon items (GS p r )
    | null (monsters r ) && items == Spoon = Same "There is no monster to attack in this room"
    | health (head(monsters r)) > 5 = Progress "You attack the troll but it still stands"(GS p r {
      monsters = [WoodTroll (health (head (monsters r))-5) (holding (head (monsters r)))]})
    | otherwise = Progress "You hit the troll with a spoon until it finally falls to the ground" 
    (GS p r{items = [(Key, "The key drops from the troll's grasp,")], monsters = []}) 

--6 
game0 :: IO()
game0 = tellContext(GS player1 armory)

player1 :: Player 
player1 = Player "Player 1 " []



---------------------------------------------------------------------------------------------------
--Chunk2 
--7
instance Parsable Item where
    parse "spoon" = Just Spoon 
    parse "key"   = Just Key 

--8 
instance Parsable Direction where 
    parse "north" = Just North
    parse "south" = Just South
    parse "east"  = Just East 
    parse "west"  = Just West 


--9
instance Parsable Command where
    parse "go north"   = Just (Move North)
    parse "go south"   = Just (Move South)
    parse "go east"    = Just (Move East)
    parse "go west"    = Just (Move West)
    parse "grab spoon" = Just (PickUp Spoon)
    parse "grab key"   = Just (PickUp Key)
    parse "use spoon"  = Just (Use Spoon)
    parse "use key"    = Just (Use Key)
    parse "end"        = Just End 

--10
tellResponse :: String -> IO()
tellResponse message = putStrLn $ "< " ++ message ++ "."

--11
readCommand :: IO (Maybe Command)
readCommand = do
        putStrLn "> "
        command <- getLine
        return (parse command)

--12
deleteFrom :: Eq a => a -> [(a, b)] -> [(a, b)]
deleteFrom a [] = []
deleteFrom a ((x, r) : xs)
    |a == x = deleteFrom a xs 
    | otherwise = (x, r) : deleteFrom a xs 

--13 
leaveRoom :: Room -> Direction -> Room -> Room
leaveRoom fromRoom dir toRoom = let room' = deleteFrom (opposite dir) (doors toRoom)
                                    in toRoom {doors = (opposite dir, fromRoom): room'}


---------------------------------------------------------------------------------------------------
--Chunk3
--14
checkInventory :: Item -> [Item] -> Maybe Item 
checkInventory a [] = Nothing 
checkInventory a (x : xs)
    |a == x = Just x
    |a /= x = checkInventory a xs

step :: Command -> GameState -> Next GameState 
step (Move dir) (GS p r) = case lookup dir (doors r) of
    Nothing -> Same "There is no room in that direction"
    Just r' -> case requires r' of 
        Nothing -> Progress "You have opened the door" (GS p (leaveRoom r dir r'))
        Just item -> if checkInventory item (inventory p) /= Nothing then
            Progress "You have opened the door" (GS p (leaveRoom r dir r'))
            else 
                Same "You do not have the key required to open this door"

step (PickUp item) (GS p r) = case lookup item (items r) of 
    Nothing -> Same "This item is not found in this room"
    Just item' -> 
        let newItem = deleteFrom item (items r)
            newInventory = item : inventory p
        in Progress "You pick up the item" (GS p{ inventory = newInventory } r{ items = newItem})

step (Use item) (GS p r) = case checkInventory item (inventory p) of
    Nothing -> Same "This item is not in your inventory"
    Just item -> actions r item (GS p r)


--15 
play :: GameState -> IO()
play (GS p r) = do 
    tellContext (GS p r )
    playLoop (GS p r)

playLoop :: GameState -> IO()
playLoop (GS p r) = 
    if isWinRoom r then do 
        tellResponse "You have won the game!"
        return()
    else do 
        command <- readCommand
        case command of 
            Nothing -> do                     
                tellResponse "Command is unkown."
                playLoop (GS p r)
            Just End -> do 
                tellResponse "Ending game, thanks for playing!"
                return()
            Just command -> case step command (GS p r ) of                     
                Same var -> do 
                    playLoop (GS p r)
                Progress var2 (GS p r) -> do
                    tellResponse var2
                    tellContext (GS p r)
                    playLoop (GS p r)

-- 16  
main :: IO()
main = do 
    game0
    playLoop (GS player1 armory)
    return()
