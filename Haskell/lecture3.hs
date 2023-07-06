identity :: a -> a 
identity x = x 

isEmpty :: [a] -> Bool
isEmpty [] = True 
isEmpty _ = False 

myHead :: [a] -> a 
myHead (x : xs) = x

myTail :: [a] -> [a]
myTail (x : xs) = xs 

myLength :: [a] -> Integer
myLength [] = 0
myLength (_ : xs) = 1 + myLength xs

sumList :: [Integer] -> Integer
sumList [] = 0
sumList (hd : tl) = hd + sumList tl

contains0 :: [Integer] -> Bool 
contains0 [] = False 
contains0 (0 : _) = True 
contains0 (_ : xs) = contains0 xs 






