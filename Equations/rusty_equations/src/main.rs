/// # SYSTEM OF EQUATIONS
/// Resolve x in a system of equations of the form:
/// x = a ^ b ^ 765; a = c ^ 1235; b = 689 ^ d....
///     where x, a, b, c, d... are variables, whose name must be formed by ascii alphabetic characters
///     other operands can be integers
///     the only operator allowed is ^, meaning XOR
/// Returns the value of x as &str or "null" if the system of equations doesn't have a solution
///     example: x = a ^ b; a = b ^ 3; b = a ^ 7
///
/// Input is in a text file with the format showed above.
/// Try to make the program to allow to solve a system of up to a million variables.
use std::fs;
use std::collections::HashMap;

enum OperandType {
    INVALID,
    VARIABLE,
    NUMBER,
}

struct Operand {
    value: i32,
    is_var: bool,
}

impl Operand {
    fn new(value: i32, is_var: bool) -> Self {
        Self {
            value,
            is_var,
        }
    }
}

fn get_operand_type(operand: &str) -> OperandType {
    if var_name_is_valid(operand) {
        return OperandType::VARIABLE;
    }
    if operand_is_number(operand) {
        return OperandType::NUMBER;
    }
    OperandType::INVALID
}

fn var_name_is_valid(var_name: &str) -> bool {
    for c in var_name.chars() {
        if !c.is_ascii_alphabetic() {
            return false;
        }
    }
    true
}

fn operand_is_number(operand: &str) -> bool {
    for c in operand.chars() {
        if !c.is_ascii_digit() {
            return false
        }
    }
    true
}

fn system_of_equations(filename: &str) -> &str {
    // Open filename and read contents into a String
    let contents = fs::read_to_string(filename)
        .expect("Something went wrong reading the file");

    // Split input into equations
    let equations: Vec<&str> = contents.split(";").collect();

    let mut var_map: HashMap<&str, i32> = HashMap::new();
    let mut equations_map: HashMap<i32, Vec<Operand>> = HashMap::new();
    let mut solved_map: HashMap<i32, i32> = HashMap::new();
    let mut last_var_id: i32 = 0;

    for equation in equations {
        let eq_parts: Vec<&str> = equation.split("=").collect();
        // println!("{}", equation);
        if eq_parts.len() != 2 {
            // Only one equal sign ("=") allowed per equation
            return "null";
        }

        // var_name is the left part of the equation
        // can only be a variable with ascii alphabetic name
        let var_name = eq_parts[0].trim();
        if !var_name_is_valid(var_name) {
            // Variables can only contain alphabetic ascii letters
            return "null";
        }

        // TODO: refactor to a Separate function
        let var_id: i32 = match var_map.get(var_name) {
            Some(var_id) => *var_id,
            None => {
                // var_id 0 won't ever be used, but I prefer to not return (last_var_id - 1) for clarity
                last_var_id += 1;
                var_map.insert(var_name, last_var_id);
                last_var_id
            }
        };

        // Each variable can only appear on the left part of an equation once.
        if let Some(_) = equations_map.get(&var_id) {
            // The same variable cannot appear on the left part of more than one equation
            return "null"
        };


        // var_value is the right part of the equation. Can be a scalar, a variable,
        // or a mix of both (with operator XOR)
        // examples:
        //     5633
        //     b
        //     b ^ a
        //     b ^ 5633 ^ a
        //     5633 ^ 7881
        //     5633 ^ a ^ 7881 ^ b
        //      etc.
        let var_value = eq_parts[1].trim();


        let operands: Vec<&str> = var_value.split("^").collect();
        let mut number_part = 0;
        let mut vec_of_operands: Vec<Operand> = Vec::new();
        let mut solved = true;
        for operand in operands {
            let operand = operand.trim();
            match get_operand_type(operand) {
                OperandType::INVALID => {
                    // Invalid operand in the right part of the equation, only numbers and variables allowed
                    return "null";
                },
                OperandType::NUMBER => {
                    number_part ^= operand.parse::<i32>().unwrap();
                },
                OperandType::VARIABLE => {
                    solved = false;
                    // todo: refactor to a Separate function
                    let var_id: i32 = match var_map.get(var_name) {
                        Some(var_id) => *var_id,
                        None => {
                            // var_id 0 won't ever be used, but I prefer to not return (last_var_id - 1) for clarity
                            last_var_id += 1;
                            var_map.insert(var_name, var_id);
                            last_var_id
                        }
                    };
                    vec_of_operands.push(Operand::new(var_id, true));
                }
            }
            // println!("{}", operand);
        }
        // If no variables in the right part, the variable on the left side is "solved"
        //     and we save its value on the solvedMap
        match solved {
            false => {
                if number_part != 0 {
                    vec_of_operands.push(Operand::new(number_part, false));
                }
                equations_map.insert(var_id, vec_of_operands);
            },
            true => {
                solved_map.insert(var_id, number_part);
            }
        }


    }



    "hola"
}

fn main() {
    const filename: &str = "thousand.txt";
    println!("{}", system_of_equations(filename));
}
