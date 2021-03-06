import random
from datetime import datetime
from collections import deque

random.seed(datetime.now().microsecond)

def get_next_char(c):
    ascii = ord(c) + 1
    if ascii > 122:
        ascii = 97
    return chr(ascii)

def get_next_var_name(var_name):
    if var_name == 'w':
        return 'y'

    next_var = var_name

    if next_var[-1] == 'z':
        count = 0
        for c in reversed(next_var):
            if c == 'z':
                count += 1
            else:
                break
        if count == len(next_var):
            next_var = next_var.replace('z','a') + 'a'
        else:
            next_var = next_var[:-count-1] + get_next_char(next_var[-count-1])
            for i in range(count):
                next_var += 'a'
        # next_var += 'a'
    else:
        next_var = f"{next_var[:-1]}{chr(ord(next_var[-1])+1)}"

    return next_var



def main():
    # print(get_next_var_name('zzabzz'))
    with open('input.txt', 'w') as f:
        f.write('x = a ^ b ^ c')
        current = 'a'
        last_used = 'c'
        # last_processed = 'x'
        queue = deque()
        queue.append('b')
        queue.append('c')
        for i in range(1000):
            equation = f'; {current} ='
            num_operands = random.randint(2,5)
            for j in range(num_operands):
                if j==0:
                    # variable
                    var_name = get_next_var_name(last_used);
                    last_used = var_name
                    equation += f' {var_name}'
                    queue.append(var_name)
                else:
                    equation += ' ^'
                    if random.randint(1,2)%2 == 0:
                        # variable
                        var_name = get_next_var_name(last_used);
                        last_used = var_name
                        equation += f' {var_name}'
                        queue.append(var_name)
                    else:
                        # number
                        equation += f' {random.randint(0,9999)}'
            # last_processed = current
            current = queue.popleft()
            f.write(equation)

        # flush
        f.write(f'; {current} = {random.randint(0,9999)}')
        for var in queue:
            for i in range(random.randint(1,3)):
                if i==0:
                    f.write(f'; {var} = {random.randint(0,9999)}')
                else:
                    f.write(f' ^ {random.randint(0,9999)}')




if __name__ == "__main__":
    main()
