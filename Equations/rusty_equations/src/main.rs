use std::fs;

fn main() {
    const filename: &str = "thousand.txt";

    // Open filename and read contents into a String
    let contents = fs::read_to_string(filename)
        .expect("Something went wrong reading the file");

    // Split input into slices
    let equations: Vec<&str> = contents.split(";").collect();


}
