import subprocess
import os

input_dir = "./input/"
output_dir = "./output/"
expected_output_dir = "./expected_output/"

test_files = [
    "TEST_01_Print_Primes.txt"
     ]

def list_test_files_by_dir():
    global test_files
    test_files = os.listdir(input_dir)

def run_on_test_files():
    # assumes all files in 'input_files' located in path 'input/'
    for filename in test_files:
        input_file =  input_dir + filename                        # input/test.txt
        output_file = output_dir + filename[:-4] + "_Output.txt"  # output/test_Output.txt
        print(input_file)
        subprocess.run(['java', '-jar', 'COMPILER', input_file, output_file], stdout=subprocess.DEVNULL)


def simple_tester():
    # compares outputs to expected ones, IGNORING starting/ending whitespaces
    print("\n\n=============== SIMPLE_TESTER ================")

    for filename in test_files:
        output_file = output_dir + filename[:-4] + "_Output.txt"                       # output/test_Output.txt
        expected_file = expected_output_dir + filename[:-4] + "_Expected_Output.txt"   # expected_output/test_Expected_Output.txt
        with open(output_file, 'r') as f1, open(expected_file, 'r') as f2:
            s1 = f1.read().strip()
            s2 = f2.read().strip()
            assert s1 == s2, "Test failed on: " + filename + "\nGot: " + s1 + "   Expected: " + s2

    print("\n============= SUCCESS - SIMPLE_TESTER ==============")



if __name__ == '__main__':
    list_test_files_by_dir()
    run_on_test_files()
    simple_tester()
