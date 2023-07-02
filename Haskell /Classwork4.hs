doubleList :: [Integer] -> [Integer]
doubleList [] = []
doubleList (hd : tl) = hd*2 : doubleList tl

justEvens :: [Integer] -> [Integer]
justEvens [] = []
justEvens (x : xs)
  | x `mod` 2 == 0 = x : justEvens xs
  | otherwise = justEvens xs

myTake :: Integer -> [a] -> [a]
myTake 0 _ = []
myTake _ [] = []
myTake n (x : xs) =
  x : myTake (n - 1) xs

myFst :: (a, b) -> a
myFst (x, y) = x

mySnd :: (a, b) -> b
mySnd (x, y) = y

myFst3 :: (a, b, c) -> a
myFst3 (x, y, z) = x

countEvensAndOdds :: [Integer] -> (Integer, Integer)
countEvensAndOdds [] = (0, 0)
countEvensAndOdds (x : xs)
  | mod x 2 == 0 = let (even, odd) = countEvensAndOdds xs in (even + 1, odd)
  | otherwise    = let (even, odd) = countEvensAndOdds xs in (even, odd + 1)
