

x = 100

y = 42

z = x + y 

sqr x = x ^ 2

quad x = 
    let sqr x = x ^ 2 
    in sqr x * sqr x 

oct x = 
    let y = quad x
    in y * y 

function1 x =
    case x of 
        0 -> 100
        1 -> 200
        n -> n * 400


function1' 0 = 100
function1' 1 = 200
function1' x = x * 400
