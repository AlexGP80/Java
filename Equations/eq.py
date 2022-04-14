import random
from datetime import datetime

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
        for i in range(1000000):
            next1 = get_next_var_name(current)
            next2 = get_next_var_name(next1)
            next3 = get_next_var_name(next2)
            f.write(f'; {current} = {next1} ^ {next2} ^ {next3}')
            current = get_next_var_name(current)
        next1 = get_next_var_name(current)
        next2 = get_next_var_name(next1)
        f.write(f'; {current} = {random.randint(0,9999)}; {next1} = {random.randint(0,9999)}; {next2} = {random.randint(0,9999)}')



if __name__ == "__main__":
    main()
