const calculator = document.querySelector('calculator')
const output = calculator.querySelector('output')
const keys = calculator.querySelector('keys')

const calculate = (n1, operator, n2 ) => {
    let result = '';
    const firstNum = parseFloat(n1);
    const secondNum = parseFloat(n2);
    if (operator === 'add'){
        result = firstNum + secondNum;
    }else if (operator =='sub'){
        result = firstNum - secondNum;
    }else if (operator == 'mul'){
        result = firstNum * secondNum;
    }else if (operator === 'div'){
        result = firstNum / secondNum;
    }
    return result;
}

keys.addEventListener('click', e => {
    if (e.target.matches('button')){
        const key = e.target;
        const action = key.dataset.action;
        const keyContent = key.textContent;
        const total = output.textContent;
        const previousKey = calculator.dataset.previousKey

        if (!action){
            if (total === '0' || previousKey === 'calculate' || previousKey === 'operator'){
                output.textContent = keyContent;
            } else {
                output.textContent = total + keyContent;
            }
            calculator.dataset.previousKey = 'number';
        }





    }
    
})