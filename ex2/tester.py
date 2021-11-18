import filecmp
from subprocess import run

input_dir = "input/"
output_dir = "output/"
expected_output_dir = "expected_output/"

test_files = [
    "input",
    "TEST_01_Print_Primes",
    "TEST_02_Bubble_Sort",
    "TEST_03_Merge_Lists",
    "TEST_04_Matrices",
    "TEST_05_Classes",
    "TEST_06_Print_Primes_Error",
    "TEST_07_Bubble_Sort_Error",
    "TEST_08_Merge_Lists_Error",
    "TEST_09_Matrices_Error",
    "TEST_10_Classes_Error"
     ]

def run_parser_on_input_files():
    # assumes all files in 'input_files' located in path 'input/'
    for filename in test_files:
        input_file =  input_dir + filename + ".txt"         # input/test.txt
        output_file = output_dir + filename + "_Output.txt"  # output/test_Output.txt
        run(['java', '-jar', 'PARSER', input_file, output_file])


def simple_tester():
    # for the tester to work - both directories should contains files with identical names (!).
    print("=============== SIMPLE_TESTER ================")

    dcmp = filecmp.dircmp(output_dir, expected_output_dir)

    if dcmp.left_only:
        print("WARNING: The following outputs have no expected-output analogue and therefore are not tested: ", dcmp.left_only)

    assert len(dcmp.diff_files) == 0, f"ERROR: The following outputs are wrong: {dcmp.diff_files}"
    
    print("SUCCESS: Tested the following files: ", dcmp.same_files)


if __name__ == '__main__':
    run_parser_on_input_files()
    simple_tester()
