x :: Integer 
x = 42 

isZero :: Integer -> Bool 
isZero 0 = True 
isZero _ = False 

isNotZero :: Integer -> Bool 
isNotZero x = myNot (isZero x)

myNot :: Bool -> Bool 
myNot False = True 
myNot True  = False 

myAnd :: Bool -> Bool -> Bool 
myAnd True True = True 
myAnd _ _       = False 

--Factorial function 
fact :: Integer -> Integer 
fact 0 = 1
fact n | n > 0 = n * fact (n - 1)

sum' :: Integer -> Integer 
sum' 0 = 0 
sum' n = sum' (n-1) + n 
