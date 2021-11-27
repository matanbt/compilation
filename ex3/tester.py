from subprocess import run

input_dir = "input/"
output_dir = "output/"
expected_output_dir = "expected_output/"

test_files = [
    "TEST_01_Print_Primes",
    "TEST_02_Bubble_Sort",
    "TEST_03_Merge_Lists",
    "TEST_04_Matrices",
    "TEST_05_Classes",
    "TEST_06_Print_Primes_Error",
    "TEST_07_Bubble_Sort_Error",
    "TEST_08_Merge_Lists_Error",
    "TEST_09_Matrices_Error",
    "TEST_10_Classes_Error",
     ]



def run_on_input_files():
    # assumes all files in 'input_files' located in path 'input/'
    for filename in test_files:
        input_file =  input_dir + filename + ".txt"          # input/test.txt
        output_file = output_dir + filename + "_Output.txt"  # output/test_Output.txt
        run(['java', '-jar', 'COMPILER', input_file, output_file])


def simple_tester():
    # compares outputs to expected ones, IGNORING starting/ending whitespaces
    print("\n\n=============== SIMPLE_TESTER ================")

    for filename in test_files:
        output_file = output_dir + filename + "_Output.txt"                       # output/test_Output.txt
        expected_file = expected_output_dir + filename + "_Expected_Output.txt"   # expected_output/test_Expected_Output.txt
        with open(output_file, 'r') as f1, open(expected_file, 'r') as f2:
            s1 = f1.read().strip()
            s2 = f2.read().strip()
            assert s1 == s2, "Test failed on: " + filename

    print("\n============= SUCCESS - SIMPLE_TESTER ==============")



if __name__ == '__main__':
    run_on_input_files()
    simple_tester()
