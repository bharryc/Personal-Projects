const calculator = document.querySelector('.calculator')
const output = calculator.querySelector('.output')
const keys = calculator.querySelector('.keys')

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

calculator.addEventListener('click', e => {
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
                output.textContent = parseFloat(total) + parseFloat(keyContent);
            }
            calculator.dataset.previousKey = 'number';
        }

        if (action === 'dec'){
            if (!total.includes('.')){
                output.textContent = total + '.';
            }else if ( previousKey === 'operator' || previousKey === 'calculate'){
                output.textContent = '0';
            }
            calculator.dataset.previousKey = 'decimal';
        }

        if ( action === 'add' || action === 'mul' || action === 'sub' || action === 'div'){
            const firstValue = calculator.dataset.firstValue;
            const operator = calculator.dataset.operator;
            const secondValue = total;

            if (firstValue && operator && previousKey !== 'operator' && previousKey !== 'calculate') {
                const calculated = calculate(firstValue, operator, secondValue);
                output.textContent = calculated;
                calculator.dataset.firstValue = calculated;
            }else {
                calculator.dataset.firstValue = total;
            }

            calculator.dataset.previousKey = 'operator';
            calculator.dataset.operator = action;
        }

        if (action === 'del'){
            if (key.textContent === 'C'){
                calculator.dataset.firstValue = '';
                calculator.dataset.modValue = '';
                calculator.dataset.operator = '';
                calculator.dataset.previousKey = '';
            }else {
                key.textContent = 'C';
            }

            output.textContent = '0';
            calculator.dataset.previousKey = 'clear';

        }

        if (action === 'calc'){
            let firstValue = calculator.dataset.firstValue;
            const operator = calculator.dataset.operator;
            let secondValue = total;

            if (firstValue){
                if (previousKey == 'calculate'){
                    firstValue = total;
                    secondValue = calculator.dataset.modValue;
                }
                output.textContent = calculate(firstValue, operator, secondValue);
            }

            calculator.dataset.modValue = secondValue;
            calculator.dataset.previousKey = 'calculate';
        }
    }
})