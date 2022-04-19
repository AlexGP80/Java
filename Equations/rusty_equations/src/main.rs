use std::fs;
use std::collections::HashMap;

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

fn system_of_equations(filename: &str) -> &str {
    // Open filename and read contents into a String
    let contents = fs::read_to_string(filename)
        .expect("Something went wrong reading the file");

    // Split input into equations
    let equations: Vec<&str> = contents.split(";").collect();

    let mut var_map: HashMap<String, i32> = HashMap::new();
    let mut equations_map: HashMap<i32, Vec<Operand>> = HashMap::new();

    for equation in equations {
        let eq_parts: Vec<&str> = equation.split("=").collect();
        // println!("{}", equation);
        if eq_parts.len() != 2 {
            return "null";
        }
        let mut left_part = eq_parts[0].trim();
        println!("{}", left_part);
        let mut right_part = eq_parts[1].trim();

        // TODO: Check integrity of either part

        let right_operands: Vec<&str> = right_part.split("^").collect();
        for right_operand in right_operands {
            let right_operand = right_operand.trim();
            println!("{}", right_operand);
        }


    }



    "hola"
}

fn main() {
    const filename: &str = "thousand.txt";
    println!("{}", system_of_equations(filename));
}
