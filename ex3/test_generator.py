import os.path
from typing import Dict

TEST_GENERATOR_FILE_PATH = "./tests/tests.txt"
TEST_GENERATOR_FILE_PATHS_LIST = ["./tests/tests.txt", 
     "./tests/matans_killer_tests.txt", 
     "./tests/matans-tests-2.txt", 
     "./tests/shirs_killin_it_tests.txt"]
TEST_INPUT_DIR = "./input"
TEST_OUTPUT_DIR = "./expected_output"


def parse_single_test(test: str) -> Dict[str, str]:
    test_name, test_body_and_res = test.split(maxsplit=1)
    test_body, res = test_body_and_res.split("***")

    test_name = test_name.strip()
    test_body = test_body.strip()
    res = res.strip()
    return {
        "name": test_name,
        "body": test_body,
        "res": res
    }


def generate_single_test(parsed_test: Dict[str, str], input_dir: str, output_dir: str):
    test_name = parsed_test["name"]
    input_test_name = f"TEST_{test_name}.txt"
    output_test_name = f"TEST_{test_name}_Expected_Output.txt"

    full_input_test_name = os.path.join(TEST_INPUT_DIR, input_test_name)
    full_output_test_name = os.path.join(TEST_OUTPUT_DIR, output_test_name)

    with open(full_input_test_name, "w") as f:
        f.write(parsed_test["body"])

    with open(full_output_test_name, "w") as f:
        f.write(parsed_test["res"])


def generate_tests(generator_path: str, input_dir: str, output_dir: str):
    with open(generator_path) as f:
        data = f.read()

    # First entry in the array is garbage, so throw it out
    raw_tests = data.split("///")[1:]
    for test in raw_tests:
        parsed_test = parse_single_test(test)
        generate_single_test(parsed_test, input_dir, output_dir)


def generate_all_tests():
    for test_generator_file_path in TEST_GENERATOR_FILE_PATHS_LIST:
            generate_tests(test_generator_file_path, TEST_INPUT_DIR, TEST_OUTPUT_DIR)


def main():
    generate_tests(TEST_GENERATOR_FILE_PATH, TEST_INPUT_DIR, TEST_OUTPUT_DIR)
    generate_all_tests()


if __name__ == '__main__':
    main()
